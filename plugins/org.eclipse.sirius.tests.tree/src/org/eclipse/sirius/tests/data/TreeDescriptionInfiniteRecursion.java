/*******************************************************************************
 * Copyright (c) 2011 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tests.data;

import org.eclipse.sirius.tree.description.TreeDescription;
import org.eclipse.sirius.tree.description.TreeItemMapping;

public class TreeDescriptionInfiniteRecursion {

    private TreeDescription val;

    public TreeDescriptionInfiniteRecursion(TreeDescription val) {
        this.val = val;
    }

    public class TreeItemMappingCurrentEObject {

        private TreeItemMapping val;

        public TreeItemMappingCurrentEObject(TreeItemMapping val) {
            this.val = val;
        }

        public TreeItemMapping object() {
            return this.val;
        }
    }

    public TreeDescription object() {
        return this.val;
    }
}
