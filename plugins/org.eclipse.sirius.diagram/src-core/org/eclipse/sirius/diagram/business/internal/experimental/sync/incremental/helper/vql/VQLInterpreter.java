package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.SiriusQuerySpecification;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.viatra.query.patternlanguage.emf.eMFPatternLanguage.PatternModel;
import org.eclipse.viatra.query.patternlanguage.emf.specification.SpecificationBuilder;
import org.eclipse.viatra.query.patternlanguage.patternLanguage.Annotation;
import org.eclipse.viatra.query.patternlanguage.patternLanguage.Pattern;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.exception.ViatraQueryException;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery;

import com.google.common.collect.Sets;

public class VQLInterpreter {
    private DiagramDescription diagramDescription;
    
    private SpecificationBuilder specificationBuilder = null;

    
    public static boolean isVQLExpression(String expression) {
        return expression.startsWith(VQLInterpreterConstants.PREFIX);
    }
    
    public VQLInterpreter(DiagramDescription diagramDescription) {
        this.diagramDescription = diagramDescription;
        this.specificationBuilder = new SpecificationBuilder();
    }
    
    /**
     * Get SiriusQuerySpecification from the given expression
     * @param context The context on which the expression is called
     * @param expression The IQPL expression
     * @return SiriusQuerySpecification created from the given IQPL expression
     */
    public SiriusQuerySpecification<? extends PQuery> getQuerySpecification(String expression, String basePatternFQN) {
        try {
            PatternModel patternModel = getPatternModel(diagramDescription, expression);
            
            if (patternModel.getPatterns() == null) {
                return null;
            }

            Pattern returnPattern = getPatternForReturn(patternModel);
            
            // Rename patterns and parameters to be unique
            int additionalPattern = 0;
            String patternName = null;
            for (Pattern pattern : patternModel.getPatterns()) {
                /* Refresh the name of the pattern to be unique */
                if (pattern == returnPattern) {
                    patternName = basePatternFQN;
                } else {
                    patternName = basePatternFQN + "_" + additionalPattern++; //$NON-NLS-1$
                }
                pattern.setName(patternName);
                /* ******************************************** */
            }

            @SuppressWarnings("unchecked")
            IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> querySpecification =
                    (IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>>) specificationBuilder.getOrCreateSpecification(returnPattern);
            
            return new SiriusQuerySpecification<PQuery>(querySpecification.getInternalQueryRepresentation());
        } catch (ViatraQueryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Parses the given expression and returns the root of the constructed model ({@link PatternModel} instance)
     * @param diagramDescription The description of the diagram
     * @param expression The VQL expression to parse
     * @return
     */
    private PatternModel getPatternModel(DiagramDescription diagramDescription, String expression) {
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
            Resource resource = resourceSet.createResource(URI.createURI(VQLInterpreterConstants.RESOURCE_URI));
            
            // Load Resource from the given expression
            resource.load(is, null);

            // Erroneous resource loading...
            if (!resource.getErrors().isEmpty()) {
                String errorMessage = ""; //$NON-NLS-1$
                for (Resource.Diagnostic error : resource.getErrors()) {
                    errorMessage += error.getMessage() + "(" + error.getLocation() + ") \n";  //$NON-NLS-1$//$NON-NLS-2$
                }
                
                throw new IOException(errorMessage);
            }
            
            // There is no PatternModel element in the loaded model...
            if (resource.getContents().isEmpty()) {
                throw new IOException("There is no PatternModel element in the loaded model!"); //$NON-NLS-1$
            }
            
            // There is no Pattern element in the loaded model!
            if (resource.getContents().get(0).eContents().isEmpty()) {
                throw new IOException("There is no Pattern element in the loaded model!"); //$NON-NLS-1$
            }
            
            // If the parsing of the expression was successful
            // The root of the Resource must be a non-empty PatternModel element
            EObject topElement = resource.getContents().get(0);
            if (topElement instanceof PatternModel) {
                return (PatternModel) topElement;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Searches for the {@link Pattern} from the given {@link PatternModel} with annotation '@Return'
     * @param patternModel In which the method searches for the {@link Pattern} to return
     * @return
     */
    private Pattern getPatternForReturn(PatternModel patternModel) {
        if (patternModel.getPatterns().size() == 0) {
            return null;
        } else if (patternModel.getPatterns().size() == 1) {
            return patternModel.getPatterns().get(0);
        } else {
            for (Pattern pattern : patternModel.getPatterns()) {
                if (getAnnotationByName(pattern, VQLInterpreterConstants.ANNOTATION_RETURN) != null) {
                    return pattern;
                }
            }
            
            return patternModel.getPatterns().get(0);
        }
    }

    /**
     * Returns the {@link Annotation} with the given name from the given {@link Pattern}.
     * @param pattern The {@link Pattern} in which the method searches for the {@link Annotation} by name
     * @param annotationName The name of the {@link Annotation} that the method searches for
     * @return
     */
    private Annotation getAnnotationByName(Pattern pattern, String annotationName) {
        if (pattern == null || annotationName == null) {
            return null;
        }
        
        for (Annotation annotation : pattern.getAnnotations()) {
            if (annotation.getName().equals(annotationName)) {
                return annotation;
            }
        }
        
        return null;
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
