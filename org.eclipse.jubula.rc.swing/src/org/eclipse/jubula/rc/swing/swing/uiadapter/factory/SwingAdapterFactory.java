/*******************************************************************************
 * Copyright (c) 2012 BREDEX GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BREDEX GmbH - initial API and implementation 
 *******************************************************************************/
package org.eclipse.jubula.rc.swing.swing.uiadapter.factory;


import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.text.JTextComponent;

import org.eclipse.jubula.rc.common.uiadapter.factory.IUIAdapterFactory;
import org.eclipse.jubula.rc.common.uiadapter.interfaces.IComponentAdapter;
import org.eclipse.jubula.rc.swing.swing.uiadapter.AbstractButtonAdapter;
import org.eclipse.jubula.rc.swing.swing.uiadapter.JComboBoxAdapter;
import org.eclipse.jubula.rc.swing.swing.uiadapter.JLabelAdapter;
import org.eclipse.jubula.rc.swing.swing.uiadapter.JListAdapter;
import org.eclipse.jubula.rc.swing.swing.uiadapter.JMenuBarAdapter;
import org.eclipse.jubula.rc.swing.swing.uiadapter.JMenuItemAdapter;
import org.eclipse.jubula.rc.swing.swing.uiadapter.JPopupMenuAdapter;
import org.eclipse.jubula.rc.swing.swing.uiadapter.JTabbedPaneAdapter;
import org.eclipse.jubula.rc.swing.swing.uiadapter.JTableAdapter;
import org.eclipse.jubula.rc.swing.swing.uiadapter.JTextComponentAdapter;
import org.eclipse.jubula.rc.swing.swing.uiadapter.JTreeAdapter;
/**
 * This is the adapter factory for all swing components. It is
 * creating the specific adapter for a swing component.
 * 
 * Since we are using adapter here, it is a adapter factory. But this must not
 * be the case. It is only relevant that the object is implementing the
 * specific interface.
 * @author BREDEX GmbH
 *
 */
public class SwingAdapterFactory implements IUIAdapterFactory {
    /**
     * 
     */
    private static final Class[] SUPPORTEDCLASSES = new Class[] { 
        JButton.class, JCheckBox.class, JRadioButton.class,
        JMenuBar.class, JMenuItem.class, JTree.class , 
        JCheckBox.class, JRadioButton.class, JTable.class, JPopupMenu.class,
        JList.class, JTextComponent.class, JComboBox.class,
        JLabel.class , JTabbedPane.class};
    
    /**
     * {@inheritDoc}
     */
    public Class[] getSupportedClasses() {

        return SUPPORTEDCLASSES;
    }
    /**
     * {@inheritDoc}
     */
    public IComponentAdapter getAdapter(Object objectToAdapt) {
        IComponentAdapter returnvalue = null;
        if (objectToAdapt instanceof JButton) {
            returnvalue = new AbstractButtonAdapter(objectToAdapt);
            
        } else if (objectToAdapt instanceof JRadioButton) {
            returnvalue = new AbstractButtonAdapter(objectToAdapt);
            
        } else if (objectToAdapt instanceof JCheckBox) {
            returnvalue = new AbstractButtonAdapter(objectToAdapt);
            
        } else if (objectToAdapt instanceof JMenuBar) {
        
            returnvalue = new JMenuBarAdapter(objectToAdapt); 
            
        } else if (objectToAdapt instanceof JMenuItem) {
            returnvalue = new JMenuItemAdapter(objectToAdapt);            
            
        } else if (objectToAdapt instanceof JTree) {
            returnvalue = new JTreeAdapter(objectToAdapt);
            
        } else if (objectToAdapt instanceof JTable) {
            returnvalue = new JTableAdapter(objectToAdapt);
            
        } else if (objectToAdapt instanceof JList) {
            returnvalue = new JListAdapter(objectToAdapt);
            
        } else if (objectToAdapt instanceof JPopupMenu) {
            returnvalue = new JPopupMenuAdapter(objectToAdapt);
        
        } else if (objectToAdapt instanceof JTextComponent) {
            returnvalue = new JTextComponentAdapter(objectToAdapt);
        
        } else if (objectToAdapt instanceof JComboBox) {
            returnvalue = new JComboBoxAdapter(objectToAdapt);
        
        } else if (objectToAdapt instanceof JLabel) {
            returnvalue = new JLabelAdapter(objectToAdapt);
        
        } else if (objectToAdapt instanceof JTabbedPane) {
            returnvalue = new JTabbedPaneAdapter(objectToAdapt);
        
        }
        
        return returnvalue;
    }

}