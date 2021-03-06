/*******************************************************************************
 * Copyright (c) 2004, 2010 BREDEX GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BREDEX GmbH - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.jubula.client.ui.rcp.sorter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jubula.client.core.model.ICapPO;
import org.eclipse.jubula.client.core.model.ICategoryPO;
import org.eclipse.jubula.client.core.model.IEventExecTestCasePO;
import org.eclipse.jubula.client.core.model.IExecTestCasePO;
import org.eclipse.jubula.client.core.model.IRefTestSuitePO;
import org.eclipse.jubula.client.core.model.IReusedProjectPO;

/**
 * @author BREDEX GmbH
 * @created 18.02.2009
 */
public class NodeNameViewerSorter extends ViewerSorter {
    /**
     * {@inheritDoc}
     */
    public int compare(Viewer viewer, Object e1, Object e2) {
        // Show categories before all other elements
        if (isOnlyFirstObjectInstanceOfClass(e1, e2, ICategoryPO.class)
                || isOnlyFirstObjectInstanceOfClass(e1, e2, 
                        IReusedProjectPO.class)) {
            return -1;
        }

        if (isOnlyFirstObjectInstanceOfClass(e2, e1, ICategoryPO.class)
                || isOnlyFirstObjectInstanceOfClass(e2, e1, 
                        IReusedProjectPO.class)) {
            return 1;
        }
        
        // Show Event Handler before all other nested exec test cases
        if (isOnlyFirstObjectInstanceOfClass(e1, e2, 
                IEventExecTestCasePO.class)) {
            return -1;
        }

        if (isOnlyFirstObjectInstanceOfClass(e2, e1, 
                IEventExecTestCasePO.class)) {
            return 1;
        }

        // do not sort the sequence of exec test cases or caps in spec test
        // cases
        if (e1 instanceof IExecTestCasePO 
                || e2 instanceof IExecTestCasePO
                || e1 instanceof ICapPO 
                || e2 instanceof ICapPO
                || e1 instanceof IRefTestSuitePO
                || e2 instanceof IRefTestSuitePO) {
            return 0;
        }

        return super.compare(viewer, e1, e2);
    }

    /**
     * @param o1
     *            the first object
     * @param o2
     *            the second object
     * @param c
     *            the object class to check
     * @return if only the first object is an instance of the given class
     */
    private boolean isOnlyFirstObjectInstanceOfClass(Object o1, Object o2,
            Class c) {
        return c.isInstance(o1) && !c.isInstance(o2);
    }
}
