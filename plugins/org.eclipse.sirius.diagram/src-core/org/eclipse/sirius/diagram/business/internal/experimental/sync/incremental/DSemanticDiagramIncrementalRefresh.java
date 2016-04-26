package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.incquery.viewmodel.configuration.Configuration;
import org.eclipse.incquery.viewmodel.configuration.ConfigurationFactory;
import org.eclipse.incquery.viewmodel.configuration.ElementRuleDescriptor;
import org.eclipse.incquery.viewmodel.configuration.PatternParameterMapping;
import org.eclipse.incquery.viewmodel.configuration.ReferenceRuleDescriptor;
import org.eclipse.incquery.viewmodel.configuration.RuleDescriptor;
import org.eclipse.incquery.viewmodel.configuration.Scheduler;
import org.eclipse.incquery.viewmodel.configuration.util.ConfigurationModelUtil;
import org.eclipse.incquery.viewmodel.core.ViewModelManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreter;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.DiagramPackage;
import org.eclipse.sirius.diagram.business.api.componentization.DiagramDescriptionMappingsManager;
import org.eclipse.sirius.diagram.business.api.componentization.DiagramMappingsManager;
import org.eclipse.sirius.diagram.business.api.query.ContainerMappingQuery;
import org.eclipse.sirius.diagram.business.internal.componentization.mappings.DiagramDescriptionMappingsManagerImpl;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.IsMappingOfCurrentDiagramDescription;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql.VQLInterpreter;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.AbstractDNodeCompositeQuerySpecification;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.CompositeQuerySpecification;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.CompositeQuerySpecification.CompositePQuery;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.RelationBasedEdgeCompositeQuerySpecification;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.AbstractDNodeElementRule.AbstractDNodeElementRuleCandidate;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.ContainmentReferenceRule.ContainmentReferenceRuleCandidate;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.DEdgeElementRule.DEdgeElementRuleCancicate;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.RelationBasedEdgeElementRule.RelationBasedEdgeElementRuleCandidate;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.RootElementRule.RootElementRuleCandidate;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.RuleCandidate;
import org.eclipse.sirius.diagram.description.ContainerMapping;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.diagram.description.DiagramElementMapping;
import org.eclipse.sirius.diagram.description.EdgeMapping;
import org.eclipse.sirius.diagram.description.NodeMapping;
import org.eclipse.sirius.ecore.extender.business.api.accessor.ModelAccessor;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.exception.ViatraQueryException;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PParameter;
import org.eclipse.viatra.transformation.runtime.emf.transformation.eventdriven.InconsistentEventSemanticsException;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class DSemanticDiagramIncrementalRefresh {
    private static final int PRIORITY_ABSTRACT_DNODE = 0;
    
    private static final int PRIORITY_DEDGE = 1;
    
    private static final int PRIORITY_REFERENCE = 2;
    
    private static final int PRIORITY_ATTRIBUTE = 3;
    
    private boolean initialized = false;
    
    private Session session = null;
    
    private DSemanticDiagram diagram = null;
    
    private DiagramDescription description = null;
    
    private IInterpreter interpreter;
    
    private ModelAccessor accessor;

    private VQLInterpreter vqlInterpreter = null;
    
    private ViewModelManager viewModelManager = null;
    
    private DiagramMappingsManager diagramMappingsManager = null;

    private SiriusNotationModelIncrementalRefreshTransformationInitializer transformationInitializer;

    // TODO: torolni, es normalisan megcsinalni!
    private static long ID = -1L;
    
    // TODO: megoldani mashogy
    private Map<String, String> fqnToExpressionMap = null;
    
    // TODO: megoldani szebben
    private Map<DiagramElementMapping, RuleDescriptor> mappingToRuleDescriptor = null;

    private DiagramDescriptionMappingsManager diagramDescriptionMappingsManager;

    
    
    public DSemanticDiagramIncrementalRefresh(DSemanticDiagram diagram) {
        if (diagram == null) {
            throw new IllegalArgumentException("The 'diagram' parameter can not be null!"); //$NON-NLS-1$
        }
        
        this.diagram = diagram;
    }
    
    public void initialize(Session session, DiagramDescription description, DiagramMappingsManager diagramMappingsManager,
            IInterpreter interpreter, ModelAccessor accessor) throws ViatraQueryException, InconsistentEventSemanticsException {
        if (!initialized) {
            if (session == null) {
                throw new IllegalArgumentException("The 'session' parameter can not be null!"); //$NON-NLS-1$
            }
            
            if (description == null) {
                throw new IllegalArgumentException("The 'description' parameter can not be null!"); //$NON-NLS-1$
            }
            
            this.session = session;
            this.description = description;
            this.diagramMappingsManager = diagramMappingsManager;
            this.interpreter = interpreter;
            this.accessor = accessor;
            this.vqlInterpreter = new VQLInterpreter();
            this.transformationInitializer = new SiriusNotationModelIncrementalRefreshTransformationInitializer(session, diagram, description, diagramMappingsManager);
            
            // TODO: torolni majd ha mar nem kell
            this.fqnToExpressionMap = Maps.newHashMap();
            
            // TODO: torolni majd ha mar nem kell
            this.mappingToRuleDescriptor = Maps.newHashMap();
    
            this.diagramDescriptionMappingsManager = new DiagramDescriptionMappingsManagerImpl(description);
            this.diagramDescriptionMappingsManager.computeMappings(session.getSelectedViewpoints(false));
            
            Configuration configuration = createConfigurationModel();
            
            viewModelManager = new ViewModelManager(configuration, transformationInitializer);
            
            viewModelManager.initialize();
            
            initialized = true;
        }
    }
    
    public void dispose() {
        if (viewModelManager != null) {
            viewModelManager.dispose();
        }
    }
    
    public void refresh() {
        if (viewModelManager != null) {
            viewModelManager.getExecutionSchema().startUnscheduledExecution();
        }
    }
    
    private Configuration createConfigurationModel() {
        Configuration configuration = ConfigurationFactory.eINSTANCE.createConfiguration();
        configuration.setScheduler(Scheduler.MANUAL);
        configuration.setSourceModel(diagram.getTarget().eResource());
        configuration.setTargetModel(session.getSessionResource());
        
        ElementRuleDescriptor ruleDescriptor = createRuleDescriptionForRootElement(configuration);

        for (DiagramElementMapping mapping : getMappings(description)) {
            processDiagramElementMapping(mapping, configuration, ruleDescriptor);
        }
        
        return configuration;
    }
    
    // TODO: ez kezdetleges megoldas!!!! Javitani kell! Forras elemet le lehessen kotni!!!
    // TODO: expression nagyon nem jo igy....
    private ElementRuleDescriptor createRuleDescriptionForRootElement(Configuration configuration) {
        ID++;
        EClass rootElementType = diagram.getTarget().eClass();
        
        // TODO:!!!
        String expression = "vql: @Return(parameter=\"semanticRoot\") pattern element_dSemanticDiagram(semanticRoot : " + rootElementType.getName() + ") { " + rootElementType.getName() + "(semanticRoot); }";   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
        IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> querySpecification = vqlInterpreter.getQuerySpecification(description, expression, ID);
        CompositeQuerySpecification<CompositePQuery> compositeQuerySpecification = new CompositeQuerySpecification<CompositePQuery>(diagram, ID, querySpecification);
        
        transformationInitializer.getFQNToQuerySpecificationMap().put(compositeQuerySpecification.getFullyQualifiedName(), compositeQuerySpecification);
        
        ElementRuleDescriptor ruleDescriptor = ConfigurationModelUtil.createElementRuleDescriptor(ID, DiagramPackage.eINSTANCE.getDSemanticDiagram(), compositeQuerySpecification.getFullyQualifiedName());
        configuration.getRuleDescriptors().add(ruleDescriptor);
        
        transformationInitializer.getRuleCandidates().add(new RootElementRuleCandidate(ruleDescriptor, PRIORITY_ABSTRACT_DNODE, diagram));
        
        return ruleDescriptor;
    }
    
    // TODO: lehet BorderedNodeMapping is...
    private void processDiagramElementMapping(DiagramElementMapping mapping, Configuration configuration, ElementRuleDescriptor parentElementRuleDescriptor) {
        // TODO: logger
        System.out.println("Process DiagramElementMapping (" + mapping.getName() + ")");  //$NON-NLS-1$ //$NON-NLS-2$
        
        ID++;
        
        if (mapping instanceof NodeMapping || mapping instanceof ContainerMapping) {
            // Get Container's type
            EClass containerType = parentElementRuleDescriptor.getElementType();
            
            String expression = mapping.getSemanticCandidatesExpression();
            
            IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> childQuerySpecification =
                    vqlInterpreter.getQuerySpecification(description, expression, ID);
            IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> parentQuerySpecification = 
                    (IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>>) transformationInitializer.getFQNToQuerySpecificationMap().get(parentElementRuleDescriptor.getPatternFQN());
            AbstractDNodeCompositeQuerySpecification compositeQuerySpecification = new AbstractDNodeCompositeQuerySpecification(diagram, ID, parentQuerySpecification, childQuerySpecification);
            
            transformationInitializer.getFQNToQuerySpecificationMap().put(compositeQuerySpecification.getFullyQualifiedName(), compositeQuerySpecification);
            
            if (mapping instanceof NodeMapping) {
                NodeMapping nodeMapping = (NodeMapping) mapping;
                
                EClass elementType = null;
                if (containerType == DiagramPackage.eINSTANCE.getDNodeList() && !isBorderedNodeMapping(nodeMapping)) {
                    elementType = DiagramPackage.eINSTANCE.getDNodeListElement();
                } else {
                    elementType = DiagramPackage.eINSTANCE.getDNode();
                }
                
                ElementRuleDescriptor ruleDescriptor = ConfigurationModelUtil.createElementRuleDescriptor(ID, elementType, compositeQuerySpecification.getFullyQualifiedName());
                configuration.getRuleDescriptors().add(ruleDescriptor);
                
                transformationInitializer.getRuleCandidates().add(new AbstractDNodeElementRuleCandidate(ruleDescriptor, PRIORITY_ABSTRACT_DNODE, null, nodeMapping));
                
                // TODO: torolni majd ha mar nem kell
                mappingToRuleDescriptor.put(mapping, ruleDescriptor);
                
                // Call this function on the child mappings
                for (DiagramElementMapping childMapping : getMappings(mapping)) {
                    processDiagramElementMapping(childMapping, configuration, ruleDescriptor);
                }
                
                createReferenceRule(configuration, parentElementRuleDescriptor, ruleDescriptor);
            } else if (mapping instanceof ContainerMapping) {
                ContainerMapping containerMapping = (ContainerMapping) mapping;
    
                EClass elementType = null;
                if (new ContainerMappingQuery(containerMapping).isListContainer()) {
                    elementType = DiagramPackage.eINSTANCE.getDNodeList();
                } else {
                    elementType = DiagramPackage.eINSTANCE.getDNodeContainer();
                }
    
                ElementRuleDescriptor ruleDescriptor = ConfigurationModelUtil.createElementRuleDescriptor(ID, elementType, compositeQuerySpecification.getFullyQualifiedName());
                configuration.getRuleDescriptors().add(ruleDescriptor);
                
                transformationInitializer.getRuleCandidates().add(new AbstractDNodeElementRuleCandidate(ruleDescriptor, PRIORITY_ABSTRACT_DNODE, null, containerMapping));
                
                // TODO: torolni majd ha mar nem kell
                mappingToRuleDescriptor.put(mapping, ruleDescriptor);
                
                // Call this function on the child mappings
                for (DiagramElementMapping childMapping : getMappings(mapping)) {
                    processDiagramElementMapping(childMapping, configuration, ruleDescriptor);
                }            
                
                createReferenceRule(configuration, parentElementRuleDescriptor, ruleDescriptor);
            }
        } else if (mapping instanceof EdgeMapping) {
            //throw new IllegalStateException("Edge mappings are not yet supported!"); //$NON-NLS-1$
            
            String expression = null;
            
            EdgeMapping edgeMapping = (EdgeMapping) mapping;
            
            final IsMappingOfCurrentDiagramDescription isMappingOfCurrentDiagramDescription = new IsMappingOfCurrentDiagramDescription(description);
            
            Predicate<EdgeMapping> edgeMappingWithoutEdgeAsSourceOrTarget = new Predicate<EdgeMapping>() {
                @Override
                public boolean apply(EdgeMapping input) {
                    // Valid if source mapping and target mapping are not
                    // EdgeMappings
                    Iterable<EdgeMapping> edgeSourceMappings = Iterables.filter(Iterables.filter(input.getSourceMapping(), EdgeMapping.class), isMappingOfCurrentDiagramDescription);
                    Iterable<EdgeMapping> edgeTargetMappings = Iterables.filter(Iterables.filter(input.getTargetMapping(), EdgeMapping.class), isMappingOfCurrentDiagramDescription);
                    return Iterables.isEmpty(edgeSourceMappings) && Iterables.isEmpty(edgeTargetMappings);
                }
            };
            
            // Firstly, we need to refresh the EdgeMapping having no other
            // EdgeMapping as source neither as target
            for (final EdgeMapping mp : Iterables.filter(Lists.newArrayList(edgeMapping), edgeMappingWithoutEdgeAsSourceOrTarget)) {
                ID++;
                
                if (mp.getSemanticCandidatesExpression() == null) {
                    // RelationBasedEdge
                    expression = mp.getTargetFinderExpression();
                    
                    if (mp.getSourceMapping().size() > 1 || mp.getTargetMapping().size() > 1) {
                        throw new IllegalStateException("Multiple source and / or target mappings for edges are not yet supported!"); //$NON-NLS-1$
                    }
                    
                    // TODO: SUPPORT MULTIPLE SOURCE AND TARGET MAPPINGS
                    ElementRuleDescriptor sourceRuleDescriptor = (ElementRuleDescriptor) this.mappingToRuleDescriptor.get(mp.getSourceMapping().get(0));
                    ElementRuleDescriptor targetRuleDescriptor = (ElementRuleDescriptor) this.mappingToRuleDescriptor.get(mp.getTargetMapping().get(0));
                    
                    IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> sourceQS = 
                            (IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>>) transformationInitializer.getFQNToQuerySpecificationMap().get(sourceRuleDescriptor.getPatternFQN());
                    IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> targetQS = 
                            (IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>>) transformationInitializer.getFQNToQuerySpecificationMap().get(targetRuleDescriptor.getPatternFQN());
                    IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> targetFinderExpressionQS = vqlInterpreter.getQuerySpecification(description, expression, ID);
                    
                    RelationBasedEdgeCompositeQuerySpecification rbeCompositeQS = new RelationBasedEdgeCompositeQuerySpecification(diagram, ID, targetFinderExpressionQS, sourceQS, targetQS);
                    
                    transformationInitializer.getFQNToQuerySpecificationMap().put(rbeCompositeQS.getFullyQualifiedName(), rbeCompositeQS);
                    
                    
                    ElementRuleDescriptor ruleDescriptor = ConfigurationModelUtil.createElementRuleDescriptor(ID, DiagramPackage.eINSTANCE.getDEdge(), rbeCompositeQS.getFullyQualifiedName());
                    configuration.getRuleDescriptors().add(ruleDescriptor);
                    
                    transformationInitializer.getRuleCandidates().add(new RelationBasedEdgeElementRuleCandidate(ruleDescriptor, PRIORITY_DEDGE, null, mp, sourceRuleDescriptor, targetRuleDescriptor));
                    
                    // TODO: torolni majd ha mar nem kell
                    mappingToRuleDescriptor.put(mp, ruleDescriptor);
                    
                    createReferenceRule(configuration, parentElementRuleDescriptor, ruleDescriptor);
                } else {
                    // ElementBasedEdge
                }
            }
        }
    }
    
    // TODO: expression igy nyilvan egyaltalan nem lesz jo!
    // TODO: nem kellene minden egyes mappingbol letrehozott szabalyhoz kulon-kulon egy-egy ilyen szabaly is, lehetne egy darab is OR-al!
    private void createReferenceRule(Configuration configuration, ElementRuleDescriptor owner, ElementRuleDescriptor target) {
        ID++;
        
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
        
        IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> ownerQS = 
                (IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>>) transformationInitializer.getFQNToQuerySpecificationMap().get(owner.getPatternFQN());
        IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> targetQS = 
                (IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>>) transformationInitializer.getFQNToQuerySpecificationMap().get(target.getPatternFQN());
        
        // TODO: ezt at kene nevezni, mert azt sugallja, hogy AbstractDnodeokhoz jó csak a QS 
        AbstractDNodeCompositeQuerySpecification querySpecification = new AbstractDNodeCompositeQuerySpecification(diagram, ID, ownerQS, targetQS);

        transformationInitializer.getFQNToQuerySpecificationMap().put(querySpecification.getFullyQualifiedName(), querySpecification);
        
        /* Create ReferenceRuleDescriptor */
        ReferenceRuleDescriptor ruleDescriptor = ConfigurationModelUtil.createReferenceRUleDescriptor(
                ID, reference, querySpecification.getFullyQualifiedName(), owner, target);
        configuration.getRuleDescriptors().add(ruleDescriptor);
        /* ****************************** */
        
        /* Create PatternParameterMapping instances */
        PParameter pParameter = null;
        PatternParameterMapping patternParameterMapping = null;
        
        for (int i = 0; i < querySpecification.getInternalQueryRepresentation().getParentQS().getParameters().size(); i++) {
            pParameter = querySpecification.getInternalQueryRepresentation().getParentQS().getParameters().get(i);
            
            patternParameterMapping = ConfigurationModelUtil.createPatternParameterMapping(pParameter.getName(), pParameter.getName());
            ruleDescriptor.getPatternParameterMappings().add(patternParameterMapping);
            ruleDescriptor.getSourceElementParameterMappings().add(patternParameterMapping);
        }
        
        for (int i = 0; i < querySpecification.getParameters().size(); i++) {
            pParameter = querySpecification.getParameters().get(i);
            
            patternParameterMapping = ConfigurationModelUtil.createPatternParameterMapping(pParameter.getName(), pParameter.getName());
            ruleDescriptor.getPatternParameterMappings().add(patternParameterMapping);
            ruleDescriptor.getTargetElementParameterMappings().add(patternParameterMapping);
        }
        /* **************************************** */
        
        DiagramElementMapping mapping = null;
        RuleCandidate ruleCandidate = transformationInitializer.getRuleCandidates(target);
        if (ruleCandidate instanceof AbstractDNodeElementRuleCandidate) {
            transformationInitializer.getRuleCandidates().add(new ContainmentReferenceRuleCandidate(ruleDescriptor, PRIORITY_REFERENCE, null, diagram, transformationInitializer.getRuleProvider(), ((AbstractDNodeElementRuleCandidate) ruleCandidate).getMapping()));
        } else if (ruleCandidate instanceof DEdgeElementRuleCancicate) {
            transformationInitializer.getRuleCandidates().add(new ContainmentReferenceRuleCandidate(ruleDescriptor, PRIORITY_REFERENCE, null, diagram, transformationInitializer.getRuleProvider(), ((DEdgeElementRuleCancicate<?, ?>) ruleCandidate).getEdgeMapping()));
        }
    }
    
    /**
     * Collect child mappings for the given element based on the Viewpoint specification.
     *  The given element must be a DiagramDescription, NodeMapping or ContainerMapping instance.
     *  In the result list, each EdgeMapping should be preceded by NodeMapping / BorderedNodeMapping / ContainerMapping.
     * @param element
     * @return
     */
    private List<DiagramElementMapping> getMappings(EObject element) {
        List<DiagramElementMapping> diagramElementMappings = Lists.newArrayList();

        if (element instanceof DiagramDescription) {
            diagramElementMappings.addAll(diagramDescriptionMappingsManager.getNodeMappings());
            diagramElementMappings.addAll(diagramDescriptionMappingsManager.getContainerMappings());
            diagramElementMappings.addAll(diagramMappingsManager.getEdgeMappings());
        } else if (element instanceof NodeMapping) {
            diagramElementMappings.addAll(diagramDescriptionMappingsManager.getBorderedNodeMappings((NodeMapping) element));
        } else if (element instanceof ContainerMapping) {
            diagramElementMappings.addAll(diagramDescriptionMappingsManager.getNodeMappings((ContainerMapping) element));            
            diagramElementMappings.addAll(diagramDescriptionMappingsManager.getBorderedNodeMappings((ContainerMapping) element));
            diagramElementMappings.addAll(diagramDescriptionMappingsManager.getContainerMappings((ContainerMapping) element));
        }
        
        return diagramElementMappings;
    }
    
    /**
     * Check if the given mapping is a bordered {@link NodeMapping}.
     * @param nodeMapping
     * @return
     */
    private boolean isBorderedNodeMapping(NodeMapping nodeMapping) {
        if (nodeMapping.eContainmentFeature() == DiagramPackage.eINSTANCE.getAbstractDNode_OwnedBorderedNodes()) {
            return true;
        }
        
        return false;
    }
}
