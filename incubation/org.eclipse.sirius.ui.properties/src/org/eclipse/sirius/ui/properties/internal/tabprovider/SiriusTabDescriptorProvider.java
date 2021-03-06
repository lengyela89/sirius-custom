/*******************************************************************************
 * Copyright (c) 2015, 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ui.properties.internal.tabprovider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.eef.EEFViewDescription;
import org.eclipse.eef.core.api.EEFExpressionUtils;
import org.eclipse.eef.core.api.EEFPage;
import org.eclipse.eef.core.api.EEFView;
import org.eclipse.eef.core.api.EEFViewFactory;
import org.eclipse.eef.ide.ui.api.EEFTabDescriptor;
import org.eclipse.eef.properties.ui.api.IEEFTabDescriptor;
import org.eclipse.eef.properties.ui.api.IEEFTabDescriptorProvider;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.sirius.business.api.query.EObjectQuery;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.common.interpreter.api.IVariableManager;
import org.eclipse.sirius.common.interpreter.api.VariableManagerFactory;
import org.eclipse.sirius.ext.base.Option;
import org.eclipse.sirius.properties.PageDescription;
import org.eclipse.sirius.properties.ViewExtensionDescription;
import org.eclipse.sirius.ui.properties.internal.Messages;
import org.eclipse.sirius.ui.properties.internal.SiriusInputDescriptor;
import org.eclipse.sirius.ui.properties.internal.SiriusInterpreter;
import org.eclipse.sirius.ui.properties.internal.SiriusUIPropertiesPlugin;
import org.eclipse.sirius.viewpoint.description.DescriptionPackage;
import org.eclipse.sirius.viewpoint.description.Group;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.ui.IWorkbenchPart;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * The {@link IEEFTabDescriptorProvider} for Eclipse Sirius.
 * 
 * @author sbegaudeau
 * @author pcdavid
 */
public class SiriusTabDescriptorProvider implements IEEFTabDescriptorProvider {

    /**
     * The URI of the model containing the default value of the properties page
     * to create.
     */
    private static final String DEFAULT_PROPERTIES_URI = "platform:/plugin/org.eclipse.sirius.ui.properties/model/properties.xmi";

    @Override
    public Collection<IEEFTabDescriptor> get(IWorkbenchPart part, ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            Object[] objects = structuredSelection.toArray();
            // FIXME We take the first one
            if (objects.length > 0) {
                if (objects.length > 1) {
                    SiriusUIPropertiesPlugin.getPlugin().warning(Messages.SiriusTabDescriptorProvider_UnsupportedMultipleSelection);
                }
                SiriusInputDescriptor sid = new SiriusInputDescriptor(objects[0]);
                if (sid.getSemanticElement() != null) {
                    // Let's find out the description of the view
                    return this.getTabDescriptors(sid);
                } else {
                    SiriusUIPropertiesPlugin.getPlugin().error(Messages.SiriusTabDescriptorProvider_UndefinedSemanticElement);
                }
            }
        }
        return new ArrayList<IEEFTabDescriptor>();
    }

    /**
     * Returns the {@link IEEFTabDescriptor} for the given semantic element.
     * 
     * @param semanticElement
     *            The semantic element
     * @return A collection of {@link IEEFTabDescriptor}
     */
    private Collection<IEEFTabDescriptor> getTabDescriptors(SiriusInputDescriptor input) {
        Session session = input.getFullContext().getSession().get();
        List<PageDescription> effectivePageDescriptions = computeEffectiveDescription(input, session);
        return getTabDescriptors(session, input, effectivePageDescriptions);
    }

    private Collection<IEEFTabDescriptor> getTabDescriptors(Session session, SiriusInputDescriptor input, List<PageDescription> effectivePageDescriptions) {
        EEFViewDescription viewDescription = new ViewDescriptionConverter(effectivePageDescriptions).convert();
        EEFView eefView = createEEFView(session, input, viewDescription);

        List<IEEFTabDescriptor> descriptors = new ArrayList<IEEFTabDescriptor>();
        List<EEFPage> eefPages = eefView.getPages();
        for (EEFPage eefPage : eefPages) {
            descriptors.add(new EEFTabDescriptor(eefPage));
        }
        return descriptors;
    }

    private EEFView createEEFView(Session session, SiriusInputDescriptor input, EEFViewDescription viewDescription) {
        IVariableManager variableManager = new VariableManagerFactory().createVariableManager();
        variableManager.put(EEFExpressionUtils.SELF, input.getSemanticElement());
        variableManager.put(EEFExpressionUtils.INPUT, input);
        EEFView eefView = new EEFViewFactory().createEEFView(viewDescription, variableManager, new SiriusInterpreter(session), session.getTransactionalEditingDomain(), input);
        return eefView;
    }

    /**
     * Computes the equivalent of:
     * 
     * <pre>
     * session.selectedViewpoints.eContainer(description::Group).eContents(properties::ViewExtensionDescription).pages
     * </pre>
     */
    private List<PageDescription> computeEffectiveDescription(SiriusInputDescriptor input, Session session) {
        Preconditions.checkNotNull(session);

        Set<ViewExtensionDescription> viewDescriptions = Sets.newLinkedHashSet();
        for (Viewpoint viewpoint : session.getSelectedViewpoints(true)) {
            Option<EObject> parent = new EObjectQuery(viewpoint).getFirstAncestorOfType(DescriptionPackage.Literals.GROUP);
            if (parent.some()) {
                Group group = (Group) parent.get();
                Iterables.addAll(viewDescriptions, Iterables.filter(group.getExtensions(), ViewExtensionDescription.class));
            }
        }

        List<PageDescription> effectivePages = Lists.newArrayList();
        for (ViewExtensionDescription ved : viewDescriptions) {
            effectivePages.addAll(ved.getPages());
        }

        if (effectivePages.size() == 0) {
            ResourceSet resourceSet = new ResourceSetImpl();
            URI uri = URI.createURI(DEFAULT_PROPERTIES_URI, true);
            Resource resource = resourceSet.getResource(uri, true);
            if (resource != null) {
                List<EObject> contents = resource.getContents();
                if (contents.size() > 0 && contents.get(0) instanceof ViewExtensionDescription) {
                    ViewExtensionDescription viewExtensionDescription = (ViewExtensionDescription) contents.get(0);
                    effectivePages.addAll(viewExtensionDescription.getPages());
                }
            } else {
                SiriusUIPropertiesPlugin.getPlugin().error(Messages.SiriusTabDescriptorProvider_DefaultPropertiesNotFound);
            }
        }

        return effectivePages;
    }
}
