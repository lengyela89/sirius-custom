/*******************************************************************************
 * Copyright (c) 2008 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.api.extender;

import java.util.Collection;

import org.eclipse.sirius.ecore.extender.business.api.accessor.MetamodelDescriptor;
import org.eclipse.sirius.viewpoint.description.Viewpoint;

/**
 * Provider able to return a list of MetamodelDescriptor from viewpoints.
 * 
 * This interfaces is deprecated, MetamodelDescriptorProvider2 should be used
 * instead as it provides the ability to handle several Viewpoints at once.
 * 
 * @author cbrun
 * 
 */
@Deprecated
public interface MetamodelDescriptorProvider {
    /**
     * return the list of metamodel descritor provided by the viewpoint.
     * 
     * This method is deprecated and will not be called if the class implements
     * {@link MetamodelDescriptorProvider2}.
     * 
     * @param vp
     *            any representation description.
     * @return the list of metamodel descritor provided by the viewpoint.
     */
    @Deprecated
    Collection<MetamodelDescriptor> provides(Viewpoint vp);
}
