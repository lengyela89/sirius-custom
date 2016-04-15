package org.eclipse.sirius.tools.api.ui.refresh.rules;

import org.eclipse.incquery.viewmodel.configuration.ElementRuleDescriptor;
import org.eclipse.incquery.viewmodel.core.ViewModelManager;
import org.eclipse.incquery.viewmodel.core.rules.ElementRule;
import org.eclipse.viatra.query.runtime.api.IMatchProcessor;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;

public class RootElementRule extends ElementRule {

    public RootElementRule(ElementRuleDescriptor ruleDescriptor, ViewModelManager viewModelManager) {
        super(ruleDescriptor, viewModelManager);
    }

    @Override
    public IMatchProcessor<?> getAppearedAction() {
        return new IMatchProcessor<IPatternMatch>() {

            @Override
            public void process(IPatternMatch match) {
                // Nothing to do...
                System.out.println("RootElementRule appeared!"); //$NON-NLS-1$
            }
            
        };
    }

    @Override
    public IMatchProcessor<?> getDisappearedAction() {
        return new IMatchProcessor<IPatternMatch>() {

            @Override
            public void process(IPatternMatch match) {
                // Nothing to do...
                System.out.println("RootElementRule disappeared!"); //$NON-NLS-1$
            }
            
        };
    }

    
}
