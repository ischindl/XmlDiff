/*
Copyright (C) 2012 Alina Ioana Florea (alina.ioana.florea@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

http://www.opensource.org/licenses/mit-license.php
*/

package com.github.alinaioanaflorea.xmldiff.ui.swing.eventhandlers;

import com.github.alinaioanaflorea.xmldiff.ui.swing.GUI;
import com.github.alinaioanaflorea.xmldiff.ui.swing.common.Utilities;

import javax.swing.JTextField;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

/**
 * This class holds the field listeners that provide help on how to set the value of a fields in the XmlDiff's main GUI
 */
public class FieldListeners
{
    // The GUI that triggered this event
    private GUI gui = null;

    private static final String TEXT_CHANGE_FILE = "Use the button to change the XML file";

    /**
     * Ctor
     * @param gui The GUI that triggered this event
     */
    public FieldListeners(GUI gui) {

        this.gui = gui;
    }

    public class FieldMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent event) {

            setFieldState(event);
        }

        @Override
        public void mouseReleased(MouseEvent event) {}
        @Override
        public void mouseEntered(MouseEvent event) {}
        @Override
        public void mouseExited(MouseEvent event) {}
        @Override
        public void mousePressed(MouseEvent event) {}
    }

    public class FieldKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent event) {

            setFieldState(event);
        }
    }

    private void setFieldState(InputEvent event) {

        gui.resetAllFieldStates();

        JTextField field = (JTextField) event.getSource();
        int fieldIndex = gui.getFields().indexOf(field);

        // Give information about how to set the field
        String fieldText = field.getText().trim();

        if (fieldText.equals(Utilities.TEXT_NO_VALUE))
            gui.setFieldState(fieldIndex, Color.RED, Utilities.TEXT_BROWSE_FILE);
        else
            gui.setFieldState(fieldIndex, Color.BLUE, TEXT_CHANGE_FILE);
    }
}