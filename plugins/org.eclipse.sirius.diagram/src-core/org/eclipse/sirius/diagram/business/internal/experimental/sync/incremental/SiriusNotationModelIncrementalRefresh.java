package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental.helper.mapping.IncrementalRefreshMappingHelper;
import org.eclipse.sirius.diagram.description.ContainerMapping;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.diagram.description.EdgeMapping;
import org.eclipse.sirius.diagram.description.NodeMapping;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.description.RepresentationDescription;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngine;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.emf.EMFScope;
import org.eclipse.viatra.query.runtime.exception.ViatraQueryException;
import org.eclipse.viatra.transformation.runtime.emf.transformation.eventdriven.InconsistentEventSemanticsException;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class SiriusNotationModelIncrementalRefresh {

	private Session session = null;
	
	private ViatraQueryEngine queryEngine = null;
	
	private TransactionalEditingDomain transactionalEditingDomain = null;
	
	private Multimap<DiagramDescription, DSemanticDiagram> descriptionToDiagrams = null;
	
	private Multimap<IQuerySpecification<ViatraQueryMatcher<? extends IPatternMatch>>, DRepresentation> querySpecificationToRepresentations = null;

	
	public SiriusNotationModelIncrementalRefresh(TransactionalEditingDomain transactionalEditingDomain) {
		this.transactionalEditingDomain = transactionalEditingDomain;
		
		this.descriptionToDiagrams = HashMultimap.create();
	}
	
	public void init() throws ViatraQueryException {
        initSessionForEditingDomain();
        
        this.queryEngine = ViatraQueryEngine.on(new EMFScope(transactionalEditingDomain.getResourceSet()));
        
        for (DRepresentation representation : DialectManager.INSTANCE.getAllRepresentations(session)) {
            RepresentationDescription description = DialectManager.INSTANCE.getDescription(representation);
            
            System.out.println("Representation: " + representation.getName()); //$NON-NLS-1$
            System.out.println(" - Description: " + description.getName()); //$NON-NLS-1$

            handleNewRepresentation(representation);
        }
	}
	
	private void initSessionForEditingDomain() {
		for(Session session : SessionManager.INSTANCE.getSessions()) {
			if (session.getTransactionalEditingDomain() == this.transactionalEditingDomain) {
				this.session = session;
				
				return;
			}
		}
		
		this.session = null;
	}
	
	private boolean isAppropriateDescription(RepresentationDescription description) {
	    
	    if (false == description instanceof DiagramDescription) {
	        // TODO: logger
	        System.out.println("The given description (" + description.getName() + ") is not a DiagramDescription!");  //$NON-NLS-1$//$NON-NLS-2$
	        
	        return false;
	    }
	    
	    DiagramDescription diagramDescription = (DiagramDescription) description;
	    
	    
        if (!IncrementalRefreshMappingHelper.hasVQLExpressions(diagramDescription)) {
            // TODO: logger
            System.out.println("The given diagram description (" + description.getName() + ") contains at least one non-VQL expression!");  //$NON-NLS-1$//$NON-NLS-2$
            
            return false;
        }
	    
	    for(NodeMapping mapping : diagramDescription.getAllNodeMappings()) {
	        if (!IncrementalRefreshMappingHelper.hasVQLExpressions(mapping)) {
	            // TODO: logger
	            System.out.println("The given node mapping (" + mapping.getName() + ") contains at least one non-VQL expression!");  //$NON-NLS-1$//$NON-NLS-2$
	            
	            return false;
	        }
	    }
	    
	    for(ContainerMapping mapping : diagramDescription.getAllContainerMappings()) {
            if (!IncrementalRefreshMappingHelper.hasVQLExpressions(mapping)) {
                // TODO: logger
                System.out.println("The given container mapping (" + mapping.getName() + ") contains at least one non-VQL expression!");  //$NON-NLS-1$//$NON-NLS-2$
                
                return false;
            }
        }
	    
	    for(EdgeMapping mapping : diagramDescription.getAllEdgeMappings()) {
            if (!IncrementalRefreshMappingHelper.hasVQLExpressions(mapping)) {
                // TODO: logger
                System.out.println("The given edge mapping (" + mapping.getName() + ") contains at least one non-VQL expression!");  //$NON-NLS-1$//$NON-NLS-2$
                
                return false;
            }
        }	    
	    
	    return true;
	}
	
	private void handleNewRepresentation(DRepresentation representation) {
	    if (false == representation instanceof DSemanticDiagram) {
	        //TODO: logger
	        System.out.println("The given representation (" + representation.getName() + ") is not a DSemanticDiagram instance!"); //$NON-NLS-1$ //$NON-NLS-2$
	        
	        return;
	    }
	    
	    RepresentationDescription description = DialectManager.INSTANCE.getDescription(representation);
	    
	    if (isAppropriateDescription(description)) {
	        // TODO: logger
	        System.out.println("Description (" + description.getName() + ") is OK!"); //$NON-NLS-1$ //$NON-NLS-2$
	        
	        descriptionToDiagrams.put((DiagramDescription) description, (DSemanticDiagram) representation);
	        
//	        DSemanticDiagramIncrementalRefresh dsdir = null; //new DSemanticDiagramIncrementalRefresh((DSemanticDiagram) representation, session);
//	        try {
//                //dsdir.initialize();
//            } catch (ViatraQueryException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (InconsistentEventSemanticsException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
	    } else {
	        System.out.println("Description (" + description.getName() + ") is not OK!"); //$NON-NLS-1$ //$NON-NLS-2$
	    }
	}
	
}
