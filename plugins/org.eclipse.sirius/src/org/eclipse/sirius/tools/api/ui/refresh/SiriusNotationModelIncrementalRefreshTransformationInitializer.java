package org.eclipse.sirius.tools.api.ui.refresh;

import java.util.Map;

import org.eclipse.incquery.viewmodel.configuration.ElementRuleDescriptor;
import org.eclipse.incquery.viewmodel.core.TransformationInitializer;
import org.eclipse.incquery.viewmodel.core.ViewModelManager;
import org.eclipse.incquery.viewmodel.traceability.TraceabilityModelManager;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.tools.api.ui.refresh.rules.RootElementRule;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.transformation.runtime.emf.filters.MatchParameterFilter;

import com.google.common.collect.Maps;

public class SiriusNotationModelIncrementalRefreshTransformationInitializer implements TransformationInitializer {

    private DSemanticDiagram diagram;
    private ElementRuleDescriptor rootElementRuleDescriptor;
    private SiriusNotationModelIncrementalRefreshRuleProvider ruleProvider;
    private Map<String, IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> fqnToQuerySpecificationMap;

    
    public SiriusNotationModelIncrementalRefreshTransformationInitializer() {
        this(null, null, Maps.<String, IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>>newHashMap());
    }
    
    public SiriusNotationModelIncrementalRefreshTransformationInitializer(DSemanticDiagram diagram,
            ElementRuleDescriptor rootElementRuleDescriptor, Map<String, IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> fqnToQuerySpecificationMap) {
        this.diagram = diagram;
        this.rootElementRuleDescriptor = rootElementRuleDescriptor;
        this.fqnToQuerySpecificationMap = fqnToQuerySpecificationMap;
        this.ruleProvider = new SiriusNotationModelIncrementalRefreshRuleProvider();
    }
    
    @Override
    public void afterInitialize(ViewModelManager viewModelManager) {
        if (this.diagram == null || this.rootElementRuleDescriptor == null || this.fqnToQuerySpecificationMap == null) {
            throw new IllegalStateException("One of the required parameters is null!"); //$NON-NLS-1$
        }
        
        /* Create trace for the root element */
        TraceabilityModelManager traceabilityModelManager = viewModelManager.getTraceabilityModelManager();
        
        // TODO: ez sem jo igy nyilvan...
        Map<String, Object> sources = Maps.newHashMap();
        sources.put("semanticRoot", diagram.getTarget()); //$NON-NLS-1$
        
        traceabilityModelManager.createTrace(rootElementRuleDescriptor.getId(), sources, diagram);
        /* ********************************* */
        
        /* Fire activations */
        viewModelManager.getExecutionSchema().startUnscheduledExecution();
    }

    @Override
    public void beforeInitialize(ViewModelManager viewModelManager) {
        if (this.fqnToQuerySpecificationMap == null) {
            throw new IllegalStateException("One of the required parameters is null!"); //$NON-NLS-1$
        }
        
        /* Adding query specifications to the ViewModelManager */
        for (IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> querySpecification : fqnToQuerySpecificationMap.values()) {
            viewModelManager.getQuerySpecifications().put(querySpecification.getFullyQualifiedName(), querySpecification);
        }
        /* *************************************************** */

        // TODO: semanticRoot string...
        /* Set RuleProvider */
        Map<String, Object> filterMap = Maps.newHashMap();
        filterMap.put("semanticRoot", diagram.getTarget()); //$NON-NLS-1$
        
        RootElementRule rootElementRule = new RootElementRule((ElementRuleDescriptor) rootElementRuleDescriptor, viewModelManager);
        rootElementRule.setEventFilter(new MatchParameterFilter(filterMap));
        
        ruleProvider.getRuleDescriptorToRuleMap().put(rootElementRuleDescriptor, rootElementRule);
        viewModelManager.setRuleProvider(ruleProvider);
        /* **************** */
       
    }
    
    public DSemanticDiagram getDiagram() {
        return diagram;
    }

    public void setDiagram(DSemanticDiagram diagram) {
        this.diagram = diagram;
    }

    public ElementRuleDescriptor getRootElementRuleDescriptor() {
        return rootElementRuleDescriptor;
    }

    public void setRootElementRuleDescriptor(ElementRuleDescriptor rootElementRuleDescriptor) {
        this.rootElementRuleDescriptor = rootElementRuleDescriptor;
    }

    public Map<String, IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> getFQNToQuerySpecificationMap() {
        return fqnToQuerySpecificationMap;
    }

    public void setFQNToQuerySpecificationMap(Map<String, IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> fqnToQuerySpecificationMap) {
        this.fqnToQuerySpecificationMap = fqnToQuerySpecificationMap;
    }

    public SiriusNotationModelIncrementalRefreshRuleProvider getRuleProvider() {
        return ruleProvider;
    }
}
