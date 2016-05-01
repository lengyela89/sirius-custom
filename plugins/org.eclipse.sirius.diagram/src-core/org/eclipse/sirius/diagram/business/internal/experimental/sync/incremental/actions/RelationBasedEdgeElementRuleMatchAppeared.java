package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.actions;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.incquery.viewmodel.configuration.TransformationRuleDescriptor;
import org.eclipse.incquery.viewmodel.traceability.EObjectTarget;
import org.eclipse.incquery.viewmodel.traceability.Trace;
import org.eclipse.sirius.diagram.DEdge;
import org.eclipse.sirius.diagram.EdgeTarget;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.RelationBasedEdgeTFEQuerySpecification;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.SiriusQuerySpecification;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.RelationBasedEdgeElementRule;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery;

import com.google.common.collect.Maps;

public class RelationBasedEdgeElementRuleMatchAppeared extends DEdgeElementRuleMatchAppeared<RelationBasedEdgeElementRule> {

    public RelationBasedEdgeElementRuleMatchAppeared(RelationBasedEdgeElementRule rule) {
        super(rule);
    }

    @Override
    public Trace doProcess(IPatternMatch match) {
        Trace trace = super.doProcess(match);
        
        RelationBasedEdgeTFEQuerySpecification querySpecification = (RelationBasedEdgeTFEQuerySpecification) match.specification();
        
        TransformationRuleDescriptor sourceRuleDescriptor = rule.getSourceRuleDescriptor();
        TransformationRuleDescriptor targetRuleDescriptor = rule.getTargetRuleDescriptor();
        
        SiriusQuerySpecification<? extends PQuery> sourceQS = querySpecification.getInternalQueryRepresentation().getSourceQS();
        SiriusQuerySpecification<? extends PQuery> targetQS = querySpecification.getInternalQueryRepresentation().getTargetQS();

        Map<String, Object> sourceParametersMap = Maps.newHashMap();
        Map<String, String> sourceQSParameterMappings = querySpecification.getInternalQueryRepresentation().getSourceQSParameterMappings();
        for (String parameterName : sourceQS.getParameterNames()) {
            sourceParametersMap.put(parameterName, match.get(sourceQSParameterMappings.get(parameterName)));
        }

        Map<String, Object> targetParametersMap = Maps.newHashMap();
        Map<String, String> targetQSParameterMappings = querySpecification.getInternalQueryRepresentation().getTargetQSParameterMappings();
        for (String parameterName : targetQS.getParameterNames()) {
            targetParametersMap.put(parameterName, match.get(targetQSParameterMappings.get(parameterName)));
        }
        
        IPatternMatch sourceMatch = getPatternMatch(sourceQS.getFullyQualifiedName(), sourceParametersMap);
        IPatternMatch targetMatch = getPatternMatch(targetQS.getFullyQualifiedName(), targetParametersMap);
        
        Trace sourceTrace = traceabilityModelManager.getTrace(sourceMatch, sourceRuleDescriptor.getId());
        Trace targetTrace = traceabilityModelManager.getTrace(targetMatch, targetRuleDescriptor.getId());
        if (sourceTrace == null || targetTrace == null) {
            throw new IllegalStateException("The source or target trace can not be found for the given match!"); //$NON-NLS-1$
        }
        
        String contextParameterName = querySpecification.getContextParameterName();
        EObject sourceSemanticElement = (EObject) match.get(contextParameterName);
        
        EdgeTarget edgeSource = (EdgeTarget) ((EObjectTarget) sourceTrace.getTarget()).getTarget();
        EdgeTarget edgeTarget = (EdgeTarget) ((EObjectTarget) targetTrace.getTarget()).getTarget();

        DEdge newEdge = (DEdge) ((EObjectTarget) trace.getTarget()).getTarget();
        newEdge.setTarget(sourceSemanticElement);
        newEdge.setSourceNode(edgeSource);
        newEdge.setTargetNode(edgeTarget);
        
        return trace;
    }
}
