package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.actions;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.incquery.viewmodel.traceability.EObjectTarget;
import org.eclipse.incquery.viewmodel.traceability.Trace;
import org.eclipse.sirius.diagram.DEdge;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.ElementBasedEdgeSCEQuerySpecification;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.ElementBasedEdgeElementRule;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;

public class ElementBasedEdgeElementRuleMatchAppeared extends DEdgeElementRuleMatchAppeared<ElementBasedEdgeElementRule> {

    public ElementBasedEdgeElementRuleMatchAppeared(ElementBasedEdgeElementRule rule) {
        super(rule);
    }

    @Override
    public Trace doProcess(IPatternMatch match) {
        Trace trace = super.doProcess(match);

        String resultParameterName = ((ElementBasedEdgeSCEQuerySpecification) match.specification()).getResultParameterName();
        EObject sourceSemanticElement = (EObject) match.get(resultParameterName);
        
        DEdge newEdge = (DEdge) ((EObjectTarget) trace.getTarget()).getTarget();
        newEdge.setTarget(sourceSemanticElement);
        
        return trace;
    }

}
