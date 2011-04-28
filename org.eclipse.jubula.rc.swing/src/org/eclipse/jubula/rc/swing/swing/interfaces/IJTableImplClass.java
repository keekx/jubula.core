package org.eclipse.jubula.rc.swing.swing.interfaces;

/** 
 * @author markus
 * @created Thu Apr 28 17:34:36 CEST 2011
 */
public interface IJTableImplClass {


    /**
     * @param text text
     * @param row row
     * @param rowOperator rowOperator
     * @param column column
     * @param columnOperator columnOperator */ 
    public void gdInputText(
        String text, 
        String row, 
        String rowOperator, 
        String column, 
        String columnOperator);

    /**
     * @param text text
     * @param row row
     * @param column column */ 
    public void gdInputText(
        String text, 
        int row, 
        int column);

    /**
     * @param text text
     * @param row row
     * @param rowOperator rowOperator
     * @param column column
     * @param columnOperator columnOperator */ 
    public void gdReplaceText(
        String text, 
        String row, 
        String rowOperator, 
        String column, 
        String columnOperator);

    /**
     * @param text text
     * @param row row
     * @param column column */ 
    public void gdReplaceText(
        String text, 
        int row, 
        int column);

    /**
     * @param row row
     * @param rowOperator rowOperator
     * @param column column
     * @param columnOperator columnOperator
     * @param clickCount clickCount
     * @param xPos xPos
     * @param xUnits xUnits
     * @param yPos yPos
     * @param yUnits yUnits
     * @param extendSelection extendSelection
     * @param mouseButton mouseButton */ 
    public void gdSelectCell(
        String row, 
        String rowOperator, 
        String column, 
        String columnOperator, 
        int clickCount, 
        int xPos, 
        String xUnits, 
        int yPos, 
        String yUnits, 
        String extendSelection, 
        int mouseButton);

    /**
     * @param row row
     * @param column column
     * @param clickCount clickCount
     * @param xPos xPos
     * @param xUnits xUnits
     * @param yPos yPos
     * @param yUnits yUnits
     * @param extendSelection extendSelection */ 
    public void gdSelectCell(
        int row, 
        int column, 
        int clickCount, 
        int xPos, 
        String xUnits, 
        int yPos, 
        String yUnits, 
        String extendSelection);

    /**
     * @param direction direction
     * @param cellCount cellCount
     * @param clickCount clickCount
     * @param xPos xPos
     * @param xUnits xUnits
     * @param yPos yPos
     * @param yUnits yUnits
     * @param extendSelection extendSelection */ 
    public void gdMove(
        String direction, 
        int cellCount, 
        int clickCount, 
        int xPos, 
        String xUnits, 
        int yPos, 
        String yUnits, 
        String extendSelection);

    /**
     * @param row row
     * @param extendSelection extendSelection */ 
    public void gdSelectRow(
        int row, 
        String extendSelection);

    /**
     * @param isEditable isEditable
     * @param row row
     * @param rowOperator rowOperator
     * @param column column
     * @param columnOperator columnOperator */ 
    public void gdVerifyEditable(
        boolean isEditable, 
        String row, 
        String rowOperator, 
        String column, 
        String columnOperator);

    /**
     * @param isEditable isEditable
     * @param row row
     * @param column column */ 
    public void gdVerifyEditable(
        boolean isEditable, 
        int row, 
        int column);

    /**
     * @param isEditable isEditable */ 
    public void gdVerifyEditableSelected(
        boolean isEditable);

    /**
     * @param isEditable isEditable */ 
    public void gdVerifyEditableMousePosition(
        boolean isEditable);

    /**
     * @param text text
     * @param textOperator textOperator
     * @param row row
     * @param rowOperator rowOperator
     * @param column column
     * @param columnOperator columnOperator */ 
    public void gdVerifyText(
        String text, 
        String textOperator, 
        String row, 
        String rowOperator, 
        String column, 
        String columnOperator);

    /**
     * @param text text
     * @param operator operator
     * @param row row
     * @param column column */ 
    public void gdVerifyText(
        String text, 
        String operator, 
        int row, 
        int column);

    /**
     * @param text text
     * @param operator operator */ 
    public void gdVerifyTextAtMousePosition(
        String text, 
        String operator);

    /**
     * @param row row
     * @param rowOperator rowOperator
     * @param cellValue cellValue
     * @param valueOperator valueOperator
     * @param searchType searchType
     * @param exists exists */ 
    public void gdVerifyValueInRow(
        String row, 
        String rowOperator, 
        String cellValue, 
        String valueOperator, 
        String searchType, 
        boolean exists);

    /**
     * @param column column
     * @param columnOperator columnOperator
     * @param cellValue cellValue
     * @param valueOperator valueOperator
     * @param searchType searchType
     * @param exists exists */ 
    public void gdVerifyValueInColumn(
        String column, 
        String columnOperator, 
        String cellValue, 
        String valueOperator, 
        String searchType, 
        boolean exists);

    /**
     * @param column column
     * @param columnOperator columnOperator
     * @param cellValue cellValue
     * @param valueOperator valueOperator
     * @param clickCount clickCount
     * @param extendSelection extendSelection
     * @param searchType searchType
     * @param mouseButton mouseButton */ 
    public void gdSelectRowByValue(
        String column, 
        String columnOperator, 
        String cellValue, 
        String valueOperator, 
        int clickCount, 
        String extendSelection, 
        String searchType, 
        int mouseButton);

    /**
     * @param column column
     * @param cellValue cellValue
     * @param clickCount clickCount
     * @param operator operator
     * @param extendSelection extendSelection
     * @param searchType searchType */ 
    public void gdSelectRowByValue(
        int column, 
        String cellValue, 
        int clickCount, 
        String operator, 
        String extendSelection, 
        String searchType);

    /**
     * @param row row
     * @param rowOperator rowOperator
     * @param cellValue cellValue
     * @param valueOperator valueOperator
     * @param clickCount clickCount
     * @param extendSelection extendSelection
     * @param searchType searchType
     * @param mouseButton mouseButton */ 
    public void gdSelectCellByColValue(
        String row, 
        String rowOperator, 
        String cellValue, 
        String valueOperator, 
        int clickCount, 
        String extendSelection, 
        String searchType, 
        int mouseButton);

    /**
     * @param row row
     * @param cellValue cellValue
     * @param operator operator
     * @param clickCount clickCount
     * @param extendSelection extendSelection
     * @param searchType searchType */ 
    public void gdSelectCellByColValue(
        int row, 
        String cellValue, 
        String operator, 
        int clickCount, 
        String extendSelection, 
        String searchType);

    /**
     * @param column column
     * @param cellValue cellValue
     * @param useRegularExpression useRegularExpression */ 
    public void gdSelectCellByRowValue(
        int column, 
        String cellValue, 
        boolean useRegularExpression);

    /**
     * @param row row
     * @param column column
     * @param indexPath indexPath */ 
    public void gdPopupByIndexPathAtCell(
        int row, 
        int column, 
        String indexPath);

    /**
     * @param row row
     * @param column column
     * @param textPath textPath */ 
    public void gdPopupByTextPathAtCell(
        int row, 
        int column, 
        String textPath);

    /**
     * @param indexPath indexPath */ 
    public void gdPopupByIndexPathAtSelectedCell(
        String indexPath);

    /**
     * @param textPath textPath */ 
    public void gdPopupByTextPathAtSelectedCell(
        String textPath);

    /**
     * @param variable variable
     * @param row row
     * @param rowOperator rowOperator
     * @param column column
     * @param columnOperator columnOperator
     * @return value */ 
    public String gdReadValue(
        String variable, 
        String row, 
        String rowOperator, 
        String column, 
        String columnOperator);

    /**
     * @param variable variable
     * @param row row
     * @param column column
     * @return value */ 
    public String gdReadValue(
        String variable, 
        int row, 
        int column);

    /**
     * @param variable variable
     * @return value */ 
    public String gdReadValueAtMousePosition(
        String variable);

    /**
     * @param mouseButton mouseButton
     * @param modifierSpecification modifierSpecification
     * @param row row
     * @param rowOperator rowOperator
     * @param column column
     * @param columnOperator columnOperator
     * @param xPos xPos
     * @param xUnits xUnits
     * @param yPos yPos
     * @param yUnits yUnits */ 
    public void gdDragCell(
        int mouseButton, 
        String modifierSpecification, 
        String row, 
        String rowOperator, 
        String column, 
        String columnOperator, 
        int xPos, 
        String xUnits, 
        int yPos, 
        String yUnits);

    /**
     * @param mouseButton mouseButton
     * @param modifierSpecification modifierSpecification
     * @param row row
     * @param column column
     * @param xPos xPos
     * @param xUnits xUnits
     * @param yPos yPos
     * @param yUnits yUnits */ 
    public void gdDragCell(
        int mouseButton, 
        String modifierSpecification, 
        int row, 
        int column, 
        int xPos, 
        String xUnits, 
        int yPos, 
        String yUnits);

    /**
     * @param row row
     * @param rowOperator rowOperator
     * @param column column
     * @param columnOperator columnOperator
     * @param xPos xPos
     * @param xUnits xUnits
     * @param yPos yPos
     * @param yUnits yUnits
     * @param delayBeforeDrop delayBeforeDrop */ 
    public void gdDropCell(
        String row, 
        String rowOperator, 
        String column, 
        String columnOperator, 
        int xPos, 
        String xUnits, 
        int yPos, 
        String yUnits, 
        int delayBeforeDrop);

    /**
     * @param row row
     * @param column column
     * @param xPos xPos
     * @param xUnits xUnits
     * @param yPos yPos
     * @param yUnits yUnits
     * @param delayBeforeDrop delayBeforeDrop */ 
    public void gdDropCell(
        int row, 
        int column, 
        int xPos, 
        String xUnits, 
        int yPos, 
        String yUnits, 
        int delayBeforeDrop);

    /**
     * @param mouseButton mouseButton
     * @param modifierSpecification modifierSpecification
     * @param column column
     * @param columnOperator columnOperator
     * @param cellValue cellValue
     * @param valueOperator valueOperator
     * @param searchType searchType */ 
    public void gdDragRowByValue(
        int mouseButton, 
        String modifierSpecification, 
        String column, 
        String columnOperator, 
        String cellValue, 
        String valueOperator, 
        String searchType);

    /**
     * @param mouseButton mouseButton
     * @param modifierSpecification modifierSpecification
     * @param column column
     * @param cellValue cellValue
     * @param operator operator
     * @param searchType searchType */ 
    public void gdDragRowByValue(
        int mouseButton, 
        String modifierSpecification, 
        int column, 
        String cellValue, 
        String operator, 
        String searchType);

    /**
     * @param column column
     * @param columnOperator columnOperator
     * @param cellValue cellValue
     * @param valueOperator valueOperator
     * @param searchType searchType
     * @param delayBeforeDrop delayBeforeDrop */ 
    public void gdDropRowByValue(
        String column, 
        String columnOperator, 
        String cellValue, 
        String valueOperator, 
        String searchType, 
        int delayBeforeDrop);

    /**
     * @param column column
     * @param cellValue cellValue
     * @param operator operator
     * @param searchType searchType
     * @param delayBeforeDrop delayBeforeDrop */ 
    public void gdDropRowByValue(
        int column, 
        String cellValue, 
        String operator, 
        String searchType, 
        int delayBeforeDrop);

    /**
     * @param mouseButton mouseButton
     * @param modifierSpecification modifierSpecification
     * @param row row
     * @param rowOperator rowOperator
     * @param cellValue cellValue
     * @param valueOperator valueOperator
     * @param searchType searchType */ 
    public void gdDragCellByColValue(
        int mouseButton, 
        String modifierSpecification, 
        String row, 
        String rowOperator, 
        String cellValue, 
        String valueOperator, 
        String searchType);

    /**
     * @param mouseButton mouseButton
     * @param modifierSpecification modifierSpecification
     * @param row row
     * @param cellValue cellValue
     * @param operator operator
     * @param searchType searchType */ 
    public void gdDragCellByColValue(
        int mouseButton, 
        String modifierSpecification, 
        int row, 
        String cellValue, 
        String operator, 
        String searchType);

    /**
     * @param row row
     * @param rowOperator rowOperator
     * @param cellValue cellValue
     * @param valueOperator valueOperator
     * @param searchType searchType
     * @param delayBeforeDrop delayBeforeDrop */ 
    public void gdDropCellByColValue(
        String row, 
        String rowOperator, 
        String cellValue, 
        String valueOperator, 
        String searchType, 
        int delayBeforeDrop);

    /**
     * @param row row
     * @param cellValue cellValue
     * @param operator operator
     * @param searchType searchType
     * @param delayBeforeDrop delayBeforeDrop */ 
    public void gdDropCellByColValue(
        int row, 
        String cellValue, 
        String operator, 
        String searchType, 
        int delayBeforeDrop);

    /**
     * @param isEditable isEditable */ 
    public void gdVerifyEditable(
        boolean isEditable);

    /**
     * @param text text */ 
    public void gdReplaceText(
        String text);

    /**
     * @param text text */ 
    public void gdInputText(
        String text);

    /**
     * @param text text
     * @param operator operator */ 
    public void gdVerifyText(
        String text, 
        String operator);

    /**
     * @param variable variable
     * @return value */ 
    public String gdReadValue(
        String variable);

    /**
     * @param text text
     * @param textSize textSize
     * @param timePerWord timePerWord
     * @param windowWidth windowWidth */ 
    public void gdShowText(
        String text, 
        int textSize, 
        int timePerWord, 
        int windowWidth);

    /**
     * @param isExisting isExisting */ 
    public void gdVerifyExists(
        boolean isExisting);

    /**
     * @param isEnabled isEnabled */ 
    public void gdVerifyEnabled(
        boolean isEnabled);

    /**
     * @param propertyName propertyName
     * @param propertyValue propertyValue
     * @param operator operator */ 
    public void gdVerifyProperty(
        String propertyName, 
        String propertyValue, 
        String operator);

    /**
     * @param hasFocus hasFocus */ 
    public void gdVerifyFocus(
        boolean hasFocus);

    /**
     * @param timeout timeout
     * @param delayAfterVisibility delayAfterVisibility */ 
    public void gdWaitForComponent(
        int timeout, 
        int delayAfterVisibility);

    /**
     * @param clickCount clickCount
     * @param mouseButton mouseButton */ 
    public void gdClick(
        int clickCount, 
        int mouseButton);

    /**
     * @param clickCount clickCount
     * @param mouseButton mouseButton
     * @param xPos xPos
     * @param xUnits xUnits
     * @param yPos yPos
     * @param yUnits yUnits */ 
    public void gdClickDirect(
        int clickCount, 
        int mouseButton, 
        int xPos, 
        String xUnits, 
        int yPos, 
        String yUnits);

    /**
     * @param xPos xPos
     * @param xUnits xUnits
     * @param yPos yPos
     * @param yUnits yUnits
     * @param menuPath menuPath
     * @param operator operator
     * @param mouseButton mouseButton */ 
    public void gdPopupSelectByTextPath(
        int xPos, 
        String xUnits, 
        int yPos, 
        String yUnits, 
        String menuPath, 
        String operator, 
        int mouseButton);

    /**
     * @param xPos xPos
     * @param yPos yPos
     * @param units units
     * @param menuPath menuPath
     * @param operator operator */ 
    public void gdPopupSelectByTextPath(
        int xPos, 
        int yPos, 
        String units, 
        String menuPath, 
        String operator);

    /**
     * @param indexPath indexPath
     * @param mouseButton mouseButton */ 
    public void gdPopupSelectByIndexPath(
        String indexPath, 
        int mouseButton);

    /**
     * @param indexPath indexPath */ 
    public void gdPopupSelectByIndexPath(
        String indexPath);

    /**
     * @param textPath textPath
     * @param operator operator
     * @param mouseButton mouseButton */ 
    public void gdPopupSelectByTextPath(
        String textPath, 
        String operator, 
        int mouseButton);

    /**
     * @param textPath textPath
     * @param operator operator */ 
    public void gdPopupSelectByTextPath(
        String textPath, 
        String operator);

    /**
     * @param xPos xPos
     * @param xUnits xUnits
     * @param yPos yPos
     * @param yUnits yUnits
     * @param indexPath indexPath
     * @param mouseButton mouseButton */ 
    public void gdPopupSelectByIndexPath(
        int xPos, 
        String xUnits, 
        int yPos, 
        String yUnits, 
        String indexPath, 
        int mouseButton);

    /**
     * @param xPos xPos
     * @param yPos yPos
     * @param units units
     * @param indexPath indexPath */ 
    public void gdPopupSelectByIndexPath(
        int xPos, 
        int yPos, 
        String units, 
        String indexPath);

    /**
     * @param indexPath indexPath
     * @param isEnabled isEnabled
     * @param mouseButton mouseButton */ 
    public void gdPopupVerifyEnabledByIndexPath(
        String indexPath, 
        boolean isEnabled, 
        int mouseButton);

    /**
     * @param xPos xPos
     * @param xUnits xUnits
     * @param yPos yPos
     * @param yUnits yUnits
     * @param indexPath indexPath
     * @param isEnabled isEnabled
     * @param mouseButton mouseButton */ 
    public void gdPopupVerifyEnabledByIndexPath(
        int xPos, 
        String xUnits, 
        int yPos, 
        String yUnits, 
        String indexPath, 
        boolean isEnabled, 
        int mouseButton);

    /**
     * @param textPath textPath
     * @param operator operator
     * @param isEnabled isEnabled
     * @param mouseButton mouseButton */ 
    public void gdPopupVerifyEnabledByTextPath(
        String textPath, 
        String operator, 
        boolean isEnabled, 
        int mouseButton);

    /**
     * @param xPos xPos
     * @param xUnits xUnits
     * @param yPos yPos
     * @param yUnits yUnits
     * @param textPath textPath
     * @param operator operator
     * @param isEnabled isEnabled
     * @param mouseButton mouseButton */ 
    public void gdPopupVerifyEnabledByTextPath(
        int xPos, 
        String xUnits, 
        int yPos, 
        String yUnits, 
        String textPath, 
        String operator, 
        boolean isEnabled, 
        int mouseButton);

    /**
     * @param indexPath indexPath
     * @param isExisting isExisting
     * @param mouseButton mouseButton */ 
    public void gdPopupVerifyExistsByIndexPath(
        String indexPath, 
        boolean isExisting, 
        int mouseButton);

    /**
     * @param xPos xPos
     * @param xUnits xUnits
     * @param yPos yPos
     * @param yUnits yUnits
     * @param indexPath indexPath
     * @param isExisting isExisting
     * @param mouseButton mouseButton */ 
    public void gdPopupVerifyExistsByIndexPath(
        int xPos, 
        String xUnits, 
        int yPos, 
        String yUnits, 
        String indexPath, 
        boolean isExisting, 
        int mouseButton);

    /**
     * @param textPath textPath
     * @param operator operator
     * @param isExisting isExisting
     * @param mouseButton mouseButton */ 
    public void gdPopupVerifyExistsByTextPath(
        String textPath, 
        String operator, 
        boolean isExisting, 
        int mouseButton);

    /**
     * @param xPos xPos
     * @param xUnits xUnits
     * @param yPos yPos
     * @param yUnits yUnits
     * @param textPath textPath
     * @param operator operator
     * @param isExisting isExisting
     * @param mouseButton mouseButton */ 
    public void gdPopupVerifyExistsByTextPath(
        int xPos, 
        String xUnits, 
        int yPos, 
        String yUnits, 
        String textPath, 
        String operator, 
        boolean isExisting, 
        int mouseButton);

    /**
     * @param indexPath indexPath
     * @param isSelected isSelected
     * @param mouseButton mouseButton */ 
    public void gdPopupVerifySelectedByIndexPath(
        String indexPath, 
        boolean isSelected, 
        int mouseButton);

    /**
     * @param xPos xPos
     * @param xUnits xUnits
     * @param yPos yPos
     * @param yUnits yUnits
     * @param indexPath indexPath
     * @param isSelected isSelected
     * @param mouseButton mouseButton */ 
    public void gdPopupVerifySelectedByIndexPath(
        int xPos, 
        String xUnits, 
        int yPos, 
        String yUnits, 
        String indexPath, 
        boolean isSelected, 
        int mouseButton);

    /**
     * @param textPath textPath
     * @param operator operator
     * @param isSelected isSelected
     * @param mouseButton mouseButton */ 
    public void gdPopupVerifySelectedByTextPath(
        String textPath, 
        String operator, 
        boolean isSelected, 
        int mouseButton);

    /**
     * @param xPos xPos
     * @param xUnits xUnits
     * @param yPos yPos
     * @param yUnits yUnits
     * @param textPath textPath
     * @param operator operator
     * @param isSelected isSelected
     * @param mouseButton mouseButton */ 
    public void gdPopupVerifySelectedByTextPath(
        int xPos, 
        String xUnits, 
        int yPos, 
        String yUnits, 
        String textPath, 
        String operator, 
        boolean isSelected, 
        int mouseButton);

    /**
     * @param mouseButton mouseButton
     * @param modifierSpecification modifierSpecification
     * @param xPos xPos
     * @param xUnits xUnits
     * @param yPos yPos
     * @param yUnits yUnits */ 
    public void gdDrag(
        int mouseButton, 
        String modifierSpecification, 
        int xPos, 
        String xUnits, 
        int yPos, 
        String yUnits);

    /**
     * @param xPos xPos
     * @param xUnits xUnits
     * @param yPos yPos
     * @param yUnits yUnits
     * @param delayBeforeDrop delayBeforeDrop */ 
    public void gdDrop(
        int xPos, 
        String xUnits, 
        int yPos, 
        String yUnits, 
        int delayBeforeDrop);

}