package org.eclipse.sirius.diagram.business.internal.experimental.sync.incremental;

import java.util.Map;

import org.eclipse.sirius.diagram.DSemanticDiagram;

import com.google.common.collect.Maps;

public class DSemanticDiagramIncrementalRefreshFactory {
    
    private static final Map<DSemanticDiagram, DSemanticDiagramIncrementalRefresh> instances = Maps.newHashMap();

    
    public static DSemanticDiagramIncrementalRefresh getInstance(DSemanticDiagram diagram) {
        if (!instances.containsKey(diagram)){
            instances.put(diagram, new DSemanticDiagramIncrementalRefresh(diagram));
        }
        
        return instances.get(diagram);
    }
}
