/*
 * (swing1.1beta3)
 * 
 */
package uk.ac.lkl.cram.ui.table;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * GroupableTableHeader represents the table header for a group of table columns.<br/>
 * Original version from
 * http://www.crionics.com/public/swing_examples/JTableExamples1.html
 * @version $Revision$
 * @author Nobuo Tamemasa
 * @author Bernard Horan 
 */
//$Date$
@SuppressWarnings({"serial", "FinalClass", "ClassWithoutLogger"})
public final class GroupableTableHeader extends JTableHeader {
    private static final String uiClassID = "GroupableTableHeaderUI";
    private List<ColumnGroup> columnGroups = null;
    private String[] toolTips;

    /**
     * Create a Groupable table header from the tableColumnModel
     * @param model the model of tablecolumns
     */
    public GroupableTableHeader(TableColumnModel model) {
	super(model);
	setUI(new GroupableTableHeaderUI());
	setReorderingAllowed(false);
    }

    @Override
    public void setReorderingAllowed(boolean b) {
	reorderingAllowed = false;
    }

    /**
     * Add a group of columns to the table header
     * @param g the group of columns
     */
    public void addColumnGroup(ColumnGroup g) {
	if (columnGroups == null) {
	    columnGroups = new ArrayList<ColumnGroup>();
	}
	columnGroups.add(g);
    }

    Iterator<ColumnGroup> getColumnGroups(TableColumn col) {
	if (columnGroups == null) {
	    return null;
	}
	for (ColumnGroup columnGroup : columnGroups) {
	    List<ColumnGroup> v_ret = columnGroup.getColumnGroups(col, new ArrayList<ColumnGroup>());
	    if (v_ret != null) {
		return v_ret.listIterator();
	    }
	}
	return null;
    }

    void setColumnMargin() {
	if (columnGroups == null) {
	    return;
	}
	int columnMargin = getColumnModel().getColumnMargin();
	for (ColumnGroup columnGroup : columnGroups) {
	    columnGroup.setColumnMargin(columnMargin);
	}
    }
    
    @Override
    public String getToolTipText(MouseEvent e) {
        int col = columnAtPoint(e.getPoint());
	int modelCol = getTable().convertColumnIndexToModel(col);
	String retStr;
	try {
	    retStr = toolTips[modelCol];
	} catch (NullPointerException | ArrayIndexOutOfBoundsException ex) {
	    retStr = "";
	}
	if (retStr.length() < 1) {
	    retStr = super.getToolTipText(e);
	}
	return retStr;
    }
    
    /**
     * Set the tooltips for the columns in the table header
     * @param toolTips a string array of tooltips (must be of the same size as the number of columns)
     */
    @SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
    public void setToolTipStrings(String[] toolTips) {
	this.toolTips = toolTips;
    }
}
