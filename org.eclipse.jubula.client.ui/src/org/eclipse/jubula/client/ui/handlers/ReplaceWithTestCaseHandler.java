/*******************************************************************************
 * Copyright (c) 2004, 2011 BREDEX GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BREDEX GmbH - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.jubula.client.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jubula.client.core.model.INodePO;
import org.eclipse.jubula.client.ui.Plugin;
import org.eclipse.jubula.client.ui.editors.AbstractTestCaseEditor;
import org.eclipse.jubula.client.ui.editors.JBEditorHelper;
import org.eclipse.jubula.client.ui.wizards.refactor.ReplaceTCRWizard;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Markus Tiede
 * @created Jul 20, 2011
 */
public class ReplaceWithTestCaseHandler extends AbstractHandler {
    /**
     * {@inheritDoc}
     */
    public Object execute(ExecutionEvent event) {
        final AbstractTestCaseEditor tce = 
            (AbstractTestCaseEditor)HandlerUtil.getActiveEditor(event);
        if (tce.getEditorHelper().requestEditableState() 
                == JBEditorHelper.EditableState.OK) {
            final INodePO editorNode = (INodePO)tce.getEditorHelper()
                    .getEditSupport().getWorkVersion();
            if (!(tce.getTreeViewer().getSelection() 
                    instanceof IStructuredSelection)) {
                return null;
            }
            WizardDialog dialog = new WizardDialog(Plugin.getShell(),
                    new ReplaceTCRWizard(editorNode));
            dialog.setHelpAvailable(true);
            dialog.open();
        }
 
        return null;
    }
}
