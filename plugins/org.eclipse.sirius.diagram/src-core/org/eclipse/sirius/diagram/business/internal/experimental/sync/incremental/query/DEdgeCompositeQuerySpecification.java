package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.CompositeQuerySpecification.CompositePQuery;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.DEdgeCompositeQuerySpecification.DEdgeCompositePQuery;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;

public abstract class DEdgeCompositeQuerySpecification extends CompositeQuerySpecification<DEdgeCompositePQuery> {

    public DEdgeCompositeQuerySpecification(DEdgeCompositePQuery pQuery) {
        super(pQuery);
    }

    public static abstract class DEdgeCompositePQuery extends CompositePQuery {

        public DEdgeCompositePQuery(DSemanticDiagram diagram, long ruleDescriptorId,
                IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>... querySpecifications) {
            super(diagram, ruleDescriptorId, querySpecifications);
        }
        
    }

}
