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
package org.eclipse.jubula.rc.swt.tester.adapter;

import java.awt.Rectangle;

import org.eclipse.jubula.rc.common.driver.ClickOptions;
import org.eclipse.jubula.rc.common.driver.IRunnable;
import org.eclipse.jubula.rc.common.exception.StepExecutionException;
import org.eclipse.jubula.rc.common.implclasses.table.Cell;
import org.eclipse.jubula.rc.common.logger.AutServerLogger;
import org.eclipse.jubula.rc.common.tester.adapter.interfaces.ITableComponent;
import org.eclipse.jubula.rc.common.util.IndexConverter;
import org.eclipse.jubula.rc.common.util.MatchUtil;
import org.eclipse.jubula.rc.swt.listener.TableSelectionTracker;
import org.eclipse.jubula.rc.swt.tester.CAPUtil;
import org.eclipse.jubula.rc.swt.tester.TableTester;
import org.eclipse.jubula.rc.swt.utils.SwtUtils;
import org.eclipse.jubula.tools.constants.SwtAUTHierarchyConstants;
import org.eclipse.jubula.tools.objects.event.EventFactory;
import org.eclipse.jubula.tools.objects.event.TestErrorEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
/**
 * Implements the Table interface for adapting a <code>SWT.Table</code>
 * 
 * @author BREDEX GmbH
 */
public class TableAdapter extends ControlAdapter implements ITableComponent {
    
    /** the logger */
    private static AutServerLogger log = new AutServerLogger(
        TableAdapter.class);
    
    /** the table */
    private Table m_table;
    
    /**
     * 
     * @param objectToAdapt graphics component which will be adapted
     */
    public TableAdapter(Object objectToAdapt) {
        super(objectToAdapt);
        m_table = (Table) objectToAdapt;
    }

    /** {@inheritDoc} */
    public int getColumnCount() {
        Integer colCount = (Integer) getEventThreadQueuer().invokeAndWait(
                "getColumnCount", new IRunnable() { //$NON-NLS-1$
                    public Object run() {
                        return  new Integer(m_table.getColumnCount());
                    }
                });
        return colCount.intValue();
    }

    /** {@inheritDoc} */
    public int getRowCount() {
        Integer itemCount = (Integer) getEventThreadQueuer().invokeAndWait(
                "getRowCount", new IRunnable() { //$NON-NLS-1$
                    public Object run() {
                        return new Integer(m_table.getItemCount());
                    }
                });
        return itemCount.intValue();
    }

    /** {@inheritDoc} */
    public String getCellText(final int row, final int column) {
        String cellText = (String)getEventThreadQueuer().invokeAndWait("getCellText", //$NON-NLS-1$
                new IRunnable() {
                    public Object run() {
                        final TableItem item = m_table.getItem(row);
                        String value = CAPUtil.getWidgetText(item,
                                SwtAUTHierarchyConstants.WIDGET_TEXT_KEY_PREFIX
                                        + column, item.getText(column));
                        if (log.isDebugEnabled()) {
                            log.debug("Getting cell text:"); //$NON-NLS-1$
                            log.debug("Row, col: " + row + ", " + column); //$NON-NLS-1$ //$NON-NLS-2$
                            log.debug("Value: " + value); //$NON-NLS-1$
                        }
                        return value;
                    }
                });
        return cellText;
    }

    /** {@inheritDoc} */
    public String getColumnHeaderText(final int colIdx) {
        String current = (String)getEventThreadQueuer().invokeAndWait("getColumnName", //$NON-NLS-1$
                new IRunnable() {
                    public Object run() {
                        final TableColumn column = m_table.getColumn(colIdx);
                        return CAPUtil.getWidgetText(column, column.getText());
                    }
                });
        return current;
    }

    /** {@inheritDoc} */
    public int getColumnFromString(final String col, final String operator) {
        int column = -2;
        try {
            int usrIdxCol = Integer.parseInt(col);
            if (usrIdxCol == 0) {
                usrIdxCol = usrIdxCol + 1;
            }
            column = IndexConverter.toImplementationIndex(
                    usrIdxCol);
        } catch (NumberFormatException nfe) {
            try {
                Boolean isVisible;
                isVisible = (Boolean)getEventThreadQueuer().invokeAndWait(
                        "getColumnFromString", //$NON-NLS-1$
                        new IRunnable() {
                            public Object run() {
                                return new Boolean(m_table.getHeaderVisible());
                            }
                        });  
                if (!(isVisible.booleanValue())) {
                    throw new StepExecutionException("No Header", //$NON-NLS-1$
                            EventFactory.createActionError(
                                TestErrorEvent.NO_HEADER));
                }
                
                Integer implCol;
                implCol = (Integer)getEventThreadQueuer().invokeAndWait(
                    "getColumnFromString", new IRunnable() { //$NON-NLS-1$
                        public Object run() throws StepExecutionException {
                            for (int i = 0; i < m_table.getColumnCount(); i++) {
                                String colHeader = getColumnHeaderText(i);
                                if (MatchUtil.getInstance().match(
                                        colHeader, col, operator)) {
                                    return new Integer (i);
                                }
                            }
                            return new Integer (-2);
                        }
                    });                
                column = implCol.intValue();                
            } catch (IllegalArgumentException iae) {
                //do nothing here
            }
        }        
        return column;
    }

    /** {@inheritDoc} */
    public String getRowText(final int rowIdx) {
        String current = (String)getEventThreadQueuer().invokeAndWait("getRowText", //$NON-NLS-1$
                new IRunnable() {
                    public Object run() {
                        final TableItem row = m_table.getItem(rowIdx);
                        return CAPUtil.getWidgetText(row, row.getText());
                    }
                });
        return current;
    }

    /** {@inheritDoc} */
    public int getRowFromString(final String row, final String operator) {
        int rowInt = -2;        
        try {
            rowInt = IndexConverter.toImplementationIndex(
                    Integer.parseInt(row));                       
            if (rowInt == -1) {
                Boolean isVisible;
                isVisible = (Boolean)getEventThreadQueuer().invokeAndWait(
                        "getRowFromString", //$NON-NLS-1$
                        new IRunnable() {
                            public Object run() {
                                return new Boolean(m_table.getHeaderVisible());
                            }
                        }); 
                if (!(isVisible.booleanValue())) {
                    throw new StepExecutionException("Header not visible", //$NON-NLS-1$
                            EventFactory.createActionError(
                                TestErrorEvent.NO_HEADER));
                }                
            }
        } catch (NumberFormatException nfe) {
            Integer implRow;
            implRow = (Integer)getEventThreadQueuer().invokeAndWait(
                "getRowFromString", //$NON-NLS-1$
                new IRunnable() {
                    public Object run() throws StepExecutionException {
                        for (int i = 0; i < m_table.getItemCount(); i++) {
                            String cellTxt = getCellText(i, 0);
                            if (MatchUtil.getInstance().match(
                                    cellTxt, row, operator)) {
                                return new Integer(i);
                            }
                        }
                        return new Integer(-2);
                    }
                });
            rowInt = implRow.intValue();
        }        
        return rowInt;
    }

    /** {@inheritDoc} */
    public Rectangle getBounds() {
        Rectangle bounds = (Rectangle) getEventThreadQueuer()
                .invokeAndWait("getBounds", //$NON-NLS-1$
                    new IRunnable() {                     
                        public Object run() throws StepExecutionException {
                            return m_table.getBounds();
                        }
                    });
        
        return bounds;
    }

    /** {@inheritDoc} */
    public Rectangle getHeaderBounds(final int col) {
        Rectangle headerBounds = (Rectangle) getEventThreadQueuer()
                .invokeAndWait("getHeaderBounds", //$NON-NLS-1$
                        new IRunnable() {
                            public Object run() throws StepExecutionException {
                                org.eclipse.swt.graphics.Rectangle rect = 
                                        m_table.getItem(0).getBounds(col);
                                rect.y = m_table.getClientArea().y;
                                return new Rectangle(rect.x, rect.y,
                                        rect.width, rect.height);
                            }
                        });
        return headerBounds;
    }

    /** {@inheritDoc} */
    public Cell getSelectedCell() throws StepExecutionException {
        Cell cell = (Cell) getEventThreadQueuer().invokeAndWait(
                "getSelectedSell", new IRunnable() {   //$NON-NLS-1$         
                    public Object run() throws StepExecutionException {
                        return TableSelectionTracker.getInstance()
                                .getSelectedCell(m_table);
                    }
                });
        return cell;
    } 

    /** {@inheritDoc} */
    public boolean isHeaderVisible() {
        Boolean isVisible;
        isVisible = (Boolean)getEventThreadQueuer().invokeAndWait(
                "isHeaderVisible", //$NON-NLS-1$
                new IRunnable() {
                    public Object run() {
                        return new Boolean(m_table.getHeaderVisible());
                    }
                });
        return isVisible.booleanValue();
    }

    /** {@inheritDoc} */
    public boolean isCellEditable(final int row, final int col) {
        final Control cellEditor = (Control) 
                activateEditor(new Cell(row, col));
        boolean isEditable = ((Boolean)getEventThreadQueuer().invokeAndWait(
                "isCellEditable", //$NON-NLS-1$
                new IRunnable() {
                    public Object run() {

                        return isEditable(cellEditor) 
                            ? Boolean.TRUE : Boolean.FALSE;
                    }
                })).booleanValue();
        return isEditable;
    }

    /** {@inheritDoc} */
    public boolean hasCellSelection() {
        TableItem[] selItems = (TableItem[])getEventThreadQueuer()
                .invokeAndWait("hasCellSelection", //$NON-NLS-1$
                    new IRunnable() {
                        public Object run() {
                            return m_table.getSelection();
                        }
                    });
        return selItems.length > 0;
    }
    
    /**
     * {@inheritDoc}
     */
    public Rectangle scrollCellToVisible(final int row, final int col)
        throws StepExecutionException {
        final Table table = m_table;
        getEventThreadQueuer().invokeAndWait("scrollCellToVisible", //$NON-NLS-1$
                new IRunnable() {
                    public Object run() {
                        if (table.getColumnCount() > 0 || col > 0) {
                            table.showColumn(table.getColumn(col));
                        }
                        table.showItem(table.getItem(row));
                        return null;
                    }
                });
        
        checkRowColBounds(row, col);
        final Rectangle cellBoundsRelativeToParent = TableTester.getCellBounds(
                getEventThreadQueuer(), m_table, row, col);
            
        getEventThreadQueuer().invokeAndWait("getCellBoundsRelativeToParent", //$NON-NLS-1$
            new IRunnable() {
                public Object run() {
                    org.eclipse.swt.graphics.Point cellOriginRelativeToParent = 
                        table.getDisplay().map(
                                table, table.getParent(), 
                                new org.eclipse.swt.graphics.Point(
                                        cellBoundsRelativeToParent.x, 
                                        cellBoundsRelativeToParent.y));
                    cellBoundsRelativeToParent.x = 
                        cellOriginRelativeToParent.x;
                    cellBoundsRelativeToParent.y = 
                        cellOriginRelativeToParent.y;
                    return null;
                }
            });

        Control parent = (Control)getEventThreadQueuer().invokeAndWait("getParent", //$NON-NLS-1$
                new IRunnable() {
                public Object run() {
                    table.getParent();
                    return null;
                }
            });

            
        getRobot().scrollToVisible(
                parent, cellBoundsRelativeToParent);
        
        return getVisibleBounds(TableTester.getCellBounds(
                getEventThreadQueuer(), m_table, row, col));
    }
    
    /**
     * Checks whether <code>0 <= value < count</code>. 
     * @param value The value to check.
     * @param count The upper bound.
     */
    private void checkBounds(int value, int count) {
        if (value < 0 || value >= count) {
            throw new StepExecutionException("Invalid row/column: " + value, //$NON-NLS-1$
                EventFactory.createActionError(
                        TestErrorEvent.INVALID_INDEX_OR_HEADER));
        }
    }
    
    /**
     * Checks if the passed row and column are inside the bounds of the Table. 
     * @param row The row
     * @param column The column
     * @throws StepExecutionException If the row or the column is outside of the Table's bounds.
     */
    protected void checkRowColBounds(int row, int column)
        throws StepExecutionException {
        checkBounds(row, getRowCount());
        
        // Corner case: Only check the bounds if the table is not being
        //              used as a list or anything other than the first column
        //              is being checked.
        int colCount = getColumnCount();
        if (colCount > 0 || column > 0) {
            checkBounds(column, colCount);
        }
    }
    
    /**
     * Computes the visible cellBounds inside the visible bounds of the table.<br>
     * The result is the intersection of the visible bounds of the table and the 
     * bounds of the cell.
     * @param cellBounds the bounds of the cell to click in. These bounds must
     *                  be relative to the table's location.
     * @return the visible cell bounds, relative to the table's location.
     */
    private Rectangle getVisibleBounds(Rectangle cellBounds) {
        org.eclipse.swt.graphics.Rectangle r = 
            (org.eclipse.swt.graphics.Rectangle)
            getEventThreadQueuer().invokeAndWait("getVisibleCellBounds: " + cellBounds,  //$NON-NLS-1$
                    new IRunnable() {

                    public Object run() {
                        return m_table.getClientArea();
                    }
                });
        
        Rectangle visibleTableBounds = new Rectangle(
            r.x, r.y, r.width, r.height);
        Rectangle visibleCellBounds = 
            visibleTableBounds.intersection(cellBounds);
        return visibleCellBounds;
    }
    
    /**
     * @param cellEditor The cell editor to check.
     * @return <code>true</code> if the given cell editor is editable.
     */
    private boolean isEditable(Control cellEditor) {

        if (cellEditor == null || cellEditor instanceof TableCursor
            || cellEditor == m_table) {
            // No actual editor found.
            return false;
        }
        
        return (cellEditor.getStyle() & SWT.READ_ONLY) == 0;
    } 
    
    /**
     * @param cellEditor The cell editor to check.
     * @return <code>true</code> if the given editor is editable. Otherwise
     *         <code>false</code>.
     */
    private boolean invokeIsEditable(final Control cellEditor) {

        boolean isEditable = ((Boolean)getEventThreadQueuer().invokeAndWait(
                "getSelectedCell", //$NON-NLS-1$
                new IRunnable() {
                    public Object run() {
                        return isEditable(cellEditor) 
                            ? Boolean.TRUE : Boolean.FALSE;
                    }
                })).booleanValue();
        return isEditable;
    }
    
    /**
     * {@inheritDoc}
     */
    public Object activateEditor(final Cell cell) {

        Rectangle rect = scrollCellToVisible(cell.getRow(), cell.getCol());
        Control editor = getTableCellEditor(cell, rect);
        // sometimes the editor only appears after doubleclick!

        if (!invokeIsEditable(editor)) {
            org.eclipse.swt.graphics.Rectangle cellBounds = 
                    new org.eclipse.swt.graphics.Rectangle(
                    rect.x, rect.y, rect.width, rect.height);
            ClickOptions co = ClickOptions.create().setClickCount(2);
            Control clickTarget = editor == null
                    || editor instanceof TableCursor ? m_table : editor;
            getRobot().click(clickTarget, cellBounds, co);
            editor = getTableCellEditor(cell, rect);
        }

        return editor;

    }
    /**
     * Gets the TableCellEditor of the given cell.
     * The Cell has to be activated before!
     * @param cell the cell.
     * @param rect 
     * @return the TableCellEditor
     */
    private Control getTableCellEditor(final Cell cell, Rectangle rect) {
        org.eclipse.swt.graphics.Rectangle swtRect = 
                new org.eclipse.swt.graphics.Rectangle(rect.x, rect.y, 
                        rect.width, rect.height);
        getRobot().click(m_table, 
            swtRect,
            ClickOptions.create().setClickCount(1));

        
        return SwtUtils.getCursorControl();
    }

    /**
     * {@inheritDoc}
     */
    public String getText() {
        final Cell selectedCell = getSelectedCell();
        return getCellText(selectedCell.getRow(), selectedCell.getCol());
    }

    /**
     * {@inheritDoc}
     */
    public Object getTableHeader() {
        return m_table;
    }
}
