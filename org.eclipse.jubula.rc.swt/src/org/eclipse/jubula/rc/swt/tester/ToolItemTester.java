/*******************************************************************************
 * Copyright (c) 2013 BREDEX GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BREDEX GmbH - initial API and implementation 
 *******************************************************************************/
package org.eclipse.jubula.rc.swt.tester;

import org.eclipse.jubula.rc.common.driver.ClickOptions;
import org.eclipse.jubula.rc.common.driver.IRunnable;
import org.eclipse.jubula.rc.common.exception.StepExecutionException;
import org.eclipse.jubula.rc.common.listener.EventLock;
import org.eclipse.jubula.rc.swt.tester.adapter.ToolItemAdapter;
import org.eclipse.jubula.rc.swt.tester.util.EventListener;
import org.eclipse.jubula.rc.swt.tester.util.EventListener.Condition;
import org.eclipse.jubula.tools.constants.TimeoutConstants;
import org.eclipse.jubula.tools.objects.event.EventFactory;
import org.eclipse.jubula.tools.objects.event.TestErrorEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolItem;

/**
 * @author BREDEX GmbH
 * @created 23.03.2007
 */
public class ToolItemTester extends ButtonTester {
    
    /**
     * Wait for the menu to appear and save it for the menu tests
     * 
     * @author BREDEX GmBH
     * 
     */
    private class MenuShownCondition implements Condition {

        /**
         * {@inheritDoc}
         */
        public boolean isTrue(Event event) {
            if (event.widget instanceof Menu) {

                // Set menu
                m_menu = (Menu) event.widget;
                // Add menuCloseListener
                m_menu.addMenuListener(m_menuCloseListener);
                return true;
            }
            return false;
        }

    }

    /**
     * The dropdown menu, or <code>null</code> when the dropdown menu is not
     * showing.
     */
    private Menu m_menu = null;

    /**
     * The testerclass used to test all menus
     */
    private MenuTester m_menuTester = new MenuTester();

    /**
     * Listener for the hiding of the dropdown menu
     */
    private MenuAdapter m_menuCloseListener = new MenuAdapter() {

        public void menuHidden(MenuEvent e) {
            if (e.widget instanceof Menu) {
                Menu menu = (Menu) e.widget;

                // Remove this listener
                menu.removeMenuListener(this);

                // Reinitialize menu variable
                m_menu = null;
            }

        }

    };

    /**
     * @return the <code>ToolItemAdapter</code>
     */
    ToolItemAdapter getToolItemAdapter() {
        return (ToolItemAdapter) getComponent();
    }

    /**
     * Opens the dropdown menu for this component by clicking on its chevron on
     * the righthand side.
     */
    private void openDropdownMenu() {
        final ToolItem item = (ToolItem) getComponent().getRealComponent();
        EventLock lock = new EventLock();
        Condition cond = new MenuShownCondition();
        final Listener menuOpenListener = new EventListener(lock, cond);
        int style = ((Integer) getEventThreadQueuer().invokeAndWait(
                "getStyle", //$NON-NLS-1$
                new IRunnable() {

                    public Object run() throws StepExecutionException {
                        return new Integer(item.getStyle());
                    }

                })).intValue();

        if ((style & SWT.DROP_DOWN) == 0) {
            // ToolItem is not DropDown style
            throw new StepExecutionException(
                    "Component does not have a dropdown menu.", //$NON-NLS-1$
                    EventFactory
                        .createActionError(TestErrorEvent.DROPDOWN_NOT_FOUND));
        }

        // Add menuOpenListener
        getEventThreadQueuer().invokeAndWait("addMenuOpenListener", //$NON-NLS-1$
                new IRunnable() {

                    public Object run() throws StepExecutionException {
                        item.getDisplay()
                                .addFilter(SWT.Show, menuOpenListener);
                        return null;
                    }

                });
        try {
            getRobot().click(item, null, ClickOptions.create().left(), 95,
                    false, 50, false);

            synchronized (lock) {
                long timeout = TimeoutConstants.SERVER_TIMEOUT_WAIT_FOR_POPUP;
                long done = System.currentTimeMillis() + timeout;
                long now;
                while (!lock.isReleased() && timeout > 0) {
                    lock.wait(timeout);
                    now = System.currentTimeMillis();
                    timeout = done - now;
                }
            }
            if (m_menu == null) {
                throw new StepExecutionException(
                        "Dropdown menu did not appear.", //$NON-NLS-1$
                        EventFactory
                            .createActionError(
                                    TestErrorEvent.DROPDOWN_NOT_FOUND));
            }
            m_menuTester.setComponent(m_menu);
            m_menuTester.setContextMenu(true);
        } catch (InterruptedException e) {
            // ignore
        } finally {
            // remove menuOpenListener
            getEventThreadQueuer().invokeAndWait("removeMenuOpenListener", //$NON-NLS-1$
                    new IRunnable() {

                        public Object run() throws StepExecutionException {
                            item.getDisplay().removeFilter(SWT.Show,
                                    menuOpenListener);

                            return null;
                        }

                    });

        }
    }
    
    /**
     * Selects an item from the button's dropdown menu.
     * 
     * @param namePath
     *            the menu item to select
     * @param operator
     *            operator used for matching
     */
    public void rcSelectContextMenuItem(String namePath, String operator) {

        // Try to open menu
        openDropdownMenu();

        // Call appropriate delegate method
        m_menuTester.selectMenuItem(namePath, operator);
    }
    
    /**
     * 
     * @param indexPath
     *            the menu item to select
     */
    public void rcSelectContextMenuItemByIndexpath(String indexPath) {
        // Try to open menu
        openDropdownMenu();

        // Call appropriate delegate method
        m_menuTester.selectMenuItemByIndexpath(indexPath);
    }

    /**
     * Verifies if a MenuItem is en-/disabled depending on the enabled
     * parameter.
     * 
     * @param namePath
     *            the menu item to select
     * @param operator
     *            operator used for matching
     * @param enabled
     *            for checking enabled or disabled
     */
    public void rcVerifyContextMenuEnabled(String namePath, String operator,
            boolean enabled) {

        // Try to open menu
        openDropdownMenu();

        // Call appropriate delegate method
        m_menuTester.verifyEnabled(namePath, operator, enabled);

    }
    
    /**
     * Verifies if a MenuItem is en-/disabled depending on the enabled
     * parameter.
     * 
     * @param indexPath
     *            the menu item to select
     * @param enabled
     *            for checking enabled or disabled
     */
    public void rcVerifyContextMenuEnabledByIndexpath(String indexPath,
            boolean enabled) {

        // Try to open menu
        openDropdownMenu();

        // Call appropriate delegate method
        m_menuTester.verifyEnabledByIndexpath(indexPath, enabled);

    }
    
    /**
     * Verifies if the specified menu item exists
     * 
     * @param namePath
     *            the menu item to verify against
     * @param operator
     *            operator operator used for matching
     * @param exists
     *            for checking existence or unexistence
     */
    public void rcVerifyContextMenuExists(String namePath, String operator,
            boolean exists) {
        // Try to open menu
        openDropdownMenu();

        // Call appropriate delegate method
        m_menuTester.verifyExists(namePath, operator, exists);

    }
    
    /**
     * Verifies if the specified menu item exists
     * 
     * @param indexPath
     *            the menu item to select
     * @param exists
     *            for checking existence or unexistence
     */
    public void rcVerifyContextMenuExistsByIndexpath(String indexPath,
            boolean exists) {

        // Try to open menu
        openDropdownMenu();

        // Call appropriate delegate method
        m_menuTester.verifyExistsByIndexpath(indexPath, exists);
    }
    
    /**
     * Checks if the specified menu item is selected.
     * 
     * @param namePath
     *            the menu item to verify against
     * @param operator
     *            operator used for matching
     * @param selected
     *            for checking selected or not selected
     */
    public void rcVerifyContextMenuSelected(String namePath, String operator, 
        boolean selected) {
        
        // Try to open menu
        openDropdownMenu();

        // Call appropriate delegate method
        m_menuTester.verifySelected(namePath, operator, selected);
    }
    
    /**
     * Checks if the specified menu item is selected.
     * 
     * @param indexPath
     *            the menu item to verify against
     * @param selected
     *            for checking selected or not selected
     */
    public void rcVerifyContextMenuSelectedByIndexpath(String indexPath,
            boolean selected) {

        // Try to open menu
        openDropdownMenu();

        // Call appropriate delegate method
        m_menuTester.verifySelectedByIndexpath(indexPath, selected);
    }
    
}
