package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.actions;

import org.eclipse.incquery.viewmodel.configuration.TransformationRuleDescriptor;
import org.eclipse.incquery.viewmodel.core.actions.ElementRuleMatchAppeared;
import org.eclipse.incquery.viewmodel.traceability.EObjectTarget;
import org.eclipse.incquery.viewmodel.traceability.Trace;
import org.eclipse.sirius.diagram.DEdge;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.DEdgeElementRule;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;

public abstract class DEdgeElementRuleMatchAppeared<T extends DEdgeElementRule<TransformationRuleDescriptor, TransformationRuleDescriptor>>
        extends ElementRuleMatchAppeared<T> {

    public DEdgeElementRuleMatchAppeared(T rule) {
        super(rule);
    }

    @Override
    public Trace doProcess(IPatternMatch match) {
        Trace trace = super.doProcess(match);
        
        DEdge newEdge = (DEdge) ((EObjectTarget) trace.getTarget()).getTarget();
        newEdge.setActualMapping(rule.getEdgeMapping());
        
        return trace;
    }
    
}
