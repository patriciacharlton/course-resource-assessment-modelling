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
 * ColumnGroup
 *
 * @version 1.0 10/20/98
 * @author Nobuo TamemasaBernard Horan Original version from
 * http://www.crionics.com/public/swing_examples/JTableExamples1.html
 */
public class ColumnGroup {

    protected TableCellRenderer renderer;
    protected List list;
    protected String text;
    protected int margin = 0;

    public ColumnGroup(String text) {
	this(null, text);
    }

    public ColumnGroup(TableCellRenderer renderer, String text) {
	if (renderer == null) {
	    this.renderer = new DefaultTableCellRenderer() {
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
     * @param obj TableColumn or ColumnGroup
     */
    public void add(Object obj) {
	if (obj == null) {
	    return;
	}
	list.add(obj);
    }

    /**
     * @param c TableColumn
     * @param g
     * @return  
     */
    public List<ColumnGroup> getColumnGroups(TableColumn c, ArrayList g) {
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

    public TableCellRenderer getHeaderRenderer() {
	return renderer;
    }

    public void setHeaderRenderer(TableCellRenderer renderer) {
	if (renderer != null) {
	    this.renderer = renderer;
	}
    }

    public Object getHeaderValue() {
	return text;
    }

    public Dimension getSize(JTable table) {
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

    public void setColumnMargin(int margin) {
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
