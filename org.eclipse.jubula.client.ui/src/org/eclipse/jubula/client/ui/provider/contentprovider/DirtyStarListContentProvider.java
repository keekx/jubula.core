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
package org.eclipse.jubula.client.ui.provider.contentprovider;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jubula.client.ui.Plugin;


/**
 * @author BREDEX GmbH
 * @created 07.12.2004
 */
public class DirtyStarListContentProvider 
    implements IStructuredContentProvider {

    /**
     * {@inheritDoc}
     */
    public Object[] getElements(Object inputElement) {
        return Plugin.getDefault().getDirtyEditorNames().toArray();
    }

    /**
     * {@inheritDoc}
     */
    public void dispose() { 
        // do nothing
    }

    /**
     * {@inheritDoc}
     *      java.lang.Object, java.lang.Object)
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        // do nothing
    }
}