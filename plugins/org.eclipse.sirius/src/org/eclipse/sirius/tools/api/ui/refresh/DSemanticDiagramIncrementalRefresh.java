package org.eclipse.sirius.tools.api.ui.refresh;

import java.util.Set;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.incquery.viewmodel.configuration.Configuration;
import org.eclipse.incquery.viewmodel.configuration.ConfigurationFactory;
import org.eclipse.incquery.viewmodel.configuration.ElementRuleDescriptor;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.DiagramFactory;
import org.eclipse.sirius.diagram.description.ContainerMapping;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.diagram.description.DiagramElementMapping;
import org.eclipse.sirius.diagram.description.EdgeMapping;
import org.eclipse.sirius.diagram.description.NodeMapping;
import org.eclipse.viatra.query.runtime.exception.ViatraQueryException;

import com.google.common.collect.Sets;

public class DSemanticDiagramIncrementalRefresh {
    private DSemanticDiagram diagram = null;

    private DiagramDescription description = null;
    
    
    public DSemanticDiagramIncrementalRefresh(DSemanticDiagram diagram) {
        if (diagram == null) {
            throw new IllegalArgumentException("The 'diagram' parameter can not be null!"); //$NON-NLS-1$
        }
        
        if (diagram.getDescription() == null) {
            throw new IllegalArgumentException("The description for the given 'diagram' parameter can not be null!"); //$NON-NLS-1$
        }
        
        this.diagram = diagram;
        this.description = diagram.getDescription();
    }
    
    public void initialize() throws ViatraQueryException {
        
    }
    
    private Configuration createConfigurationModel() {
        Configuration configuration = ConfigurationFactory.eINSTANCE.createConfiguration();
        
        ResourceSet queryResourceSet = new ResourceSetImpl();
        
        Set<DiagramElementMapping> diagramElementMappings = Sets.newHashSet();
        diagramElementMappings.addAll(description.getNodeMappings());
        diagramElementMappings.addAll(description.getContainerMappings());
//        diagramElementMappings.addAll(description.getEdgeMappings());

        for (DiagramElementMapping mapping : diagramElementMappings) {
            processDiagramElementMapping(mapping, configuration, queryResourceSet);
        }
        
        refreshTransformationRuleDescriptorIds(configuration);
        
        return configuration;
    }
    
    // TODO: DiagramMappingsManager / DiagramDescriptionMappingsManager használata lenne az ideális a mappingek lekérdezésére
    // TODO: egymásbaágyazott mappingek kezelése!
    private void processDiagramElementMapping(DiagramElementMapping mapping, Configuration configuration, ResourceSet queryResourceSet) {
        // TODO: logger
        System.out.println("Process DiagramElementMapping (" + mapping.getName() + ")");  //$NON-NLS-1$ //$NON-NLS-2$
        
        if (mapping instanceof NodeMapping) {
            NodeMapping nodeMapping = (NodeMapping) mapping;

            
            
            ElementRuleDescriptor ruleDescriptor = ConfigurationFactory.eINSTANCE.createElementRuleDescriptor();
            ruleDescriptor.setConfiguration(configuration);
            ruleDescriptor.setElementType(DiagramFactory.eINSTANCE.getDiagramPackage().getDNode());
        } else if (mapping instanceof ContainerMapping) {

        } else if (mapping instanceof EdgeMapping) {
            
        }
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
