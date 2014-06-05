/*
 * (swing1.1beta3)
 * 
 */
package uk.ac.lkl.cram.ui.table;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * ColumnGroup represents a group of columns in a JTable.<br/>
 * Original version from
 * http://www.crionics.com/public/swing_examples/JTableExamples1.html
 * @version $Revision$ <br/>
 * @author Nobuo Tamemasa
 * @author Bernard Horan 
 */
//$Date$
@SuppressWarnings("ClassWithoutLogger")
public class ColumnGroup {

    private TableCellRenderer renderer;
    private List list;
    private String text;
    private int margin = 0;

    /**
     * Create a new column group with the title provided
     * @param text the title of the columngroup
     */
    public ColumnGroup(String text) {
	this(null, text);
    }
    
    /**
     * Create a column group from the title provided, using the supplied renderer
     * @param renderer the renderer for the column group
     * @param text the title of the column group
     */
    public ColumnGroup(TableCellRenderer renderer, String text) {
	if (renderer == null) {
	    this.renderer = new DefaultTableCellRenderer() {
                private static final long serialVersionUID = 1L;
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		    JTableHeader header = table.getTableHeader();
		    if (header != null) {
			setForeground(header.getForeground());
			setBackground(header.getBackground());
			setFont(header.getFont());
		    }
		    setHorizontalAlignment(JLabel.CENTER);
		    setText((value == null) ? "" : value.toString());
		    setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		    return this;
		}
	    };
	} else {
	    this.renderer = renderer;
	}
	this.text = text;
	list = new ArrayList();
    }

    /**
     * Add an object to the list of columns/columngroups
     * @param obj TableColumn or ColumnGroup
     */
    @SuppressWarnings("unchecked")
    public void add(Object obj) {
	if (obj == null) {
	    return;
	}
	list.add(obj);
    }

    /**
     * Get the list of column groups contained by this group
     * @param c TableColumn
     * @param g List of ColumnGroup or Column
     * @return  List of ColumnGroup
     */
    @SuppressWarnings("unchecked")
    List<ColumnGroup> getColumnGroups(TableColumn c, ArrayList g) {
	g.add(this);
	if (list.contains(c)) {
	    return g;
	}
	Iterator it = list.listIterator();
	while (it.hasNext()) {
	    Object obj = it.next();
	    if (obj instanceof ColumnGroup) {
		List<ColumnGroup> groups = ((ColumnGroup) obj).getColumnGroups(c, (ArrayList<ColumnGroup>)g.clone());
		if (groups != null) {
		    return groups;
		}
	    }
	}
	return null;
    }

    TableCellRenderer getHeaderRenderer() {
	return renderer;
    }

    void setHeaderRenderer(TableCellRenderer renderer) {
	if (renderer != null) {
	    this.renderer = renderer;
	}
    }

    Object getHeaderValue() {
	return text;
    }

    Dimension getSize(JTable table) {
	Component comp = renderer.getTableCellRendererComponent(
		table, getHeaderValue(), false, false, -1, -1);
	int height = comp.getPreferredSize().height;
	int width = 0;
	Iterator it = list.listIterator();
	while (it.hasNext()) {
	    Object obj = it.next();
	    if (obj instanceof TableColumn) {
		TableColumn aColumn = (TableColumn) obj;
		width += aColumn.getWidth();
		width += margin;
	    } else {
		width += ((ColumnGroup) obj).getSize(table).width;
	    }
	}
	return new Dimension(width, height);
    }

    void setColumnMargin(int margin) {
	this.margin = margin;
	Iterator it = list.listIterator();
	while (it.hasNext()) {
	    Object obj = it.next();
	    if (obj instanceof ColumnGroup) {
		((ColumnGroup) obj).setColumnMargin(margin);
	    }
	}
    }
}
