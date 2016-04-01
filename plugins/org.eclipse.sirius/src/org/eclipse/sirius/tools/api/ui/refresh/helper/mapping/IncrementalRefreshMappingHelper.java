package org.eclipse.sirius.tools.api.ui.refresh.helper.mapping;

import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.sirius.diagram.description.ContainerMapping;
import org.eclipse.sirius.diagram.description.DescriptionFactory;
import org.eclipse.sirius.diagram.description.DescriptionPackage;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.diagram.description.EdgeMapping;
import org.eclipse.sirius.diagram.description.NodeMapping;
import org.eclipse.sirius.tools.api.ui.refresh.helper.vql.VQLInterpreter;

import com.google.common.collect.Sets;

public class IncrementalRefreshMappingHelper {
    
    public static final Set<Integer> DIAGRAM_DESCRIPTION_EXPRESSIONS = Sets.newHashSet(
            DescriptionPackage.DIAGRAM_DESCRIPTION__ROOT_EXPRESSION,
            DescriptionPackage.DIAGRAM_DESCRIPTION__PRECONDITION_EXPRESSION,
            DescriptionPackage.DIAGRAM_DESCRIPTION__TITLE_EXPRESSION
    );

    public static final Set<Integer> NODE_MAPPING_EXPRESSIONS = Sets.newHashSet(
            DescriptionPackage.ABSTRACT_NODE_MAPPING__PRECONDITION_EXPRESSION,
            DescriptionPackage.ABSTRACT_NODE_MAPPING__SEMANTIC_CANDIDATES_EXPRESSION,
            DescriptionPackage.ABSTRACT_NODE_MAPPING__SEMANTIC_ELEMENTS
    );
    
    public static final Set<Integer> CONTAINER_MAPPING_EXPRESSIONS = Sets.newHashSet(
            DescriptionPackage.CONTAINER_MAPPING__PRECONDITION_EXPRESSION,
            DescriptionPackage.CONTAINER_MAPPING__SEMANTIC_CANDIDATES_EXPRESSION,
            DescriptionPackage.CONTAINER_MAPPING__SEMANTIC_ELEMENTS
    );
    
    public static final Set<Integer> EDGE_MAPPING_EXPRESSIONS = Sets.newHashSet(
            DescriptionPackage.EDGE_MAPPING__PRECONDITION_EXPRESSION,
            DescriptionPackage.EDGE_MAPPING__SEMANTIC_CANDIDATES_EXPRESSION,
            DescriptionPackage.EDGE_MAPPING__SEMANTIC_ELEMENTS,
            DescriptionPackage.EDGE_MAPPING__PATH_EXPRESSION,
            DescriptionPackage.EDGE_MAPPING__SOURCE_FINDER_EXPRESSION,
            DescriptionPackage.EDGE_MAPPING__TARGET_FINDER_EXPRESSION,
            DescriptionPackage.EDGE_MAPPING__TARGET_EXPRESSION
    );
    
    
    public static boolean hasVQLExpressions(DiagramDescription description) {
        DescriptionPackage descriptionPackage = DescriptionFactory.eINSTANCE.getDescriptionPackage();
        EClass descriptionEClass = descriptionPackage.getDiagramDescription();
        
        EStructuralFeature eStructuralFeature = null;
        for (int featureId : DIAGRAM_DESCRIPTION_EXPRESSIONS) {
            eStructuralFeature = descriptionEClass.getEStructuralFeature(featureId);
            
            String expression = (String) description.eGet(eStructuralFeature);
            
            if (expression != null && !expression.equals("") && !VQLInterpreter.isVQLExpression(expression)) { //$NON-NLS-1$
                return false;
            }
        }
        
        return true;
    }    
    
    public static boolean hasVQLExpressions(NodeMapping mapping) {
        DescriptionPackage descriptionPackage = DescriptionFactory.eINSTANCE.getDescriptionPackage();
        EClass mappingEClass = descriptionPackage.getAbstractNodeMapping();
        
        EStructuralFeature eStructuralFeature = null;
        for (int featureId : NODE_MAPPING_EXPRESSIONS) {
            eStructuralFeature = mappingEClass.getEStructuralFeature(featureId);
            
            String expression = (String) mapping.eGet(eStructuralFeature);
            
            if (expression != null && !expression.equals("") && !VQLInterpreter.isVQLExpression(expression)) { //$NON-NLS-1$
                return false;
            }
        }
        
        return true;
    }
    
    public static boolean hasVQLExpressions(ContainerMapping mapping) {
        DescriptionPackage descriptionPackage = DescriptionFactory.eINSTANCE.getDescriptionPackage();
        EClass mappingEClass = descriptionPackage.getContainerMapping();
        
        EStructuralFeature eStructuralFeature = null;
        for (int featureId : CONTAINER_MAPPING_EXPRESSIONS) {
            eStructuralFeature = mappingEClass.getEStructuralFeature(featureId);
            
            String expression = (String) mapping.eGet(eStructuralFeature);
            
            if (expression != null && !expression.equals("") && !VQLInterpreter.isVQLExpression(expression)) { //$NON-NLS-1$
                return false;
            }
        }
        
        return true;
    }
    
    public static boolean hasVQLExpressions(EdgeMapping mapping) {
        DescriptionPackage descriptionPackage = DescriptionFactory.eINSTANCE.getDescriptionPackage();
        EClass mappingEClass = descriptionPackage.getContainerMapping();
        
        EStructuralFeature eStructuralFeature = null;
        for (int featureId : EDGE_MAPPING_EXPRESSIONS) {
            eStructuralFeature = mappingEClass.getEStructuralFeature(featureId);
            
            String expression = (String) mapping.eGet(eStructuralFeature);
            
            if (expression != null && !expression.equals("") && !VQLInterpreter.isVQLExpression(expression)) { //$NON-NLS-1$
                return false;
            }
        }
        
        return true;
    }
    
}
