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
package org.eclipse.jubula.client.core.commands;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jubula.client.core.communication.ConnectionException;
import org.eclipse.jubula.client.core.communication.ServerConnection;
import org.eclipse.jubula.communication.ICommand;
import org.eclipse.jubula.communication.message.DisconnectFromAutAgentResponseMessage;
import org.eclipse.jubula.communication.message.Message;


/**
 * @author BREDEX GmbH
 * @created Apr 19, 2010
 * 
 */
public class DisconnectFromAutAgentResponseCommand implements ICommand {
    /** the logger */
    private static Log log = LogFactory
            .getLog(DisconnectFromAutAgentResponseCommand.class);

    /**
     * <code>m_message</code>
     */
    private DisconnectFromAutAgentResponseMessage m_message;

    /**
     * constructor
     * 
     */
    public DisconnectFromAutAgentResponseCommand() {
    // empty
    }

    /**
     * {@inheritDoc}
     */
    public Message execute() {
        try {
            ServerConnection.getInstance().close();
        } catch (ConnectionException e) {
            if (log.isInfoEnabled()) {
                log.info(e);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public DisconnectFromAutAgentResponseMessage getMessage() {
        return m_message;
    }

    /**
     * {@inheritDoc}
     */
    public void setMessage(Message message) {
        m_message = (DisconnectFromAutAgentResponseMessage)message;
    }

    /**
     * {@inheritDoc}
     */
    public void timeout() {
        log.error(this.getClass().getName() + ".timeout() called"); //$NON-NLS-1$
    }

}