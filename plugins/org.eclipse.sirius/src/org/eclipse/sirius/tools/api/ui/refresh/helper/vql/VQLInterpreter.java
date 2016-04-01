package org.eclipse.sirius.tools.api.ui.refresh.helper.vql;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.viatra.query.patternlanguage.emf.eMFPatternLanguage.PatternModel;
import org.eclipse.viatra.query.patternlanguage.emf.specification.SpecificationBuilder;
import org.eclipse.viatra.query.patternlanguage.patternLanguage.Pattern;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.exception.ViatraQueryException;
import org.eclipse.viatra.query.runtime.matchers.psystem.annotations.PAnnotation;

import com.google.common.collect.Sets;

public class VQLInterpreter {
    private static final String RESOURCE_URI = "dummy:/queries.vql"; //$NON-NLS-1$
    
    private static final String ANNOTATION_BIND = "Bind"; //$NON-NLS-1$

    private static final String ANNOTATION_RETURN = "Return"; //$NON-NLS-1$
    
    
    private Map<String, IQuerySpecification<?>> expressionToQuerySpecificationMap = null;

    
    
    public static boolean isVQLExpression(String expression) {
        return expression.startsWith(VQLInterpreterConstants.PREFIX);
    }
    
    public VQLInterpreter() {
        expressionToQuerySpecificationMap = new HashMap<String, IQuerySpecification<?>>();
    }
    
    /**
     * Get IQuerySpecification from the given expression
     * @param context The context on which the expression is called
     * @param expression The IQPL expression
     * @return IQuerySpecification created from the given IQPL expression
     */
    private IQuerySpecification<?> getQuerySpecification(DiagramDescription diagramDescription, String expression) {
        String originalExpression = expression;
        if (expressionToQuerySpecificationMap.containsKey(expression)) {
            return expressionToQuerySpecificationMap.get(expression);
        }

        // Remove the prefix from the expression
        expression = expression.replace(VQLInterpreterConstants.PREFIX, ""); //$NON-NLS-1$

        /* Load imports from DiagramDescription */
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n"); //$NON-NLS-1$
        
        for (EPackage ePackage : getImportedMetamodels(diagramDescription)) {
            stringBuilder
                .append("import \"") //$NON-NLS-1$
                .append(ePackage.getNsURI())
                .append("\"\n"); //$NON-NLS-1$
        }
        
        stringBuilder.append("\n"); //$NON-NLS-1$
        stringBuilder.append(expression);
        
        expression = stringBuilder.toString();
        /* ************************************ */
        
        
        InputStream is = new ByteArrayInputStream( expression.getBytes() );
        try {
            ResourceSet resourceSet = new ResourceSetImpl();
            Resource resource = resourceSet.createResource(URI.createURI(RESOURCE_URI));
            SpecificationBuilder specificationBuilder = new SpecificationBuilder();
            
            // Load Resource from the given expression
            resource.load(is, null);
            
            // If the parsing of the expression was successfull
            if (resource.getErrors().size() == 0 && resource.getContents().size() >= 1) {
                
                // The root of the Resource must be a non-empty PatternModel element
                EObject topElement = resource.getContents().get(0);
                if (topElement instanceof PatternModel) {
                    PatternModel patternModel = (PatternModel) topElement;
                    
                    if (patternModel.getPatterns() == null) {
                        return null;
                    }
                    
                    List<IQuerySpecification<?>> querySpecifications = new ArrayList<IQuerySpecification<?>>();
                    for (Pattern pattern : patternModel.getPatterns()) {
                        querySpecifications.add(specificationBuilder.getOrCreateSpecification(pattern));
                    }
                    
                    IQuerySpecification<?> querySpecification = getQuerySpecificationForReturn(querySpecifications);
                    expressionToQuerySpecificationMap.put(originalExpression, querySpecification);
                    
                    return querySpecification;
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ViatraQueryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Returns that IQuerySpecification instance from the given list, which will provide the result(s).
     * @param querySpecifications The IQuerySpecification instances which were created from the given VQL expression
     * @return Returns that IQuerySpecification instance from the given list, which will provide the result(s)
     */
    private IQuerySpecification<?> getQuerySpecificationForReturn(List<IQuerySpecification<?>> querySpecifications) {
        if (querySpecifications == null) {
            return null;
        }
        
        if (querySpecifications.size() == 0) {
            return null;
        } else if (querySpecifications.size() == 1) {
            return querySpecifications.get(0);
        } else {
            PAnnotation returnAnnotation = null;
            for (IQuerySpecification<?> querySpecification : querySpecifications) {
                // TODO validation!!!! Mi van, ha elirja a param��tert...
                returnAnnotation = querySpecification.getFirstAnnotationByName(ANNOTATION_RETURN);
                
                if (returnAnnotation != null) {
                    return querySpecification;
                }
            }
            
            return querySpecifications.get(0);
        }
    }

    
    /**
     * Returns the imported metamodels from the DiagramDescription model.
     * @param diagramDescription
     * @return
     */
    private Set<EPackage> getImportedMetamodels(DiagramDescription diagramDescription) {
        if (diagramDescription != null) {
            return Sets.newHashSet(diagramDescription.getMetamodel());
        }
        
        return Sets.newHashSet();
    }
}
