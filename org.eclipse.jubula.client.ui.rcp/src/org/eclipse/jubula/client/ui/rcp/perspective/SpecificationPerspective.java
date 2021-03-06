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
package org.eclipse.jubula.client.ui.rcp.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * Perspective Factory for Specification Perspective.
 * 
 * @author BREDEX GmbH
 * @created 06.07.2004
 */
public class SpecificationPerspective implements IPerspectiveFactory {
    
    /**
     * 
     * {@inheritDoc}
     */
    public void createInitialLayout(IPageLayout layout) {
        // Nothing to initialize. Layout information is contained in the 
        // plugin.xml.
    }
}