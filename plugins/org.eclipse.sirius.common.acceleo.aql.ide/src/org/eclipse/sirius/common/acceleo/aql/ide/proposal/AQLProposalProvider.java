/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.common.acceleo.aql.ide.proposal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.acceleo.query.runtime.ICompletionProposal;
import org.eclipse.acceleo.query.runtime.ICompletionResult;
import org.eclipse.acceleo.query.runtime.IQueryEnvironment;
import org.eclipse.acceleo.query.runtime.impl.BasicFilter;
import org.eclipse.acceleo.query.runtime.impl.QueryCompletionEngine;
import org.eclipse.acceleo.query.validation.type.EClassifierType;
import org.eclipse.acceleo.query.validation.type.IType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.common.acceleo.aql.business.api.AQLConstants;
import org.eclipse.sirius.common.acceleo.aql.business.api.ExpressionTrimmer;
import org.eclipse.sirius.common.acceleo.aql.business.api.TypesUtil;
import org.eclipse.sirius.common.acceleo.aql.business.internal.AQLSiriusInterpreter;
import org.eclipse.sirius.common.tools.api.contentassist.ContentContext;
import org.eclipse.sirius.common.tools.api.contentassist.ContentInstanceContext;
import org.eclipse.sirius.common.tools.api.contentassist.ContentProposal;
import org.eclipse.sirius.common.tools.api.contentassist.IProposalProvider;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreter;
import org.eclipse.sirius.ecore.extender.business.api.accessor.EcoreMetamodelDescriptor;
import org.eclipse.sirius.ecore.extender.business.api.accessor.MetamodelDescriptor;

import com.google.common.collect.Lists;

/**
 * This implementation of the {@link IProposalProvider} interface will be used
 * in order to provide completion for Acceleo Query expressions.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class AQLProposalProvider implements IProposalProvider {

    /**
     * {@inheritDoc}
     * 
     */
    public ContentProposal getNewEmtpyExpression() {
        return new ContentProposal(AQLConstants.AQL_PREFIX, AQLConstants.AQL_PREFIX, "New acceleo query language expression.", AQLConstants.AQL_PREFIX.length());
    }

    /**
     * {@inheritDoc}
     * 
     */
    public List<ContentProposal> getProposals(IInterpreter interpreter, ContentContext context) {
        List<ContentProposal> proposals = new ArrayList();
        if (interpreter instanceof AQLSiriusInterpreter) {
            /*
             * the instance of interpreter has actually been created for the
             * purpose of the completion and can be modified at will with no
             * risks.
             */
            ExpressionTrimmer trimer = new ExpressionTrimmer(context.getContents());
            if (trimer.positionIsWithinAQL(context.getPosition())) {
                AQLSiriusInterpreter aqlInterpreter = (AQLSiriusInterpreter) interpreter;
                setupInterpreter(context, aqlInterpreter);
                Map<String, Set<IType>> variableTypes = TypesUtil.createAQLVariableTypesFromInterpreterContext(context.getInterpreterContext(), aqlInterpreter.getQueryEnvironment());

                addProposals(proposals, trimer, context.getPosition(), aqlInterpreter.getQueryEnvironment(), variableTypes);
            }
        }
        return proposals;
    }

    private void setupInterpreter(ContentContext context, AQLSiriusInterpreter interpreter) {
        Collection<MetamodelDescriptor> metamodels = new ArrayList<MetamodelDescriptor>();
        for (EPackage pak : context.getInterpreterContext().getAvailableEPackages()) {
            if (pak != null) {
                metamodels.add(new EcoreMetamodelDescriptor(pak));
            }
        }
        interpreter.activateMetamodels(metamodels);
        Resource vsmResource = context.getInterpreterContext().getElement().eResource();
        if (vsmResource != null) {
            interpreter.setProperty(IInterpreter.FILES, Lists.newArrayList(vsmResource.getURI().toPlatformString(true)));
        }
        for (String imp : context.getInterpreterContext().getDependencies()) {
            interpreter.addImport(imp);
        }
    }

    private void addProposals(List<ContentProposal> proposals, ExpressionTrimmer trimmer, int position, IQueryEnvironment queryEnvironment, Map<String, Set<IType>> variableTypes) {
        QueryCompletionEngine engine = new QueryCompletionEngine(queryEnvironment);
        final ICompletionResult completionResult = engine.getCompletion(trimmer.getExpression(), trimmer.getPositionWithinAQL(position), variableTypes);
        /*
         * completionResult.sort(new ProposalComparator());
         */
        final List<ICompletionProposal> proposal = completionResult.getProposals(new BasicFilter(completionResult));

        for (ICompletionProposal propFromAQL : proposal) {
            ContentProposal propForSirius = new ContentProposal(propFromAQL.getProposal(), propFromAQL.getProposal(), propFromAQL.getClass().getSimpleName(), propFromAQL.getCursorOffset());
            proposals.add(propForSirius);
        }
    }

    /**
     * {@inheritDoc}
     * 
     */
    public List<ContentProposal> getProposals(IInterpreter interpreter, ContentInstanceContext context) {
        List<ContentProposal> proposals = new ArrayList();
        if (interpreter instanceof AQLSiriusInterpreter) {
            IQueryEnvironment queryEnvironment = ((AQLSiriusInterpreter) interpreter).getQueryEnvironment();

            Map<String, Set<IType>> variableTypes = new LinkedHashMap<String, Set<IType>>();
            if (context.getCurrentSelected() != null) {
                queryEnvironment.registerEPackage(context.getCurrentSelected().eClass().getEPackage());
                final Set<IType> potentialTypes = new LinkedHashSet<IType>(1);
                potentialTypes.add(new EClassifierType(context.getCurrentSelected().eClass()));
                variableTypes.put("self", potentialTypes);
            }

            ExpressionTrimmer trimer = new ExpressionTrimmer(context.getTextSoFar());
            if (trimer.positionIsWithinAQL(context.getCursorPosition())) {
                addProposals(proposals, trimer, context.getCursorPosition(), queryEnvironment, variableTypes);
            }

        }
        return proposals;
    }
}
