package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql.VQLInterpreter;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.CompositeQuerySpecification.CompositePQuery;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.ElementBasedEdgeCompositeQuerySpecification.ElementBasedEdgeCompositePQuery;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.matchers.psystem.PBody;
import org.eclipse.viatra.query.runtime.matchers.psystem.PVariable;
import org.eclipse.viatra.query.runtime.matchers.psystem.annotations.PAnnotation;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicdeferred.Equality;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PParameter;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.QueryInitializationException;

import com.google.common.collect.Sets;

public class ElementBasedEdgeCompositeQuerySpecification extends CompositeQuerySpecification<ElementBasedEdgeCompositePQuery> {

    public ElementBasedEdgeCompositeQuerySpecification(DSemanticDiagram diagram, long ruleDescriptorId,
            IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> finderExpressionQS,
            IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> finderMappingQS,
            IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> semanticCandidatesExpressionQS) {
        super(new ElementBasedEdgeCompositePQuery(diagram, ruleDescriptorId, finderExpressionQS, finderMappingQS, semanticCandidatesExpressionQS));
    }

    public static class ElementBasedEdgeCompositePQuery extends CompositePQuery {
        private IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> finderExpressionQS;

        private IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> finderMappingQS;

        private IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> semanticCandidatesExpressionQS;

        private PParameter semanticCandidatesExpressionResultParameter;

        private PParameter finderMappingResultParameter;

        private PParameter finderExpressionContextParameter;

        private PParameter finderExpressionResultParameter;


        
        @SuppressWarnings("unchecked")
        public ElementBasedEdgeCompositePQuery(DSemanticDiagram diagram, long ruleDescriptorId,
                IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> finderExpressionQS,
                IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> finderMappingQS,
                IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> semanticCandidatesExpressionQS) {
            
            super(diagram, ruleDescriptorId, finderExpressionQS, finderMappingQS, semanticCandidatesExpressionQS);

            this.finderExpressionQS = finderExpressionQS;
            this.finderMappingQS = finderMappingQS;
            this.semanticCandidatesExpressionQS = semanticCandidatesExpressionQS;

            this.finderMappingResultParameter = getPParameterByName(VQLInterpreter.getResultParameterName(this.finderMappingQS));
            this.finderExpressionResultParameter = getPParameterByName(VQLInterpreter.getResultParameterName(this.finderExpressionQS));
            this.finderExpressionContextParameter = getPParameterByName(VQLInterpreter.getContextParameterName(this.finderExpressionQS));
            this.semanticCandidatesExpressionResultParameter = getPParameterByName(VQLInterpreter.getResultParameterName(this.semanticCandidatesExpressionQS));
            
            /* Adding annotations */
            PAnnotation newAnnotation = null;
            for (PAnnotation annotation : finderExpressionQS.getAllAnnotations()) {
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
            Set<PBody> bodies = Sets.newHashSet();

            for (PBody body : bodies) {
                PVariable var_finderMappingResult = body.getOrCreateVariableByName(finderMappingResultParameter.getName());
                PVariable var_finderExpressionResult = body.getOrCreateVariableByName(finderExpressionResultParameter.getName());
                PVariable var_finderExpressionContext = body.getOrCreateVariableByName(finderExpressionContextParameter.getName());
                PVariable var_semanticCandidatesExpressionResult = body.getOrCreateVariableByName(semanticCandidatesExpressionResultParameter.getName());
                
                new Equality(body, var_semanticCandidatesExpressionResult, var_finderExpressionContext);
                new Equality(body, var_finderMappingResult, var_finderExpressionResult);
            }
            
            return bodies;
            
        }
        
    }
}
