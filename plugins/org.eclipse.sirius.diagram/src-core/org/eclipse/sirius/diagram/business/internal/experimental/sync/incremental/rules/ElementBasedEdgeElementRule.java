package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules;

import org.eclipse.incquery.viewmodel.configuration.ElementRuleDescriptor;
import org.eclipse.incquery.viewmodel.configuration.TransformationRuleDescriptor;
import org.eclipse.incquery.viewmodel.core.ViewModelManager;
import org.eclipse.incquery.viewmodel.core.rules.ViewModelRule;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.actions.ElementBasedEdgeElementRuleMatchAppeared;
import org.eclipse.sirius.diagram.description.EdgeMapping;
import org.eclipse.viatra.query.runtime.api.IMatchProcessor;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.transformation.evm.api.event.EventFilter;

public class ElementBasedEdgeElementRule extends DEdgeElementRule<TransformationRuleDescriptor, TransformationRuleDescriptor> {

    public ElementBasedEdgeElementRule(ElementRuleDescriptor ruleDescriptor, EventFilter<IPatternMatch> eventFilter, ViewModelManager viewModelManager, EdgeMapping edgeMapping,
            TransformationRuleDescriptor sourceRuleDescriptor, TransformationRuleDescriptor targetRuleDescriptor) {
        super(ruleDescriptor, eventFilter, viewModelManager, edgeMapping, sourceRuleDescriptor, targetRuleDescriptor);
    }

    @Override
    public IMatchProcessor<?> getAppearedAction() {
        return new ElementBasedEdgeElementRuleMatchAppeared(this);
    }
    
    public static class ElementBasedEdgeElementRuleCandidate extends DEdgeElementRuleCandidate<TransformationRuleDescriptor, TransformationRuleDescriptor> {

        public ElementBasedEdgeElementRuleCandidate(ElementRuleDescriptor ruleDescriptor, Integer priority, EventFilter<IPatternMatch> eventFilter, EdgeMapping edgeMapping,
                TransformationRuleDescriptor sourceRuleDescriptor, TransformationRuleDescriptor targetRuleDescriptor) {
            super(ruleDescriptor, priority, eventFilter, edgeMapping, sourceRuleDescriptor, targetRuleDescriptor);
        }

        @Override
        public ViewModelRule<ElementRuleDescriptor> createRuleCandidate(ViewModelManager viewModelManager) {
            return new ElementBasedEdgeElementRule(ruleDescriptor, eventFilter, viewModelManager, edgeMapping, sourceRuleDescriptor, targetRuleDescriptor);
        }
        
    }
}
