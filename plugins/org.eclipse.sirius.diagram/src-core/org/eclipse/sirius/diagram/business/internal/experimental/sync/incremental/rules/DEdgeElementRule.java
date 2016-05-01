package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules;

import org.eclipse.incquery.viewmodel.configuration.ElementRuleDescriptor;
import org.eclipse.incquery.viewmodel.configuration.TransformationRuleDescriptor;
import org.eclipse.incquery.viewmodel.core.ViewModelManager;
import org.eclipse.incquery.viewmodel.core.rules.ElementRule;
import org.eclipse.sirius.diagram.description.EdgeMapping;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.transformation.evm.api.event.EventFilter;

public abstract class DEdgeElementRule<SourceRuleDescriptor extends TransformationRuleDescriptor,
        TargetRuleDescriptor extends TransformationRuleDescriptor> extends ElementRule<ElementRuleDescriptor> {

    protected EdgeMapping edgeMapping;
    protected SourceRuleDescriptor sourceRuleDescriptor;
    protected TargetRuleDescriptor targetRuleDescriptor;

    
    public static abstract class DEdgeElementRuleCandidate<SourceRuleDescriptor extends TransformationRuleDescriptor,
            TargetRuleDescriptor extends TransformationRuleDescriptor> extends RuleCandidate<ElementRuleDescriptor> {

        protected EventFilter<IPatternMatch> eventFilter;
        protected EdgeMapping edgeMapping;
        protected SourceRuleDescriptor sourceRuleDescriptor;
        protected TargetRuleDescriptor targetRuleDescriptor;
        
        
        public DEdgeElementRuleCandidate(ElementRuleDescriptor ruleDescriptor, Integer priority,
                EventFilter<IPatternMatch> eventFilter, EdgeMapping edgeMapping,
                SourceRuleDescriptor sourceRuleDescriptor, TargetRuleDescriptor targetRuleDescriptor) {
            super(ruleDescriptor, priority);
            
            this.eventFilter = eventFilter;
            this.edgeMapping = edgeMapping;
            this.sourceRuleDescriptor = sourceRuleDescriptor;
            this.targetRuleDescriptor = targetRuleDescriptor;
        }


        public EdgeMapping getEdgeMapping() {
            return edgeMapping;
        }
    }
    
    public DEdgeElementRule(ElementRuleDescriptor ruleDescriptor, EventFilter<IPatternMatch> eventFilter, ViewModelManager viewModelManager,
            EdgeMapping edgeMapping, SourceRuleDescriptor sourceRuleDescriptor, TargetRuleDescriptor targetRuleDescriptor) {
        super(ruleDescriptor, eventFilter, viewModelManager);
        
        this.edgeMapping = edgeMapping;
        this.sourceRuleDescriptor = sourceRuleDescriptor;
        this.targetRuleDescriptor = targetRuleDescriptor;
    }

    public EdgeMapping getEdgeMapping() {
        return edgeMapping;
    }
    
    public SourceRuleDescriptor getSourceRuleDescriptor() {
        return sourceRuleDescriptor;
    }

    public TargetRuleDescriptor getTargetRuleDescriptor() {
        return targetRuleDescriptor;
    }
}
