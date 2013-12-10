package uk.ac.lkl.cram.ui.table;

import java.awt.event.MouseEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 * $Date$ 
 * $Revision$
 * @version 1.0 02/25/99
 * @author Nobuo Tamemasa
 * @author Bernard Horan Original version from
 * http://www.crionics.com/public/swing_examples/JTableExamples6.html
 */
public class ToolTipHeader extends JTableHeader {

    private String[] toolTips;

    public ToolTipHeader(TableColumnModel model) {
	super(model);
    }

    @Override
    public String getToolTipText(MouseEvent e) {
	System.out.println("ToolTipHeader.getToolTipText");
	int col = columnAtPoint(e.getPoint());
	int modelCol = getTable().convertColumnIndexToModel(col);
	String retStr;
	try {
	    retStr = toolTips[modelCol];
	} catch (NullPointerException ex) {
	    retStr = "";
	} catch (ArrayIndexOutOfBoundsException ex) {
	    retStr = "";
	}
	if (retStr.length() < 1) {
	    retStr = super.getToolTipText(e);
	}
	return retStr;
    }

    public void setToolTipStrings(String[] toolTips) {
	this.toolTips = toolTips;
    }
}
