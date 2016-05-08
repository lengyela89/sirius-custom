package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental;

import java.util.List;
import java.util.Map;

import org.eclipse.incquery.viewmodel.configuration.ElementRuleDescriptor;
import org.eclipse.incquery.viewmodel.configuration.RuleDescriptor;
import org.eclipse.incquery.viewmodel.core.TransformationInitializer;
import org.eclipse.incquery.viewmodel.core.ViewModelManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.business.api.componentization.DiagramMappingsManager;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.SiriusQuerySpecification;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.RuleCandidate;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery;
import org.eclipse.viatra.sirius.incrementalrefresh.measurement.util.MeasurementUtil;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SiriusNotationModelIncrementalRefreshTransformationInitializer implements TransformationInitializer {

    private Session session;
    private DSemanticDiagram diagram;
    private DiagramDescription description;
    private ElementRuleDescriptor rootElementRuleDescriptor;
    private SiriusNotationModelIncrementalRefreshRuleProvider ruleProvider;

    private List<RuleCandidate<?>> ruleCandidates;
    
    private Map<String, SiriusQuerySpecification<? extends PQuery>> fqnToQuerySpecificationMap;
    
    // TODO
    private int measurementId;
    private int mId;

    
    public SiriusNotationModelIncrementalRefreshTransformationInitializer(Session session, DSemanticDiagram diagram, DiagramDescription description,
            DiagramMappingsManager diagramMappingsManager) {
        this.session = session;
        this.diagram = diagram;
        this.description = description;
        
        this.ruleProvider = new SiriusNotationModelIncrementalRefreshRuleProvider(session, diagram, description, diagramMappingsManager);
        
        this.fqnToQuerySpecificationMap = Maps.newHashMap();
        
        this.ruleCandidates = Lists.newArrayList();
    }
    
    @Override
    public void afterInitialize(ViewModelManager viewModelManager) {
        if (this.diagram == null || this.fqnToQuerySpecificationMap == null) {
            throw new IllegalStateException("One of the required parameters is null!"); //$NON-NLS-1$
        }
        
        // TODO
        MeasurementUtil.getInstance().measureExecutionTime_Stop(mId, MeasurementUtil.MEASUREMENT_VMM_INIT);
        
        // TODO
        MeasurementUtil.getInstance().measureModelSize(MeasurementUtil.MEASUREMENT_SOURCE_MODEL_SIZE, diagram.getTarget());
        
        // TODO
        mId = MeasurementUtil.getInstance().measureExecutionTime_Start();
        
        /* Fire activations */
        viewModelManager.getExecutionSchema().startUnscheduledExecution();
        
        // TODO
        MeasurementUtil.getInstance().measureExecutionTime_Stop(mId, MeasurementUtil.MEASUREMENT_VMM_FIRST_EXEC);
        MeasurementUtil.getInstance().measureModelSize(MeasurementUtil.MEASUREMENT_TRACEABILITY_MODEL_SIZE, viewModelManager.getTraceabilityModelManager().getTraceability());
    }

    @Override
    public void beforeInitialize(ViewModelManager viewModelManager) {
        if (this.fqnToQuerySpecificationMap == null) {
            throw new IllegalStateException("One of the required parameters is null!"); //$NON-NLS-1$
        }
        
        // TODO
        mId = MeasurementUtil.getInstance().measureExecutionTime_Start();
        
        /**
         *  Set the TransactionalEditingDomain for the target model (Notation model).
         *   While the Sirius' refresh operation executed within a transaction, we don't
         *   have to execute the target modifications (used during the refresh) within transactions.
         */
        viewModelManager.setTargetTransactionalEditingDomain(null);
        
        /* Adding query specifications to the ViewModelManager */
        for (IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> querySpecification : fqnToQuerySpecificationMap.values()) {
            viewModelManager.getQuerySpecifications().put(querySpecification.getFullyQualifiedName(), querySpecification);
        }
        /* *************************************************** */
        
        /* Initialize SiriusNotationModelIncrementalRefreshRuleProvider */
        viewModelManager.setRuleProvider(ruleProvider);
        
        for (RuleCandidate<?> ruleCandidate : ruleCandidates) {
            this.ruleProvider.getRuleDescriptorToRuleMap().put(ruleCandidate.getRuleDescriptor(),
                    ruleCandidate.createRuleCandidate(viewModelManager));
        }
        /* ************************************************************ */
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

    public void addRuleQuerySpecification(SiriusQuerySpecification<? extends PQuery> querySpecification) {
        this.fqnToQuerySpecificationMap.put(querySpecification.getFullyQualifiedName(), querySpecification);
    }
    
    public SiriusQuerySpecification<? extends PQuery> getRuleQuerySpecification(String fullyQualifiedName) {
        return this.fqnToQuerySpecificationMap.get(fullyQualifiedName);
    }
    
    public void removeRuleQuerySpecification(SiriusQuerySpecification<? extends PQuery> querySpecification) {
        this.fqnToQuerySpecificationMap.remove(querySpecification.getFullyQualifiedName());
    }

    public SiriusNotationModelIncrementalRefreshRuleProvider getRuleProvider() {
        return ruleProvider;
    }

    public List<RuleCandidate<?>> getRuleCandidates() {
        return ruleCandidates;
    }
    
    public RuleCandidate<?> getRuleCandidate(RuleDescriptor ruleDescriptor) {
        for (RuleCandidate<?> ruleCandidate : ruleCandidates) {
            if (ruleCandidate.getRuleDescriptor() == ruleDescriptor) {
                return ruleCandidate;
            }
        }
        
        return null;
    }
}
