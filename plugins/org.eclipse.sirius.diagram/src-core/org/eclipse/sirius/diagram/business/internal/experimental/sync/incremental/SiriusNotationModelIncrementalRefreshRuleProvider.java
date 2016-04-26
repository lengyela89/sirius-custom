package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental;

import java.util.Map;

import org.eclipse.incquery.viewmodel.configuration.RuleDescriptor;
import org.eclipse.incquery.viewmodel.core.AbstractRuleProvider;
import org.eclipse.incquery.viewmodel.core.rules.ViewModelRule;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreter;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.business.api.componentization.DiagramMappingsManager;
import org.eclipse.sirius.diagram.business.internal.componentization.mappings.DiagramDescriptionMappingsManagerImpl;
import org.eclipse.sirius.diagram.business.internal.componentization.mappings.DiagramMappingsManagerImpl;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.DDiagramElementSynchronizer;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.ecore.extender.business.api.accessor.ModelAccessor;
import org.eclipse.sirius.viewpoint.SiriusPlugin;

public class SiriusNotationModelIncrementalRefreshRuleProvider extends AbstractRuleProvider {

    private Session session = null;

    private DSemanticDiagram diagram = null;
    
    private DiagramDescription description = null;

    private IInterpreter interpreter = null;
    
    private ModelAccessor modelAccessor = null;
    
    private DiagramMappingsManager diagramMappingsManager = null;
    
    private DDiagramElementSynchronizer dDiagramElementSynchronizer = null;


    
    
    public SiriusNotationModelIncrementalRefreshRuleProvider(Session session, DSemanticDiagram diagram, DiagramDescription description,
            DiagramMappingsManager diagramMappingsManager) {
        super();
        
        this.session = session;

        this.diagram = diagram;

        this.description = description;

        // TODO
        this.interpreter = SiriusPlugin.getDefault().getInterpreterRegistry().getInterpreter(diagram);
        
        // TODO
        this.modelAccessor = SiriusPlugin.getDefault().getModelAccessorRegistry().getModelAccessor(diagram);
        
        this.dDiagramElementSynchronizer = new DDiagramElementSynchronizer(diagram, interpreter, modelAccessor);

        this.diagramMappingsManager = diagramMappingsManager;
    }
    
    public Map<RuleDescriptor, ViewModelRule<? extends RuleDescriptor>> getRuleDescriptorToRuleMap() {
        return ruleDescriptorToRule;
    }
    
    public Session getSession() {
        return session;
    }

    public DSemanticDiagram getDiagram() {
        return diagram;
    }

    public IInterpreter getInterpreter() {
        return interpreter;
    }

    public ModelAccessor getModelAccessor() {
        return modelAccessor;
    }

    public DiagramMappingsManager getDiagramMappingsManager() {
        return diagramMappingsManager;
    }

    public DDiagramElementSynchronizer getdDiagramElementSynchronizer() {
        return dDiagramElementSynchronizer;
    }
}
