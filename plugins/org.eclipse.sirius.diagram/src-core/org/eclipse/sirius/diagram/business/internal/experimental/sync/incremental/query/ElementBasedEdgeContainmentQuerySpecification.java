package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.ReferenceQuerySpecification.ReferencePQuery;

import java.util.Set;

import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.ElementBasedEdgeContainmentQuerySpecification.ElementBasedEdgeContainmentPQuery;
import org.eclipse.viatra.query.runtime.matchers.psystem.PBody;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.QueryInitializationException;

public class ElementBasedEdgeContainmentQuerySpecification extends ReferenceQuerySpecification<ElementBasedEdgeContainmentPQuery> {

    public ElementBasedEdgeContainmentQuerySpecification(String patternFQN,
            SiriusQuerySpecification<? extends PQuery> ownerQS, SiriusQuerySpecification<? extends PQuery> targetQS) {
        super(patternFQN, ownerQS, targetQS);
    }

    public static class ElementBasedEdgeContainmentPQuery extends ReferencePQuery {

        public ElementBasedEdgeContainmentPQuery(String patternFQN,
                SiriusQuerySpecification<? extends PQuery> ownerQS, SiriusQuerySpecification<? extends PQuery> targetQS) {
            super(patternFQN, ownerQS, targetQS);
        }

        @Override
        protected Set<PBody> doGetContainedBodies() throws QueryInitializationException {
            Set<PBody> bodies = super.doGetContainedBodies();
            
            for (PBody body : bodies) {
                addEquality(body,
                        getCorrespondingParameter(ownerQS, ownerQS.getResultParameterName()),
                        getCorrespondingParameter(targetQS, targetQS.getContextParameterName()));
            }
            
            return bodies;
        }

        
    }
}
