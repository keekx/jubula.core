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
package org.eclipse.jubula.client.ui.widgets;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jubula.client.core.model.ICategoryPO;
import org.eclipse.jubula.client.core.model.INodePO;
import org.eclipse.jubula.client.core.model.IReusedProjectPO;
import org.eclipse.jubula.client.core.model.ISpecTestCasePO;
import org.eclipse.jubula.client.core.persistence.GeneralStorage;
import org.eclipse.jubula.client.core.utils.DependencyFinderOp;
import org.eclipse.jubula.client.core.utils.TreeTraverser;
import org.eclipse.jubula.client.ui.constants.IconConstants;
import org.eclipse.jubula.client.ui.constants.Layout;
import org.eclipse.jubula.client.ui.filter.JBFilteredTree;
import org.eclipse.jubula.client.ui.filter.JBPatternFilter;
import org.eclipse.jubula.client.ui.provider.contentprovider.TestCaseTreeCompositeContentProvider;
import org.eclipse.jubula.client.ui.provider.labelprovider.GeneralLabelProvider;
import org.eclipse.jubula.client.ui.sorter.NodeNameViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.FilteredTree;

/**
 * @author Markus Tiede
 * @created Jul 20, 2011
 */
public class TestCaseTreeComposite extends Composite {
    /** width hint = 300 */
    private static final int WIDTH_HINT = 300;
    
    /** the local tree viewer */
    private TreeViewer m_treeViewer;

    /**
     * <code>m_parentTestCase</code>
     */
    private final INodePO m_parentTestCase;
    
    /** a list with the item numbers of circular dependenced test cases */
    private Set < INodePO > m_circDependList = new HashSet < INodePO > ();

    /**
     * @param parent
     *            the parent
     * @param treeStyle
     *            the tree style to use
     * @param parentTestCase
     *            the parent test case
     */
    public TestCaseTreeComposite(Composite parent, int treeStyle, 
        INodePO parentTestCase) {
        super(parent, SWT.NONE);
        m_parentTestCase = parentTestCase;
        // use Gridlayout
        final GridLayout gridLayout = new GridLayout();

        this.setLayout(gridLayout);

        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;

        this.setLayoutData(gridData);

        final FilteredTree ft = new JBFilteredTree(this, treeStyle,
                new JBPatternFilter(), true);

        m_treeViewer = ft.getViewer();

        GridData layoutData = new GridData();
        layoutData.grabExcessHorizontalSpace = true;
        layoutData.grabExcessVerticalSpace = true;
        layoutData.horizontalAlignment = GridData.FILL;
        layoutData.verticalAlignment = GridData.FILL;
        layoutData.heightHint = WIDTH_HINT;
        Layout.addToolTipAndMaxWidth(layoutData, m_treeViewer.getControl());
        m_treeViewer.getControl().setLayoutData(layoutData);
        m_treeViewer.setUseHashlookup(true);
        getInitialInput();
        m_treeViewer.setLabelProvider(new LabelProvider());
        m_treeViewer.setContentProvider(
                new TestCaseTreeCompositeContentProvider());
        m_treeViewer.setInput(GeneralStorage.getInstance().getProject());
        m_treeViewer.setSorter(new NodeNameViewerSorter());
    }

    /**
     * gets a list of all test cases
     */
    private void getInitialInput() {
        if (m_parentTestCase != null) {
            DependencyFinderOp op = new DependencyFinderOp(m_parentTestCase);
            TreeTraverser traverser = new TreeTraverser(GeneralStorage.
                getInstance().getProject(), op, true);
            traverser.traverse(true);
            m_circDependList = op.getDependentNodes();
        }
    }

    /**
     * @return the tree viewer
     */
    public TreeViewer getTreeViewer() {
        return m_treeViewer;
    }

    /**
     * @return a flag indicating whether the selection is valid (e.g. no
     *         category or a node which would cause recursive loops)
     */
    public boolean hasValidSelection() {
        IStructuredSelection selection = 
            (IStructuredSelection)getTreeViewer().getSelection();
        for (Object selectedObj : selection.toArray()) {
            if (m_circDependList.contains(selectedObj)
                    || selectedObj instanceof ICategoryPO) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * LabelProvider for m_treeViewer
     *
     * @author BREDEX GmbH
     * @created 14.06.2005
     */
    private class LabelProvider implements IColorProvider, ILabelProvider {

        /**
         * {@inheritDoc}
         */
        public Image getImage(Object element) {
            if (element instanceof ISpecTestCasePO) {
                if (m_circDependList.contains(element)) {
                    return IconConstants.TC_DISABLED_IMAGE; 
                } 
                return IconConstants.TC_IMAGE;
            }

            if (element instanceof ICategoryPO
                    || element instanceof IReusedProjectPO) {
                return IconConstants.CATEGORY_IMAGE;
            }
            
            return null;
        }

        /**
         * {@inheritDoc}
         */
        public String getText(Object element) {
            return GeneralLabelProvider.getGDText(element);
        }

        /**
         * {@inheritDoc}
         */
        public void addListener(ILabelProviderListener listener) {
            // do nothing
        }

        /**
         * {@inheritDoc}
         */
        public void dispose() {
            // do nothing
        }

        /**
         * {@inheritDoc}
         */
        public boolean isLabelProperty(Object element, String property) {
            // do nothing
            return false;
        }

        /**
         * {@inheritDoc}
         */
        public void removeListener(ILabelProviderListener listener) {
            // do nothing
        }

        /**
         * {@inheritDoc}
         */
        public Color getForeground(Object element) {
            if (element instanceof ISpecTestCasePO) {
                if (m_circDependList.contains(element)) {
                    return Layout.GRAY_COLOR; 
                } 
                return Layout.DEFAULT_OS_COLOR;
            }
            
            if (element instanceof ICategoryPO
                    || element instanceof IReusedProjectPO) {
                return Layout.GRAY_COLOR;
            }
            return null;
        }

        /**
         * {@inheritDoc}
         */
        public Color getBackground(Object element) {
            return null;
        }        
    }
}
