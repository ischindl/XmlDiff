/*
Copyright (C) 2012 Alina Ioana Florea (alina.ioana.florea@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

http://www.opensource.org/licenses/mit-license.php
*/

package com.github.alinaioanaflorea.xmldiff.ui.swing.eventhandlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * This class holds the Listeners that close all the opened XmlDiff windows
 */
public class CloseAppListeners {

    class CloseAppActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {

            closeApp();
        }
    }

    class CloseAppWindowListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {}

        /*
         * Invoked when the user attempts to close the window
         * from the window's system menu.
         */
        @Override
        public void windowClosing(WindowEvent e) {}

        /*
         * Invoked when a window has been closed as the result
         * of calling dispose on the window.
         */
        @Override
        public void windowClosed(WindowEvent e) {

            closeApp();
        }

        @Override
        public void windowIconified(WindowEvent e) {}

        @Override
        public void windowDeiconified(WindowEvent e) {}

        @Override
        public void windowActivated(WindowEvent e) {}

        @Override
        public void windowDeactivated(WindowEvent e) {}
    }

    private void closeApp() {

        System.exit(0); // Successful termination
    }
}