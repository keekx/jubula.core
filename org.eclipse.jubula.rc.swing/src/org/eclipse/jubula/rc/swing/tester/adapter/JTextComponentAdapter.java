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

import javax.swing.text.JTextComponent;

import org.eclipse.jubula.rc.common.driver.IRunnable;
import org.eclipse.jubula.rc.common.tester.adapter.interfaces.ITextInputComponent;
/**
 * Implementation of the Interface <code>ITextComponentAdapter</code> as a
 * adapter for the <code>JTextComponent</code> component.
 * @author BREDEX GmbH
 *
 */
public class JTextComponentAdapter extends JComponentAdapter
    implements ITextInputComponent {
    
    /** */
    private JTextComponent m_textComponent;

    /**
     * Creates an object with the adapted JTextComponent.
     * @param objectToAdapt 
     */
    public JTextComponentAdapter(Object objectToAdapt) {
        super(objectToAdapt);
        m_textComponent = (JTextComponent) objectToAdapt;
    }

    /**
     * {@inheritDoc}
     */
    public String getText() {
        return m_textComponent.getText();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setSelection(final int position) {
        getEventThreadQueuer().invokeAndWait("setSelection", //$NON-NLS-1$
                new IRunnable() {
                    public Object run() {
                        m_textComponent.setCaretPosition(position);
                        return null;
                    }
                });
    }
    
    /**
     * {@inheritDoc}
     */
    public void setSelection(final int start, final int end) {
        getEventThreadQueuer().invokeAndWait("setSelection", //$NON-NLS-1$
                new IRunnable() {
                    public Object run() {
                        m_textComponent.setSelectionStart(start);
                        m_textComponent.setSelectionEnd(end);
                        return null;
                    }
                });
    }

    /**
     * {@inheritDoc}
     */
    public String getSelectionText() {
        String actual = (String)getEventThreadQueuer().invokeAndWait(
                "getSelectionText", new IRunnable() { //$NON-NLS-1$
                    public Object run() {
                        return m_textComponent.getSelectedText();
                    }
                });
        return actual;
    }

    /**
     * {@inheritDoc}
     */
    public void selectAll() {
        getRobot().keyStroke(getRobot().getSystemModifierSpec() + " A"); //$NON-NLS-1$

        if (!getText().equals(getSelectionText())) {
            getEventThreadQueuer().invokeAndWait(
                    "selectAll", new IRunnable() { //$NON-NLS-1$
                        public Object run() {
                            m_textComponent.selectAll();
                            return null;
                        }
                    });
        }
        
    }

    
    /**
     * {@inheritDoc}
     */
    public boolean isEditable() {
        return ((Boolean) getEventThreadQueuer().
                invokeAndWait("isEditable", new IRunnable() { //$NON-NLS-1$
                    public Object run() {
                        return m_textComponent.isEditable() 
                            && m_textComponent.isEnabled()
                                ? Boolean.TRUE : Boolean.FALSE; // see findBugs
                    }
                })).booleanValue();
    }

    
}
