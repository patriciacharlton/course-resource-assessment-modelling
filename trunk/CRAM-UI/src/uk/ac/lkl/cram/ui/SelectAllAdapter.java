package uk.ac.lkl.cram.ui;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SelectAllAdapter extends FocusAdapter {

    @Override
    public void focusGained(final FocusEvent fe) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JTextField tf = (JTextField) fe.getComponent();
                tf.selectAll();
            }
        });
    }
}
