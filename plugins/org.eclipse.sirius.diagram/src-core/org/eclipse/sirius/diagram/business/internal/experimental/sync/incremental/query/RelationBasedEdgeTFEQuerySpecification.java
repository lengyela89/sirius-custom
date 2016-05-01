package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql.VQLInterpreterConstants;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.RelationBasedEdgeTFEQuerySpecification.RelationBasedEdgeTFEPQuery;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.SiriusCompositeQuerySpecification.SiriusCompositePQuery;
import org.eclipse.viatra.query.runtime.matchers.psystem.PBody;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.QueryInitializationException;

import com.google.common.collect.Sets;

public class RelationBasedEdgeTFEQuerySpecification extends SiriusCompositeQuerySpecification<RelationBasedEdgeTFEPQuery> {
    
    public RelationBasedEdgeTFEQuerySpecification(String patternFQN, SiriusQuerySpecification<? extends PQuery> sourceQS,
            SiriusQuerySpecification<? extends PQuery> targetQS, SiriusQuerySpecification<? extends PQuery> targetFinderExpressionQS) {
        super(new RelationBasedEdgeTFEPQuery(patternFQN, sourceQS, targetQS, targetFinderExpressionQS));
    }

    public static class RelationBasedEdgeTFEPQuery extends SiriusCompositePQuery {

        private SiriusQuerySpecification<? extends PQuery> sourceQS;
        private SiriusQuerySpecification<? extends PQuery> targetQS;
        private SiriusQuerySpecification<? extends PQuery> targetFinderExpressionQS;

        @SuppressWarnings("unchecked")
        public RelationBasedEdgeTFEPQuery(String patternFQN, SiriusQuerySpecification<? extends PQuery> sourceQS,
                SiriusQuerySpecification<? extends PQuery> targetQS, SiriusQuerySpecification<? extends PQuery> targetFinderExpressionQS) {
            super(patternFQN, sourceQS, targetQS, targetFinderExpressionQS);

            this.sourceQS = sourceQS;
            this.targetQS = targetQS;
            this.targetFinderExpressionQS = targetFinderExpressionQS;
            
            addAnnotations(targetFinderExpressionQS, Sets.<String>newHashSet(VQLInterpreterConstants.ANNOTATION_RETURN, VQLInterpreterConstants.ANNOTATION_CONTEXT));
        }

        public SiriusQuerySpecification<? extends PQuery> getSourceQS() {
            return sourceQS;
        }

        public SiriusQuerySpecification<? extends PQuery> getTargetQS() {
            return targetQS;
        }

        public SiriusQuerySpecification<? extends PQuery> getTargetFinderExpressionQS() {
            return targetFinderExpressionQS;
        }
        
        public Map<String, String> getSourceQSParameterMappings() {
            return Collections.unmodifiableMap(getQuerySpecificationParameterMappings().get(sourceQS));
        }
        
        public Map<String, String> getTargetQSParameterMappings() {
            return Collections.unmodifiableMap(getQuerySpecificationParameterMappings().get(targetQS));
        }

        @Override
        protected Set<PBody> doGetContainedBodies() throws QueryInitializationException {
            Set<PBody> bodies = super.doGetContainedBodies();
            
            for (PBody body : bodies) {
                addEquality(
                        body,
                        getCorrespondingParameter(sourceQS, sourceQS.getResultParameterName()),
                        getCorrespondingParameter(targetFinderExpressionQS, targetFinderExpressionQS.getContextParameterName()));
                
                addEquality(
                        body,
                        getCorrespondingParameter(targetQS, targetQS.getResultParameterName()),
                        getCorrespondingParameter(targetFinderExpressionQS, targetFinderExpressionQS.getResultParameterName()));
            }
            
            return bodies;
        }
    }

}
