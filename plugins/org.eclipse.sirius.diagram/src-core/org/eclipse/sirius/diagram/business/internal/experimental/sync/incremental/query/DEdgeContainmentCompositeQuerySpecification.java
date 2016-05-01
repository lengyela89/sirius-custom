package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.CompositeQuerySpecification.CompositePQuery;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.DEdgeContainmentCompositeQuerySpecification.DEdgeContainmentCompositePQuery;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;

public class DEdgeContainmentCompositeQuerySpecification extends CompositeQuerySpecification<DEdgeContainmentCompositePQuery> {

    public DEdgeContainmentCompositeQuerySpecification(DSemanticDiagram diagram, long ruleDescriptorId,
            IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> dSemanticDiagramCompositeQuerySpecification,
            IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> dEdgeCompositeQuerySpecification) {
        super(new DEdgeContainmentCompositePQuery(diagram, ruleDescriptorId, dSemanticDiagramCompositeQuerySpecification, dEdgeCompositeQuerySpecification));
    }
    
    public static class DEdgeContainmentCompositePQuery extends CompositePQuery {

        @SuppressWarnings("unchecked")
        public DEdgeContainmentCompositePQuery(DSemanticDiagram diagram, long ruleDescriptorId, 
                IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> dSemanticDiagramCompositeQuerySpecification,
                IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> dEdgeCompositeQuerySpecification) {
            super(diagram, ruleDescriptorId, dSemanticDiagramCompositeQuerySpecification, dEdgeCompositeQuerySpecification);
        }
        
    }

}
