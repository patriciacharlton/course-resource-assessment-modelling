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

import javax.swing.UIManager;
import javax.swing.undo.CompoundEdit;

/**
 * Extends CompundEdit with the ability to set the name to be used to undo
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("ClassWithoutLogger")
public class NamedCompoundEdit extends CompoundEdit {
    private String name;
    
    /**
     * Create a new instance of a CompoundEdit using the name provided
     * @param name the name to be used as the presentation name for the compound edit
     */
    public NamedCompoundEdit(String name) {
        super();
        this.name = name;
    }
    
    /**
     * Default constructor leaves name as null
     */
    public NamedCompoundEdit() {
        this(null);
    }
    
    /**
     * Set the name of the compoundedit
     * @param name the name to be used as the presentation name by the edit
     * @see NamedCompoundEdit#getPresentationName() 
     */
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String getRedoPresentationName() {
        if (name != null) {
            name = UIManager.getString("AbstractUndoableEdit.redoText") +
                " " + name;
        } else {
            name = UIManager.getString("AbstractUndoableEdit.redoText");
        }

        return name;
    }
    
    @Override
    public String getUndoPresentationName() {
        if (name != null) {
            name = UIManager.getString("AbstractUndoableEdit.undoText") +
                " " + name;
        } else {
            name = UIManager.getString("AbstractUndoableEdit.undoText");
        }

        return name;
    }
}
