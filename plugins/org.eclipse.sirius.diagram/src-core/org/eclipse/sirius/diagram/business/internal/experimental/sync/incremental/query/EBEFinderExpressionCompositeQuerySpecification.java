package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import java.util.Set;

import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql.VQLInterpreter;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql.VQLInterpreterConstants;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.CompositeQuerySpecification.CompositePQuery;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.EBEFinderExpressionCompositeQuerySpecification.EBEFinderExpressionCompositePQuery;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.matchers.psystem.PBody;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PParameter;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.QueryInitializationException;

import com.google.common.collect.Sets;

public class EBEFinderExpressionCompositeQuerySpecification extends CompositeQuerySpecification<EBEFinderExpressionCompositePQuery> {

    
    public EBEFinderExpressionCompositeQuerySpecification(DSemanticDiagram diagram, long ruleDescriptorId,
            IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> mappingQS,
            IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> finderExpressionQS,                        
            IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> semanticCandidatesExpressionQS) {
        super(new EBEFinderExpressionCompositePQuery(diagram, ruleDescriptorId, mappingQS, finderExpressionQS, semanticCandidatesExpressionQS));
    }

    public static class EBEFinderExpressionCompositePQuery extends CompositePQuery {

        private PParameter feResultParameter;
        private PParameter feContextParameter;
        private PParameter sceResultParameter;
        private PParameter mappingResultParameter;

        @SuppressWarnings("unchecked")
        public EBEFinderExpressionCompositePQuery(DSemanticDiagram diagram, long ruleDescriptorId,
                IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> mappingQS,
                IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> finderExpressionQS,                        
                IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> semanticCandidatesExpressionQS) {
            super(diagram, ruleDescriptorId, mappingQS, finderExpressionQS, semanticCandidatesExpressionQS);
            
            String feContextParameterName = VQLInterpreter.getContextParameterName(finderExpressionQS);
            this.feContextParameter = getPParameterByName(getParameterMappings(finderExpressionQS).get(feContextParameterName));
            
            String feResultParameterName = VQLInterpreter.getResultParameterName(finderExpressionQS);
            this.feResultParameter = getPParameterByName(getParameterMappings(finderExpressionQS).get(feResultParameterName));
            
            String mappingResultParameterName = VQLInterpreter.getResultParameterName(mappingQS);
            this.mappingResultParameter = getPParameterByName(getParameterMappings(mappingQS).get(mappingResultParameterName));
            
            String sceResultParameterName = VQLInterpreter.getResultParameterName(semanticCandidatesExpressionQS);
            this.sceResultParameter = getPParameterByName(getParameterMappings(semanticCandidatesExpressionQS).get(sceResultParameterName));
            
            addAnnotations(finderExpressionQS, Sets.newHashSet(VQLInterpreterConstants.ANNOTATION_RETURN, VQLInterpreterConstants.ANNOTATION_CONTEXT));
        }

        @Override
        protected Set<PBody> doGetContainedBodies() throws QueryInitializationException {
            Set<PBody> bodies = super.doGetContainedBodies();
            
            for (PBody body : bodies) {
                addEquality(body, mappingResultParameter.getName(), feResultParameter.getName());
                addEquality(body, sceResultParameter.getName(), feContextParameter.getName());
            }
            
            return bodies;
        }
        
    }
}
