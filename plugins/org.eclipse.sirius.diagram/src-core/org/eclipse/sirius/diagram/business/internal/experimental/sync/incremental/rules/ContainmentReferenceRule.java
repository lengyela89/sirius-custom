package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules;

import org.eclipse.incquery.viewmodel.configuration.ReferenceRuleDescriptor;
import org.eclipse.incquery.viewmodel.core.ViewModelManager;
import org.eclipse.incquery.viewmodel.core.rules.ReferenceRule;
import org.eclipse.incquery.viewmodel.core.rules.ViewModelRule;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.SiriusNotationModelIncrementalRefreshRuleProvider;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.actions.ContainmentReferenceRuleMatchAppeared;
import org.eclipse.sirius.diagram.description.DiagramElementMapping;
import org.eclipse.viatra.query.runtime.api.IMatchProcessor;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.transformation.evm.api.event.EventFilter;

public class ContainmentReferenceRule extends ReferenceRule<ReferenceRuleDescriptor> {

    private Integer priority;
    private DSemanticDiagram diagram;
    private DiagramElementMapping mapping;
    private SiriusNotationModelIncrementalRefreshRuleProvider ruleProvider;
    
    
    public static class ContainmentReferenceRuleCandidate extends RuleCandidate<ReferenceRuleDescriptor> {
        private EventFilter<IPatternMatch> eventFilter;
        private DSemanticDiagram diagram;
        private SiriusNotationModelIncrementalRefreshRuleProvider ruleProvider;
        private DiagramElementMapping mapping;

        public ContainmentReferenceRuleCandidate(ReferenceRuleDescriptor ruleDescriptor, Integer priority, EventFilter<IPatternMatch> eventFilter,
                DSemanticDiagram diagram, SiriusNotationModelIncrementalRefreshRuleProvider ruleProvider, DiagramElementMapping mapping) {
            super(ruleDescriptor, priority);
            
            this.eventFilter = eventFilter;
            this.diagram = diagram;
            this.ruleProvider = ruleProvider;
            this.mapping = mapping;
        }
        
        @Override
        public ViewModelRule<ReferenceRuleDescriptor> createRuleCandidate(ViewModelManager viewModelManager) {
            return new ContainmentReferenceRule(ruleDescriptor, priority, eventFilter, viewModelManager, diagram, ruleProvider, mapping);
        }

        public EventFilter<IPatternMatch> getEventFilter() {
            return eventFilter;
        }

        public SiriusNotationModelIncrementalRefreshRuleProvider getRuleProvider() {
            return ruleProvider;
        }

        public DiagramElementMapping getMapping() {
            return mapping;
        }

        public DSemanticDiagram getDiagram() {
            return diagram;
        }
    }
    
    public ContainmentReferenceRule(ReferenceRuleDescriptor ruleDescriptor, Integer priority, EventFilter<IPatternMatch> eventFilter,
            ViewModelManager viewModelManager, DSemanticDiagram diagram, SiriusNotationModelIncrementalRefreshRuleProvider ruleProvider,
            DiagramElementMapping mapping) {
        super(ruleDescriptor, eventFilter, viewModelManager);
        
        this.diagram = diagram;
        this.mapping = mapping;
        this.priority = priority;
        this.ruleProvider = ruleProvider;
    }

    @Override
    public IMatchProcessor<?> getAppearedAction() {
        return new ContainmentReferenceRuleMatchAppeared(this);
    }

    public DiagramElementMapping getMapping() {
        return mapping;
    }
    
    public DSemanticDiagram getDiagram() {
        return diagram;
    }

    @Override
    public int getPriority() {
        if (this.priority == null) {
            return super.getPriority();
        }
        
        return this.priority;
    }

    public SiriusNotationModelIncrementalRefreshRuleProvider getRuleProvider() {
        return ruleProvider;
    }
}
