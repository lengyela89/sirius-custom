/**
 * Copyright (c) 2015 Obeo
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Obeo - initial API and implementation
 * 
 */
package org.eclipse.sirius.tree.business.internal.dialect;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.eclipse.sirius.business.api.extender.MetamodelDescriptorProvider2;
import org.eclipse.sirius.ecore.extender.business.api.accessor.EcoreMetamodelDescriptor;
import org.eclipse.sirius.ecore.extender.business.api.accessor.MetamodelDescriptor;
import org.eclipse.sirius.viewpoint.description.Viewpoint;

import com.google.common.collect.Sets;

/**
 * Provides the descriptors for the tree metamodels of Sirius.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 *
 */
public class TreeMetamodelsProvider implements MetamodelDescriptorProvider2 {

    @Override
    public Collection<MetamodelDescriptor> provides(Collection<Viewpoint> vp) {
        Set<MetamodelDescriptor> result = Sets.newLinkedHashSet();
        result.add(new EcoreMetamodelDescriptor(org.eclipse.sirius.tree.TreePackage.eINSTANCE));
        result.add(new EcoreMetamodelDescriptor(org.eclipse.sirius.tree.description.DescriptionPackage.eINSTANCE));
        return result;
    }

    @Override
    public Collection<MetamodelDescriptor> provides(Viewpoint vp) {
        return Collections.<MetamodelDescriptor> emptyList();
    }
}
