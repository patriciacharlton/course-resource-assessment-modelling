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
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Abstract utility class to listen to changes in a textfield's document, which 
 * also handles the transferring of focus on carriage return
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
public abstract class TextFieldAdapter implements DocumentListener, ActionListener {
    
    private JTextField textField;
    
    /**
     * The textfield to which the listeners are added
     * @param textField
     */
    public TextFieldAdapter(JTextField textField) {
	this.textField = textField;
	addListeners();
    }

    @Override
    public void insertUpdate(DocumentEvent de) {
	updateText(textField.getText());
    }

    @Override
    public void removeUpdate(DocumentEvent de) {
	updateText(textField.getText());   
    }

    @Override
    @SuppressWarnings("NoopMethodInAbstractClass")
    public void changedUpdate(DocumentEvent de) {
	//ignore this
    }

    /**
     * The user had changed the text in the text field. 
     * @param text
     */
    public abstract void updateText(String text);

    @Override
    public void actionPerformed(ActionEvent e) {
	textField.transferFocus();
    }

    private void addListeners() {
	textField.getDocument().addDocumentListener(this);
	textField.addActionListener(this);
    }
}
