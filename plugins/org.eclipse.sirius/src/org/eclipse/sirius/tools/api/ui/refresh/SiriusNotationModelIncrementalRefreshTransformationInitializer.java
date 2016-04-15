package org.eclipse.sirius.tools.api.ui.refresh;

import java.util.Map;
import java.util.Set;

import org.eclipse.incquery.viewmodel.configuration.ElementRuleDescriptor;
import org.eclipse.incquery.viewmodel.configuration.TransformationRuleDescriptor;
import org.eclipse.incquery.viewmodel.core.TransformationInitializer;
import org.eclipse.incquery.viewmodel.core.ViewModelManager;
import org.eclipse.incquery.viewmodel.traceability.TraceabilityModelManager;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.tools.api.ui.refresh.rules.RootElementRule;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class SiriusNotationModelIncrementalRefreshTransformationInitializer implements TransformationInitializer {

    private DSemanticDiagram diagram;
    private TransformationRuleDescriptor rootElementRuleDescriptor;
    private Set<IQuerySpecification<?>> querySpecifications;
    private SiriusNotationModelIncrementalRefreshRuleProvider ruleProvider;

    
    public SiriusNotationModelIncrementalRefreshTransformationInitializer() {
        this(null, null, Sets.<IQuerySpecification<?>>newHashSet());
    }
    
    public SiriusNotationModelIncrementalRefreshTransformationInitializer(DSemanticDiagram diagram,
            TransformationRuleDescriptor rootElementRuleDescriptor, Set<IQuerySpecification<?>> querySpecifications) {
        this.diagram = diagram;
        this.rootElementRuleDescriptor = rootElementRuleDescriptor;
        this.querySpecifications = querySpecifications;
        this.ruleProvider = new SiriusNotationModelIncrementalRefreshRuleProvider();
    }
    
    @Override
    public void afterInitialize(ViewModelManager viewModelManager) {
        if (this.diagram == null || this.rootElementRuleDescriptor == null || this.querySpecifications == null) {
            throw new IllegalStateException("One of the required parameters is null!"); //$NON-NLS-1$
        }
        
        /* Set RuleProvider */
        ruleProvider.getRuleDescriptorToRuleMap().put(rootElementRuleDescriptor,
                new RootElementRule((ElementRuleDescriptor) rootElementRuleDescriptor, viewModelManager));
        viewModelManager.setRuleProvider(ruleProvider);
        /* **************** */
        
        
        /* Create trace for the root element */
        TraceabilityModelManager traceabilityModelManager = viewModelManager.getTraceabilityModelManager();
        
        // TODO: ez sem jo így nyilván...
        Map<String, Object> sources = Maps.newHashMap();
        sources.put("semanticRoot", diagram.getTarget()); //$NON-NLS-1$
        
        traceabilityModelManager.createTrace(rootElementRuleDescriptor.getId(), sources, diagram);
        /* ********************************* */
        
        /* Fire activations */
        viewModelManager.getExecutionSchema().startUnscheduledExecution();
    }

    @Override
    public void beforeInitialize(ViewModelManager viewModelManager) {
        if (this.querySpecifications == null) {
            throw new IllegalStateException("One of the required parameters is null!"); //$NON-NLS-1$
        }
        
        /* Adding query specifications to the ViewModelManager */
        for (IQuerySpecification<?> querySpecification : querySpecifications) {
            viewModelManager.getQuerySpecifications().put(querySpecification.getFullyQualifiedName(), querySpecification);
        }
        /* *************************************************** */
    }
    
    public DSemanticDiagram getDiagram() {
        return diagram;
    }

    public void setDiagram(DSemanticDiagram diagram) {
        this.diagram = diagram;
    }

    public TransformationRuleDescriptor getRootElementRuleDescriptor() {
        return rootElementRuleDescriptor;
    }

    public void setRootElementRuleDescriptor(TransformationRuleDescriptor rootElementRuleDescriptor) {
        this.rootElementRuleDescriptor = rootElementRuleDescriptor;
    }

    public Set<IQuerySpecification<?>> getQuerySpecifications() {
        return querySpecifications;
    }

    public void setQuerySpecifications(Set<IQuerySpecification<?>> querySpecifications) {
        this.querySpecifications = querySpecifications;
    }

    public SiriusNotationModelIncrementalRefreshRuleProvider getRuleProvider() {
        return ruleProvider;
    }
}
