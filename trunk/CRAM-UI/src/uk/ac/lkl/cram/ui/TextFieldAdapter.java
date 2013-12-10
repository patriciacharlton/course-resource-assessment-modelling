package uk.ac.lkl.cram.ui;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
public abstract class TextFieldAdapter implements DocumentListener {
    
    private JTextField textField;
    
    public TextFieldAdapter(JTextField textField) {
	this.textField = textField;
	textField.getDocument().addDocumentListener(this);
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
    public void changedUpdate(DocumentEvent de) {
	//ignore this
    }

    public abstract void updateText(String text);

}
