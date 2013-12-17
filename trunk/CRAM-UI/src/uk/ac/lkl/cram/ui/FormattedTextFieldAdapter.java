package uk.ac.lkl.cram.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
public abstract class FormattedTextFieldAdapter implements DocumentListener, ActionListener {
    
    protected JFormattedTextField textField;
    
    public FormattedTextFieldAdapter(JFormattedTextField textField) {
	this.textField = textField;
	textField.getDocument().addDocumentListener(this);
	textField.addActionListener(this);
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

    @Override
    public void actionPerformed(ActionEvent e) {
	textField.transferFocus();
    }

}
