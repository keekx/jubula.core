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
package org.eclipse.jubula.communication.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jubula.tools.exception.GDVersionException;
import org.eclipse.jubula.tools.messagehandling.MessageIDs;
import org.eclipse.jubula.tools.utils.TimeUtil;



/**
 * A socket extension implementing the protocol for establishing a connection.
 * <br>
 * 
 * After creating the connections, wait a given period and reads the server
 * state from the inputstream.
 * 
 * @author BREDEX GmbH
 * @created 20.09.2004
 */
public class GDSocket extends Socket {
    /** the logger */
    private static Log log = LogFactory.getLog(GDSocket.class);
    
    /** flag wheter a connection could established or not */
    private boolean m_connectionEstablished = false;
    
    /** the state send from the other site, see GuiDancerConnectionState */
    private int m_state = ConnectionState.UNKNOWN; 
    
    /** 
     * The input stream reader for the socket. This reader should be 
     * instantiated once and always reused rather than instantiating a new 
     * reader for the socket. 
     */
    private BufferedReader m_inputStreamReader;
    
    /**
     * creates a new socket and waits <code>waitForServer</code> for the
     * server. This constructor blocks <code>waitForServer</code> in milli
     * seconds.
     * 
     * @param address the inetAddress to connect to
     * @param port the port number
     * @param waitForServer timeout for server sending SERVER_OK
     * @throws IOException from super class java.net.Socket
     * @throws GDVersionException in case of a version error between Client
     * and AutStarter
     */
    public GDSocket(InetAddress address, int port, long waitForServer) 
        throws IOException, GDVersionException {
        super(address, port);
    
        setSoLinger(true, 10);
        
        respondToTypeRequest(waitForServer);
        waitForServerState(waitForServer);
    }

    /**
     * Waits for a "client type" request from the server and responds to that
     * request.
     * 
     * @param waitForServer The maximum amount of time (in milliseconds) to 
     *                      wait for the request from the server.
     * @throws GDVersionException in case of a version error between client
     *                            and server.
     */
    private void respondToTypeRequest(long waitForServer) 
        throws GDVersionException {
        
        try {
            if (log.isDebugEnabled()) {
                log.debug("waiting for server's 'client type request' with timeout: " //$NON-NLS-1$ 
                        + String.valueOf(waitForServer));
            }
            
            InputStream inputStream = getInputStream();
            PrintStream outputStream = 
                new PrintStream(getOutputStream());

            ConnectionState.respondToTypeRequest(waitForServer, 
                    getInputStreamReader(), 
                    inputStream, outputStream, 
                    ConnectionState.CLIENT_TYPE_SINGLE);
        } catch (GDVersionException gdve) {
            handleState(false);
            throw gdve;
        } catch (IOException ioe) {
            log.error(ioe);
            handleState(false);
        }
    }

    /**
     * @return Returns the connected.
     */
    public synchronized boolean isConnectionEstablished() {
        return m_connectionEstablished;
    }

    /**
     * @return Returns the state.
     */
    public synchronized int getState() {
        return m_state;
    }
    /**
     * waits the given time, read a byte from the inputstream and sets the
     * <code>connected</code> flag.
     * 
     * @param waitForServer timeout for the server to send OK
     * @throws GDVersionException in case of a version error between Client
     * and AutStarter.
     */
    private void waitForServerState(long waitForServer) throws 
        GDVersionException {
        
        try {
            if (log.isDebugEnabled()) {
                log.debug("waiting for server ok:" //$NON-NLS-1$ 
                        + String.valueOf(waitForServer));
            }
            
            long waitTime = 0;
            boolean success = false;
            InputStream inputStream = getInputStream();
            BufferedReader inputReader = getInputStreamReader();
            while (!success && (waitTime <= waitForServer)) {
                if (inputStream.available() > 0) {
                    // read one int, it's the state from the server

                    String line = inputReader.readLine();
                    int state = ConnectionState.parseState(line);
                    success = true;
                    setState(state);
                    switch (state) {
                        case ConnectionState.SERVER_OK:
                            handleState(true);
                            break;
                        case ConnectionState.SERVER_BUSY:
                            handleState(false);
                            break;
                        case ConnectionState.VERSION_ERROR:
                            handleState(false);
                            throw new GDVersionException(
                                "Version error between Client and AUT Agent!", //$NON-NLS-1$ 
                                MessageIDs.E_VERSION_ERROR);
                        case ConnectionState.UNKNOWN:
                            // fall through to default
                        default:
                            log.error("unknown state received from server: " //$NON-NLS-1$
                                    + line);
                            handleState(false);
                    }
                } else {
                    TimeUtil.delay(500);
                    waitTime += 500;
                }
            }
            if (!success) {
                log.debug("no response from server"); //$NON-NLS-1$
                setConnectionEstablished(false);
            }
        } catch (IOException ioe) {
            log.error(ioe);
            handleState(false);
        }
    }
    
    /**
     * @param state the connection state
     */
    private void handleState(boolean state) {
        setConnectionEstablished(state);
        if (!state) {
            try {
                close();
            } catch (IOException ioe) {
                if (log.isDebugEnabled()) {
                    log.debug("IOException raised during closing an unconnected socket", //$NON-NLS-1$
                        ioe);
                }
            }
        }
    }

    /**
     * @param connected The connected to set.
     */
    private synchronized void setConnectionEstablished(boolean connected) {
        m_connectionEstablished = connected;
    }
    
    /**
     * @param state The state to set.
     */
    private synchronized void setState(int state) {
        m_state = state;
    }

    /**
     * 
     * @return the input stream reader for this socket.
     * @throws UnsupportedEncodingException if the used encoding 
     *                                      is not supported.
     * @throws IOException if an I/O error occurs while initializing the reader.
     */
    public BufferedReader getInputStreamReader() 
        throws UnsupportedEncodingException, IOException {
        
        if (m_inputStreamReader == null) {
            m_inputStreamReader = new BufferedReader(
                    new InputStreamReader(getInputStream(), 
                            Connection.IO_STREAM_ENCODING));
        }
        
        return m_inputStreamReader;
    }
}