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

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This class resets the content of the GUI field that holds the path to the configuration file
 */
public class ResetButtonActionListener implements ActionListener {

    // The GUI that triggered this event
    private GUI gui = null;

    /**
     * Ctor
     * @param gui The GUI that triggered this event
     */
    public ResetButtonActionListener(GUI gui) {

        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        gui.resetAllFieldStates();

        JButton resetButton = (JButton) event.getSource();
        int resetButtonIndex = gui.getButtons().indexOf(resetButton);

        gui.getFields().get(resetButtonIndex-1).setText(Utilities.TEXT_DEFAULT_VALUE);

        gui.setFocusOnButton(resetButtonIndex);     // Set focus on self
    }
}