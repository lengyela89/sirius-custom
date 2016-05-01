package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import java.util.Set;

import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql.VQLInterpreter;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql.VQLInterpreterConstants;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.DEdgeCompositeQuerySpecification.DEdgeCompositePQuery;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.RelationBasedEdgeCompositeQuerySpecification.RelationBasedEdgeCompositePQuery;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.matchers.psystem.PBody;
import org.eclipse.viatra.query.runtime.matchers.psystem.PVariable;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicdeferred.Equality;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PParameter;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.QueryInitializationException;

import com.google.common.collect.Sets;

public class RelationBasedEdgeCompositeQuerySpecification extends CompositeQuerySpecification<RelationBasedEdgeCompositePQuery> {

    public RelationBasedEdgeCompositeQuerySpecification(DSemanticDiagram diagram, long ruleDescriptorId,
            IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> targetFinderExpressionQS,
            IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> sourceQS,
            IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> targetQS) {
        super(new RelationBasedEdgeCompositePQuery(diagram, ruleDescriptorId, targetFinderExpressionQS, sourceQS, targetQS));
    }

    public static class RelationBasedEdgeCompositePQuery extends DEdgeCompositePQuery {
        
        private IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> targetFinderExpressionQS;

        private IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> sourceQS;

        private IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> targetQS;

        private PParameter sourceResultParameter;

        private PParameter targetResultParameter;

        private PParameter tfeResultParameter;

        private PParameter tfeContextParameter;


        
        @SuppressWarnings("unchecked")
        public RelationBasedEdgeCompositePQuery(DSemanticDiagram diagram, long ruleDescriptorId,
                IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> targetFinderExpressionQS,
                IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> sourceQS,
                IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> targetQS) {
            
            super(diagram, ruleDescriptorId, targetFinderExpressionQS, sourceQS, targetQS);

            this.targetFinderExpressionQS = targetFinderExpressionQS;
            this.sourceQS = sourceQS;
            this.targetQS = targetQS;
            
            String sourceResultParameterName = VQLInterpreter.getResultParameterName(this.sourceQS);
            this.sourceResultParameter = getPParameterByName(querySpecificationToParameterMappingsMap
                    .get(this.sourceQS).get(sourceResultParameterName));
            
            String targetResultParameterName = VQLInterpreter.getResultParameterName(this.targetQS);
            this.targetResultParameter = getPParameterByName(querySpecificationToParameterMappingsMap
                    .get(this.targetQS).get(targetResultParameterName));
            
            String tfeResultParameterName = VQLInterpreter.getResultParameterName(this.targetFinderExpressionQS);
            this.tfeResultParameter = getPParameterByName(querySpecificationToParameterMappingsMap
                    .get(targetFinderExpressionQS).get(tfeResultParameterName));
            
            String tfeContextParameterName = VQLInterpreter.getContextParameterName(this.targetFinderExpressionQS);
            this.tfeContextParameter = getPParameterByName(querySpecificationToParameterMappingsMap
                    .get(targetFinderExpressionQS).get(tfeContextParameterName));

            addAnnotations(targetFinderExpressionQS, Sets.<String>newHashSet(VQLInterpreterConstants.ANNOTATION_RETURN, VQLInterpreterConstants.ANNOTATION_CONTEXT));
        }

        @Override
        protected Set<PBody> doGetContainedBodies() throws QueryInitializationException {
            Set<PBody> bodies = super.doGetContainedBodies();
            
            for (PBody body : bodies) {
                PVariable var_sourceResult = body.getOrCreateVariableByName(sourceResultParameter.getName());
                PVariable var_targetResult = body.getOrCreateVariableByName(targetResultParameter.getName());
                PVariable var_tfeResult = body.getOrCreateVariableByName(tfeResultParameter.getName());
                PVariable var_tfeContext = body.getOrCreateVariableByName(tfeContextParameter.getName());
                
                new Equality(body, var_sourceResult, var_tfeContext);
                new Equality(body, var_targetResult, var_tfeResult);
            }
            
            return bodies;
        }

        public IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> getTargetFinderExpressionQS() {
            return targetFinderExpressionQS;
        }

        public IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> getSourceQS() {
            return sourceQS;
        }

        public IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> getTargetQS() {
            return targetQS;
        }
    }
}
