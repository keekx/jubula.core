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
package org.eclipse.jubula.client.ui.handlers.project;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.jubula.client.core.persistence.CompNamePM;
import org.eclipse.jubula.client.core.persistence.DatabaseConnectionInfo;
import org.eclipse.jubula.client.core.persistence.GeneralStorage;
import org.eclipse.jubula.client.core.persistence.Persistor;
import org.eclipse.jubula.client.core.persistence.locking.LockManager;
import org.eclipse.jubula.client.core.preferences.database.DatabaseConnection;
import org.eclipse.jubula.client.core.preferences.database.DatabaseConnectionConverter;
import org.eclipse.jubula.client.core.preferences.database.H2ConnectionInfo;
import org.eclipse.jubula.client.ui.dialogs.DBLoginDialog;
import org.eclipse.jubula.client.ui.handlers.AbstractHandler;
import org.eclipse.jubula.client.ui.i18n.Messages;
import org.eclipse.jubula.client.ui.utils.DialogUtils;
import org.eclipse.jubula.tools.constants.DebugConstants;
import org.eclipse.jubula.tools.constants.StringConstants;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author BREDEX GmbH
 * @created 18.04.2005
 */
public abstract class AbstractSelectDatabaseHandler extends AbstractHandler {
    /** the logger */
    private static Logger log = 
        LoggerFactory.getLogger(AbstractSelectDatabaseHandler.class);
    
    /**
     * {@inheritDoc}
     */
    public Object executeImpl(ExecutionEvent event) {
        IStatus returnStatus = Status.CANCEL_STATUS;
        boolean performLogin = false;
        String userName = StringConstants.EMPTY;
        String pwd = StringConstants.EMPTY;
        DatabaseConnectionInfo dbInfo = null;

        List<DatabaseConnection> availableConnections = 
            DatabaseConnectionConverter
                .computeAvailableConnections();
        if (availableConnections.size() == 1
                && availableConnections.get(0).getConnectionInfo() 
                    instanceof H2ConnectionInfo) {
            dbInfo = availableConnections.get(0).getConnectionInfo();
            userName = dbInfo.getProperty(PersistenceUnitProperties.JDBC_USER);
            pwd = dbInfo.getProperty(PersistenceUnitProperties.JDBC_PASSWORD);
            performLogin = true;
        } else {
            DBLoginDialog dialog = new DBLoginDialog(getActiveShell());
            dialog.create();
            DialogUtils.setWidgetNameForModalDialog(dialog);
            dialog.open();
            if (dialog.getReturnCode() == Window.OK) {
                userName = dialog.getUser();
                pwd = dialog.getPwd();
                dbInfo = dialog.getDatabaseConnection().getConnectionInfo();
                performLogin = true;
            }
        }

        if (performLogin) {
            try {
                returnStatus = connectToDatabase(userName, pwd, dbInfo);
            } catch (InterruptedException e) {
                log.error(DebugConstants.ERROR, e);
            }
        }
        
        Persistor.setUser(null);
        Persistor.setPw(null);
        return returnStatus;
    }

    /**
     * 
     * @param username
     *            the username to use
     * @param pwd
     *            the password to use
     * @param info
     *            the database connection info
     * @return a status
     */
    private IStatus connectToDatabase(final String username, final String pwd,
        final DatabaseConnectionInfo info) throws InterruptedException {
        final AtomicReference<IStatus> returnStatus = 
            new AtomicReference<IStatus>(Status.CANCEL_STATUS);
        try {
            PlatformUI.getWorkbench().getProgressService()
                    .run(true, false, new IRunnableWithProgress() {
                        /** {@inheritDoc} */
                        public void run(IProgressMonitor monitor) {
                            monitor.beginTask(Messages.PluginConnectProgress,
                                    IProgressMonitor.UNKNOWN);
                            clearClient();
                            Persistor.setUser(username);
                            Persistor.setPw(pwd);
                            Persistor.setDbConnectionName(info);

                            if (Persistor.instance() != null) {
                                CompNamePM.dispose();
                                GeneralStorage.getInstance().dispose();
                                if (LockManager.isRunning()) {
                                    LockManager.instance().dispose();
                                }
                                Persistor.instance().dispose();
                            }
                            if (Persistor.init()) {
                                LockManager.instance();
                                writeLineToConsole(
                                    Messages.SelectDatabaseConnectSuccessful);
                                returnStatus.set(Status.OK_STATUS);
                            } else {
                                writeLineToConsole(
                                    Messages.SelectDatabaseConnectFailed);
                            }
                        }
                    });
        } catch (InvocationTargetException ite) {
            // Exception occurred during operation
            log.error(DebugConstants.ERROR, ite.getCause());
        } catch (InterruptedException ie) {
            throw ie;
        }

        return returnStatus.get();
    }
    
    /**
     * Clears the ITE of all Project-related elements.
     */
    protected abstract void clearClient();

    /**
     * Writes the given line to the ITE's console.
     * 
     * @param line The text to write to the console.
     */
    protected abstract void writeLineToConsole(String line);
}