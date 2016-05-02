package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.actions;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.incquery.viewmodel.core.actions.ElementRuleMatchAppeared;
import org.eclipse.incquery.viewmodel.traceability.EObjectTarget;
import org.eclipse.incquery.viewmodel.traceability.Trace;
import org.eclipse.sirius.diagram.AbstractDNode;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.DNodeList;
import org.eclipse.sirius.diagram.DNodeListElement;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.SiriusQuerySpecification;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.AbstractDNodeElementRule;
import org.eclipse.sirius.diagram.description.ContainerMapping;
import org.eclipse.sirius.diagram.description.NodeMapping;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery;

public class AbstractDNodeElementRuleMatchAppeared extends ElementRuleMatchAppeared<AbstractDNodeElementRule> {

    public AbstractDNodeElementRuleMatchAppeared(AbstractDNodeElementRule rule) {
        super(rule);
    }

    
    @Override
    public Trace doProcess(IPatternMatch match) {
        Trace trace = super.doProcess(match);
        EObjectTarget traceTarget = (EObjectTarget) trace.getTarget();

        @SuppressWarnings("unchecked")
        String resultParameterName = ((SiriusQuerySpecification<? extends PQuery>) match.specification()).getResultParameterName();
        EObject target = (EObject) match.get(resultParameterName);
        
        AbstractDNode newNode = (AbstractDNode) traceTarget.getTarget();
        newNode.setTarget(target);
        if (newNode instanceof DNodeContainer) {
            ((DNodeContainer) newNode).setActualMapping((ContainerMapping) rule.getMapping());
            ((DNodeContainer) newNode).setChildrenPresentation(((ContainerMapping) rule.getMapping()).getChildrenPresentation());
        } else if (newNode instanceof DNodeList) {
            ((DNodeList) newNode).setActualMapping((ContainerMapping) rule.getMapping());
        } else if (newNode instanceof DNode) {
            ((DNode) newNode).setActualMapping((NodeMapping) rule.getMapping());
        } else if (newNode instanceof DNodeListElement) {
            ((DNodeListElement) newNode).setActualMapping((NodeMapping) rule.getMapping());
        }
        
        return trace;
    }
}
