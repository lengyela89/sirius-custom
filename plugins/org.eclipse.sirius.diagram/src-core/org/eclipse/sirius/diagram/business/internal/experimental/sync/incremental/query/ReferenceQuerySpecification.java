package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import java.util.Collections;
import java.util.Map;

import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.ReferenceQuerySpecification.ReferencePQuery;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery;

public class ReferenceQuerySpecification<T extends ReferencePQuery> extends SiriusCompositeQuerySpecification<T> {

    public ReferenceQuerySpecification(T wrappedQuery) {
        super(wrappedQuery);
    }
    
    @SuppressWarnings("unchecked")
    public ReferenceQuerySpecification(String patternFQN,
            SiriusQuerySpecification<? extends PQuery> ownerQS, SiriusQuerySpecification<? extends PQuery> targetQS) {
        super((T) new ReferencePQuery(patternFQN, ownerQS, targetQS));
    }
    
    public static class ReferencePQuery extends SiriusCompositePQuery {
        protected SiriusQuerySpecification<? extends PQuery> ownerQS = null;
        
        protected SiriusQuerySpecification<? extends PQuery> targetQS = null;


        @SuppressWarnings("unchecked")
        public ReferencePQuery(String patternFQN,
                SiriusQuerySpecification<? extends PQuery> ownerQS,
                SiriusQuerySpecification<? extends PQuery> targetQS) {

            super(patternFQN, ownerQS, targetQS);
            
            this.ownerQS = ownerQS;
            this.targetQS = targetQS;
        }
        
        public SiriusQuerySpecification<? extends PQuery> getOwnerQS() {
            return this.ownerQS;
        }

        public SiriusQuerySpecification<? extends PQuery> getTargetQS() {
            return this.targetQS;
        }
        
        public Map<String, String> getOwnerParameterMappings() {
            return Collections.unmodifiableMap(getQuerySpecificationParameterMappings().get(ownerQS));
        }
        
        public Map<String, String> getTargetParameterMappings() {
            return Collections.unmodifiableMap(getQuerySpecificationParameterMappings().get(targetQS));
        }
    }
}
