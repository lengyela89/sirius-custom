package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.actions;

import org.eclipse.incquery.viewmodel.core.actions.ElementRuleMatchAppeared;
import org.eclipse.incquery.viewmodel.traceability.Trace;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.RootElementRule;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;

public class RootElementRuleMatchAppeared extends ElementRuleMatchAppeared<RootElementRule> {

    public RootElementRuleMatchAppeared(RootElementRule rule) {
        super(rule);
    }

    @Override
    public Trace doProcess(IPatternMatch match) {
        Trace trace = traceabilityModelManager.getTrace(match, rule.getRuleDescriptor().getId());
        if (trace != null) {
            return trace;
        } else {
            return traceabilityModelManager.createTrace(match, rule.getRuleDescriptor().getId(), getSourcesForMatch(match), rule.getDiagram());
        }
    }

    
}
