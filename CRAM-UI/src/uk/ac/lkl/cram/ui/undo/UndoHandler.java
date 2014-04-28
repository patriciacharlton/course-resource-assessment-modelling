package uk.ac.lkl.cram.ui.undo;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 *
 * @author Bernard Horan
 */
public class UndoHandler implements UndoableEditListener {

    private static final Logger LOGGER = Logger.getLogger(UndoHandler.class.getName());
    private UndoAction undoAction;
    private RedoAction redoAction;
    private final UndoManager um;

    public UndoHandler() {
	this(new UndoManager());
    }
    
    public UndoHandler(UndoManager um) {
	this.um = um;
	undoAction = new UndoAction();
	redoAction = new RedoAction();
    }
    
    @Override
    public void undoableEditHappened(UndoableEditEvent uee) {
	//Remember the edit
	um.addEdit(uee.getEdit());
	undoAction.updateUndoState();
	redoAction.updateRedoState();
    }

    public UndoAction getUndoAction() {
	return undoAction;
    }
    
    public RedoAction getRedoAction() {
	return redoAction;
    }

    public class UndoAction extends AbstractAction {

	public UndoAction() {
	    super("Undo");
	    setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    try {
		um.undo();
	    } catch (CannotUndoException ex) {
		LOGGER.log(Level.SEVERE, "Unable to undo", ex);
	    }
	    updateUndoState();
	    redoAction.updateRedoState();
	}

	protected void updateUndoState() {
	    if (um.canUndo()) {
		setEnabled(true);
		putValue(Action.NAME, um.getUndoPresentationName());
	    } else {
		setEnabled(false);
		putValue(Action.NAME, "Undo");
	    }
	}
    }

    public class RedoAction extends AbstractAction {

	public RedoAction() {
	    super("Redo");
	    setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    try {
		um.redo();
	    } catch (CannotRedoException ex) {
		LOGGER.log(Level.SEVERE, "Unable to redo", ex);

	    }
	    updateRedoState();
	    undoAction.updateUndoState();
	}

	protected void updateRedoState() {
	    if (um.canRedo()) {
		setEnabled(true);
		putValue(Action.NAME, um.getRedoPresentationName());
	    } else {
		setEnabled(false);
		putValue(Action.NAME, "Redo");
	    }
	}
    }
}
