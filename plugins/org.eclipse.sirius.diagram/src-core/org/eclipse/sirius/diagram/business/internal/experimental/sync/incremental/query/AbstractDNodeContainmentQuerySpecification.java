package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import java.util.Set;

import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql.VQLInterpreterConstants;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.AbstractDNodeContainmentQuerySpecification.AbstractDNodeContainmentPQuery;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.ReferenceQuerySpecification.ReferencePQuery;
import org.eclipse.viatra.query.runtime.matchers.psystem.PBody;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.QueryInitializationException;

import com.google.common.collect.Sets;

public class AbstractDNodeContainmentQuerySpecification extends ReferenceQuerySpecification<AbstractDNodeContainmentPQuery> {

    public AbstractDNodeContainmentQuerySpecification(String patternFQN,
            SiriusQuerySpecification<? extends PQuery> ownerQS, SiriusQuerySpecification<? extends PQuery> targetQS) {
        super(new AbstractDNodeContainmentPQuery(patternFQN, ownerQS, targetQS));
    }
    
    public static class AbstractDNodeContainmentPQuery extends ReferencePQuery {
        public AbstractDNodeContainmentPQuery(String patternFQN,
                SiriusQuerySpecification<? extends PQuery> ownerQS,
                SiriusQuerySpecification<? extends PQuery> targetQS) {

            super(patternFQN, ownerQS, targetQS);
            
            addAnnotations(targetQS, Sets.<String>newHashSet(VQLInterpreterConstants.ANNOTATION_RETURN, VQLInterpreterConstants.ANNOTATION_CONTEXT));
        }
        
        @Override
        protected Set<PBody> doGetContainedBodies() throws QueryInitializationException {
            Set<PBody> bodies = super.doGetContainedBodies();
            
            for (PBody body : bodies) {
                if (ownerQS != null) {
                    addEquality(
                            body,
                            getCorrespondingParameter(ownerQS, ownerQS.getResultParameterName()),
                            getCorrespondingParameter(targetQS, targetQS.getContextParameterName()));
                }
            }
            
            return bodies;
        }
    }
}
