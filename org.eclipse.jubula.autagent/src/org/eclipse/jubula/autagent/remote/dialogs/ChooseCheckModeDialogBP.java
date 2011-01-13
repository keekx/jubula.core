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
package org.eclipse.jubula.autagent.remote.dialogs;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jubula.autagent.AutStarter;
import org.eclipse.jubula.communication.Communicator;
import org.eclipse.jubula.communication.message.CAPRecordedMessage;
import org.eclipse.jubula.communication.message.ChangeAUTModeMessage;
import org.eclipse.jubula.communication.message.MessageCap;
import org.eclipse.jubula.communication.message.MessageParam;
import org.eclipse.jubula.communication.message.ServerShowDialogResponseMessage;
import org.eclipse.jubula.tools.constants.StringConstants;
import org.eclipse.jubula.tools.exception.CommunicationException;
import org.eclipse.jubula.tools.i18n.CompSystemI18n;
import org.eclipse.jubula.tools.objects.IComponentIdentifier;
import org.eclipse.jubula.tools.xml.businessmodell.Action;
import org.eclipse.jubula.tools.xml.businessmodell.Component;
import org.eclipse.jubula.tools.xml.businessmodell.Param;


/**
 * @author BREDEX GmbH
 * @created 20.06.2005
 * 
 */
public class ChooseCheckModeDialogBP {

    /** the logger */
    private static final Log LOG = LogFactory
        .getLog(ChooseCheckModeDialogBP.class);    
    
    /**
     * singleton
     */
    private static ChooseCheckModeDialog dialog = null;

    /**
     * instance
     */
    private static ChooseCheckModeDialogBP instance = null;
    
    /**
     * a list of actions
     */
    private Map m_actionsObj = new HashMap();

    /**
     * a list of actions
     */
    private List m_actionsNames = new ArrayList();

    /** ComponentIdentifier of actual component */
    private IComponentIdentifier m_compId;
    
    /** String for logical Name */
    private String m_logName;
    
    /**
     * constructor
     *
     */
    private ChooseCheckModeDialogBP() {
        // private constructor
    }

    /**
     * creates a dialog
     * @param component Component
     * @param ci ComponentIdentifier
     * @param point Point on Screen
     * @param checkValues Map
     * @param logName String
     */
    public void create(final Component component, IComponentIdentifier ci, 
            final Point point, final Map checkValues, String logName) {
        
        m_compId = ci;
        m_logName = logName;
        if (dialog != null) {
            closeDialog(ChangeAUTModeMessage.RECORD_MODE);
        }

        EventQueue.invokeLater(new Runnable() {

            public void run() {
                dialog = new ChooseCheckModeDialog();
                dialog.setCheckValues(checkValues);
                initValues(component);
                initialize();
                dialog.createParameterPanel(((Action) m_actionsObj.get(
                        m_actionsNames.get(dialog.getAction()))));

                addListener();
                addListenerStopCheckMode();
                dialog.getOkAndCheckOnButton().requestFocus();

                Dimension screenSize = java.awt.Toolkit
                    .getDefaultToolkit().getScreenSize();
                Dimension frameSize = dialog.getSize();
                if (frameSize.height > screenSize.height) {
                    frameSize.height = screenSize.height;
                }
                if (frameSize.width > screenSize.width) {
                    frameSize.width = screenSize.width;
                }
                dialog.setLocation((screenSize.width - frameSize.width) / 2,
                                   (screenSize.height - frameSize.height) / 2);
                dialog.show();
                dialog.toFront();
            }
        });
    }
    
    /**
     * adds the listeners to the dialog
     *
     */
    private void addListener() {
        dialog.getOkAndCheckOnButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                createCAP();
                closeDialog(ChangeAUTModeMessage.CHECK_MODE);
            }
        });
        dialog.getOkAndCheckOnButton().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    createCAP();
                    closeDialog(ChangeAUTModeMessage.CHECK_MODE);
                }
            }
        });
        dialog.getCancelButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                closeDialog(ChangeAUTModeMessage.CHECK_MODE);
            }
        });
        dialog.getCancelButton().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    closeDialog(ChangeAUTModeMessage.CHECK_MODE);
                }
            }
        });
        dialog.getActionCombo().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dialog.createParameterPanel(((Action) m_actionsObj.get(
                    m_actionsNames.get(dialog.getAction()))));
                for (int i = 0; i < dialog.getTextFields().size(); i++) {
                    JTextField field = (JTextField)dialog.getTextFields()
                        .get(i);
                    field.addKeyListener(new KeyAdapter() {
                        public void keyPressed(KeyEvent e) {
                            if (e.getKeyCode() == 10) {
                                createCAP();
                                for (int i1 = 0; i1 < dialog.getTextFields()
                                    .size(); i1++) {
                                    JTextField field1 = (JTextField)dialog
                                        .getTextFields().get(i1);
                                    field1.removeKeyListener(this);
                                }
                                closeDialog(ChangeAUTModeMessage.CHECK_MODE);
                            }
                        }
                    });
                }
            }
        });
        for (int i = 0; i < dialog.getTextFields().size(); i++) {
            JTextField field = (JTextField)dialog.getTextFields().get(i);
            field.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == 10) {
                        createCAP();
                        for (int i1 = 0; i1 < dialog.getTextFields().size(); 
                            i1++) {
                            JTextField field1 = (JTextField)dialog
                                .getTextFields().get(i1);
                            field1.removeKeyListener(this);
                        }
                        closeDialog(ChangeAUTModeMessage.CHECK_MODE);
                    }
                }
            });
        }
    }
    
    /**
     * adds the listeners to the dialog
     *
     */
    private void addListenerStopCheckMode() {
        dialog.getOkButStopCheckButton().addActionListener(
                new ActionListener() {            
                    public void actionPerformed(ActionEvent event) {
                        createCAP();
                        closeDialog(ChangeAUTModeMessage.RECORD_MODE);
                    }
                });
        dialog.getOkButStopCheckButton().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    createCAP();
                    closeDialog(ChangeAUTModeMessage.RECORD_MODE);
                }
            }
        });
    }

    /**
     * records and executes a cap
     *
     */
    private void createCAP() {
        MessageCap messageCap = new MessageCap();
        
        // setup Action in MessageCap
        Action action = (Action) m_actionsObj.get(m_actionsNames.
                get(dialog.getAction()));
        messageCap.setMethod(action.getMethod());
        
        messageCap.setAction(action);
        
        // setup ComponentIdentifier in MessageCap
        messageCap.setCi(m_compId);
        
        messageCap.setLogicalName(m_logName);
        
        // setup parameters in MessageCap
        List parameterValues = dialog.getParameter();
        List params = ((Action) m_actionsObj.get(m_actionsNames.get(dialog.
                getAction()))).getParams();

        for (int i = 0; i < params.size(); i++) {
            Param param = (Param) params.get(i);
            MessageParam messageParam = new MessageParam();
            messageParam.setType(param.getType());
            String emptyString = StringConstants.EMPTY;
            String value = 
                emptyString.equals(parameterValues.get(i)) ? null 
                    : (String)parameterValues.get(i);
            if (value.length() > 3999) {
                String extraMsg = 
                    "Actions with Strings larger than 3999 are not supported"; //$NON-NLS-1$
                ObservationConsoleBP.getInstance().setExtraMessage(extraMsg);
                return;            
            }
            messageParam.setValue(value);
            messageCap.addMessageParam(messageParam);
        }
        CAPRecordedMessage capTestMessage = new CAPRecordedMessage(messageCap);
        try {
            AutStarter.getInstance().getCommunicator().send(capTestMessage);
        } catch (CommunicationException e) { // NOPMD by al on 4/11/07 3:39 PM
            // no log available here
        }
    }
    
    /**
     * initializing values
     * @param comp
     *      Component
     */
    private void initValues(Component comp) {
        Component component = comp;
        while (component != null && !component.isVisible()
                && !component.getRealized().isEmpty()) {
            List realizedComponents = component.getRealized();
            component = (Component)realizedComponents.get(0);
        }
        if (component.getType() != null) {
            dialog.setNameLabel(CompSystemI18n.getString(component.getType()));
        } else {
            dialog.setNameLabel(
                    CompSystemI18n.getString(m_compId.getSupportedClassName()));
        }
        m_actionsObj.clear();
        Iterator iter = component.getActions().iterator();
        List names = new LinkedList();
        while (iter.hasNext()) {
            Action action = (Action) iter.next();
            if (!action.isDeprecated()
                    && action.getName().indexOf("Verify") != -1 //$NON-NLS-1$
                    && !action.getName().equals(
                            "CompSystem.VerifyTableCellTextAtMousePosition") //$NON-NLS-1$
                    && !action.getName().endsWith("Indexpath") //$NON-NLS-1$
                    && !action.getName().endsWith("Indexpath (Specify Position)") //$NON-NLS-1$
                    && !action.getName().endsWith("Index") //$NON-NLS-1$
                    && !action.getName().endsWith("Indices")) { //$NON-NLS-1$
                names.add(CompSystemI18n.getString(action.getName()));
                m_actionsObj.put(CompSystemI18n.getString(action.getName()), 
                    action);
            }
        }
        m_actionsNames = names;
        Collections.sort(m_actionsNames, Collections.reverseOrder());
        dialog.setActions(m_actionsNames);
    }
    
    /**
     * Initialize the AutFrame
     */
    private void initialize() {
        dialog.setSize(320, 250);
        dialog.setResizable(false);
    }
    
    /** 
     * disposes the dialog if open and sets mode
     * @param mode int
     */
    public void closeDialog(int mode) {
        if (dialog != null) {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    dialog.dispose();
                    dialog = null;
                }
            });
            m_actionsNames.clear();
            m_actionsObj.clear();
            try {
                ServerShowDialogResponseMessage responseMsg = 
                    new ServerShowDialogResponseMessage(false, mode);
                responseMsg.setBelongsToDialog(true);
                Communicator autCommunicator = 
                    AutStarter.getInstance().getAutCommunicator();
                if (autCommunicator != null
                        && autCommunicator.getConnection() != null) {
                    AutStarter.getInstance().getAutCommunicator().send(
                            responseMsg);
                }
            } catch (CommunicationException e) {
                LOG.error("Error sending message!", e); //$NON-NLS-1$
            }
        }
    }
    
    /** 
     * disposes the dialog if open;
     *
     */
    public void closeDialog() {
        if (dialog != null) {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    dialog.dispose();
                    dialog = null;
                }
            });
            m_actionsNames.clear();
            m_actionsObj.clear();
        }
    }
    
    /**
     * getting instance of this class
     * @return instance
     */
    public static ChooseCheckModeDialogBP getInstance() {
        if (instance == null) {
            instance = new ChooseCheckModeDialogBP();
        }
        return instance;
    }
}