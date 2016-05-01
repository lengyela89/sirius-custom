package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql.VQLInterpreter;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery;

public class SiriusQuerySpecificationFactory {
    private static final String PATTERN_NAME_PREFIX = "_pattern_sirius_"; //$NON-NLS-1$
    
    private DSemanticDiagram diagram;

    private VQLInterpreter interpreter;
    
    private long querySpecificationID = -1L;

    
    public SiriusQuerySpecificationFactory(DSemanticDiagram diagram) {
        this.diagram = diagram;
        this.interpreter = new VQLInterpreter(diagram.getDescription());
        this.querySpecificationID = -1L;
    }
    
    public SiriusQuerySpecification<? extends PQuery> createSiriusQuerySpecification(String expression) {
        return interpreter.getQuerySpecification(expression, getNextUniquePatternFQN());
    }
    
    public DSemanticDiagramSiriusQuerySpecification createDSemanticDiagramSiriusQuerySpecification(EClass semanticRootType) {
        return new DSemanticDiagramSiriusQuerySpecification(diagram, semanticRootType, getNextUniquePatternFQN());
    }
    
    public AbstractDNodeSCEQuerySpecification createAbstractDNodeSCEQuerySpecification(SiriusQuerySpecification<? extends PQuery> parentQS, SiriusQuerySpecification<? extends PQuery> childQS) {
        return new AbstractDNodeSCEQuerySpecification(getNextUniquePatternFQN(), parentQS, childQS);
    }
    
    public AbstractDNodeContainmentQuerySpecification createAbstractDNodeContainmentQuerySpecification(
            SiriusQuerySpecification<? extends PQuery> ownerQS, SiriusQuerySpecification<? extends PQuery> targetQS) {
        return new AbstractDNodeContainmentQuerySpecification(getNextUniquePatternFQN(), ownerQS, targetQS);
    }
    
    public RelationBasedEdgeTFEQuerySpecification createRelationBasedEdgeTFEQuerySpecification(
            SiriusQuerySpecification<? extends PQuery> sourceQS, SiriusQuerySpecification<? extends PQuery> targetQS,
            SiriusQuerySpecification<? extends PQuery> targetFinderExpressionQS) {
        return new RelationBasedEdgeTFEQuerySpecification(getNextUniquePatternFQN(), sourceQS, targetQS, targetFinderExpressionQS);
    }
    
    public RelationBasedEdgeContainmentQuerySpecification createRelationBasedEdgeContainmentQuerySpecification(
            SiriusQuerySpecification<? extends PQuery> sourceQS, SiriusQuerySpecification<? extends PQuery> targetQS) {
        return new RelationBasedEdgeContainmentQuerySpecification(getNextUniquePatternFQN(), sourceQS, targetQS);
    }
 
    public ElementBasedEdgeSCEQuerySpecification createElementBasedEdgeSCEQuerySpecification(SiriusQuerySpecification<? extends PQuery> parentQS, SiriusQuerySpecification<? extends PQuery> childQS) {
        return new ElementBasedEdgeSCEQuerySpecification(getNextUniquePatternFQN(), parentQS, childQS);
    }
    
    public ElementBasedEdgeContainmentQuerySpecification createElementBasedEdgeContainmentQuerySpecification(
            SiriusQuerySpecification<? extends PQuery> ownerQS, SiriusQuerySpecification<? extends PQuery> targetQS) {
        return new ElementBasedEdgeContainmentQuerySpecification(getNextUniquePatternFQN(), ownerQS, targetQS);
    }
    
    private String getNextUniquePatternFQN() {
        querySpecificationID++;
        
        return diagram.getName().hashCode() + PATTERN_NAME_PREFIX + querySpecificationID;
    }
}
