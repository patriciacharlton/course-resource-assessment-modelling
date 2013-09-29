/*
 * (swing1.1beta3)
 * 
 */
package uk.ac.lkl.cram.ui.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * GroupableTableHeader
 * $Date$
 * $Revision$
 *
 * @version 1.0 10/20/98
 * @author Nobuo Tamemasa
 * @author Bernard Horan 
 * Original version from
 * http://www.crionics.com/public/swing_examples/JTableExamples1.html
 */
@SuppressWarnings({"serial", "ClassWithoutLogger", "FinalClass"})
public final class GroupableTableHeader extends JTableHeader {

    private static final String uiClassID = "GroupableTableHeaderUI";
    private List<ColumnGroup> columnGroups = null;

    public GroupableTableHeader(TableColumnModel model) {
	super(model);
	setUI(new GroupableTableHeaderUI());
	setReorderingAllowed(false);
    }

    @Override
    public void setReorderingAllowed(boolean b) {
	reorderingAllowed = false;
    }

    public void addColumnGroup(ColumnGroup g) {
	if (columnGroups == null) {
	    columnGroups = new ArrayList<ColumnGroup>();
	}
	columnGroups.add(g);
    }

    public Iterator<ColumnGroup> getColumnGroups(TableColumn col) {
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

    public void setColumnMargin() {
	if (columnGroups == null) {
	    return;
	}
	int columnMargin = getColumnModel().getColumnMargin();
	for (ColumnGroup columnGroup : columnGroups) {
	    columnGroup.setColumnMargin(columnMargin);
	}
    }
    

}
