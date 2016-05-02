package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import java.util.Set;

import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql.VQLInterpreterConstants;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.ElementBasedEdgeFEQuerySpecification.ElementBasedEdgeFEPQuery;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.SiriusCompositeQuerySpecification.SiriusCompositePQuery;
import org.eclipse.viatra.query.runtime.matchers.psystem.PBody;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.QueryInitializationException;

import com.google.common.collect.Sets;

public class ElementBasedEdgeFEQuerySpecification extends SiriusCompositeQuerySpecification<ElementBasedEdgeFEPQuery> {

    
    public ElementBasedEdgeFEQuerySpecification(String patternFQN, SiriusQuerySpecification<? extends PQuery> mappingQS,
            SiriusQuerySpecification<? extends PQuery> finderExpressionQS, SiriusQuerySpecification<? extends PQuery> semanticCandidatesExpressionQS) {
        super(new ElementBasedEdgeFEPQuery(patternFQN, mappingQS, finderExpressionQS, semanticCandidatesExpressionQS));
    }

    public static class ElementBasedEdgeFEPQuery extends SiriusCompositePQuery {

        private SiriusQuerySpecification<? extends PQuery> mappingQS;
        private SiriusQuerySpecification<? extends PQuery> finderExpressionQS;
        private SiriusQuerySpecification<? extends PQuery> semanticCandidatesExpressionQS;

        @SuppressWarnings("unchecked")
        public ElementBasedEdgeFEPQuery(String patternFQN, SiriusQuerySpecification<? extends PQuery> mappingQS,
                SiriusQuerySpecification<? extends PQuery> finderExpressionQS, SiriusQuerySpecification<? extends PQuery> semanticCandidatesExpressionQS) {
            super(patternFQN, mappingQS, finderExpressionQS, semanticCandidatesExpressionQS);
            
            this.mappingQS = mappingQS;
            this.finderExpressionQS = finderExpressionQS;
            this.semanticCandidatesExpressionQS = semanticCandidatesExpressionQS;
            
            addAnnotations(finderExpressionQS, Sets.<String>newHashSet(VQLInterpreterConstants.ANNOTATION_RETURN, VQLInterpreterConstants.ANNOTATION_CONTEXT));
        }

        @Override
        protected Set<PBody> doGetContainedBodies() throws QueryInitializationException {
            Set<PBody> bodies = super.doGetContainedBodies();
            
            for (PBody body : bodies) {
                addEquality(
                        body,
                        getCorrespondingParameter(mappingQS, mappingQS.getResultParameterName()),
                        getCorrespondingParameter(finderExpressionQS, finderExpressionQS.getResultParameterName()));
                
                addEquality(
                        body,
                        getCorrespondingParameter(semanticCandidatesExpressionQS, semanticCandidatesExpressionQS.getResultParameterName()),
                        getCorrespondingParameter(finderExpressionQS, finderExpressionQS.getContextParameterName()));                
            }
            
            return bodies;
        }
        
    }
}
