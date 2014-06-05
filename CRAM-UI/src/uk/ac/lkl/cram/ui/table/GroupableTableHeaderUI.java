/*
 * (swing1.1beta3)
 * 
 */
package uk.ac.lkl.cram.ui.table;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * GroupableTableHeaderUI is responsible for rendering the groupable table header.<br/>
 * Original version from
 * http://www.crionics.com/public/swing_examples/JTableExamples1.html
 * @version $Revision$
 * @author Nobuo Tamemasa
 * @author Bernard Horan
 */
//$Date$
public class GroupableTableHeaderUI extends BasicTableHeaderUI {

    private static final Logger LOGGER = Logger.getLogger(GroupableTableHeaderUI.class.getName());

    @Override
    public void paint(Graphics g, JComponent c) {
	Rectangle clipBounds = g.getClipBounds();
	if (header.getColumnModel() == null) {
	    return;
	}
	((GroupableTableHeader) header).setColumnMargin();
	int column = 0;
	Dimension size = header.getSize();
	Rectangle cellRect = new Rectangle(0, 0, size.width, size.height);
	Map<ColumnGroup, Rectangle> h = new HashMap<ColumnGroup, Rectangle>();
	int columnMargin = header.getColumnModel().getColumnMargin();

	Enumeration<TableColumn> enumeration = header.getColumnModel().getColumns();
	while (enumeration.hasMoreElements()) {
	    cellRect.height = size.height;
	    cellRect.y = 0;
	    TableColumn aColumn = enumeration.nextElement();
	    Iterator<ColumnGroup> cGroups = ((GroupableTableHeader) header).getColumnGroups(aColumn);
	    if (cGroups != null) {
		int groupHeight = 0;
		while (cGroups.hasNext()) {
		    ColumnGroup cGroup = cGroups.next();
		    Rectangle groupRect = h.get(cGroup);
		    if (groupRect == null) {
			groupRect = new Rectangle(cellRect);
			Dimension d = cGroup.getSize(header.getTable());
			groupRect.width = d.width;
			groupRect.height = d.height;
			h.put(cGroup, groupRect);
		    }
		    paintCell(g, groupRect, cGroup);
		    groupHeight += groupRect.height;
		    cellRect.height = size.height - groupHeight;
		    cellRect.y = groupHeight;
		}
	    }
	    cellRect.width = aColumn.getWidth() + columnMargin;
	    if (cellRect.intersects(clipBounds)) {
		paintCell(g, cellRect, column);
	    }
	    cellRect.x += cellRect.width;
	    column++;
	}
    }

    private void paintCell(Graphics g, Rectangle cellRect, int columnIndex) {
	TableColumn aColumn = header.getColumnModel().getColumn(columnIndex);
	TableCellRenderer renderer = aColumn.getHeaderRenderer();
	if (renderer == null) {
	    renderer = header.getTable().getTableHeader().getDefaultRenderer();
	}
	Component component = renderer.getTableCellRendererComponent(
		header.getTable(), aColumn.getHeaderValue(), false, false, -1, columnIndex);
	rendererPane.add(component);
	rendererPane.paintComponent(g, component, header, cellRect.x, cellRect.y,
		cellRect.width, cellRect.height, true);
    }

    private void paintCell(Graphics g, Rectangle cellRect, ColumnGroup cGroup) {
	TableCellRenderer renderer = cGroup.getHeaderRenderer();
	Component component = renderer.getTableCellRendererComponent(
		header.getTable(), cGroup.getHeaderValue(), false, false, -1, -1);
	rendererPane.add(component);
	rendererPane.paintComponent(g, component, header, cellRect.x, cellRect.y,
		cellRect.width, cellRect.height, true);
    }

    private int getHeaderHeight() {
	int height = 0;
	TableColumnModel columnModel = header.getColumnModel();
	for (int column = 0; column < columnModel.getColumnCount(); column++) {
	    TableColumn aColumn = columnModel.getColumn(column);
	    TableCellRenderer renderer = aColumn.getHeaderRenderer();
	    if (renderer == null) {
		renderer = header.getTable().getTableHeader().getDefaultRenderer();
	    }
	    Component comp = renderer.getTableCellRendererComponent(
		    header.getTable(), aColumn.getHeaderValue(), false, false, -1, column);
	    int cHeight = comp.getPreferredSize().height;
	    Iterator<ColumnGroup> e = ((GroupableTableHeader) header).getColumnGroups(aColumn);
	    if (e != null) {
		while (e.hasNext()) {
		    ColumnGroup cGroup = e.next();
		    cHeight += cGroup.getSize(header.getTable()).height;
		}
	    }
	    height = Math.max(height, cHeight);
	}
	return height;
    }

    private Dimension createHeaderSize(long w) {
	TableColumnModel columnModel = header.getColumnModel();
	long width = w + columnModel.getColumnMargin() * columnModel.getColumnCount();
	if (width > Integer.MAX_VALUE) {
	    width = Integer.MAX_VALUE;
	}
	return new Dimension((int) width, getHeaderHeight());
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
	long width = 0;
	Enumeration<TableColumn> enumeration = header.getColumnModel().getColumns();
	while (enumeration.hasMoreElements()) {
	    TableColumn aColumn = enumeration.nextElement();
	    width += aColumn.getPreferredWidth();
	}
	return createHeaderSize(width);
    }
}
