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
package org.eclipse.jubula.rc.swt.listener;

import org.eclipse.jubula.communication.message.ObjectMappedMessage;
import org.eclipse.jubula.rc.common.AUTServer;
import org.eclipse.jubula.rc.common.Constants;
import org.eclipse.jubula.rc.common.exception.NoIdentifierForComponentException;
import org.eclipse.jubula.rc.swt.SwtAUTServer;
import org.eclipse.jubula.tools.exception.CommunicationException;
import org.eclipse.jubula.tools.objects.IComponentIdentifier;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The SWTEventListener for mode OBJECT_MAPPING. <br>
 * 
 * This listener listens to mouse- an key events. 
 *  The component is marked by calling the methods
 * highLight() and lowLight() respectively of the corresponding implementation
 * class. <br>
 * 
 * The key events are tapped for selecting the <code>m_currentComponent</code>
 * to be used for the object mapping. The method <code>accept(KeyEvent)</code>
 * from the <code>MappingAcceptor</code> is queried to decide, whether the
 * event suits the active configuration. <br>
 * 
 * A <code>ComponentHandler</code> is used to determine the identifaction of
 * the component. See the <code>ComponentHandler</code> for details.
 * 
 * @author BREDEX GmbH
 * @created 19.04.2006
 *
 */
public class MappingListener extends AbstractAutSwtEventListener {
    
    /** the logger */
    private static final Logger LOG = 
        LoggerFactory.getLogger(MappingListener.class);

    /**
     * {@inheritDoc}
     */
    protected Color getBorderColor() {
        return new Color(null, Constants.MAPPING_R, Constants.MAPPING_G, 
                Constants.MAPPING_B);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void handleEvent(final Event event) {
        final Display display = ((SwtAUTServer)
                AUTServer.getInstance()).getAutDisplay();
        if (display != null) {
            display.syncExec(new Runnable() {
                public void run() {
                    if (event.equals(getLastEvent())) {
                        return;
                    }
                    setLastEvent(event);
                    switch (event.type) {
                        case SWT.MouseMove:
                        case SWT.MouseEnter:
                        case SWT.MouseDown:
                        case SWT.Arm:
                            setCurrentWidget();
                            highlightComponent();
                            break;
                        case SWT.KeyDown:
                        case SWT.MouseUp:
                            handleKeyEvent(event);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    protected void handleKeyEvent(final Event event) {
        if (LOG.isInfoEnabled()) {
            LOG.info("handleKeyEvent: event = " + event.type); //$NON-NLS-1$                     
        }
        // is a component selected? AND the correct keys pressed?
        final Widget currComp = getCurrentComponent();
        if (currComp != null 
            && getAcceptor().accept(event) == KeyAcceptor.MAPPING_KEY_COMB) {
            try {
                IComponentIdentifier id = ComponentHandler.getIdentifier(
                    currComp);
                if (LOG.isInfoEnabled()) {
                    LOG.info("send a message with identifier " //$NON-NLS-1$
                            + "for the component '" + id //$NON-NLS-1$ 
                            + "'"); //$NON-NLS-1$
                }
                // send a message with the identifier 
                // of the selected component
                ObjectMappedMessage message = new ObjectMappedMessage();
                message.setComponentIdentifier(id);
                AUTServer.getInstance().getCommunicator().send(message);
            } catch (NoIdentifierForComponentException nifce) { 
                
                // no identifier for the component, LOG this as an error
                LOG.error("no identifier for '" + currComp); //$NON-NLS-1$
            } catch (CommunicationException ce) {
                LOG.error(ce.getLocalizedMessage(), ce);
                // do nothing here: a closed connection 
                // is handled by the AUTServer
            }
        }
    }
}