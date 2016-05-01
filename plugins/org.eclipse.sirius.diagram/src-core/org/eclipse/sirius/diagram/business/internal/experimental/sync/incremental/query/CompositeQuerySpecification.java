package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql.VQLInterpreterConstants;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.CompositeQuerySpecification.CompositePQuery;
import org.eclipse.viatra.query.runtime.api.GenericPatternMatcher;
import org.eclipse.viatra.query.runtime.api.GenericQuerySpecification;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngine;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.api.impl.BaseGeneratedEMFPQuery;
import org.eclipse.viatra.query.runtime.api.scope.QueryScope;
import org.eclipse.viatra.query.runtime.emf.EMFScope;
import org.eclipse.viatra.query.runtime.exception.ViatraQueryException;
import org.eclipse.viatra.query.runtime.matchers.psystem.PBody;
import org.eclipse.viatra.query.runtime.matchers.psystem.PVariable;
import org.eclipse.viatra.query.runtime.matchers.psystem.annotations.PAnnotation;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicdeferred.Equality;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicdeferred.ExportedParameter;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicenumerables.PositivePatternCall;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PParameter;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.QueryInitializationException;
import org.eclipse.viatra.query.runtime.matchers.tuple.FlatTuple;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class CompositeQuerySpecification<T extends CompositePQuery> extends GenericQuerySpecification<GenericPatternMatcher> {

    public CompositeQuerySpecification(T pQuery) {
        super(pQuery);
    }
    
    public CompositeQuerySpecification(DSemanticDiagram diagram, long ruleDescriptorId,
            IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>... querySpecifications) {
        super(new CompositePQuery(diagram, ruleDescriptorId, querySpecifications));
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

    public static class CompositePQuery extends BaseGeneratedEMFPQuery {
        private static final String PATTERN_PREFIX = "_pattern_composite_"; //$NON-NLS-1$
        
        private static final String PARAMETER_PREFIX = "param_"; //$NON-NLS-1$
        
        protected DSemanticDiagram diagram;
        
        protected long ruleDescriptorId;

        protected List<PParameter> parameters = null;

        protected Map<IQuerySpecification<?>, List<PParameter>> querySpecificationToParametersMap;
        
        protected Map<IQuerySpecification<?>, Map<String, String>> querySpecificationToParameterMappingsMap;

        protected IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>[] querySpecifications;


        public CompositePQuery(DSemanticDiagram diagram, long ruleDescriptorId,
                IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>... querySpecifications) {
            
            this.diagram = diagram;
            this.ruleDescriptorId = ruleDescriptorId;
            this.querySpecifications = querySpecifications;
            this.querySpecificationToParametersMap = Maps.newHashMap();
            this.querySpecificationToParameterMappingsMap = Maps.newHashMap();
            
            /* Collect the parameters of the Composite pattern */
            this.parameters = Lists.newArrayList();
            addParameters();
            /* *********************************************** */
        }

        // TODO: FQN egyedi kell legyen a sessionon beluli diagrammok kozott is,
        // igy ezt meg kell oldani valahogy (pl. egyedi diagramnev)
        @Override
        public String getFullyQualifiedName() {
            return diagram.getName().hashCode() + PATTERN_PREFIX + ruleDescriptorId;
        }

        @Override
        public List<PParameter> getParameters() {
            return parameters;
        }
        
        private void addParameters() {
            PParameter newPParameter = null;
            
            int index = 0;
            for (IQuerySpecification<?> querySpecification : querySpecifications) {
                List<PParameter> querySpecificationParameters = this.querySpecificationToParametersMap.get(querySpecification);
                if (querySpecificationParameters == null) {
                    querySpecificationParameters = Lists.newArrayList();
                    
                    this.querySpecificationToParametersMap.put(querySpecification, querySpecificationParameters);
                }
        
                Map<String, String> querySpecificationParameterMappings = this.querySpecificationToParameterMappingsMap.get(querySpecification);
                if (querySpecificationParameterMappings == null) {
                    querySpecificationParameterMappings = Maps.newHashMap();
                    
                    this.querySpecificationToParameterMappingsMap.put(querySpecification, querySpecificationParameterMappings);
                }
                
                for (PParameter parameter : querySpecification.getParameters()) {
                    newPParameter = new PParameter(PARAMETER_PREFIX + index, parameter.getTypeName());
                    
                    this.parameters.add(newPParameter);
                    querySpecificationParameters.add(newPParameter);
                    querySpecificationParameterMappings.put(parameter.getName(), newPParameter.getName());
                    
                    index++;
                }
            }
        }
        
        /**
         * Adds the given annotations (by name) from the given query specification to the new
         *  query specification instance! The annotation-parameters will be transformed if it's needed to point
         *  at the right parameter in the new query specification.
         * @param querySpecification Annotations from this instance will be added to the new query specification.
         * @param annotations Annotations that will be added from the given query specification instance.
         */
        public void addAnnotations(IQuerySpecification<?> querySpecification, Collection<String> annotations) {
            /* Adding annotations */
            PAnnotation newAnnotation = null;
            for (PAnnotation annotation : querySpecification.getAllAnnotations()) {
                /*
                 * We only process those annotations, which are contained in 'annotations' parameter (by name)!
                 */
                if (annotations.contains(annotation.getName())) {
                    newAnnotation = new PAnnotation(annotation.getName());
                    for (Entry<String, Object> entry : annotation.getAllValues()) {
                        /*
                         * The 'parameter' annotation-parameter always point at a pattern parameter.
                         *  In this case we have to rewrite the value belongs to the 'parameter' to the
                         *  new parameter name...
                         */
                        if (entry.getKey().equals(VQLInterpreterConstants.ANNOTATION_PARAMETER_PARAMETER)) {
                            newAnnotation.addAttribute(entry.getKey(), querySpecificationToParameterMappingsMap.get(querySpecification).get(entry.getValue()));
                        }
                    }
                    
                    addAnnotation(newAnnotation);
                }
            }
            /* ****************** */
        }
        
        protected PParameter getPParameterByName(String parameterName) {
            for (PParameter parameter : this.parameters) {
                if (parameter.getName().equals(parameterName)) {
                    return parameter;
                }
            }
            
            return null;
        }
        
        public Map<String, String> getParameterMappings(IQuerySpecification<?> querySpecification) {
            return Collections.unmodifiableMap(querySpecificationToParameterMappingsMap.get(querySpecification));
        }

        @Override
        protected Set<PBody> doGetContainedBodies() throws QueryInitializationException {
            Set<PBody> bodies = Sets.newHashSet();
            
            PBody body = new PBody(this);
            
            /* Create exported parameters */
            List<ExportedParameter> exportedParameters = Lists.newArrayList();
            
            PVariable var_parameter = null;
            for (PParameter parameter : parameters) {
                var_parameter = body.getOrCreateVariableByName(parameter.getName());
                
                exportedParameters.add(new ExportedParameter(body, var_parameter, parameter.getName()));
            }
            
            body.setSymbolicParameters(exportedParameters);
            /* ************************** */

            for (IQuerySpecification<?> querySpecification : querySpecifications) {
                new PositivePatternCall(body, new FlatTuple(createPVariablesFromPParameters(body,
                        querySpecificationToParametersMap.get(querySpecification))), querySpecification.getInternalQueryRepresentation());
            }
            
            bodies.add(body);
            
            return bodies;
        }
        
        protected void addEquality(PBody body, String variable1_name, String variable2_name) {
            PVariable variable1 = body.getOrCreateVariableByName(variable1_name);
            PVariable variable2 = body.getOrCreateVariableByName(variable2_name);

            new Equality(body, variable1, variable2);
        }
        
        private Object[] createPVariablesFromPParameters(PBody body, List<PParameter> parameterList) {
            List<PVariable> variableList = Lists.newArrayList();
            
            for (PParameter parameter : parameterList) {
                variableList.add(body.getOrCreateVariableByName(parameter.getName()));
            }
            
            return variableList.toArray();
        }
        
    }
}
