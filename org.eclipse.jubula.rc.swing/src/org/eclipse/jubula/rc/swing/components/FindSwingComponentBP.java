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
package org.eclipse.jubula.rc.swing.components;

import java.awt.Component;

import org.eclipse.jubula.rc.common.components.FindComponentBP;
import org.eclipse.jubula.tools.objects.IComponentIdentifier;

import com.bredexsw.guidancer.autserver.swing.implclasses.GraphicApplication;


/**
 * @author BREDEX GmbH
 * @created Nov 10, 2005
 */
public class FindSwingComponentBP extends FindComponentBP {

    /**
     * Searchs for the component in the AUT with the given
     * <code>componentIdentifier</code>.
     * 
     * @param componentIdentifier the identifier created in object mapping mode
     * @param autHierarchy the current aut hierarchy
     * @throws IllegalArgumentException if the given identifer is null or <br>
     *             the hierarchy is not valid: empty or containing null elements
     * @return the instance of the component of the AUT 
     */
    protected Component findComponent(IComponentIdentifier componentIdentifier, 
            AUTSwingHierarchy autHierarchy) 
        throws IllegalArgumentException {
        
        return (Component)super.findComponent(componentIdentifier, 
                autHierarchy);
    }

    /**
     * {@inheritDoc}
     */
    protected String getCompName(Object currentComponent) {
        return ((Component)currentComponent).getName();
    }

    /**
     * {@inheritDoc}
     */
    protected boolean isAvailable(Object currComp) {
        Component comp = (Component)currComp;
        if (comp instanceof GraphicApplication) {
            return true;
        }
        return comp.isShowing();
    }
}