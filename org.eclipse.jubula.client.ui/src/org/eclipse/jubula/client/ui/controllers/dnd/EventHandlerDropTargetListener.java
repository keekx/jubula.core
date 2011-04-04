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
package org.eclipse.jubula.client.ui.controllers.dnd;

import java.util.Iterator;

import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.jubula.client.core.model.IEventExecTestCasePO;
import org.eclipse.jubula.client.core.model.INodePO;
import org.eclipse.jubula.client.core.model.ISpecTestCasePO;
import org.eclipse.jubula.client.ui.Plugin;
import org.eclipse.jubula.client.ui.constants.Constants;
import org.eclipse.jubula.client.ui.editors.AbstractTestCaseEditor;
import org.eclipse.jubula.client.ui.editors.JBEditorHelper;
import org.eclipse.swt.dnd.TransferData;


/**
 * @author BREDEX GmbH
 * @created 17.05.2005
 */
public class EventHandlerDropTargetListener extends ViewerDropAdapter {
    /**
     * <code>m_editor</code>
     */
    private AbstractTestCaseEditor m_editor;

    /**
     * @param editor the editor which contains the viewer.
     */
    public EventHandlerDropTargetListener(AbstractTestCaseEditor editor) {
        super(editor.getEventHandlerTreeViewer());
        m_editor = editor;
        boolean scrollExpand = Plugin.getDefault().getPreferenceStore().
            getBoolean(Constants.TREEAUTOSCROLL_KEY);
        setScrollExpandEnabled(scrollExpand);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean performDrop(Object data) {
        if (m_editor.getEditorHelper().requestEditableState() 
                != JBEditorHelper.EditableState.OK) {
            return false;
        }
        LocalSelectionTransfer transfer = LocalSelectionTransfer.getInstance();
        Iterator iter = (transfer.getSelection()).iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            if (!(obj instanceof INodePO)) {
                return false;
            }
            INodePO node = (INodePO)obj;
            if (node instanceof ISpecTestCasePO) {
                INodePO target = (INodePO)getCurrentTarget();
                ISpecTestCasePO specTcGUI = (ISpecTestCasePO)node;
                if (target != node) {
                    addEventHandler(target, specTcGUI);
                }
            }
        }
        return true;
    }

    /**
     * @param target
     *            the target to drop on.
     * @param specTcGUI
     *            the TestCase used as EventHandler.
     */
    private void addEventHandler(INodePO target, ISpecTestCasePO specTcGUI) {
        if (target == null || target instanceof IEventExecTestCasePO) {

            m_editor.addEventHandler(specTcGUI, (ISpecTestCasePO)m_editor
                    .getEventHandlerTreeViewer().getInput());
            LocalSelectionTransfer.getInstance().setSelection(null);
        }
    }


    /**
     * {@inheritDoc}
     */
    public boolean validateDrop(Object target, int operation,
        TransferData transferType) {
        if (LocalSelectionTransfer.getInstance().getSelection() == null) {
            return false;
        }
        LocalSelectionTransfer transfer = LocalSelectionTransfer.getInstance();
        Iterator iter = transfer.getSelection().iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            if (!(obj instanceof INodePO)) {
                return false;
            }
            INodePO node = (INodePO)obj;
            if (!(node instanceof ISpecTestCasePO)) {
                return false;
            }
            INodePO parent = (INodePO)getViewer().getInput();
            if (node.hasCircularDependences(parent)) {
                return false;
            }
        }
        return true;
    }
}
