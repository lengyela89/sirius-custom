package org.eclipse.sirius.tools.api.ui.refresh;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.incquery.viewmodel.configuration.Configuration;
import org.eclipse.incquery.viewmodel.configuration.ConfigurationFactory;
import org.eclipse.incquery.viewmodel.configuration.ElementRuleDescriptor;
import org.eclipse.incquery.viewmodel.configuration.PatternParameterMapping;
import org.eclipse.incquery.viewmodel.configuration.ReferenceRuleDescriptor;
import org.eclipse.incquery.viewmodel.configuration.Scheduler;
import org.eclipse.incquery.viewmodel.core.ViewModelManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.DiagramFactory;
import org.eclipse.sirius.diagram.DiagramPackage;
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
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.exception.ViatraQueryException;
import org.eclipse.viatra.transformation.runtime.emf.transformation.eventdriven.InconsistentEventSemanticsException;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class DSemanticDiagramIncrementalRefresh {

    private Session session = null;
    
    private DSemanticDiagram diagram = null;
    
    private DiagramDescription description = null;
    
    private VQLInterpreter vqlInterpreter = null;
    
    private ViewModelManager viewModelManager = null;
    
    private DiagramMappingsManager diagramMappingsManager = null;

    private SiriusNotationModelIncrementalRefreshTransformationInitializer transformationInitializer;

    // TODO: torolni, es normalisan megcsinalni!
    private static long ID = -1L;
    
    // TODO: megoldani mashogy
    private Map<String, String> fqnToExpressionMap = null;
    
    
    
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
        
        // TODO: torolni
        this.fqnToExpressionMap = Maps.newHashMap();
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
            processDiagramElementMapping(mapping, configuration, transformationInitializer.getRootElementRuleDescriptor());
        }
        
        // TODO: elvileg nem kell, ha helyesen adjuk az IDkat!
        //refreshTransformationRuleDescriptorIds(configuration);
        
        return configuration;
    }
    
    // TODO: ez kezdetleges megoldas!!!! Javitani kell! Forras elemet le lehessen kotni!!!
    // TODO: expression nagyon nem jo igy....
    private void createRuleDescriptionForRootElement(Configuration configuration) {
        EClass rootElementType = diagram.getTarget().eClass();
        
        String expression = "vql: pattern element_dSemanticDiagram(semanticRoot : " + rootElementType.getName() + ") { " + rootElementType.getName() + "(semanticRoot); }";   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
        IQuerySpecification<?> querySpecification = vqlInterpreter.getQuerySpecification(description, expression);
        transformationInitializer.getFQNToQuerySpecificationMap().put(querySpecification.getFullyQualifiedName(), querySpecification);
        fqnToExpressionMap.put(querySpecification.getFullyQualifiedName(), expression.replace("vql:", ""));  //$NON-NLS-1$//$NON-NLS-2$
        
        ElementRuleDescriptor ruleDescriptor = ConfigurationFactory.eINSTANCE.createElementRuleDescriptor();
        ruleDescriptor.setId(++ID);
        ruleDescriptor.setConfiguration(configuration);
        ruleDescriptor.setElementType(DiagramFactory.eINSTANCE.getDiagramPackage().getDSemanticDiagram());
        ruleDescriptor.setPatternFQN(querySpecification.getFullyQualifiedName());
        
        transformationInitializer.setRootElementRuleDescriptor(ruleDescriptor);
    }
    
    // TODO: lehetne szebben is, okosabban is...(hasonloan a Siriusos eredeti megvalositashoz)
    // TODO: egymasbaagyazott mappingek kezelese!
    private void processDiagramElementMapping(DiagramElementMapping mapping, Configuration configuration, ElementRuleDescriptor parentElementRuleDescriptor) {
        // TODO: logger
        System.out.println("Process DiagramElementMapping (" + mapping.getName() + ")");  //$NON-NLS-1$ //$NON-NLS-2$
        
        if (mapping instanceof NodeMapping) {
            NodeMapping nodeMapping = (NodeMapping) mapping;

            String expression = nodeMapping.getSemanticCandidatesExpression();
            IQuerySpecification<?> querySpecification = vqlInterpreter.getQuerySpecification(description, expression);
            transformationInitializer.getFQNToQuerySpecificationMap().put(querySpecification.getFullyQualifiedName(), querySpecification);
            fqnToExpressionMap.put(querySpecification.getFullyQualifiedName(), expression.replace("vql:", ""));  //$NON-NLS-1$//$NON-NLS-2$
            
            ElementRuleDescriptor ruleDescriptor = ConfigurationFactory.eINSTANCE.createElementRuleDescriptor();
            ruleDescriptor.setId(++ID);
            ruleDescriptor.setConfiguration(configuration);
            ruleDescriptor.setElementType(DiagramFactory.eINSTANCE.getDiagramPackage().getDNode());
            ruleDescriptor.setPatternFQN(querySpecification.getFullyQualifiedName());

            createReferenceRule(configuration, parentElementRuleDescriptor, ruleDescriptor);
        } else if (mapping instanceof ContainerMapping) {
            ContainerMapping containerMapping = (ContainerMapping) mapping;

            String expression = containerMapping.getSemanticCandidatesExpression();
            IQuerySpecification<?> querySpecification = vqlInterpreter.getQuerySpecification(description, expression);
            transformationInitializer.getFQNToQuerySpecificationMap().put(querySpecification.getFullyQualifiedName(), querySpecification);
            fqnToExpressionMap.put(querySpecification.getFullyQualifiedName(), expression.replace("vql:", ""));  //$NON-NLS-1$//$NON-NLS-2$
            
            EClass elementType = null;
            if (new ContainerMappingQuery(containerMapping).isListContainer()) {
                elementType = DiagramFactory.eINSTANCE.getDiagramPackage().getDNodeList();
            } else {
                elementType = DiagramFactory.eINSTANCE.getDiagramPackage().getDNodeContainer();
            }
            
            ElementRuleDescriptor ruleDescriptor = ConfigurationFactory.eINSTANCE.createElementRuleDescriptor();
            ruleDescriptor.setId(++ID);
            ruleDescriptor.setConfiguration(configuration);
            ruleDescriptor.setElementType(elementType);
            ruleDescriptor.setPatternFQN(querySpecification.getFullyQualifiedName());
            
            createReferenceRule(configuration, parentElementRuleDescriptor, ruleDescriptor);
        } else if (mapping instanceof EdgeMapping) {
            throw new IllegalStateException("Edge mappings are not yet supported!"); //$NON-NLS-1$
        }
    }
    
    // TODO: expression igy nyilvan egyaltalan nem lesz jo!
    // TODO: nem kellene minden egyes mappingbol letrehozott szabalyhoz kulon-kulon egy-egy ilyen szabaly is, lehetne egy darab is OR-al!
    private void createReferenceRule(Configuration configuration, ElementRuleDescriptor owner, ElementRuleDescriptor target) {
        EReference reference = null;

        if (owner.getElementType() == DiagramPackage.eINSTANCE.getDSemanticDiagram()) {
            reference = (EReference) DiagramPackage.eINSTANCE.getDSemanticDiagram().getEStructuralFeature(DiagramPackage.DSEMANTIC_DIAGRAM__OWNED_DIAGRAM_ELEMENTS);
        } else if (owner.getElementType() == DiagramPackage.eINSTANCE.getDNodeContainer()) {
            reference = (EReference) DiagramPackage.eINSTANCE.getDNodeContainer().getEStructuralFeature(DiagramPackage.DNODE_CONTAINER__OWNED_DIAGRAM_ELEMENTS);
            // TODO: ez is lehet!
            //DiagramPackage.eINSTANCE.getDNodeContainer().getEStructuralFeature(DiagramPackage.DNODE_CONTAINER__OWNED_BORDERED_NODES);
        } else if (owner.getElementType() == DiagramPackage.eINSTANCE.getDNodeList()) {
            reference = (EReference) DiagramPackage.eINSTANCE.getDNodeList().getEStructuralFeature(DiagramPackage.DNODE_LIST__OWNED_ELEMENTS);
            
            // TODO: ez is lehet!
            //DiagramPackage.eINSTANCE.getDNodeList().getEStructuralFeature(DiagramPackage.DNODE_LIST__OWNED_BORDERED_NODES);
        } else if (owner.getElementType() == DiagramPackage.eINSTANCE.getDNode()) {
            reference = (EReference) DiagramPackage.eINSTANCE.getDNode().getEStructuralFeature(DiagramPackage.DNODE__OWNED_BORDERED_NODES);
        } else if (owner.getElementType() == DiagramPackage.eINSTANCE.getDNodeListElement()) {
            reference = (EReference) DiagramPackage.eINSTANCE.getDNodeListElement().getEStructuralFeature(DiagramPackage.DNODE_LIST_ELEMENT__OWNED_BORDERED_NODES);
        } else if (owner.getElementType() == DiagramPackage.eINSTANCE.getDEdge()) {
            // DEdge
            // TODO!
            throw new UnsupportedOperationException("Edges are not yet supported!"); //$NON-NLS-1$
        }
        
        IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> ownerQS = transformationInitializer.getFQNToQuerySpecificationMap().get(owner.getPatternFQN());
        IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> targetQS = transformationInitializer.getFQNToQuerySpecificationMap().get(target.getPatternFQN());
        
        // TODO
        String ownerParameter = ownerQS.getParameterNames().get(0);
        String targetParameter = targetQS.getParameterNames().get(0);
        
        // TODO: ez igy nagyon ocsmany!!!!!!!!!
        String expression = "vql: pattern ref_" + owner.getElementType().getName() + "_" + reference.getName() + "_" + target.getId() + "(" + ownerParameter + ", " + targetParameter +") { find " + ownerQS.getFullyQualifiedName() + "(" + ownerParameter + "); find " + targetQS.getFullyQualifiedName() + "(" + targetParameter + ");}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
        expression = expression + " " + fqnToExpressionMap.get(ownerQS.getFullyQualifiedName()) + " " + fqnToExpressionMap.get(targetQS.getFullyQualifiedName());  //$NON-NLS-1$//$NON-NLS-2$
        IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> querySpecification = vqlInterpreter.getQuerySpecification(description, expression);
        transformationInitializer.getFQNToQuerySpecificationMap().put(querySpecification.getFullyQualifiedName(), querySpecification);
        fqnToExpressionMap.put(querySpecification.getFullyQualifiedName(), expression.replace("vql:", ""));  //$NON-NLS-1$//$NON-NLS-2$
        
        /* Create ReferenceRuleDescriptor */
        ReferenceRuleDescriptor ruleDescriptor = ConfigurationFactory.eINSTANCE.createReferenceRuleDescriptor();
        ruleDescriptor.setId(++ID);
        ruleDescriptor.setConfiguration(configuration);
        ruleDescriptor.setOwnerElementRuleDescriptor(owner);
        ruleDescriptor.setTargetElementRuleDescriptor(target);
        ruleDescriptor.setReference(reference);
        ruleDescriptor.setPatternFQN(querySpecification.getFullyQualifiedName());
        /* ****************************** */
        
        /* Create PatternParameterMapping instances */
        PatternParameterMapping patternParameterMapping = null;
        
        patternParameterMapping = ConfigurationFactory.eINSTANCE.createPatternParameterMapping();
        patternParameterMapping.setERulePatternParameterName(ownerParameter);
        patternParameterMapping.setSfRulePatternParameterName(ownerParameter);
        ruleDescriptor.getPatternParameterMappings().add(patternParameterMapping);
        ruleDescriptor.getSourceElementParameterMappings().add(patternParameterMapping);
        
        patternParameterMapping = ConfigurationFactory.eINSTANCE.createPatternParameterMapping();
        patternParameterMapping.setERulePatternParameterName(targetParameter);
        patternParameterMapping.setSfRulePatternParameterName(targetParameter);
        ruleDescriptor.getPatternParameterMappings().add(patternParameterMapping);
        ruleDescriptor.getTargetElementParameterMappings().add(patternParameterMapping);
        /* **************************************** */
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
