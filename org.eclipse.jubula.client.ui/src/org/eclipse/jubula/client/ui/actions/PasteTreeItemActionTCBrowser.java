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
package org.eclipse.jubula.client.ui.actions;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jubula.client.core.persistence.PMException;
import org.eclipse.jubula.client.ui.Plugin;
import org.eclipse.jubula.client.ui.controllers.PMExceptionHandler;
import org.eclipse.jubula.client.ui.controllers.dnd.LocalSelectionClipboardTransfer;
import org.eclipse.jubula.client.ui.controllers.dnd.TCBrowserDndSupport;
import org.eclipse.jubula.client.ui.model.GuiNode;
import org.eclipse.jubula.client.ui.views.TestCaseBrowser;
import org.eclipse.jubula.tools.exception.GDProjectDeletedException;


/**
 * Implementation of the Paste action within the Test Case Browser.
 *
 * @author BREDEX GmbH
 * @created 19.03.2008
 */
public class PasteTreeItemActionTCBrowser extends AbstractPasteTreeItemAction {

    /**
     * {@inheritDoc}
     */
    public void run() {
        TestCaseBrowser tstv = (TestCaseBrowser)Plugin.getActiveView();
        LocalSelectionClipboardTransfer transfer = 
            LocalSelectionClipboardTransfer.getInstance();
        
        if (!(tstv.getClipboard().getContents(transfer)
                instanceof IStructuredSelection)) {
            return;
        }
        IStructuredSelection pasteSelection = 
            (IStructuredSelection)tstv.getClipboard().getContents(transfer);
        if (pasteSelection != null) {
            List <GuiNode> nodesToBeMoved = pasteSelection.toList();
            
            // Paste will always occur at the most recently selected node.
            if (!(tstv.getSelection() instanceof IStructuredSelection)) {
                return;
            }
            IStructuredSelection selection = 
                (IStructuredSelection)tstv.getSelection();
            Object [] selArray = selection.toArray();
            GuiNode target = (GuiNode)selArray[selArray.length - 1];
            
            tstv.getClipboard().clearContents();
            transfer.setSelection(null, null);
            
            // Update enablement manually because these is no selection
            // change to update the enablement automatically.
            setEnabled(false);
            
            try {
                TCBrowserDndSupport.moveNodes(nodesToBeMoved, target);
            } catch (PMException e) {
                PMExceptionHandler.handlePMExceptionForMasterSession(e);
            } catch (GDProjectDeletedException e) {
                PMExceptionHandler.handleGDProjectDeletedException();
            }
        }
    }

}