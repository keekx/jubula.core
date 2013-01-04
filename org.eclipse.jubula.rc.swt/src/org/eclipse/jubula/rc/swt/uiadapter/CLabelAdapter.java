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
package org.eclipse.jubula.rc.swt.uiadapter;

import org.eclipse.jubula.rc.common.driver.IRunnable;
import org.eclipse.jubula.rc.common.exception.StepExecutionException;
import org.eclipse.jubula.rc.common.uiadapter.interfaces.ITextVerifiable;
import org.eclipse.jubula.rc.swt.utils.SwtUtils;
import org.eclipse.jubula.tools.objects.event.EventFactory;
import org.eclipse.swt.custom.CLabel;
/**
 * 
 * @author BREDEX GmbH
 *
 */
public class CLabelAdapter extends WidgetAdapter implements ITextVerifiable {

    /**
     * 
     * @param objectToAdapt the component to adapt
     */
    public CLabelAdapter(Object objectToAdapt) {
        super(objectToAdapt);
    }

    /**
     * {@inheritDoc}
     */
    public String getText() {
        final CLabel label = (CLabel) getRealComponent();
        return (String)getEventThreadQueuer().invokeAndWait(
                "getText", new IRunnable() { //$NON-NLS-1$
                    public Object run() {
                        try {
                            return SwtUtils.removeMnemonics(label.getText());
                        } catch (NullPointerException e) {
                            throw new StepExecutionException(
                                    "component must not be null", //$NON-NLS-1$
                                    EventFactory
                                    .createComponentNotFoundErrorEvent());
                        }
                    }
                });
    }

}