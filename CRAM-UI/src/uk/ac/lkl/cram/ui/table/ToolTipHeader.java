/*
 * (swing1.1beta3)
 * 
 */
package uk.ac.lkl.cram.ui.table;

import java.awt.event.MouseEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 * A table header that can display a tooltip.<br/> 
 * Original version from
 * http://www.crionics.com/public/swing_examples/JTableExamples6.html
 * @version $Revision$
 * @author Nobuo Tamemasa
 * @author Bernard Horan 
 */
//$Date$ 
@SuppressWarnings({"serial", "ClassWithoutLogger"})
public class ToolTipHeader extends JTableHeader {

    private String[] toolTips;

    /**
     * Create the tooltip header from a table column model
     * @param model the tableColumn model
     */
    public ToolTipHeader(TableColumnModel model) {
	super(model);
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
     * Set the tooltips for the header
     * @param toolTips an array of string that is of the same size as the number of underlying columns
     */
    @SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
    public void setToolTipStrings(String[] toolTips) {
	this.toolTips = toolTips;
    }
}
