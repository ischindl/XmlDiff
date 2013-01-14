/*
Copyright (C) 2012 Alina Ioana Florea (alina.ioana.florea@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

http://www.opensource.org/licenses/mit-license.php
*/

package com.github.alinaioanaflorea.xmldiff.ui.swing.eventhandlers;

import com.github.alinaioanaflorea.xmldiff.core.service.XmlDiffService;
import static com.github.alinaioanaflorea.xmldiff.ui.swing.common.Utilities.*;

import com.github.alinaioanaflorea.xmldiff.ui.swing.GUI;

import javax.swing.JTextField;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.github.alinaioanaflorea.xmldiff.ui.swing.common.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class validates the input file paths and triggers the output generation, while updating the gui's state messages
 */
public class ProcessButtonActionListener implements ActionListener {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessButtonActionListener.class);

    // The GUI that triggered this event
    private GUI gui = null;

    // The XmlDiff service used to process the input files
    private XmlDiffService xmlDiffService = null;

    // The value of the GUI field that holds the path to the older version of the XML file being compared
    private String oldFieldValue = null;

    // The value of the GUI field that holds the path to the newer version of the XML file being compared
    private String newFieldValue = null;

    // The value of the GUI field that holds the path to the optional configuration XML file
    private String confFieldValue = null;

    // Labels for the processing button
    public static final String TEXT_PROCESSING = "Processing ...";
    public static final String TEXT_PROCESSING_SUCCEEDED = "Processing succeeded, click to open: ";
    public static final String TEXT_PROCESSING_FAILED = "Processing failed, click to open: ";

    /**
     * Ctor
     * @param gui The GUI that triggered this event
     * @param xmlDiffService The XmlDiff service used to process the input files
     */
    public ProcessButtonActionListener(GUI gui, XmlDiffService xmlDiffService) {

        this.gui = gui;
        this.xmlDiffService = xmlDiffService;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        gui.resetAllFieldStates();
        Utilities.resetFileContent(Utilities.PATH_FILE_LOG);

        if (!validateInput()) return;

        gui.setFieldState(INDEX_BUTTON_PROCESS, Color.BLUE, TEXT_PROCESSING);

        // To return the control to the GUI so it can be refreshed and display its new state,
        // the processing will have to continue in a new thread:
        Thread thread = new Thread( new Runnable(){ 
            
            @Override public void run() {
                process(oldFieldValue, newFieldValue, confFieldValue);
            } } );

        thread.setName(ProcessButtonActionListener.class.getName());
        thread.start();
    }

    // Validates the content of the GUI's fields.
    // Returns "true" on successful validation, and "false" otherwise.
    private boolean validateInput() {

        List<JTextField> guiFields = gui.getFields();

        oldFieldValue = guiFields.get(INDEX_FILE_OLD_XML).getText().trim();
        newFieldValue = guiFields.get(INDEX_FILE_NEW_XML).getText().trim();
        confFieldValue = guiFields.get(INDEX_FILE_CONF_XML).getText().trim();

        confFieldValue = (!confFieldValue.equals(TEXT_NO_VALUE) ) ? confFieldValue : null;

        if (oldFieldValue.equals(TEXT_NO_VALUE)) {

            gui.setFieldState(INDEX_FILE_OLD_XML, Color.RED, TEXT_BROWSE_FILE);
            return false;
        }
        else if (newFieldValue.equals(TEXT_NO_VALUE)) {

            gui.setFieldState(INDEX_FILE_NEW_XML, Color.RED, TEXT_BROWSE_FILE);
            return false;
        }

        return true;
    }

    /**
     * This calls the XmlDiff's service to process the input file and generate an output <br />
     * <br />
     * @param pathToOldXmlFile The path to the older version of the XML file being compared <br />
     * @param pathToNewXmlFile The path to the newer version of the XML file being compared <br />
     * @param pathToConfigXmlFile The path to the optional configuration XML file <br />
     */
    public void process(String pathToOldXmlFile, String pathToNewXmlFile, String pathToConfigXmlFile) {

        gui.disableButton(INDEX_BUTTON_PROCESS);

        try
        {
            gui.setXmlDiffOutput(xmlDiffService.getDiff(pathToOldXmlFile, pathToNewXmlFile, pathToConfigXmlFile));

            // Display a status message
            gui.setFieldState(INDEX_BUTTON_PROCESS, Color.GREEN, TEXT_PROCESSING_SUCCEEDED);
            gui.setFieldState(INDEX_BUTTON_LINK, Color.BLUE, XMLDIFF_OUTPUT);
        }
        catch (Exception e) {

            LOG.error(e.getMessage());

            gui.setFieldState(INDEX_BUTTON_PROCESS, Color.RED, TEXT_PROCESSING_FAILED);
            gui.setFieldState(INDEX_BUTTON_LINK, Color.BLUE, NAME_FILE_LOG);
        }
        finally {

            gui.enableButton(INDEX_BUTTON_PROCESS);
            gui.setFocusOnButton(INDEX_BUTTON_PROCESS+1);
        }
    }
}
