package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql.VQLInterpreter;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.CompositeQuerySpecification.CompositePQuery;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.RelationBasedEdgeCompositeQuerySpecification.RelationBasedEdgeCompositePQuery;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.matchers.psystem.PBody;
import org.eclipse.viatra.query.runtime.matchers.psystem.PVariable;
import org.eclipse.viatra.query.runtime.matchers.psystem.annotations.PAnnotation;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicdeferred.Equality;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PParameter;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.QueryInitializationException;

public class RelationBasedEdgeCompositeQuerySpecification extends CompositeQuerySpecification<RelationBasedEdgeCompositePQuery> {

    public RelationBasedEdgeCompositeQuerySpecification(DSemanticDiagram diagram, long ruleDescriptorId,
            IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> targetFinderExpressionQS,
            IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> sourceQS,
            IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> targetQS) {
        super(new RelationBasedEdgeCompositePQuery(diagram, ruleDescriptorId, targetFinderExpressionQS, sourceQS, targetQS));
    }

    public static class RelationBasedEdgeCompositePQuery extends CompositePQuery {
        
        private IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> targetFinderExpressionQS;

        private IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> sourceQS;

        private IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> targetQS;

        private PParameter sourceResultParameter;

        private PParameter targetResultParameter;

        private PParameter tfeResultParameter;

        private PParameter tfeContextParameter;


        
        @SuppressWarnings("unchecked")
        public RelationBasedEdgeCompositePQuery(DSemanticDiagram diagram, long ruleDescriptorId,
                IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> targetFinderExpressionQS,
                IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> sourceQS,
                IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> targetQS) {
            
            super(diagram, ruleDescriptorId, targetFinderExpressionQS, sourceQS, targetQS);

            this.targetFinderExpressionQS = targetFinderExpressionQS;
            this.sourceQS = sourceQS;
            this.targetQS = targetQS;
            
            this.sourceResultParameter = getPParameterByName(VQLInterpreter.getResultParameterName(this.sourceQS));
            this.targetResultParameter = getPParameterByName(VQLInterpreter.getResultParameterName(this.targetQS));
            this.tfeResultParameter = getPParameterByName(VQLInterpreter.getResultParameterName(this.targetFinderExpressionQS));
            this.tfeContextParameter = getPParameterByName(VQLInterpreter.getContextParameterName(this.targetFinderExpressionQS));
            
            /* Adding annotations */
            PAnnotation newAnnotation = null;
            for (PAnnotation annotation : targetFinderExpressionQS.getAllAnnotations()) {
                newAnnotation = new PAnnotation(annotation.getName());
                for (Entry<String, Object> entry : annotation.getAllValues()) {
                    newAnnotation.addAttribute(entry.getKey(), entry.getValue());
                }
                
                addAnnotation(newAnnotation);
            }
            /* ****************** */
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

        public IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> getTargetFinderExpressionQS() {
            return targetFinderExpressionQS;
        }

        public IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> getSourceQS() {
            return sourceQS;
        }

        public IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> getTargetQS() {
            return targetQS;
        }
    }
}
