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
package org.eclipse.jubula.rc.common.commands;

import org.eclipse.jubula.communication.Communicator;
import org.eclipse.jubula.communication.ICommand;
import org.eclipse.jubula.communication.connection.Connection;
import org.eclipse.jubula.communication.listener.IErrorHandler;
import org.eclipse.jubula.communication.message.CAPTestResponseMessage;
import org.eclipse.jubula.communication.message.Message;
import org.eclipse.jubula.communication.message.MessageCap;
import org.eclipse.jubula.communication.message.MessageHeader;
import org.eclipse.jubula.communication.message.PrepareForShutdownMessage;
import org.eclipse.jubula.rc.common.AUTServer;
import org.eclipse.jubula.tools.constants.AUTServerExitConstants;
import org.eclipse.jubula.tools.exception.CommunicationException;
import org.eclipse.jubula.tools.objects.ComponentIdentifier;
import org.eclipse.jubula.tools.utils.EnvironmentUtils;
import org.eclipse.jubula.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Prepares the AUT Server for shutdown (i.e. deregisters communication 
 * error listeners).
 *
 * @author BREDEX GmbH
 * @created Mar 23, 2010
 */
public class PrepareForShutdownCommand implements ICommand {
    /**
     * Amount of milliseconds to delay the AUTs termination
     */
    public static final int AUT_TERMINATION_DELAY = 2000;

    /** the logger */
    private static final Logger LOG = 
        LoggerFactory.getLogger(PrepareForShutdownCommand.class);

    /** the message */
    private PrepareForShutdownMessage m_message;
    
    /**
     * {@inheritDoc}
     */
    public Message execute() {
        Communicator agentCommunicator = 
            AUTServer.getInstance().getServerCommunicator();
        if (agentCommunicator != null) {
            agentCommunicator.clearListeners();
        }
        Connection autAgentConnection = agentCommunicator.getConnection();
        boolean isForce = m_message.isForce();
        if (isForce) {
            if (autAgentConnection != null) {
                // Add a listener to exit the AUT normally when the connection is 
                // closed
                autAgentConnection.addErrorHandler(new IErrorHandler() {
                    public void shutDown() {
                        terminate();
                    }
                    public void sendFailed(MessageHeader header, 
                        String message) {
                        terminate();
                    }
                    private void terminate() {
                        try {
                            AUTServer.getInstance().shutdown();
                        } finally {
                            System.exit(AUTServerExitConstants.EXIT_OK);
                        }
                    }
                });
            }
        } else {
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                public void run() {
                    AUTServer autRC = AUTServer.getInstance();
                    // FIXME improve AUT toolkit handling 
                    if (!autRC.isRcpAccessible()) {
                        // send fake message back - say last CAP execution went OK
                        // this is necessary as e.g. in Swing the AUT event thread blocks 
                        // and thereby our event confirming until the AUT terminates
                        Communicator iteCom = autRC
                                .getCommunicator();
                        CAPTestResponseMessage fakeMessage = 
                                new CAPTestResponseMessage();
                        MessageCap fakeMessageCap = new MessageCap();
                        fakeMessageCap.setCi(new ComponentIdentifier());
                        fakeMessage.setMessageCap(fakeMessageCap);
                        try {
                            iteCom.send(fakeMessage);
                        } catch (CommunicationException e) {
                            // This might also occur if hook has been registered but
                            // AUT terminates without a connection to the ITE e.g.
                            // normal AUT shutdown 
                            LOG.error(e.getLocalizedMessage(), e);
                        }
                    }

                    // keep the AUT alive to perform proper AUT termination synchronization
                    TimeUtil.delay(AUT_TERMINATION_DELAY);
                }
            }));
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Message getMessage() {
        return m_message;
    }

    /**
     * {@inheritDoc}
     */
    public void setMessage(Message message) {
        m_message = (PrepareForShutdownMessage)message;
    }

    /**
     * {@inheritDoc}
     */
    public void timeout() {
        LOG.error(this.getClass().getName() + "timeout() called"); //$NON-NLS-1$
    }

}
