package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import java.util.Set;

import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql.VQLInterpreter;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql.VQLInterpreterConstants;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.CompositeQuerySpecification.CompositePQuery;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.ResultContextEqualityCompositeQuerySpecification.AbstractDNodeCompositePQuery;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.matchers.psystem.PBody;
import org.eclipse.viatra.query.runtime.matchers.psystem.PVariable;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicdeferred.Equality;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PParameter;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.QueryInitializationException;

import com.google.common.collect.Sets;

public class ResultContextEqualityCompositeQuerySpecification extends CompositeQuerySpecification<AbstractDNodeCompositePQuery> {

    public ResultContextEqualityCompositeQuerySpecification(DSemanticDiagram diagram, long ruleDescriptorId,
            IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> parentQS,
            IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> childQS) {
        super(new AbstractDNodeCompositePQuery(diagram, ruleDescriptorId, parentQS, childQS));
    }

    public static class AbstractDNodeCompositePQuery extends CompositePQuery {
        private PParameter childContextParameter = null;
        
        private PParameter parentResultParameter = null;
        
        private IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> parentQS = null;
        
        private IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> childQS = null;


        @SuppressWarnings("unchecked")
        public AbstractDNodeCompositePQuery(DSemanticDiagram diagram, long ruleDescriptorId,
                IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> parentQS,
                IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> childQS) {

            super(diagram, ruleDescriptorId, parentQS, childQS);
            
            this.parentQS = parentQS;
            this.childQS = childQS;
            
            String childContextParameterName = VQLInterpreter.getContextParameterName(this.childQS);
            this.childContextParameter = getPParameterByName(querySpecificationToParameterMappingsMap.get(childQS).get(childContextParameterName));
            if (parentQS != null) {
                String parentResultParameterName = VQLInterpreter.getResultParameterName(this.parentQS);
                this.parentResultParameter = getPParameterByName(querySpecificationToParameterMappingsMap.get(parentQS).get(parentResultParameterName));
            }
            
            addAnnotations(childQS, Sets.<String>newHashSet(VQLInterpreterConstants.ANNOTATION_RETURN, VQLInterpreterConstants.ANNOTATION_CONTEXT));
        }
        
        public IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> getChildQS() {
            return this.childQS;
        }
        
        public IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> getParentQS() {
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
