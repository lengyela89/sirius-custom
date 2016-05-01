package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql.VQLInterpreterConstants;
import org.eclipse.viatra.query.runtime.api.GenericPatternMatcher;
import org.eclipse.viatra.query.runtime.api.GenericQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngine;
import org.eclipse.viatra.query.runtime.api.scope.QueryScope;
import org.eclipse.viatra.query.runtime.emf.EMFScope;
import org.eclipse.viatra.query.runtime.exception.ViatraQueryException;
import org.eclipse.viatra.query.runtime.matchers.psystem.annotations.PAnnotation;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery;

public class SiriusQuerySpecification<T extends PQuery> extends GenericQuerySpecification<GenericPatternMatcher> {
    
    private String resultParameterName;
    private String contextParameterName;

    public SiriusQuerySpecification(PQuery wrappedPQuery) {
        super(wrappedPQuery);
        
        PAnnotation returnAnnotation = wrappedPQuery.getFirstAnnotationByName(VQLInterpreterConstants.ANNOTATION_RETURN);
        if (returnAnnotation != null) {
            Object returnAnnotationParameterValue = returnAnnotation.getFirstValue(VQLInterpreterConstants.ANNOTATION_PARAMETER_PARAMETER);
            if (returnAnnotationParameterValue != null && returnAnnotationParameterValue instanceof String) {
                this.resultParameterName = (String) returnAnnotationParameterValue;
            }
        }
        
        PAnnotation contextAnnotation = wrappedPQuery.getFirstAnnotationByName(VQLInterpreterConstants.ANNOTATION_CONTEXT);
        if (contextAnnotation != null) {
            Object contextAnnotationParameterValue = contextAnnotation.getFirstValue(VQLInterpreterConstants.ANNOTATION_PARAMETER_PARAMETER);
            if (contextAnnotationParameterValue != null && contextAnnotationParameterValue instanceof String) {
                this.contextParameterName = (String) contextAnnotationParameterValue;
            }
        }
    }

    @Override
    public Class<? extends QueryScope> getPreferredScopeClass() {
        return EMFScope.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getInternalQueryRepresentation() {
        return (T) super.getInternalQueryRepresentation();
    }

    @Override
    protected GenericPatternMatcher instantiate(ViatraQueryEngine engine) throws ViatraQueryException {
        GenericPatternMatcher matcher = defaultInstantiate(engine);
        return matcher;
    }

    public String getResultParameterName() {
        return resultParameterName;
    }
    
    public String getContextParameterName() {
        return contextParameterName;
    }
    
}
