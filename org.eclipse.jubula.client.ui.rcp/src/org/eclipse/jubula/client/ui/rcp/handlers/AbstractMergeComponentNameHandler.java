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
package org.eclipse.jubula.client.ui.rcp.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jubula.client.core.events.DataEventDispatcher;
import org.eclipse.jubula.client.core.events.DataEventDispatcher.DataState;
import org.eclipse.jubula.client.core.events.DataEventDispatcher.UpdateState;
import org.eclipse.jubula.client.core.model.IComponentNamePO;
import org.eclipse.jubula.client.ui.constants.ContextHelpIds;
import org.eclipse.jubula.client.ui.constants.IconConstants;
import org.eclipse.jubula.client.ui.handlers.AbstractSelectionBasedHandler;
import org.eclipse.jubula.client.ui.rcp.Plugin;
import org.eclipse.jubula.client.ui.rcp.dialogs.DirectComboBoxDialog;
import org.eclipse.jubula.client.ui.rcp.i18n.Messages;
import org.eclipse.jubula.client.ui.utils.DialogUtils;


/**
 * Abstract base class for handlers for "Merge Component Names"
 *
 * @author BREDEX GmbH
 * @created Mar 13, 2009
 */
public abstract class AbstractMergeComponentNameHandler 
        extends AbstractSelectionBasedHandler {

    /**
     * Opens the Merge dialog and returns the dialog's result.
     * 
     * @param compNames The Component Names to be merged. If the dialog is
     *                  closed via OK, the selected Component Name from the 
     *                  dialog will be removed from this collection.
     * @return the Component Name selected from the dialog if the dialog was 
     *         closed via OK. Otherwise, <code>null</code>.
     */
    protected IComponentNamePO openDialog(Set<IComponentNamePO> compNames) {
        DirectComboBoxDialog<IComponentNamePO> dialog = 
            createDialog(compNames);
        dialog.setHelpAvailable(true);
        dialog.create();
        DialogUtils.setWidgetNameForModalDialog(dialog);
        Plugin.getHelpSystem().setHelp(dialog.getShell(), 
                ContextHelpIds.MERGE_COMPONENT_NAME);
        
        if (dialog.open() == Window.OK) {
            IComponentNamePO selectedCompNamePo = dialog.getSelection();
            compNames.remove(selectedCompNamePo);
            return selectedCompNamePo;
        }
        
        return null;
    }
    
    /**
     * @param compNames The Component Names to merge.
     * @return the Merge Component Names dialog.
     */
    protected DirectComboBoxDialog<IComponentNamePO> createDialog(
            Set<IComponentNamePO> compNames) {

        List<IComponentNamePO> compNamesList = 
            new ArrayList<IComponentNamePO>(compNames);
        Collections.sort(compNamesList, new Comparator<IComponentNamePO>() {

            public int compare(
                    IComponentNamePO compName1, IComponentNamePO compName2) {
                
                return compName1.getName().compareTo(compName2.getName());
            }
            
        });
        List<String> displayNameList = new ArrayList<String>();
        for (IComponentNamePO compName : compNamesList) {
            displayNameList.add(compName.getName());
        }

        DirectComboBoxDialog<IComponentNamePO> dialog = 
            new DirectComboBoxDialog<IComponentNamePO>(
                getActiveShell(), 
                compNamesList, 
                displayNameList, 
                Messages.MergeComponentNamesMessage,
                Messages.MergeComponentNamesShellTitle,
                IconConstants.MERGE_COMPONENT_NAME_DIALOG_IMAGE,
                Messages.MergeComponentNamesShellTitle,
                Messages.MergeComponentNamesLabel);
        return dialog;
    }
    
    /**
     * Perform the actual merge operation.
     * 
     * @param compNames The Component Names to merge.
     * @param selectedCompNamePo The Component Name that will be used in place
     *                           of the merged Component Names.
     */
    protected void performOperation(
            Set<IComponentNamePO> compNames,
            IComponentNamePO selectedCompNamePo) {

        for (IComponentNamePO compName : compNames) {
            compName.setReferencedGuid(selectedCompNamePo.getGuid());
        }

    }

    /**
     * Fires data change events via the Data Event Dispatcher.
     * 
     * @param compNames The Component Names for which to fire the events.
     */
    protected void fireChangeEvents(Set<IComponentNamePO> compNames) {
        for (IComponentNamePO compName : compNames) {
            DataEventDispatcher.getInstance().fireDataChangedListener(
                    compName, DataState.Deleted, UpdateState.all);
        }
    }

    /**
     * 
     * @param structuredSel The selection to check for Component Names.
     * @return all Component Names contained in the given selection.
     */
    protected Set<IComponentNamePO> getComponentNames(
            IStructuredSelection structuredSel) {
        Set<IComponentNamePO> compNames = 
            new HashSet<IComponentNamePO>();
        for (Object obj : structuredSel.toArray()) {
            if (obj instanceof IComponentNamePO) {
                compNames.add((IComponentNamePO)obj);
            }
        }
        return compNames;
    }

}