package uk.ac.lkl.cram.ui;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 * $Date$
 *
 * @author Bernard Horan
 */
public class MultiLineHeaderRenderer extends JList implements TableCellRenderer {

    private static final Logger LOGGER = Logger.getLogger(MultiLineHeaderRenderer.class.getName());
    
    public MultiLineHeaderRenderer() {
	super();
	setOpaque(true);
	setForeground(UIManager.getColor("TableHeader.foreground"));
	setBackground(UIManager.getColor("TableHeader.background"));
	setFont(UIManager.getFont("TableHeader.font"));
	setBorder(BorderFactory.createEtchedBorder());
	ListCellRenderer renderer = getCellRenderer();
	((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
	setCellRenderer(renderer);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
	    boolean isSelected, boolean hasFocus, int row, int column) {
	String str = (value == null) ? "" : value.toString();
	BufferedReader br = new BufferedReader(new StringReader(str));
	String line;
	List<String> v = new ArrayList<String>();
	try {
	    while ((line = br.readLine()) != null) {
		v.add(line);
	    }
	} catch (IOException ex) {
	    LOGGER.log(Level.SEVERE, "Failed to read string: " + value);
	}
	setListData(v.toArray());
	return this;
    }
}
