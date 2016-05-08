package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.business.api.session.SessionManagerListener;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreter;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.DiagramPackage;
import org.eclipse.sirius.diagram.business.api.componentization.DiagramDescriptionMappingsManager;
import org.eclipse.sirius.diagram.business.api.componentization.DiagramMappingsManager;
import org.eclipse.sirius.diagram.business.api.query.ContainerMappingQuery;
import org.eclipse.sirius.diagram.business.internal.componentization.mappings.DiagramDescriptionMappingsManagerImpl;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.IsMappingOfCurrentDiagramDescription;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.AbstractDNodeContainmentQuerySpecification;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.AbstractDNodeSCEQuerySpecification;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.DSemanticDiagramQuerySpecification;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.ElementBasedEdgeFEQuerySpecification;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.ElementBasedEdgeSCEQuerySpecification;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.ReferenceQuerySpecification;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.ReferenceQuerySpecification.ReferencePQuery;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.RelationBasedEdgeTFEQuerySpecification;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.SiriusQuerySpecification;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.SiriusQuerySpecificationFactory;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.AbstractDNodeElementRule.AbstractDNodeElementRuleCandidate;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.ContainmentReferenceRule.ContainmentReferenceRuleCandidate;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.DEdgeElementRule.DEdgeElementRuleCandidate;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.ElementBasedEdgeElementRule.ElementBasedEdgeElementRuleCandidate;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.ElementBasedEdgeReferenceRule.ElementBasedEdgeReferenceRuleCandidate;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.RelationBasedEdgeElementRule.RelationBasedEdgeElementRuleCandidate;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.RootElementRule.RootElementRuleCandidate;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.rules.RuleCandidate;
import org.eclipse.sirius.diagram.description.ContainerMapping;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.diagram.description.DiagramElementMapping;
import org.eclipse.sirius.diagram.description.EdgeMapping;
import org.eclipse.sirius.diagram.description.NodeMapping;
import org.eclipse.sirius.ecore.extender.business.api.accessor.ModelAccessor;
import org.eclipse.sirius.ui.business.api.dialect.DialectEditor;
import org.eclipse.sirius.ui.business.api.session.IEditingSession;
import org.eclipse.sirius.ui.business.api.session.SessionUIManager;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.viatra.query.runtime.exception.ViatraQueryException;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery;
import org.eclipse.viatra.sirius.incrementalrefresh.measurement.util.MeasurementUtil;
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
    
    private ViewModelManager viewModelManager = null;
    
    private DiagramMappingsManager diagramMappingsManager = null;

    private SiriusNotationModelIncrementalRefreshTransformationInitializer transformationInitializer;

    // TODO: torolni, es normalisan megcsinalni!
    private static long ID = -1L;
    
    // TODO: megoldani szebben
    private Map<EObject, RuleDescriptor> mappingToRuleDescriptor = null;

    private DiagramDescriptionMappingsManager diagramDescriptionMappingsManager;

    private SiriusQuerySpecificationFactory querySpecificationFactory;

    
    
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
            
            // TODO
            int mId = MeasurementUtil.getInstance().measureExecutionTime_Start();
            
            this.session = session;
            this.description = description;
            this.diagramMappingsManager = diagramMappingsManager;
            this.querySpecificationFactory = new SiriusQuerySpecificationFactory(diagram);
            this.transformationInitializer = new SiriusNotationModelIncrementalRefreshTransformationInitializer(session, diagram, description, diagramMappingsManager);
            
            // TODO: torolni majd ha mar nem kell
            this.mappingToRuleDescriptor = Maps.newHashMap();
    
            this.diagramDescriptionMappingsManager = new DiagramDescriptionMappingsManagerImpl(description);
            this.diagramDescriptionMappingsManager.computeMappings(session.getSelectedViewpoints(false));
            
            Configuration configuration = createConfigurationModel();
            
            viewModelManager = new ViewModelManager(configuration, transformationInitializer);
            
            viewModelManager.initialize();
            
            // TODO
            MeasurementUtil.getInstance().measureExecutionTime_Stop(mId, MeasurementUtil.MEASUREMENT_INCREMENTAL_REFRESH_INIT);
            
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

            // TODO
//            MeasurementUtil.getInstance().measureModelSize(MeasurementUtil.MEASUREMENT_TRACEABILITY_MODEL_SIZE,
//                    viewModelManager.getTraceabilityModelManager().getTraceability());
        }
    }
    
    private Configuration createConfigurationModel() {
        Configuration configuration = ConfigurationFactory.eINSTANCE.createConfiguration();
        configuration.setScheduler(Scheduler.MANUAL);
        configuration.setSourceModel(diagram.getTarget().eResource());
        configuration.setTargetModel(session.getSessionResource());
        
        ElementRuleDescriptor ruleDescriptor = createRuleDescriptorForRootElement(configuration);

        for (DiagramElementMapping mapping : getMappings(description)) {
            processDiagramElementMapping(mapping, configuration, ruleDescriptor);
        }
        
        return configuration;
    }
    
    private ElementRuleDescriptor createRuleDescriptorForRootElement(Configuration configuration) {
        ID++;
        
        EClass rootElementType = diagram.getTarget().eClass();
        
        DSemanticDiagramQuerySpecification querySpecification = querySpecificationFactory.createDSemanticDiagramSiriusQuerySpecification(rootElementType);
        
        transformationInitializer.addRuleQuerySpecification(querySpecification);
        
        ElementRuleDescriptor ruleDescriptor = ConfigurationModelUtil.createElementRuleDescriptor(ID, DiagramPackage.eINSTANCE.getDSemanticDiagram(), querySpecification.getFullyQualifiedName());
        configuration.getRuleDescriptors().add(ruleDescriptor);
        
        transformationInitializer.getRuleCandidates().add(new RootElementRuleCandidate(ruleDescriptor, PRIORITY_ABSTRACT_DNODE, diagram));
        
        // TODO: torolni majd ha mar nem kell
        this.mappingToRuleDescriptor.put(description, ruleDescriptor);
        
        return ruleDescriptor;
    }
    
    private void createRuleDescriptorFromNodeMapping(AbstractDNodeSCEQuerySpecification querySpecification, NodeMapping mapping,
            Configuration configuration, ElementRuleDescriptor parentElementRuleDescriptor) {
        ID++;
        
        // Get Container's type
        EClass containerType = parentElementRuleDescriptor.getElementType();
                
        EClass elementType = null;
        if (containerType == DiagramPackage.eINSTANCE.getDNodeList() && !isBorderedNodeMapping(mapping)) {
            elementType = DiagramPackage.eINSTANCE.getDNodeListElement();
        } else {
            elementType = DiagramPackage.eINSTANCE.getDNode();
        }
        
        ElementRuleDescriptor ruleDescriptor = ConfigurationModelUtil.createElementRuleDescriptor(ID, elementType, querySpecification.getFullyQualifiedName());
        configuration.getRuleDescriptors().add(ruleDescriptor);
        
        transformationInitializer.getRuleCandidates().add(new AbstractDNodeElementRuleCandidate(ruleDescriptor, PRIORITY_ABSTRACT_DNODE, null, mapping));
        
        // TODO: torolni majd ha mar nem kell
        mappingToRuleDescriptor.put(mapping, ruleDescriptor);
        
        // Call this function on the child mappings
        for (DiagramElementMapping childMapping : getMappings(mapping)) {
            processDiagramElementMapping(childMapping, configuration, ruleDescriptor);
        }
        
        createContainementReferenceRuleForAbstractDNode(
                configuration,
                querySpecificationFactory.createAbstractDNodeContainmentQuerySpecification(
                        querySpecification.getInternalQueryRepresentation().getParentQS(), querySpecification),
                parentElementRuleDescriptor,
                ruleDescriptor);

    }
    
    private void createRuleDescriptorFromContainerMapping(AbstractDNodeSCEQuerySpecification querySpecification, ContainerMapping mapping,
            Configuration configuration, ElementRuleDescriptor parentElementRuleDescriptor) {
        ID++;
        
        EClass elementType = null;
        if (new ContainerMappingQuery(mapping).isListContainer()) {
            elementType = DiagramPackage.eINSTANCE.getDNodeList();
        } else {
            elementType = DiagramPackage.eINSTANCE.getDNodeContainer();
        }

        ElementRuleDescriptor ruleDescriptor = ConfigurationModelUtil.createElementRuleDescriptor(ID, elementType, querySpecification.getFullyQualifiedName());
        configuration.getRuleDescriptors().add(ruleDescriptor);
        
        transformationInitializer.getRuleCandidates().add(new AbstractDNodeElementRuleCandidate(ruleDescriptor, PRIORITY_ABSTRACT_DNODE, null, mapping));
        
        // TODO: torolni majd ha mar nem kell
        mappingToRuleDescriptor.put(mapping, ruleDescriptor);
        
        // Call this function on the child mappings
        for (DiagramElementMapping childMapping : getMappings(mapping)) {
            processDiagramElementMapping(childMapping, configuration, ruleDescriptor);
        }            
        
        createContainementReferenceRuleForAbstractDNode(
                configuration,
                querySpecificationFactory.createAbstractDNodeContainmentQuerySpecification(
                        querySpecification.getInternalQueryRepresentation().getParentQS(), querySpecification),
                parentElementRuleDescriptor,
                ruleDescriptor);
    }
    
    private void createRuleDescriptorFromRelationBasedEdgeMapping(EdgeMapping mapping, Configuration configuration, ElementRuleDescriptor parentElementRuleDescriptor) {
        if (mapping.getSourceMapping().size() > 1 || mapping.getTargetMapping().size() > 1) {
            throw new IllegalStateException("Multiple source and/or target mappings for edges are not yet supported!"); //$NON-NLS-1$
        }
        
        ID++;

        // TODO: SUPPORT MULTIPLE SOURCE AND TARGET MAPPINGS
        ElementRuleDescriptor sourceRuleDescriptor = (ElementRuleDescriptor) this.mappingToRuleDescriptor.get(mapping.getSourceMapping().get(0));
        ElementRuleDescriptor targetRuleDescriptor = (ElementRuleDescriptor) this.mappingToRuleDescriptor.get(mapping.getTargetMapping().get(0));

        // RelationBasedEdge
        String expression = mapping.getTargetFinderExpression();
        
        SiriusQuerySpecification<? extends PQuery> ownerQS = transformationInitializer.getRuleQuerySpecification(parentElementRuleDescriptor.getPatternFQN());

        SiriusQuerySpecification<? extends PQuery> sourceQS = transformationInitializer.getRuleQuerySpecification(sourceRuleDescriptor.getPatternFQN());
        SiriusQuerySpecification<? extends PQuery> targetQS = transformationInitializer.getRuleQuerySpecification(targetRuleDescriptor.getPatternFQN());
        SiriusQuerySpecification<? extends PQuery> targetFinderExpressionQS = querySpecificationFactory.createSiriusQuerySpecification(expression);

        RelationBasedEdgeTFEQuerySpecification querySpecification = querySpecificationFactory.
                createRelationBasedEdgeTFEQuerySpecification(sourceQS, targetQS, targetFinderExpressionQS);
        transformationInitializer.addRuleQuerySpecification(querySpecification);

        ElementRuleDescriptor ruleDescriptor = ConfigurationModelUtil.createElementRuleDescriptor(ID, DiagramPackage.eINSTANCE.getDEdge(), querySpecification.getFullyQualifiedName());
        configuration.getRuleDescriptors().add(ruleDescriptor);

        transformationInitializer.getRuleCandidates().add(new RelationBasedEdgeElementRuleCandidate(ruleDescriptor, PRIORITY_DEDGE, null, mapping, sourceRuleDescriptor, targetRuleDescriptor));

        // TODO: torolni majd ha mar nem kell
        mappingToRuleDescriptor.put(mapping, ruleDescriptor);

        createContainmentReferenceRule(
                configuration,
                querySpecificationFactory.createRelationBasedEdgeContainmentQuerySpecification(ownerQS, querySpecification),
                parentElementRuleDescriptor,
                ruleDescriptor,
                DiagramPackage.eINSTANCE.getDDiagram_OwnedDiagramElements());
    }
    
    private void createRuleDescriptorFromElementBasedEdgeMapping(EdgeMapping mapping, Configuration configuration, ElementRuleDescriptor parentElementRuleDescriptor) {
        if (mapping.getSourceMapping().size() > 1 || mapping.getTargetMapping().size() > 1) {
            throw new IllegalStateException("Multiple source and/or target mappings for edges are not yet supported!"); //$NON-NLS-1$
        }
        
        // TODO: SUPPORT MULTIPLE SOURCE AND TARGET MAPPINGS
        ElementRuleDescriptor sourceRuleDescriptor = (ElementRuleDescriptor) this.mappingToRuleDescriptor.get(mapping.getSourceMapping().get(0));
        ElementRuleDescriptor targetRuleDescriptor = (ElementRuleDescriptor) this.mappingToRuleDescriptor.get(mapping.getTargetMapping().get(0));

        String sourceFinderExpression = mapping.getSourceFinderExpression();
        String targetFinderExpression = mapping.getTargetFinderExpression();
        String semanticCandidatesExpression = mapping.getSemanticCandidatesExpression();

        SiriusQuerySpecification<? extends PQuery> ownerQS = transformationInitializer.getRuleQuerySpecification(parentElementRuleDescriptor.getPatternFQN());

        SiriusQuerySpecification<? extends PQuery> sourceMappingQS = transformationInitializer.getRuleQuerySpecification(sourceRuleDescriptor.getPatternFQN());

        SiriusQuerySpecification<? extends PQuery> targetMappingQS = transformationInitializer.getRuleQuerySpecification(targetRuleDescriptor.getPatternFQN());

        SiriusQuerySpecification<? extends PQuery> sourceFinderExpressionQS = querySpecificationFactory.createSiriusQuerySpecification(sourceFinderExpression);

        SiriusQuerySpecification<?extends PQuery> targetFinderExpressionQS = querySpecificationFactory.createSiriusQuerySpecification(targetFinderExpression);

        SiriusQuerySpecification<? extends PQuery> semanticCandidatesExpressionQS = querySpecificationFactory.createSiriusQuerySpecification(semanticCandidatesExpression);


        /* Creating RuleCandidate for ElementBasedEdge */
        ID++;

        ElementBasedEdgeSCEQuerySpecification querySpecification = querySpecificationFactory.createElementBasedEdgeSCEQuerySpecification(ownerQS, semanticCandidatesExpressionQS);
        transformationInitializer.addRuleQuerySpecification(querySpecification);

        ElementRuleDescriptor ruleDescriptor = ConfigurationModelUtil.createElementRuleDescriptor(ID, DiagramPackage.eINSTANCE.getDEdge(), querySpecification.getFullyQualifiedName());
        configuration.getRuleDescriptors().add(ruleDescriptor);

        transformationInitializer.getRuleCandidates().add(new ElementBasedEdgeElementRuleCandidate(ruleDescriptor, PRIORITY_DEDGE, null, mapping, sourceRuleDescriptor, targetRuleDescriptor));

        // TODO: torolni majd ha mar nem kell
        mappingToRuleDescriptor.put(mapping, ruleDescriptor);

        createContainmentReferenceRule(
                configuration,
                querySpecificationFactory.createElementBasedEdgeContainmentQuerySpecification(ownerQS, querySpecification),
                parentElementRuleDescriptor,
                ruleDescriptor,
                DiagramPackage.eINSTANCE.getDDiagram_OwnedDiagramElements());
        /* ******************************************* */

        ReferenceRuleDescriptor referenceRuleDescriptor = null;
        ElementBasedEdgeFEQuerySpecification finderExpressionQS = null;
        /* Create RuleDescriptor for 'Source Finder Expression' */
        finderExpressionQS = querySpecificationFactory.createElementBasedEdgeFEQuerySpecification(sourceMappingQS, sourceFinderExpressionQS, querySpecification);
        
        referenceRuleDescriptor = createReferenceRule(
                configuration,
                querySpecificationFactory.createElementBasedEdgeFEReferenceQuerySpecification(querySpecification, finderExpressionQS),
                ruleDescriptor,
                sourceRuleDescriptor,
                DiagramPackage.eINSTANCE.getDEdge_SourceNode());
        transformationInitializer.getRuleCandidates().add(new ElementBasedEdgeReferenceRuleCandidate(referenceRuleDescriptor, PRIORITY_REFERENCE, null, transformationInitializer.getRuleProvider(), mapping));
        /* **************************************************** */

        /* Create RuleDescriptor for 'Target Finder Expression' */
        finderExpressionQS = querySpecificationFactory.createElementBasedEdgeFEQuerySpecification(targetMappingQS, targetFinderExpressionQS, querySpecification);
        
        referenceRuleDescriptor = createReferenceRule(
                configuration,
                querySpecificationFactory.createElementBasedEdgeFEReferenceQuerySpecification(querySpecification, finderExpressionQS),
                ruleDescriptor,
                targetRuleDescriptor,
                DiagramPackage.eINSTANCE.getDEdge_TargetNode());
        transformationInitializer.getRuleCandidates().add(new ElementBasedEdgeReferenceRuleCandidate(referenceRuleDescriptor, PRIORITY_REFERENCE, null, transformationInitializer.getRuleProvider(), mapping));
        /* **************************************************** */

    }
    
    // TODO: lehet BorderedNodeMapping is...
    private void processDiagramElementMapping(DiagramElementMapping mapping, Configuration configuration, ElementRuleDescriptor parentElementRuleDescriptor) {
        // TODO: logger
        //System.out.println("Process DiagramElementMapping (" + mapping.getName() + ")");  //$NON-NLS-1$ //$NON-NLS-2$
        
        if (mapping instanceof NodeMapping || mapping instanceof ContainerMapping) {
            String expression = mapping.getSemanticCandidatesExpression();

            SiriusQuerySpecification<? extends PQuery> parentQS = transformationInitializer.getRuleQuerySpecification(parentElementRuleDescriptor.getPatternFQN());
            SiriusQuerySpecification<? extends PQuery> childQS = querySpecificationFactory.createSiriusQuerySpecification(expression);
            
            AbstractDNodeSCEQuerySpecification querySpecification = querySpecificationFactory.createAbstractDNodeSCEQuerySpecification(parentQS, childQS);
            transformationInitializer.addRuleQuerySpecification(querySpecification);
            
            if (mapping instanceof NodeMapping) {
                createRuleDescriptorFromNodeMapping(querySpecification, (NodeMapping) mapping, configuration, parentElementRuleDescriptor);
            } else if (mapping instanceof ContainerMapping) {
                createRuleDescriptorFromContainerMapping(querySpecification, (ContainerMapping) mapping, configuration, parentElementRuleDescriptor);
            }
        } else if (mapping instanceof EdgeMapping) {
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
                if (mp.getSemanticCandidatesExpression() == null) {
                    // RelationBasedEdge
                    createRuleDescriptorFromRelationBasedEdgeMapping(mp, configuration, parentElementRuleDescriptor);
                } else {
                    // ElementBasedEdge
                    createRuleDescriptorFromElementBasedEdgeMapping(mp, configuration, parentElementRuleDescriptor);
                }
            }
        }
    }
    
    // TODO: nem kellene minden egyes mappingbol letrehozott szabalyhoz kulon-kulon egy-egy ilyen szabaly is, lehetne egy darab is OR-al!
    private void createContainementReferenceRuleForAbstractDNode(Configuration configuration, AbstractDNodeContainmentQuerySpecification querySpecification, ElementRuleDescriptor owner, ElementRuleDescriptor target) {
        EReference reference = null;
        EClass ownerElementType = owner.getElementType();
        EClass targetElementType = target.getElementType();
        
        if (ownerElementType == DiagramPackage.eINSTANCE.getDSemanticDiagram()) {
            reference = DiagramPackage.eINSTANCE.getDDiagram_OwnedDiagramElements();
        } else if (ownerElementType == DiagramPackage.eINSTANCE.getDNodeContainer()) {
            reference = DiagramPackage.eINSTANCE.getDNodeContainer_OwnedDiagramElements();

            // TODO: ez is lehet!
            //DiagramPackage.eINSTANCE.getDNodeContainer().getEStructuralFeature(DiagramPackage.DNODE_CONTAINER__OWNED_BORDERED_NODES);
        } else if (ownerElementType == DiagramPackage.eINSTANCE.getDNodeList()) {
            reference = DiagramPackage.eINSTANCE.getDNodeList_OwnedElements();
            
            // TODO: ez is lehet!
            //DiagramPackage.eINSTANCE.getDNodeList().getEStructuralFeature(DiagramPackage.DNODE_LIST__OWNED_BORDERED_NODES);
        } else if (ownerElementType == DiagramPackage.eINSTANCE.getDNode()) {
            reference = (EReference) DiagramPackage.eINSTANCE.getDNode().getEStructuralFeature(DiagramPackage.DNODE__OWNED_BORDERED_NODES);
        } else if (ownerElementType == DiagramPackage.eINSTANCE.getDNodeListElement()) {
            reference = (EReference) DiagramPackage.eINSTANCE.getDNodeListElement().getEStructuralFeature(DiagramPackage.DNODE_LIST_ELEMENT__OWNED_BORDERED_NODES);
        } else {
            throw new IllegalStateException("The given 'Owner Element type' is not supported!"); //$NON-NLS-1$
        }
        
        if (targetElementType != DiagramPackage.eINSTANCE.getDNodeContainer()
                && targetElementType != DiagramPackage.eINSTANCE.getDNodeList()
                && targetElementType != DiagramPackage.eINSTANCE.getDNode()
                && targetElementType != DiagramPackage.eINSTANCE.getDNodeListElement()) {
            throw new IllegalStateException("The given 'Target Element type' is not supported!"); //$NON-NLS-1$
        }
        
        createContainmentReferenceRule(configuration, querySpecification, owner, target, reference);
    }
    
    private ReferenceRuleDescriptor createReferenceRule(Configuration configuration, ReferenceQuerySpecification<? extends ReferencePQuery> querySpecification, ElementRuleDescriptor owner, ElementRuleDescriptor target, EReference reference) {
        ID++;
        
        transformationInitializer.addRuleQuerySpecification(querySpecification);
        
        /* Create ReferenceRuleDescriptor */
        ReferenceRuleDescriptor ruleDescriptor = ConfigurationModelUtil.createReferenceRuleDescriptor(
                ID, reference, querySpecification.getFullyQualifiedName(), owner, target);
        configuration.getRuleDescriptors().add(ruleDescriptor);
        /* ****************************** */
        
        /* Create PatternParameterMapping instances */
        PatternParameterMapping patternParameterMapping = null;
        for (Entry<String, String> entry : querySpecification.getInternalQueryRepresentation().getOwnerParameterMappings().entrySet()) {
            patternParameterMapping = ConfigurationModelUtil.createPatternParameterMapping(entry.getKey(), entry.getValue());
            ruleDescriptor.getPatternParameterMappings().add(patternParameterMapping);
            ruleDescriptor.getSourceElementParameterMappings().add(patternParameterMapping);
        }
        
        for (Entry<String, String> entry : querySpecification.getInternalQueryRepresentation().getTargetParameterMappings().entrySet()) {
            patternParameterMapping = ConfigurationModelUtil.createPatternParameterMapping(entry.getKey(), entry.getValue());
            ruleDescriptor.getPatternParameterMappings().add(patternParameterMapping);
            ruleDescriptor.getTargetElementParameterMappings().add(patternParameterMapping);
        }
        /* **************************************** */
        
        return ruleDescriptor;
    }
    
    private void createContainmentReferenceRule(Configuration configuration, ReferenceQuerySpecification<? extends ReferencePQuery> querySpecification, ElementRuleDescriptor owner, ElementRuleDescriptor target, EReference reference) {
        ReferenceRuleDescriptor ruleDescriptor = createReferenceRule(configuration, querySpecification, owner, target, reference);
        
        RuleCandidate<?> ruleCandidate = transformationInitializer.getRuleCandidate(target);
        if (ruleCandidate instanceof AbstractDNodeElementRuleCandidate) {
            transformationInitializer.getRuleCandidates().add(new ContainmentReferenceRuleCandidate(ruleDescriptor, PRIORITY_REFERENCE, null, diagram, transformationInitializer.getRuleProvider(), ((AbstractDNodeElementRuleCandidate) ruleCandidate).getMapping()));
        } else if (ruleCandidate instanceof DEdgeElementRuleCandidate) {
            transformationInitializer.getRuleCandidates().add(new ContainmentReferenceRuleCandidate(ruleDescriptor, PRIORITY_REFERENCE, null, diagram, transformationInitializer.getRuleProvider(), ((DEdgeElementRuleCandidate<?, ?>) ruleCandidate).getEdgeMapping()));
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
