/*
 * Copyright 2014 London Knowledge Lab, Institute of Education.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.lkl.cram.ui.undo;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * This class provides the undo/redo functionality in the menu bar.
 *
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
public class UndoHandler implements UndoableEditListener {

    private static final Logger LOGGER = Logger.getLogger(UndoHandler.class.getName());
    /**
     * Property name to indicate change in whether I can undo
     */
    public static final String CAN_UNDO_PROP = "can_undo";
    //property change listener support
    private final PropertyChangeSupport undoChangeSupport;
    private final UndoAction undoAction;
    private final RedoAction redoAction;
    private final UndoManager undoManager;

    /**
     * Default constructor. Sets up the undo and redo actions.
     */
    public UndoHandler() {
        undoChangeSupport = new PropertyChangeSupport(this);
        this.undoManager = new UndoManager();
        undoAction = new UndoAction();
        redoAction = new RedoAction();
    }

    @Override
    public void undoableEditHappened(UndoableEditEvent uee) {
        //Remember the edit
        addEdit(uee.getEdit());
    }

    /**
     * Add an edit to the undoManager
     * @param edit the edit to be added to the undoManager
     * @see UndoManager#addEdit(javax.swing.undo.UndoableEdit) 
     */
    public void addEdit(UndoableEdit edit) {
        boolean oldValue = undoManager.canUndo();
        undoManager.addEdit(edit);
        undoChangeSupport.firePropertyChange(CAN_UNDO_PROP, oldValue, undoManager.canUndo());
        updateActions();
    }
    
    /**
     * Discard all the edits
     * @see UndoManager#discardAllEdits() 
     */
    public void discardAllEdits() {
        boolean oldValue = undoManager.canUndo();
        undoManager.discardAllEdits();
        undoChangeSupport.firePropertyChange(CAN_UNDO_PROP, oldValue, undoManager.canUndo());
        updateActions();
    }

    /**
     * Return the action that represents undo
     *
     * @return the undo action
     */
    public UndoAction getUndoAction() {
        return undoAction;
    }

    /**
     * Returns the action that represents redo
     *
     * @return the redo action
     */
    public RedoAction getRedoAction() {
        return redoAction;
    }

    
    private void updateActions() {
        if (undoManager.canUndo()) {
            undoAction.setEnabled(true);
            undoAction.putValue(Action.NAME, undoManager.getUndoPresentationName());
        } else {
            undoAction.setEnabled(false);
            undoAction.putValue(Action.NAME, "Undo");
        }
        if (undoManager.canRedo()) {
            redoAction.setEnabled(true);
            redoAction.putValue(Action.NAME, undoManager.getRedoPresentationName());
        } else {
            redoAction.setEnabled(false);
            redoAction.putValue(Action.NAME, "Redo");
        }
    }
    
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        undoChangeSupport.addPropertyChangeListener(pcl);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        undoChangeSupport.removePropertyChangeListener(pcl);
    }

    
    /**
     * Class to represent the undo action
     */
    @SuppressWarnings("PublicInnerClass")
    public class UndoAction extends AbstractAction {

        /**
         * Create the UndoAction
         */
        public UndoAction() {
            super("Undo");
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean oldValue = undoManager.canUndo();
            try {
                undoManager.undo();
            } catch (CannotUndoException ex) {
                LOGGER.log(Level.SEVERE, "Unable to undo", ex);
            }
            undoChangeSupport.firePropertyChange(CAN_UNDO_PROP, oldValue, undoManager.canUndo());
            updateActions();
        }
    }

    /**
     * Class to represent the redo action
     */
    @SuppressWarnings("PublicInnerClass")
    public class RedoAction extends AbstractAction {

        /**
         * Create the RedoAction.
         */
        public RedoAction() {
            super("Redo");
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean oldValue = undoManager.canUndo();
            try {
                undoManager.redo();
            } catch (CannotRedoException ex) {
                LOGGER.log(Level.SEVERE, "Unable to redo", ex);
            }
            undoChangeSupport.firePropertyChange(CAN_UNDO_PROP, oldValue, undoManager.canUndo());
            updateActions();
        }
    }
}
