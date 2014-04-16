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
package uk.ac.lkl.cram.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Abstract utility class to listen to changes in a formatted textfield's document, which 
 * also handles the transferring of focus on carriage return
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
public abstract class FormattedTextFieldAdapter implements DocumentListener, ActionListener {
    
    /**
     * The textfield to which the listeners are added
     */
    protected JFormattedTextField textField;
    
    /**
     * Create a new instance of the adaptor and add it as a listener to 
     * the textfield parameter.
     * @param textField the textfield we are listening to
     */
    public FormattedTextFieldAdapter(JFormattedTextField textField) {
	this.textField = textField;
	addListeners();
    }

    @Override
    public void insertUpdate(DocumentEvent de) {
	updateValue(textField.getValue());
    }

    @Override
    public void removeUpdate(DocumentEvent de) {
	updateValue(textField.getValue());
    }

    @Override
    @SuppressWarnings("NoopMethodInAbstractClass")
    public void changedUpdate(DocumentEvent de) {
	//ignore this
    }

    /**
     * The user has changed the value of the text field. (A formatted textfield
     * has a value, unlike a textfield, which only has a string text.) 
     * @param value the new value of the text field.
     */
    public abstract void updateValue(Object value);

    @Override
    public void actionPerformed(ActionEvent e) {
	textField.transferFocus();
    }

    private void addListeners() {
	textField.getDocument().addDocumentListener(this);
	textField.addActionListener(this);
    }
}
