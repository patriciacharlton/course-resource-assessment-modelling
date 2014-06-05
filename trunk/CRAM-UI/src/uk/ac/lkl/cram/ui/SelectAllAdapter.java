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

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * A class that acts as a may of managing the focus on a text field. When the 
 * text field receives focus, an instance of this class will cause the contents
 * of the text field to be selected, so that it is easier to edit.
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
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
