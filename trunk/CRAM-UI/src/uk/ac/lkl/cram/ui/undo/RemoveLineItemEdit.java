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

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import uk.ac.lkl.cram.model.LineItem;
import uk.ac.lkl.cram.model.Module;

/**
 * This class represents an undo-able edit where the user has removed a LineItem
 * from a module.
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("ClassWithoutLogger")
public class RemoveLineItemEdit extends AbstractUndoableEdit {

    private LineItem lineItem;
    private int index;
    private Module module;

    /**
     * Create an edit from the parameters supplied.
     * @param module the module from which the line item has been removed
     * @param lineItem the lineitem that has been removed
     * @param index the index of the line item in the underlying collection from which it has been removed
     */
    public RemoveLineItemEdit(Module module, LineItem lineItem, int index) {
        this.module = module;
        this.lineItem = lineItem;
        this.index = index;
    }

    @Override
    public void undo() throws CannotUndoException {
        lineItem.insertLineItemAt(module, index);
    }

    @Override
    public void redo() throws CannotRedoException {
        lineItem.removeFrom(module);
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public boolean canRedo() {
        return true;
    }

    @Override
    public String getPresentationName() {
        return "Remove " + lineItem.getName();
    }
}
