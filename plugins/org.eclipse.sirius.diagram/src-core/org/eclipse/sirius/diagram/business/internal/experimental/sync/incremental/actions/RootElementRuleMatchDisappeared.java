package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.actions;

import org.eclipse.incquery.viewmodel.core.actions.ElementRuleMatchDisappeared;
import org.eclipse.incquery.viewmodel.traceability.Trace;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.RootElementRule;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;

public class RootElementRuleMatchDisappeared extends ElementRuleMatchDisappeared<RootElementRule> {

    public RootElementRuleMatchDisappeared(RootElementRule rule) {
        super(rule);
    }

    @Override
    public Trace doProcess(IPatternMatch match) {
        Trace trace = traceabilityModelManager.getTrace(match, rule.getRuleDescriptor().getId());
        if (trace != null) {
            traceabilityModelManager.removeTrace(match, rule.getRuleDescriptor().getId(), trace);
        }
        
        return trace;
    }

}
