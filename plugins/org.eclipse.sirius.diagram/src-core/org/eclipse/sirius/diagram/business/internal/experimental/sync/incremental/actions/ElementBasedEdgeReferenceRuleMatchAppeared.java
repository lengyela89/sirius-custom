package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.actions;

import java.util.Collection;

import org.eclipse.incquery.viewmodel.core.actions.ReferenceRuleMatchAppeared;
import org.eclipse.incquery.viewmodel.traceability.ReferenceTarget;
import org.eclipse.incquery.viewmodel.traceability.Trace;
import org.eclipse.sirius.diagram.DEdge;
import org.eclipse.sirius.diagram.EdgeTarget;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.DDiagramElementSynchronizer;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.ElementBasedEdgeReferenceRule;
import org.eclipse.sirius.diagram.description.DiagramElementMapping;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;

import com.google.common.collect.Maps;

public class ElementBasedEdgeReferenceRuleMatchAppeared extends ReferenceRuleMatchAppeared<ElementBasedEdgeReferenceRule> {

    public ElementBasedEdgeReferenceRuleMatchAppeared(ElementBasedEdgeReferenceRule rule) {
        super(rule);
    }

    @Override
    public Trace doProcess(IPatternMatch match) {
        Trace trace = super.doProcess(match);
        
        DEdge edge = (DEdge) ((ReferenceTarget) trace.getTarget()).getOwner().getTarget();

        DDiagramElementSynchronizer dDiagramElementSynchronizer = rule.getRuleProvider().getdDiagramElementSynchronizer();
        
        if (edge.getSourceNode() instanceof EdgeTarget &&
                edge.getTargetNode() instanceof EdgeTarget) {
            //dDiagramElementSynchronizer.refresh(edge);
            dDiagramElementSynchronizer.updatePath(edge, rule.getMapping(),
                    Maps.<DiagramElementMapping, Collection<EdgeTarget>>newHashMap());
        }
        
        return trace;
    }

}
