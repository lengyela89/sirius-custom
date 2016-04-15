package org.eclipse.sirius.tools.api.ui.refresh;

import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.incquery.viewmodel.configuration.Configuration;
import org.eclipse.incquery.viewmodel.configuration.ConfigurationFactory;
import org.eclipse.incquery.viewmodel.configuration.ElementRuleDescriptor;
import org.eclipse.incquery.viewmodel.configuration.ReferenceRuleDescriptor;
import org.eclipse.incquery.viewmodel.configuration.Scheduler;
import org.eclipse.incquery.viewmodel.core.ViewModelManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.DiagramFactory;
import org.eclipse.sirius.diagram.business.api.componentization.DiagramMappingsManager;
import org.eclipse.sirius.diagram.business.api.query.ContainerMappingQuery;
import org.eclipse.sirius.diagram.business.internal.componentization.mappings.DiagramDescriptionMappingsManagerImpl;
import org.eclipse.sirius.diagram.business.internal.componentization.mappings.DiagramMappingsManagerImpl;
import org.eclipse.sirius.diagram.description.ContainerMapping;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.diagram.description.DiagramElementMapping;
import org.eclipse.sirius.diagram.description.EdgeMapping;
import org.eclipse.sirius.diagram.description.NodeMapping;
import org.eclipse.sirius.tools.api.ui.refresh.helper.vql.VQLInterpreter;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.exception.ViatraQueryException;
import org.eclipse.viatra.transformation.runtime.emf.transformation.eventdriven.InconsistentEventSemanticsException;

import com.google.common.collect.Sets;

public class DSemanticDiagramIncrementalRefresh {

    private Session session = null;
    
    private DSemanticDiagram diagram = null;
    
    private DiagramDescription description = null;
    
    private VQLInterpreter vqlInterpreter = null;
    
    private ViewModelManager viewModelManager = null;
    
    private DiagramMappingsManager diagramMappingsManager = null;

    private SiriusNotationModelIncrementalRefreshTransformationInitializer transformationInitializer;

    
    public DSemanticDiagramIncrementalRefresh(DSemanticDiagram diagram, Session session) {
        if (diagram == null) {
            throw new IllegalArgumentException("The 'diagram' parameter can not be null!"); //$NON-NLS-1$
        }
        
        if (session == null) {
            throw new IllegalArgumentException("The 'session' parameter can not be null!"); //$NON-NLS-1$
        }
        
        if (diagram.getDescription() == null) {
            throw new IllegalArgumentException("The description for the given 'diagram' parameter can not be null!"); //$NON-NLS-1$
        }
        
        this.diagram = diagram;
        this.session = session;
        this.description = diagram.getDescription();
        this.vqlInterpreter = new VQLInterpreter();
        this.transformationInitializer = new SiriusNotationModelIncrementalRefreshTransformationInitializer();
        this.diagramMappingsManager = new DiagramMappingsManagerImpl(diagram, new DiagramDescriptionMappingsManagerImpl(description));
    }
    
    public void initialize() throws ViatraQueryException, InconsistentEventSemanticsException {
        diagramMappingsManager.computeMappings(session.getSelectedViewpoints(false), true);
        
        Configuration configuration = createConfigurationModel();
        
        // TODO: ez nem ide kellene
        transformationInitializer.setDiagram(diagram);
        
        viewModelManager = new ViewModelManager(configuration, transformationInitializer);
        
        viewModelManager.initialize();
    }
    
    private Configuration createConfigurationModel() {
        Configuration configuration = ConfigurationFactory.eINSTANCE.createConfiguration();
        configuration.setScheduler(Scheduler.MANUAL);
        configuration.setSourceModel(diagram.getTarget().eResource());
        configuration.setTargetModel(diagram.eResource());
        
        createRuleDescriptionForRootElement(configuration);
        
        Set<DiagramElementMapping> diagramElementMappings = Sets.newHashSet();
        diagramElementMappings.addAll(diagramMappingsManager.getNodeMappings());
        diagramElementMappings.addAll(diagramMappingsManager.getContainerMappings());
//        diagramElementMappings.addAll(diagramMappingsManager.getEdgeMappings());
        
        for (DiagramElementMapping mapping : diagramElementMappings) {
            processDiagramElementMapping(mapping, configuration);
        }
        
        refreshTransformationRuleDescriptorIds(configuration);
        
        return configuration;
    }
    
    // TODO: ez kezdetleges megoldas!!!! Javitani kell! Forras elemet le lehessen kotni!!!
    // TODO: expression nagyon nem jo igy....
    private void createRuleDescriptionForRootElement(Configuration configuration) {
        EClass rootElementType = diagram.getTarget().eClass();
        
        String expression = "vql: pattern element_dSemanticDiagram(semanticRoot : " + rootElementType.getName() + ") { " + rootElementType.getName() + "(semanticRoot); }";   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
        
        IQuerySpecification<?> querySpecification = vqlInterpreter.getQuerySpecification(description, expression);
        transformationInitializer.getQuerySpecifications().add(querySpecification);
        
        ElementRuleDescriptor ruleDescriptor = ConfigurationFactory.eINSTANCE.createElementRuleDescriptor();
        ruleDescriptor.setConfiguration(configuration);
        ruleDescriptor.setElementType(DiagramFactory.eINSTANCE.getDiagramPackage().getDSemanticDiagram());
        ruleDescriptor.setPatternFQN(querySpecification.getFullyQualifiedName());
        
        transformationInitializer.setRootElementRuleDescriptor(ruleDescriptor);
    }
    
    // TODO: lehetne szebben is, okosabban is...(hasonloan a Siriusos eredeti megvalositashoz)
    // TODO: egymasbaagyazott mappingek kezelese!
    private void processDiagramElementMapping(DiagramElementMapping mapping, Configuration configuration) {
        // TODO: logger
        System.out.println("Process DiagramElementMapping (" + mapping.getName() + ")");  //$NON-NLS-1$ //$NON-NLS-2$
        
        if (mapping instanceof NodeMapping) {
            NodeMapping nodeMapping = (NodeMapping) mapping;

            IQuerySpecification<?> querySpecification = vqlInterpreter.getQuerySpecification(description, nodeMapping.getSemanticCandidatesExpression());
            transformationInitializer.getQuerySpecifications().add(querySpecification);
            
            ElementRuleDescriptor ruleDescriptor = ConfigurationFactory.eINSTANCE.createElementRuleDescriptor();
            ruleDescriptor.setConfiguration(configuration);
            ruleDescriptor.setElementType(DiagramFactory.eINSTANCE.getDiagramPackage().getDNode());
            ruleDescriptor.setPatternFQN(querySpecification.getFullyQualifiedName());
        } else if (mapping instanceof ContainerMapping) {
            ContainerMapping containerMapping = (ContainerMapping) mapping;

            IQuerySpecification<?> querySpecification = vqlInterpreter.getQuerySpecification(description, containerMapping.getSemanticCandidatesExpression());
            transformationInitializer.getQuerySpecifications().add(querySpecification);
            
            EClass elementType = null;
            if (new ContainerMappingQuery(containerMapping).isListContainer()) {
                elementType = DiagramFactory.eINSTANCE.getDiagramPackage().getDNodeList();
            } else {
                elementType = DiagramFactory.eINSTANCE.getDiagramPackage().getDNodeContainer();
            }
            
            ElementRuleDescriptor ruleDescriptor = ConfigurationFactory.eINSTANCE.createElementRuleDescriptor();
            ruleDescriptor.setConfiguration(configuration);
            ruleDescriptor.setElementType(elementType);
            ruleDescriptor.setPatternFQN(querySpecification.getFullyQualifiedName());
        } else if (mapping instanceof EdgeMapping) {
            throw new IllegalStateException("Edge mappings are not yet supported!"); //$NON-NLS-1$
        }
    }
    
    // TODO: expression így nyilván egyáltalán nem lesz jó!
    private void createReferenceRule(Configuration configuration, ElementRuleDescriptor owner, ElementRuleDescriptor target, EReference reference) {
        String expression = "vql: pattern ref_dSemanticDiagram_ownedDiagramElements(semanticRoot, ) {}";
        
        ReferenceRuleDescriptor ruleDescriptor = ConfigurationFactory.eINSTANCE.createReferenceRuleDescriptor();
        ruleDescriptor.setConfiguration(configuration);
        ruleDescriptor.setOwnerElementRuleDescriptor(owner);
        ruleDescriptor.setTargetElementRuleDescriptor(target);
        ruleDescriptor.setReference(reference);
        ruleDescriptor.setPatternFQN(querySpecification.getFullyQualifiedName());
    }
    
    private void refreshTransformationRuleDescriptorIds(Configuration configurationModel) {
        if (configurationModel == null) {
            throw new IllegalStateException("ConfigurationModel can not be null!"); //$NON-NLS-1$
        }
        
        for (int i = 0; i < configurationModel.getTransformationRuleDescriptors().size(); i++) {
            configurationModel.getTransformationRuleDescriptors().get(i).setId(i);
        }
    }
}
