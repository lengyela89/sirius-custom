package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.vql.VQLInterpreterConstants;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.query.DSemanticDiagramQuerySpecification.DSemanticDiagramPQuery;
import org.eclipse.viatra.query.runtime.api.impl.BaseGeneratedEMFPQuery;
import org.eclipse.viatra.query.runtime.emf.types.EClassTransitiveInstancesKey;
import org.eclipse.viatra.query.runtime.matchers.psystem.PBody;
import org.eclipse.viatra.query.runtime.matchers.psystem.PVariable;
import org.eclipse.viatra.query.runtime.matchers.psystem.annotations.PAnnotation;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicdeferred.ExportedParameter;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicenumerables.TypeConstraint;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PParameter;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.QueryInitializationException;
import org.eclipse.viatra.query.runtime.matchers.tuple.FlatTuple;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class DSemanticDiagramQuerySpecification extends SiriusQuerySpecification<DSemanticDiagramPQuery> {

    public DSemanticDiagramQuerySpecification(DSemanticDiagram diagram, EClass semanticRootType,
            String patternFQN) {
        super(new DSemanticDiagramPQuery(diagram, semanticRootType, patternFQN));
    }

    public static class DSemanticDiagramPQuery extends BaseGeneratedEMFPQuery {
        public static final String PARAMETER_SEMANTIC_ROOT = "semanticRoot"; //$NON-NLS-1$

        private String patternFQN;
        private EClass semanticRootType;
        private List<PParameter> parameters;
        
        public DSemanticDiagramPQuery(DSemanticDiagram diagram, EClass semanticRootType, String patternFQN) {
            this.patternFQN = patternFQN;
            this.semanticRootType = semanticRootType;
            this.parameters = Lists.newArrayList();
            
            PParameter parameter = new PParameter(PARAMETER_SEMANTIC_ROOT, semanticRootType.getName());
            this.parameters.add(parameter);
            
            PAnnotation returnAnnotation = new PAnnotation(VQLInterpreterConstants.ANNOTATION_RETURN);
            returnAnnotation.addAttribute(VQLInterpreterConstants.ANNOTATION_PARAMETER_PARAMETER, PARAMETER_SEMANTIC_ROOT);
            addAnnotation(returnAnnotation);
        }
        
        @Override
        public String getFullyQualifiedName() {
            return patternFQN;
        }

        @Override
        public List<PParameter> getParameters() {
            return this.parameters;
        }

        @Override
        protected Set<PBody> doGetContainedBodies() throws QueryInitializationException {
            Set<PBody> bodies = Sets.newLinkedHashSet();

            PBody body = new PBody(this);
            
            PVariable var_semanticRoot = body.getOrCreateVariableByName(PARAMETER_SEMANTIC_ROOT);
            body.setSymbolicParameters(Arrays.<ExportedParameter> asList(new ExportedParameter(body, var_semanticRoot, PARAMETER_SEMANTIC_ROOT)));

            new TypeConstraint(body, new FlatTuple(var_semanticRoot), new EClassTransitiveInstancesKey((EClass) getClassifierLiteral(semanticRootType.getEPackage().getNsURI(), semanticRootType.getName())));

            bodies.add(body);

            return bodies;
        }
    }

}
