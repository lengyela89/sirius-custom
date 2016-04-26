package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql.VQLInterpreter;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.AbstractDNodeCompositeQuerySpecification.AbstractDNodeCompositePQuery;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.CompositeQuerySpecification.CompositePQuery;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.matchers.psystem.PBody;
import org.eclipse.viatra.query.runtime.matchers.psystem.PVariable;
import org.eclipse.viatra.query.runtime.matchers.psystem.annotations.PAnnotation;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicdeferred.Equality;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PParameter;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.QueryInitializationException;

public class AbstractDNodeCompositeQuerySpecification extends CompositeQuerySpecification<AbstractDNodeCompositePQuery> {

    public AbstractDNodeCompositeQuerySpecification(DSemanticDiagram diagram, long ruleDescriptorId,
            IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> parentQS,
            IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> childQS) {
        super(new AbstractDNodeCompositePQuery(diagram, ruleDescriptorId, parentQS, childQS));
    }

    public static class AbstractDNodeCompositePQuery extends CompositePQuery {
        private PParameter childContextParameter = null;
        
        private PParameter parentResultParameter = null;
        
        private IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> parentQS = null;
        
        private IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> childQS = null;


        @SuppressWarnings("unchecked")
        public AbstractDNodeCompositePQuery(DSemanticDiagram diagram, long ruleDescriptorId,
                IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> parentQS,
                IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> childQS) {

            super(diagram, ruleDescriptorId, parentQS, childQS);
            
            this.parentQS = parentQS;
            this.childQS = childQS;
            
            this.childContextParameter = getPParameterByName(VQLInterpreter.getContextParameterName(this.childQS));
            if (parentQS != null) {
                this.parentResultParameter = getPParameterByName(VQLInterpreter.getResultParameterName(this.parentQS));
            }
            
            /* Adding annotations */
            PAnnotation newAnnotation = null;
            for (PAnnotation annotation : childQS.getAllAnnotations()) {
                newAnnotation = new PAnnotation(annotation.getName());
                for (Entry<String, Object> entry : annotation.getAllValues()) {
                    newAnnotation.addAttribute(entry.getKey(), entry.getValue());
                }
                
                addAnnotation(newAnnotation);
            }
            /* ****************** */
        }

        public IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> getChildQS() {
            return this.childQS;
        }
        
        public IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> getParentQS() {
            return this.parentQS;
        }
        
        @Override
        protected Set<PBody> doGetContainedBodies() throws QueryInitializationException {
            Set<PBody> bodies = super.doGetContainedBodies();
            
            for (PBody body : bodies) {
                if (parentQS != null) {
                    PVariable var_context = body.getOrCreateVariableByName(childContextParameter.getName());
                    PVariable var_result = body.getOrCreateVariableByName(parentResultParameter.getName());

                    new Equality(body, var_context, var_result);
                }
            }
            
            return bodies;
        }
    }
}
