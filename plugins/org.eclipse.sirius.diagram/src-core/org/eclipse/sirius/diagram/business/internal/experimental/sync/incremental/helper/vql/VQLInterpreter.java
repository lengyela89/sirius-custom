package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import org.eclipse.viatra.query.patternlanguage.patternLanguage.Annotation;
import org.eclipse.viatra.query.patternlanguage.patternLanguage.AnnotationParameter;
import org.eclipse.viatra.query.patternlanguage.patternLanguage.ParameterRef;
import org.eclipse.viatra.query.patternlanguage.patternLanguage.Pattern;
import org.eclipse.viatra.query.patternlanguage.patternLanguage.PatternBody;
import org.eclipse.viatra.query.patternlanguage.patternLanguage.StringValue;
import org.eclipse.viatra.query.patternlanguage.patternLanguage.Variable;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.exception.ViatraQueryException;
import org.eclipse.viatra.query.runtime.matchers.psystem.annotations.PAnnotation;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class VQLInterpreter {
    private static final String RESOURCE_URI = "dummy:/queries.vql"; //$NON-NLS-1$
    
    private static final String ANNOTATION_BIND = "Bind"; //$NON-NLS-1$

    private static final String ANNOTATION_RETURN = "Return"; //$NON-NLS-1$
    
    private static final String ANNOTATION_CONTEXT = "Context"; //$NON-NLS-1$
    
    private static final String ANNOTATION_PARAMETER_PARAMETER = "parameter"; //$NON-NLS-1$
    
    private static final String PATTERN_NAME_PREFIX = "pattern_"; //$NON-NLS-1$
    
    private static final String PATTERN_PARAMETER_NAME_PREFIX = "_param_"; //$NON-NLS-1$
    
    
    
    private SpecificationBuilder specificationBuilder = null;
    
    
    
    public static boolean isVQLExpression(String expression) {
        return expression.startsWith(VQLInterpreterConstants.PREFIX);
    }
    
    public VQLInterpreter() {
        specificationBuilder = new SpecificationBuilder();
    }
    
    /**
     * Get IQuerySpecification from the given expression
     * @param context The context on which the expression is called
     * @param expression The IQPL expression
     * @return IQuerySpecification created from the given IQPL expression
     */
    public IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> getQuerySpecification(DiagramDescription diagramDescription, String expression, long id) {
        try {
            // TODO
            System.out.println("EXPRESSION:" + expression); //$NON-NLS-1$
            
            PatternModel patternModel = getPatternModel(diagramDescription, expression);
            
            if (patternModel.getPatterns() == null) {
                return null;
            }

            Pattern returnPattern = getPatternForReturn(patternModel);
            
            // Rename patterns and parameters to be unique
            int additionalPattern = 0;
            String patternName = null;
            Map<String, String> parameterNameMap = null;
            for (Pattern pattern : patternModel.getPatterns()) {
                parameterNameMap = Maps.newHashMap();

                /* Refresh the name of the pattern to be unique */
                if (pattern == returnPattern) {
                    patternName = PATTERN_NAME_PREFIX + id;
                } else {
                    patternName = PATTERN_NAME_PREFIX + id + "_" + additionalPattern++; //$NON-NLS-1$
                }
                pattern.setName(patternName);
                /* ******************************************** */
                
                rewritePatternParameterNames(pattern, parameterNameMap);
                
                rewriteAnnotations(pattern, parameterNameMap);
            }
            
            return (IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>>) specificationBuilder.getOrCreateSpecification(returnPattern);
        } catch (ViatraQueryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
    
    // TODO: comment!
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
            Resource resource = resourceSet.createResource(URI.createURI(RESOURCE_URI));
            
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
            
            // If the parsing of the expression was successfull
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
     * Returns that IQuerySpecification instance from the given list, which will provide the result(s).
     * @param querySpecifications The IQuerySpecification instances which were created from the given VQL expression
     * @return Returns that IQuerySpecification instance from the given list, which will provide the result(s)
     */
    private IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> getQuerySpecificationForReturn(List<IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>>> querySpecifications) {
        if (querySpecifications == null) {
            return null;
        }
        
        if (querySpecifications.size() == 0) {
            return null;
        } else if (querySpecifications.size() == 1) {
            return querySpecifications.get(0);
        } else {
            PAnnotation returnAnnotation = null;
            for (IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>> querySpecification : querySpecifications) {
                // TODO validation!!!! Mi van, ha elirja a parametert...
                returnAnnotation = querySpecification.getFirstAnnotationByName(ANNOTATION_RETURN);
                
                if (returnAnnotation != null) {
                    return querySpecification;
                }
            }
            
            return querySpecifications.get(0);
        }
    }
    
    // TODO: comment
    private Pattern getPatternForReturn(PatternModel patternModel) {
        if (patternModel.getPatterns().size() == 0) {
            return null;
        } else if (patternModel.getPatterns().size() == 1) {
            return patternModel.getPatterns().get(0);
        } else {
            for (Pattern pattern : patternModel.getPatterns()) {
                if (getAnnotationByName(pattern, ANNOTATION_RETURN) != null) {
                    return pattern;
                }
            }
            
            return patternModel.getPatterns().get(0);
        }
    }
    
    // TODO: comment!
    private String getResultParameterName(Pattern pattern) {
        Annotation annotation = getAnnotationByName(pattern, ANNOTATION_RETURN);
        
        if (annotation != null) {
            AnnotationParameter annotationParameter = getAnnotationParameterByName(annotation, ANNOTATION_PARAMETER_PARAMETER);
            
            if (annotationParameter != null) {
                return ((StringValue) annotationParameter.getValue()).getValue();
            }
        }
        
        return null;
    }
    
    // TODO: comment!
    private void rewriteAnnotations(Pattern pattern, Map<String, String> parameterNameMap) {
        for (Annotation annotation : pattern.getAnnotations()) {
            if (annotation.getName().equals(ANNOTATION_RETURN)
                    || annotation.getName().equals(ANNOTATION_CONTEXT)) {
                AnnotationParameter annotationParameter = getAnnotationParameterByName(annotation, ANNOTATION_PARAMETER_PARAMETER);
                if (annotationParameter == null) {
                    throw new IllegalStateException("Not appropriate annotation! Annotation parameter ('"+ ANNOTATION_PARAMETER_PARAMETER +"') can not be found!");  //$NON-NLS-1$//$NON-NLS-2$
                }
                
                rewriteAnnotationParameterValue(annotationParameter, parameterNameMap.get(((StringValue) annotationParameter.getValue()).getValue()));
            } else if (annotation.getName().equals(ANNOTATION_BIND)) {
                rewriteAnnotationParameterName(
                        annotation.getParameters().get(0),
                        parameterNameMap.get(annotation.getParameters().get(0).getName()));
            }
        }
    }
    
    // TODO: comment!
    private void rewriteAnnotationParameterValue(AnnotationParameter annotationParameter, String newValue) {
        ((StringValue) annotationParameter.getValue()).setValue(newValue);
    }
    
    // TODO: comment
    private void rewriteAnnotationParameterName(AnnotationParameter annotationParameter, String newValue) {
        annotationParameter.setName(newValue);
    }
    
    // TODO: comment
    private void rewritePatternParameterNames(Pattern pattern, Map<String, String> parameterNameMap) {
        // Refresh the name of the parameters to be unique
        int parameterIndex = -1;
        String parameterName = null;
        for (Variable parameter : pattern.getParameters()) {
            parameterIndex = pattern.getParameters().indexOf(parameter);
            
            parameterName = pattern.getName() + PATTERN_PARAMETER_NAME_PREFIX + parameterIndex;
            
            parameterNameMap.put(parameter.getName(), parameterName);
            
            parameter.setName(parameterName);
        }
        
        // Refresh the name of ParameterRef instances in the pattern bodies to be
        //  the same as corresponding parameter's name
        for (PatternBody patternBody : pattern.getBodies()) {
            for (Variable variable : patternBody.getVariables()) {
                if (variable instanceof ParameterRef) {
                    variable.setName(((ParameterRef) variable).getReferredParam().getName());
                }
            }
        }

    }
    
    // TODO: remove
//    public String getContainerSemanticElementParameterName(Pattern pattern) {
//        Annotation annotation = getAnnotationByName(pattern, ANNOTATION_CONTAINER_SEMANTIC_ELEMENT);
//        
//        if (annotation != null) {
//            AnnotationParameter annotationParameter = getAnnotationParameterByName(annotation, ANNOTATION_PARAMETER_PARAMETER);
//            
//            if (annotationParameter != null) {
//                return ((StringValue) annotationParameter.getValue()).getValue();
//            }
//        }
//        
//        return null;
//    }
    
    
    // TODO: Comment!
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
    
    // TODO: Comment!
    private AnnotationParameter getAnnotationParameterByName(Annotation annotation, String annotationParameterName) {
        if (annotation == null || annotationParameterName == null) {
            return null;
        }
        
        for (AnnotationParameter annotationParameter : annotation.getParameters()) {
            if (annotationParameter.getName().equals(annotationParameterName)) {
                return annotationParameter;
            }
        }
        
        return null;
    }

    /**
     * Returns the bindings belong to this query specification. The key is the parameter's name,
     *  the value is the name of the bound Sirius' variable.
     * @param querySpecification
     * @return
     */
    public static Map<String, String> getBindings(IQuerySpecification<?> querySpecification) {
        if (querySpecification == null) {
            return null;
        }

        Map<String, String> result = Maps.newHashMap();
        
        for (PAnnotation bindAnnotation : querySpecification.getAnnotationsByName(ANNOTATION_BIND)) {
            for (Entry<String, Object> entry : bindAnnotation.getAllValues()) {
                if (entry.getValue() instanceof String) {
                    result.put(entry.getKey(), (String) entry.getValue());
                    
                    break;
                }
            }
        }
        
        return result;
    }    
    
    /**
     * Get the name of that parameter, which will contain the result.
     * @param querySpecification The IQuerySpecification instance, which were created from the given IQPL expression
     * @return The name of that parameter, which will contain the result
     */
    public static String getResultParameterName(IQuerySpecification<?> querySpecification) {
        if (querySpecification == null || querySpecification.getParameterNames().isEmpty()) {
            return null;
        }
        
        PAnnotation returnAnnotation = querySpecification.getFirstAnnotationByName(ANNOTATION_RETURN);
        
        if (returnAnnotation != null) {
            // TODO validation itt is!!!! MI van ha nem Stringet irt be...
            return (String) returnAnnotation.getFirstValue(ANNOTATION_PARAMETER_PARAMETER);
        }
        
        return querySpecification.getParameterNames().get(0);
    }
    
    public static String getContextParameterName(IQuerySpecification<?> querySpecification) {
        if (querySpecification == null || querySpecification.getParameterNames().isEmpty()) {
            return null;
        }
        
        PAnnotation returnAnnotation = querySpecification.getFirstAnnotationByName(ANNOTATION_CONTEXT);
        
        if (returnAnnotation != null) {
            // TODO validation itt is!!!! MI van ha nem Stringet irt be...
            return (String) returnAnnotation.getFirstValue(ANNOTATION_PARAMETER_PARAMETER);
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
