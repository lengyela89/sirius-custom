package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql.VQLInterpreterConstants;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.SiriusCompositeQuerySpecification.SiriusCompositePQuery;
import org.eclipse.viatra.query.runtime.api.impl.BaseGeneratedEMFPQuery;
import org.eclipse.viatra.query.runtime.matchers.psystem.PBody;
import org.eclipse.viatra.query.runtime.matchers.psystem.PVariable;
import org.eclipse.viatra.query.runtime.matchers.psystem.annotations.PAnnotation;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicdeferred.Equality;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicdeferred.ExportedParameter;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicenumerables.PositivePatternCall;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PParameter;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.QueryInitializationException;
import org.eclipse.viatra.query.runtime.matchers.tuple.FlatTuple;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class SiriusCompositeQuerySpecification<T extends SiriusCompositePQuery> extends SiriusQuerySpecification<T> {

    public SiriusCompositeQuerySpecification(T wrappedPQuery) {
        super(wrappedPQuery);
    }
    
    public SiriusCompositeQuerySpecification(String patternFQN, SiriusQuerySpecification<? extends PQuery>... querySpecifications) {
        super(new SiriusCompositePQuery(patternFQN, querySpecifications));
    }

    
    
    public static class SiriusCompositePQuery extends BaseGeneratedEMFPQuery {
        private static final String PARAMETER_PREFIX = "param_"; //$NON-NLS-1$

        private String patternFQN;

        private SiriusQuerySpecification<? extends PQuery>[] querySpecifications;
        
        private List<PParameter> parameters;

        private Map<SiriusQuerySpecification<? extends PQuery>, List<String>> querySpecificationParameters;

        private HashMap<SiriusQuerySpecification<? extends PQuery>, Map<String, String>> querySpecificationParameterMappings;

        
        public SiriusCompositePQuery(String patternFQN,
                SiriusQuerySpecification<? extends PQuery>... querySpecifications) {
            this.patternFQN = patternFQN;
            
            this.querySpecifications = querySpecifications;
            
            this.querySpecificationParameters = Maps.newHashMap();
            
            this.querySpecificationParameterMappings = Maps.newHashMap();
            
            /* Collect the parameters of the Composite pattern */
            this.parameters = Lists.newArrayList();
            addParameters();
            /* *********************************************** */
        }
        
        @Override
        public String getFullyQualifiedName() {
            return patternFQN;
        }

        @Override
        public List<PParameter> getParameters() {
            return parameters;
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

            for (SiriusQuerySpecification<? extends PQuery> querySpecification : querySpecifications) {
                new PositivePatternCall(body, new FlatTuple(
                        transformParameters(body, this.querySpecificationParameters.get(querySpecification))),
                        querySpecification.getInternalQueryRepresentation());
            }
            
            bodies.add(body);
            
            return bodies;

        }
     
        private void addParameters() {
            PParameter newPParameter = null;
            
            int index = 0;
            for (SiriusQuerySpecification<? extends PQuery> querySpecification : querySpecifications) {
                List<String> querySpecificationParameters = this.querySpecificationParameters.get(querySpecification);
                if (querySpecificationParameters == null) {
                    querySpecificationParameters = Lists.newArrayList();
                    
                    this.querySpecificationParameters.put(querySpecification, querySpecificationParameters);
                }
        
                Map<String, String> querySpecificationParameterMappings = this.querySpecificationParameterMappings.get(querySpecification);
                if (querySpecificationParameterMappings == null) {
                    querySpecificationParameterMappings = Maps.newHashMap();
                    
                    this.querySpecificationParameterMappings.put(querySpecification, querySpecificationParameterMappings);
                }
                
                for (PParameter parameter : querySpecification.getParameters()) {
                    newPParameter = new PParameter(PARAMETER_PREFIX + index, parameter.getTypeName());
                    
                    this.parameters.add(newPParameter);
                    querySpecificationParameters.add(newPParameter.getName());
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
        protected void addAnnotations(SiriusQuerySpecification<? extends PQuery> querySpecification, Collection<String> annotations) {
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
                            newAnnotation.addAttribute(entry.getKey(), getCorrespondingParameter(querySpecification, (String) entry.getValue()));
                        }
                    }
                    
                    addAnnotation(newAnnotation);
                }
            }
            /* ****************** */
        }
        
        /**
         * Creates an Equality constraint between the given parameters in the given body.
         * @param body The {@link PBody} instance to which the created {@link Equality} constraint will be added
         * @param parameter1
         * @param parameter2
         */
        protected void addEquality(PBody body, String parameter1, String parameter2) {
            PVariable variable1 = body.getOrCreateVariableByName(parameter1);
            PVariable variable2 = body.getOrCreateVariableByName(parameter2);

            new Equality(body, variable1, variable2);
        }
        
        private Object[] transformParameters(PBody body, List<String> parameterList) {
            Object[] result = new Object[parameterList.size()];
            
            for (int i = 0; i < parameterList.size(); i++) {
                result[i] = body.getOrCreateVariableByName(parameterList.get(i));
            }
            
            return result;
        }
        
        /**
         * Returns the name of the parameter, which belongs to the given query specification's parameter.
         * @param querySpecification
         * @param parameter
         * @return
         */
        protected String getCorrespondingParameter(SiriusQuerySpecification<? extends PQuery> querySpecification, String parameter) {
            return this.querySpecificationParameterMappings.get(querySpecification).get(parameter);
        }
        
        protected HashMap<SiriusQuerySpecification<? extends PQuery>, Map<String, String>> getQuerySpecificationParameterMappings() {
            return querySpecificationParameterMappings;
        }
    }
}
