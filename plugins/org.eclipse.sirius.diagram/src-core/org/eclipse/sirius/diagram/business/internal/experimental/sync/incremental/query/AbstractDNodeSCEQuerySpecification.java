package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import java.util.Set;

import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql.VQLInterpreterConstants;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.AbstractDNodeSCEQuerySpecification.AbstractDNodeSCEPQuery;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.SiriusCompositeQuerySpecification.SiriusCompositePQuery;
import org.eclipse.viatra.query.runtime.matchers.psystem.PBody;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.QueryInitializationException;

import com.google.common.collect.Sets;

public class AbstractDNodeSCEQuerySpecification extends SiriusCompositeQuerySpecification<AbstractDNodeSCEPQuery> {

    public AbstractDNodeSCEQuerySpecification(String patternFQN, SiriusQuerySpecification<? extends PQuery> parentQS,
            SiriusQuerySpecification<? extends PQuery> childQS) {
        super(new AbstractDNodeSCEPQuery(patternFQN, parentQS, childQS));
    }
    
    public static class AbstractDNodeSCEPQuery extends SiriusCompositePQuery {

        private SiriusQuerySpecification<? extends PQuery> parentQS;
        private SiriusQuerySpecification<? extends PQuery> childQS;

        @SuppressWarnings("unchecked")
        public AbstractDNodeSCEPQuery(String patternFQN, SiriusQuerySpecification<? extends PQuery> parentQS,
                SiriusQuerySpecification<? extends PQuery> childQS) {
            super(patternFQN, parentQS, childQS);
            
            this.parentQS = parentQS;
            this.childQS = childQS;
            
            addAnnotations(this.childQS, Sets.newHashSet(VQLInterpreterConstants.ANNOTATION_RETURN, VQLInterpreterConstants.ANNOTATION_CONTEXT));
        }

        public SiriusQuerySpecification<? extends PQuery> getParentQS() {
            return parentQS;
        }

        public SiriusQuerySpecification<? extends PQuery> getChildQS() {
            return childQS;
        }

        @Override
        protected Set<PBody> doGetContainedBodies() throws QueryInitializationException {
            Set<PBody> bodies = super.doGetContainedBodies();
            for (PBody body : bodies) {
                addEquality(
                        body,
                        getCorrespondingParameter(parentQS, parentQS.getResultParameterName()),
                        getCorrespondingParameter(childQS, childQS.getContextParameterName()));
            }
            
            return bodies;
        }
        
    }

}
