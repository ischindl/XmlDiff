/*
Copyright (C) 2012 Alina Ioana Florea (alina.ioana.florea@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

http://www.opensource.org/licenses/mit-license.php
*/

package com.github.alinaioanaflorea.xmldiff.ui.swing.eventhandlers;

import javax.swing.*;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

import com.github.alinaioanaflorea.xmldiff.core.service.XmlDiffService;
import com.github.alinaioanaflorea.xmldiff.ui.swing.common.Utilities;
import com.github.alinaioanaflorea.xmldiff.ui.swing.GUI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class opens a new windows to display the XmlDiff's output, log or documentation data.
 * It must be provided the name of the window in order to determine which window to open.
 */
public class NewWindowMouseListener implements MouseListener {

    private final Logger LOG = LoggerFactory.getLogger(NewWindowMouseListener.class);

    // The GUI that triggered this event
    private GUI gui = null;

    // The name of the window to open
    private String windowName = null;

    // The name of the file to open
    private String fileName = null;

    // The container from which to retrieve the content of the output stream
    private BufferedReader inputContent = null;

    // The content of the new window
    private StringBuilder outputContent = null;

    // File paths
    private static final String PATH_FILE_DOC = "doc" + Utilities.DIR_SEPARATOR + Utilities.NAME_FILE_HELP;

    // Window names
    public static final String NAME_WINDOW_HELP = "XmlDiff - help";
    public static final String NAME_WINDOW_LOG = "XmlDiff - logs";
    public static final String NAME_WINDOW_OUTPUT = "XmlDiff - outputData";

    /**
     * Ctor
     * @param gui The GUI that triggered this event
     */
    public NewWindowMouseListener(GUI gui) {

        this.gui = gui;
    }

    @Override
    public void mouseClicked(MouseEvent event) {

        // Check needed else label links will be open a window twice because they are considered to be both pressed and clicked
        if (!(event.getSource() instanceof JMenuItem)) return;

        process(event);
    }

    @Override
    public void mouseEntered(MouseEvent event) {

        // Apply only for JJLabels
        if (!(event.getSource() instanceof JLabel)) return;

        // Change the mouse cursor icon to a link/hand icon
        JLabel resultLink = (JLabel) event.getSource();
        resultLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent event) {}

    @Override
    public void mouseReleased(MouseEvent event) {}

    @Override
    public void mousePressed(MouseEvent event) {

        process(event);
    }

    private void process(MouseEvent event) {

        outputContent = null;      // Reset the output stream

        setWindowProperties(event);
        buildWindow();
        cleanup();
    }

    private void setWindowProperties(MouseEvent event) {

        Object sourceEvent = event.getSource();

        try {

            if (sourceEvent instanceof JLabel) {

                JLabel clickedLabel = (JLabel) sourceEvent;

                // Determine which file to open
                if (clickedLabel.getText().equals(Utilities.XMLDIFF_OUTPUT)) {

                    fileName = Utilities.XMLDIFF_OUTPUT;
                    windowName = NAME_WINDOW_OUTPUT;
                    outputContent = gui.getXmlDiffOutput();
                }
                else if (clickedLabel.getText().equals(Utilities.NAME_FILE_LOG)) {

                    fileName = Utilities.NAME_FILE_LOG;
                    windowName = NAME_WINDOW_LOG;

                    inputContent = new BufferedReader(new FileReader(Utilities.PATH_FILE_LOG));
                }
            }
            else if (sourceEvent instanceof JMenuItem) {

                gui.resetAllFieldStates();

                fileName = Utilities.NAME_FILE_HELP;
                windowName = NAME_WINDOW_HELP;

                // Get the XmlDiff's documentation file from a location in the classpath
                // IMPORTANT: reassign the content each time else the reader cursor remains at the end, plus the content may change
                InputStream docContent = XmlDiffService.class.getClassLoader().getResourceAsStream(PATH_FILE_DOC);
                inputContent = new BufferedReader(new InputStreamReader(docContent));
            }
        }
        catch (IOException e) {

            LOG.error(e.getMessage());
        }
    }

    private void buildWindow() {

        JFrame window = new JFrame(windowName);

        setWindowContent(window);

        // Window settings
        window.setSize(610, 500);
        window.setResizable(true);
        window.setIconImage(Utilities.IMG_XMLDIFF_LOGO);
        SwingUtilities.updateComponentTreeUI(window);
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.setVisible(true);
    }

    private void setWindowContent(JFrame window) {

        if (outputContent == null) {

            outputContent = new StringBuilder();

            try {

                String line;
                while ((line = inputContent.readLine()) != null) {

                    outputContent.append(line).append("\n");
                }
            }
            catch (IOException e) {

                LOG.error(e.getMessage());

                outputContent.replace(0, outputContent.length(), "Could not read the file: ").append(fileName);
            }
        }

        JTextArea text = new JTextArea();
        text.setText(outputContent.toString());
        text.setCaretPosition(0);
        text.setOpaque(false);
        text.setWrapStyleWord(true);

        JPanel windowContent = new JPanel();
        windowContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        windowContent.add(text);

        JScrollPane scrollableContent = new JScrollPane(windowContent);
        window.add(scrollableContent);
    }

    private void cleanup() {

        try {
            if (inputContent != null) inputContent.close();
        }
        catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
}
