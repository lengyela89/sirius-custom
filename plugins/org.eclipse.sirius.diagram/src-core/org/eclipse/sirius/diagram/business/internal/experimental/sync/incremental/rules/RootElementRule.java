package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules;

import org.eclipse.incquery.viewmodel.configuration.ElementRuleDescriptor;
import org.eclipse.incquery.viewmodel.core.ViewModelManager;
import org.eclipse.incquery.viewmodel.core.rules.ElementRule;
import org.eclipse.incquery.viewmodel.core.rules.ViewModelRule;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.actions.RootElementRuleMatchAppeared;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.actions.RootElementRuleMatchDisappeared;
import org.eclipse.viatra.query.runtime.api.IMatchProcessor;

public class RootElementRule extends ElementRule<ElementRuleDescriptor> {

    private Integer priority = null;
	private DSemanticDiagram diagram = null;
	
	
	public static class RootElementRuleCandidate extends RuleCandidate<ElementRuleDescriptor> {

		private DSemanticDiagram diagram;

		
        public RootElementRuleCandidate(ElementRuleDescriptor ruleDescriptor, Integer priority, DSemanticDiagram diagram) {
        	super(ruleDescriptor, priority);
        	
            this.diagram = diagram;
        }
		
        @Override
        public ViewModelRule<ElementRuleDescriptor> createRuleCandidate(ViewModelManager viewModelManager) {
            return new RootElementRule(ruleDescriptor, priority, viewModelManager, diagram);
        }
		
	}
	
    public RootElementRule(ElementRuleDescriptor ruleDescriptor, Integer priority, ViewModelManager viewModelManager,
    		DSemanticDiagram diagram) {
        super(ruleDescriptor, viewModelManager);
        
        this.diagram = diagram;
        
        this.priority = priority;
    }

    @Override
    public IMatchProcessor<?> getAppearedAction() {
        return new RootElementRuleMatchAppeared(this);
    }

    @Override
    public IMatchProcessor<?> getDisappearedAction() {
        return new RootElementRuleMatchDisappeared(this);
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
}
