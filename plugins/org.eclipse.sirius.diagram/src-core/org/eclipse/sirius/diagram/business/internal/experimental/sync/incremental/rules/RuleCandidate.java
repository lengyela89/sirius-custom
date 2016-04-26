package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules;

import org.eclipse.incquery.viewmodel.configuration.RuleDescriptor;
import org.eclipse.incquery.viewmodel.core.ViewModelManager;
import org.eclipse.incquery.viewmodel.core.rules.ViewModelRule;

public abstract class RuleCandidate<T extends RuleDescriptor> {
    protected Integer priority;
    
    protected T ruleDescriptor;
    
    public RuleCandidate(T ruleDescriptor) {
        // Null priority value means that we'll use the default priority value
        this(ruleDescriptor, null);
    }
    
    public RuleCandidate(T ruleDescriptor, Integer priority) {
        this.priority = priority;
        this.ruleDescriptor = ruleDescriptor;
    }
    
    public abstract ViewModelRule<T> createRuleCandidate(ViewModelManager viewModelManager);

    public T getRuleDescriptor() {
        return ruleDescriptor;
    }

    public Integer getPriority() {
        return priority;
    }
}
