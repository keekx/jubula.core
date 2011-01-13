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
package org.eclipse.jubula.rc.swt.components;

import org.eclipse.jubula.rc.common.components.FindComponentBP;
import org.eclipse.jubula.rc.common.driver.IRunnable;
import org.eclipse.jubula.rc.common.exception.StepExecutionException;
import org.eclipse.jubula.rc.swt.driver.EventThreadQueuerSwtImpl;
import org.eclipse.jubula.tools.constants.SwtAUTHierarchyConstants;
import org.eclipse.jubula.tools.objects.IComponentIdentifier;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

import com.bredexsw.guidancer.autswtserver.implclasses.GraphicApplication;


/**
 * @author BREDEX GmbH
 * @created 19.04.2006
 */
public class FindSWTComponentBP extends FindComponentBP {


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
    protected Widget findComponent(final IComponentIdentifier 
        componentIdentifier, final SwtAUTHierarchy autHierarchy) 
        throws IllegalArgumentException {
        
        EventThreadQueuerSwtImpl etQueuer = new EventThreadQueuerSwtImpl();
        return (Widget)etQueuer.invokeAndWait(this.getClass().getName() 
            + ".findComponent", new IRunnable() { //$NON-NLS-1$
                public Object run() throws StepExecutionException {
                    return findComponentImpl(componentIdentifier, 
                        autHierarchy);
                }
            });
    }
    
    /**
     * 
     * @param componentIdentifier the identifier created in object mapping mode
     * @param autHierarchy the current aut hierarchy
     * @return a widget 
     * @see FindComponentBP#findComponent
     */
    private Widget findComponentImpl (final IComponentIdentifier 
        componentIdentifier, final SwtAUTHierarchy autHierarchy) {
        
        return (Widget)super.findComponent(componentIdentifier, autHierarchy);
    }

    /**
     * {@inheritDoc}
     */
    protected String getCompName(Object currentComponent) {
        return (String)((Widget)currentComponent).getData(
                SwtAUTHierarchyConstants.WIDGET_NAME);
    }

    /**
     * {@inheritDoc}
     */
    protected boolean isAvailable(Object currComp) {
        if (currComp instanceof GraphicApplication) {
            return true;
        }
        if (currComp instanceof Control) {
            return !((Control)currComp).isDisposed() 
                && ((Control)currComp).isVisible();
        }
        return !((Widget)currComp).isDisposed();
    }
}