package uk.ac.lkl.cram.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
public abstract class TextFieldAdapter implements DocumentListener, ActionListener {
    
    private JTextField textField;
    
    public TextFieldAdapter(JTextField textField) {
	this.textField = textField;
	textField.getDocument().addDocumentListener(this);
	textField.addActionListener(this);
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

    @Override
    public void actionPerformed(ActionEvent e) {
	textField.transferFocus();
    }

}
