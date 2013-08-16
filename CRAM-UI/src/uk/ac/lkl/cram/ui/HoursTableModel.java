package uk.ac.lkl.cram.ui;

import javax.swing.table.AbstractTableModel;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModulePresentation;

/**
 *
 * @author Bernard Horan, 
 */
public abstract class HoursTableModel extends AbstractTableModel {
    protected static final String[] COLUMN_NAMES = {"Activity", "Weekly", "Non-Weekly", "Total", "%Senior", "Cost"};
    protected final Module module;
    protected final ModulePresentation mp;

    public HoursTableModel(Module module, ModulePresentation mp) {
	super();
	this.module = module;
	this.mp = mp;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
	return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public int getColumnCount() {
	return COLUMN_NAMES.length;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
	//Note that the data/cell address is constant,
	//no matter where the cell appears onscreen.
	//Every column but the first one is editable
	return false;
    }

    @Override
    public String getColumnName(int i) {
	return COLUMN_NAMES[i];
    }

}
