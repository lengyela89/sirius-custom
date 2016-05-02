package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules;

import org.eclipse.incquery.viewmodel.configuration.ReferenceRuleDescriptor;
import org.eclipse.incquery.viewmodel.core.ViewModelManager;
import org.eclipse.incquery.viewmodel.core.rules.ReferenceRule;
import org.eclipse.incquery.viewmodel.core.rules.ViewModelRule;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.SiriusNotationModelIncrementalRefreshRuleProvider;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.actions.ElementBasedEdgeReferenceRuleMatchAppeared;
import org.eclipse.sirius.diagram.description.EdgeMapping;
import org.eclipse.viatra.query.runtime.api.IMatchProcessor;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.transformation.evm.api.event.EventFilter;

public class ElementBasedEdgeReferenceRule extends ReferenceRule<ReferenceRuleDescriptor> {

    private SiriusNotationModelIncrementalRefreshRuleProvider ruleProvider;
    private EdgeMapping mapping;

    public ElementBasedEdgeReferenceRule(ReferenceRuleDescriptor ruleDescriptor, EventFilter<IPatternMatch> eventFilter, ViewModelManager viewModelManager,
            SiriusNotationModelIncrementalRefreshRuleProvider ruleProvider, EdgeMapping mapping) {
        super(ruleDescriptor, eventFilter, viewModelManager);
        
        this.ruleProvider = ruleProvider;
        this.mapping = mapping;
    }

    public EdgeMapping getMapping() {
        return mapping;
    }

    public SiriusNotationModelIncrementalRefreshRuleProvider getRuleProvider() {
        return ruleProvider;
    }

    @Override
    public IMatchProcessor<?> getAppearedAction() {
        return new ElementBasedEdgeReferenceRuleMatchAppeared(this);
    }
    
    public static class ElementBasedEdgeReferenceRuleCandidate extends RuleCandidate<ReferenceRuleDescriptor> {

        private EdgeMapping mapping;
        private EventFilter<IPatternMatch> eventFilter;
        private SiriusNotationModelIncrementalRefreshRuleProvider ruleProvider;

        public ElementBasedEdgeReferenceRuleCandidate(ReferenceRuleDescriptor ruleDescriptor, Integer priority,
                EventFilter<IPatternMatch> eventFilter, SiriusNotationModelIncrementalRefreshRuleProvider ruleProvider, EdgeMapping mapping) {
            super(ruleDescriptor, priority);
            
            this.eventFilter = eventFilter;
            this.ruleProvider = ruleProvider;
            this.mapping = mapping;
        }

        @Override
        public ViewModelRule<ReferenceRuleDescriptor> createRuleCandidate(ViewModelManager viewModelManager) {
            return new ElementBasedEdgeReferenceRule(ruleDescriptor, eventFilter, viewModelManager, ruleProvider, mapping);
        }
        
    }
    
}
