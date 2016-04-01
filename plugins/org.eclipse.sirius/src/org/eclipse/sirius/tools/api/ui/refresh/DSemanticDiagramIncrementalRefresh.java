package org.eclipse.sirius.tools.api.ui.refresh;

import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngine;
import org.eclipse.viatra.query.runtime.exception.ViatraQueryException;
import org.eclipse.viatra.transformation.evm.api.RuleEngine;
import org.eclipse.viatra.transformation.evm.specific.ConflictResolvers;
import org.eclipse.viatra.transformation.evm.specific.RuleEngines;
import org.eclipse.viatra.transformation.evm.specific.resolver.FixedPriorityConflictResolver;
import org.eclipse.viatra.transformation.runtime.emf.transformation.eventdriven.EventDrivenTransformation;
import org.eclipse.viatra.transformation.runtime.emf.transformation.eventdriven.EventDrivenTransformation.EventDrivenTransformationBuilder;
import org.eclipse.viatra.transformation.runtime.emf.transformation.eventdriven.ExecutionSchemaBuilder;

public class DSemanticDiagramIncrementalRefresh {
    
    private ViatraQueryEngine engine = null;
    
    private DSemanticDiagram diagram = null;

    private DiagramDescription description = null;
    
    private EventDrivenTransformation transformation = null;
    
    
    public DSemanticDiagramIncrementalRefresh(ViatraQueryEngine engine, DSemanticDiagram diagram) {
        if (engine == null) {
            throw new IllegalArgumentException("The 'engine' parameter can not be null!"); //$NON-NLS-1$
        }
        
        if (diagram == null) {
            throw new IllegalArgumentException("The 'diagram' parameter can not be null!"); //$NON-NLS-1$
        }
        
        if (diagram.getDescription() == null) {
            throw new IllegalArgumentException("The description for the given 'diagram' parameter can not be null!"); //$NON-NLS-1$
        }
        
        this.engine = engine;
        this.diagram = diagram;
        this.description = diagram.getDescription();
    }
    
    public void initialize() throws ViatraQueryException {
        FixedPriorityConflictResolver conflictResolver = ConflictResolvers.createFixedPriorityResolver();
        
        RuleEngine ruleEngine = RuleEngines.createViatraQueryRuleEngine(engine);
        ruleEngine.setConflictResolver(conflictResolver);;
        
        //ruleEngine.addRule(null);
    }
}
