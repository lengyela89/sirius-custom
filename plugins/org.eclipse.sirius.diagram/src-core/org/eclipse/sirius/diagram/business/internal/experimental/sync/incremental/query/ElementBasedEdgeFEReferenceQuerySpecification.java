package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import java.util.Set;

import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.ElementBasedEdgeFEReferenceQuerySpecification.ElementBasedEdgeFEReferencePQuery;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.ReferenceQuerySpecification.ReferencePQuery;
import org.eclipse.viatra.query.runtime.matchers.psystem.PBody;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.QueryInitializationException;

public class ElementBasedEdgeFEReferenceQuerySpecification extends ReferenceQuerySpecification<ElementBasedEdgeFEReferencePQuery> {

    
    public ElementBasedEdgeFEReferenceQuerySpecification(String patternFQN,
            SiriusQuerySpecification<? extends PQuery> ownerQS, SiriusQuerySpecification<? extends PQuery> targetQS) {
        super(new ElementBasedEdgeFEReferencePQuery(patternFQN, ownerQS, targetQS));
    }

    public static class ElementBasedEdgeFEReferencePQuery extends ReferencePQuery {

        public ElementBasedEdgeFEReferencePQuery(String patternFQN,
                SiriusQuerySpecification<? extends PQuery> ownerQS,
                SiriusQuerySpecification<? extends PQuery> targetQS) {
            super(patternFQN, ownerQS, targetQS);
        }

        @Override
        protected Set<PBody> doGetContainedBodies() throws QueryInitializationException {
            Set<PBody> bodies = super.doGetContainedBodies();
            
            for (PBody body : bodies) {
                addEquality(
                        body,
                        getCorrespondingParameter(ownerQS, ownerQS.getResultParameterName()),
                        getCorrespondingParameter(targetQS, targetQS.getContextParameterName()));
            }
            
            return bodies;
        }

        
    }
}
