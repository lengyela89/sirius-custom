package org.eclipse.sirius.tools.api.ui.refresh;

import java.util.Map;

import org.eclipse.incquery.viewmodel.configuration.RuleDescriptor;
import org.eclipse.incquery.viewmodel.core.AbstractRuleProvider;
import org.eclipse.incquery.viewmodel.core.rules.ViewModelRule;

public class SiriusNotationModelIncrementalRefreshRuleProvider extends AbstractRuleProvider {

    public SiriusNotationModelIncrementalRefreshRuleProvider() {
        super();
    }
    
    public Map<RuleDescriptor, ViewModelRule<? extends RuleDescriptor>> getRuleDescriptorToRuleMap() {
        return ruleDescriptorToRule;
    }
    
}
