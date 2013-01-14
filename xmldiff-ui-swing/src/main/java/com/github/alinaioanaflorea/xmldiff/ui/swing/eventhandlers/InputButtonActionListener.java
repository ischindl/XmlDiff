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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class facilitates the searching and opening of a file from the user's machine
 */
public class InputButtonActionListener implements ActionListener
{
    private final Logger LOG = LoggerFactory.getLogger(InputButtonActionListener.class);

    // Use only one file chooser to ease the search by remembering the last searched location
    private static JFileChooser fileChooser = new JFileChooser();

    // The GUI that triggered this event
    private GUI gui = null;

    /**
     * Ctor
     * @param gui The GUI that triggered this event
     */
    public InputButtonActionListener(GUI gui) {

        this.gui = gui;

        // Get the operating system's look and feel
        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {

            LOG.error(e.getMessage());
        }

        // Give the file choose window the same look and feel as the user's operating system
        SwingUtilities.updateComponentTreeUI(fileChooser);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        gui.resetAllFieldStates();

        JButton button = (JButton) event.getSource();
        int buttonIndex = gui.getButtons().indexOf(button);

        if (fileChooser.showOpenDialog(gui.getWindow()) == JFileChooser.APPROVE_OPTION) {

            // Set the path of the opened file in the corresponding GUI field
            gui.getFields().get(buttonIndex).setText(fileChooser.getSelectedFile().getAbsolutePath());
        }

        gui.setFocusOnButton(++buttonIndex);
    }
}