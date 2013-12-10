package uk.ac.lkl.cram.ui;

import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
public abstract class FormattedTextFieldAdapter implements DocumentListener {
    
    protected JFormattedTextField textField;
    
    public FormattedTextFieldAdapter(JFormattedTextField textField) {
	this.textField = textField;
	textField.getDocument().addDocumentListener(this);
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
    public void changedUpdate(DocumentEvent de) {
	//ignore this
    }

    public abstract void updateValue(Object value);

}
