/*******************************************************************************
 * Copyright (c) 2012 BREDEX GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BREDEX GmbH - initial API and implementation 
 *******************************************************************************/
package org.eclipse.jubula.rc.swing.tester.adapter;

import javax.swing.AbstractButton;

import org.eclipse.jubula.rc.common.driver.IRunnable;
import org.eclipse.jubula.rc.common.tester.adapter.interfaces.IButtonComponent;
/**
 * Implementation of the button interface as an adapter which holds
 * the <code>javax.swing.AbstractButton</code>.
 * 
 * @author BREDEX GmbH
 */
public class AbstractButtonAdapter extends JComponentAdapter
    implements IButtonComponent {

    /**
     * Creates an object with the adapted JMenu.
     * @param objectToAdapt this must be an object of the Type 
     *      <code>AbstractButton</code>
     */
    public AbstractButtonAdapter(Object objectToAdapt) {
        super(objectToAdapt);
    }
    
    /**
     * @return the casted Object 
     */
    private AbstractButton getAbstractButton() {
        return (AbstractButton) getRealComponent();
    }

    /**
     * {@inheritDoc}
     */
    public String getText() {
        return (String) getEventThreadQueuer().invokeAndWait(
                "getText", new IRunnable() { //$NON-NLS-1$
                    public Object run() {
                        return getAbstractButton().getText();
                    }
                });
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEnabled() {
        Boolean returnvalue = (Boolean) getEventThreadQueuer().invokeAndWait(
                "isEnabled", new IRunnable() { //$NON-NLS-1$
                    public Object run() {
                        return getAbstractButton().getModel().isEnabled()
                                ? Boolean.TRUE : Boolean.FALSE;
                    }
                });
        return (boolean) returnvalue.booleanValue();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSelected() {
        Boolean returnvalue = (Boolean) getEventThreadQueuer().invokeAndWait(
                "isSelected", new IRunnable() { //$NON-NLS-1$
                    public Object run() {
                        return getAbstractButton().getModel().isSelected()
                                ? Boolean.TRUE : Boolean.FALSE;
                    }
                });
        return (boolean) returnvalue.booleanValue();
    }
}