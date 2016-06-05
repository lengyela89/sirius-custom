package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental;

import java.util.List;
import java.util.Map;

import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionListener;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.business.api.session.SessionManagerListener;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class DSemanticDiagramIncrementalRefreshFactory {
    
    private static IPartListener2 partListener = new IPartListener2() {
        
        @Override
        public void partVisible(IWorkbenchPartReference partRef) {
            // Nothing to do...
        }
        
        @Override
        public void partOpened(IWorkbenchPartReference partRef) {
            // Nothing to do...
        }
        
        @Override
        public void partInputChanged(IWorkbenchPartReference partRef) {
            // Nothing to do...            
        }
        
        @Override
        public void partHidden(IWorkbenchPartReference partRef) {
            // Nothing to do...            
        }
        
        @Override
        public void partDeactivated(IWorkbenchPartReference partRef) {
            // Nothing to do...            
        }
        
        @Override
        public void partClosed(IWorkbenchPartReference partRef) {
            if ("org.eclipse.sirius.diagram.ui.part.SiriusDiagramEditorID".equals(partRef.getId()) //$NON-NLS-1$
                    && diagramNameToDSemanticDiagram.containsKey(partRef.getTitle())) {
                DRepresentation dSemanticDiagram = diagramNameToDSemanticDiagram.get(partRef.getTitle());

                disposeIncrementalRefresh(dSemanticDiagram);
            }
        }
        
        @Override
        public void partBroughtToTop(IWorkbenchPartReference partRef) {
            // Nothing to do...            
        }
        
        @Override
        public void partActivated(IWorkbenchPartReference partRef) {
            // Nothing to do...
        }
    };
    
    private static IWindowListener windowListener = new IWindowListener() {
        
        @Override
        public void windowOpened(IWorkbenchWindow window) {
            addPartListener(window);
        }
        
        @Override
        public void windowDeactivated(IWorkbenchWindow window) {
            // Nothing to do...
        }
        
        @Override
        public void windowClosed(IWorkbenchWindow window) {
            removePartListener(window);
        }
        
        @Override
        public void windowActivated(IWorkbenchWindow window) {
            addPartListener(window);
        }
    };
    
    private static final Map<DRepresentation, DSemanticDiagramIncrementalRefresh> instances = Maps.newHashMap();
    private static final Map<String, DRepresentation> diagramNameToDSemanticDiagram = Maps.newHashMap();

    static {
        IWorkbench workbench = PlatformUI.getWorkbench();
        workbench.addWindowListener(windowListener);
        
        SessionManager.INSTANCE.addSessionsListener(new SessionManagerListener() {
            
            @Override
            public void viewpointSelected(Viewpoint selectedSirius) {
                // Nothing to do...
            }
            
            @Override
            public void viewpointDeselected(Viewpoint deselectedSirius) {
                // Nothing to do...
            }
            
            @Override
            public void notifyRemoveSession(Session removedSession) {
                // Nothing to do...
            }
            
            @Override
            public void notifyAddSession(Session newSession) {
                // Nothing to do...
            }
            
            @Override
            public void notify(Session session, int notification) {
                if (notification == SessionListener.CLOSING) {
                    // For those DRepresentation instances, that belongs to the session
                    for (DRepresentation representation : DialectManager.INSTANCE.getAllRepresentations(session)) {
                        disposeIncrementalRefresh(representation);
                    }
                    
                    // Handle removed representations.
                    List<DRepresentation> removedRepresentations = Lists.newArrayList();
                    for (DRepresentation representation : instances.keySet()) {
                        if (representation.eContainer() == null) {
                            removedRepresentations.add(representation);
                        }
                    }
                    
                    for (DRepresentation representation : removedRepresentations) {
                        disposeIncrementalRefresh(representation);
                    }
                    /* ************************************* */
                }
            }
        });
    }
    
    public static DSemanticDiagramIncrementalRefresh getInstance(DSemanticDiagram diagram) {
        if (!instances.containsKey(diagram)) {
            instances.put(diagram, new DSemanticDiagramIncrementalRefresh(diagram));
            diagramNameToDSemanticDiagram.put(diagram.getName(), diagram);
        }
        
        return instances.get(diagram);
    }
    
    private static void addPartListener(IWorkbenchWindow window) {
        window.getPartService().addPartListener(partListener);
    }
    
    private static void removePartListener(IWorkbenchWindow window) {
        window.getPartService().removePartListener(partListener);
    }
    
    public static void disposeIncrementalRefresh(DRepresentation representation) {
        // TODO
        //System.out.println("Disposing Incremental refresh..."); //$NON-NLS-1$
        DSemanticDiagramIncrementalRefresh ir = instances.get(representation);
        if (ir != null) {
            ir.dispose();
            instances.remove(representation);
            diagramNameToDSemanticDiagram.remove(representation.getName());
        }
    }
}
