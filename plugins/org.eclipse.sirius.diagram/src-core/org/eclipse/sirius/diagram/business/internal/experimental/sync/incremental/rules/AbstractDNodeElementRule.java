package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules;

import org.eclipse.incquery.viewmodel.configuration.ElementRuleDescriptor;
import org.eclipse.incquery.viewmodel.core.ViewModelManager;
import org.eclipse.incquery.viewmodel.core.rules.ElementRule;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.actions.AbstractDNodeElementRuleMatchAppeared;
import org.eclipse.sirius.diagram.description.DiagramElementMapping;
import org.eclipse.viatra.query.runtime.api.IMatchProcessor;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.transformation.evm.api.event.EventFilter;

public class AbstractDNodeElementRule extends ElementRule<ElementRuleDescriptor> {

    private Integer priority;
    private DiagramElementMapping mapping;

    
    public static class AbstractDNodeElementRuleCandidate extends RuleCandidate<ElementRuleDescriptor> {
        private EventFilter<IPatternMatch> eventFilter;
        private DiagramElementMapping mapping;

        
        public AbstractDNodeElementRuleCandidate(ElementRuleDescriptor ruleDescriptor, Integer priority, EventFilter<IPatternMatch> eventFilter,
                DiagramElementMapping mapping) {
            super(ruleDescriptor, priority);
            
            this.eventFilter = eventFilter;
            this.mapping = mapping;
        }
        
        @Override
        public AbstractDNodeElementRule createRuleCandidate(ViewModelManager viewModelManager) {
            return new AbstractDNodeElementRule(ruleDescriptor, priority, eventFilter, viewModelManager, mapping);
        }

        public EventFilter<IPatternMatch> getEventFilter() {
            return eventFilter;
        }

        public DiagramElementMapping getMapping() {
            return mapping;
        }
    }
    
    public AbstractDNodeElementRule(ElementRuleDescriptor ruleDescriptor, Integer priority, EventFilter<IPatternMatch> eventFilter,
            ViewModelManager viewModelManager, DiagramElementMapping mapping) {
        super(ruleDescriptor, eventFilter, viewModelManager);

        this.mapping = mapping;
        this.priority = priority;
    }

    @Override
    public IMatchProcessor<?> getAppearedAction() {
        return new AbstractDNodeElementRuleMatchAppeared(this);
    }

    public DiagramElementMapping getMapping() {
        return mapping;
    }

    @Override
    public int getPriority() {
        if (this.priority == null) {
            return super.getPriority();
        }
        
        return this.priority;
    }
}
