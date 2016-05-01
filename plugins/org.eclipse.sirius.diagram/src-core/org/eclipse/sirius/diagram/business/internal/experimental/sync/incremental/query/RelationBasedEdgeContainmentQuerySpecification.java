package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.ReferenceQuerySpecification.ReferencePQuery;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.RelationBasedEdgeContainmentQuerySpecification.RelationBasedEdgeContainmentPQuery;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery;

public class RelationBasedEdgeContainmentQuerySpecification extends ReferenceQuerySpecification<RelationBasedEdgeContainmentPQuery> {

    public RelationBasedEdgeContainmentQuerySpecification(String patternFQN,
            SiriusQuerySpecification<? extends PQuery> ownerQS, SiriusQuerySpecification<? extends PQuery> targetQS) {
        super(patternFQN, ownerQS, targetQS);
    }

    public static class RelationBasedEdgeContainmentPQuery extends ReferencePQuery {

        public RelationBasedEdgeContainmentPQuery(String patternFQN,
                SiriusQuerySpecification<? extends PQuery> ownerQS, SiriusQuerySpecification<? extends PQuery> targetQS) {
            super(patternFQN, ownerQS, targetQS);
        }

    }
}
