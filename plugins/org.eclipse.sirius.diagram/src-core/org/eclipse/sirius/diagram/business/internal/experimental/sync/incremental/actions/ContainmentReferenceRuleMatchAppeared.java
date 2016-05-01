package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.actions;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.incquery.viewmodel.core.actions.ReferenceRuleMatchAppeared;
import org.eclipse.incquery.viewmodel.traceability.ReferenceTarget;
import org.eclipse.incquery.viewmodel.traceability.Trace;
import org.eclipse.sirius.diagram.AbstractDNode;
import org.eclipse.sirius.diagram.DEdge;
import org.eclipse.sirius.diagram.EdgeTarget;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.DDiagramElementSynchronizer;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.ContainmentReferenceRule;
import org.eclipse.sirius.diagram.description.AbstractNodeMapping;
import org.eclipse.sirius.diagram.description.DiagramElementMapping;
import org.eclipse.sirius.diagram.description.EdgeMapping;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;

import com.google.common.collect.Maps;

public class ContainmentReferenceRuleMatchAppeared extends ReferenceRuleMatchAppeared<ContainmentReferenceRule> {

    public ContainmentReferenceRuleMatchAppeared(ContainmentReferenceRule rule) {
        super(rule);
    }

    @Override
    public Trace doProcess(IPatternMatch match) {
        Trace trace = super.doProcess(match);
        
        EObject target = ((ReferenceTarget) trace.getTarget()).getTarget().getTarget();
        DDiagramElementSynchronizer dDiagramElementSynchronizer = rule.getRuleProvider().getdDiagramElementSynchronizer();
        if (target instanceof AbstractDNode) {
            AbstractDNode node = (AbstractDNode) target;
            
            dDiagramElementSynchronizer.refresh(node);
            dDiagramElementSynchronizer.refreshSemanticElements(node, rule.getMapping());
            dDiagramElementSynchronizer.createStyle(node, (AbstractNodeMapping) rule.getMapping());
            dDiagramElementSynchronizer.computeVisibilityOnCreation(rule.getRuleProvider().getDiagramMappingsManager(), node);
        } else if (target instanceof DEdge) {
            DEdge edge = (DEdge) target;
            
            dDiagramElementSynchronizer.refreshSemanticElements(edge, rule.getMapping());
            dDiagramElementSynchronizer.createStyle(edge, (EdgeMapping) rule.getMapping(), rule.getDiagram());
            dDiagramElementSynchronizer.refresh(edge);
            dDiagramElementSynchronizer.updatePath(edge, (EdgeMapping) rule.getMapping(),
                    Maps.<DiagramElementMapping, Collection<EdgeTarget>>newHashMap());
        }
        
        return trace;
    }
}
