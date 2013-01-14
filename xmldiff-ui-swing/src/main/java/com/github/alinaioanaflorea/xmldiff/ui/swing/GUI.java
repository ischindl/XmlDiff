/*
Copyright (C) 2012 Alina Ioana Florea (alina.ioana.florea@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

http://www.opensource.org/licenses/mit-license.php
*/

package com.github.alinaioanaflorea.xmldiff.ui.swing;

import static com.github.alinaioanaflorea.xmldiff.ui.swing.common.Utilities.*;

import com.github.alinaioanaflorea.xmldiff.core.service.impl.XmlDiffServiceImpl;
import com.github.alinaioanaflorea.xmldiff.ui.swing.eventhandlers.*;
import com.github.alinaioanaflorea.xmldiff.ui.swing.eventhandlers.ExitWindowListener;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class builds and manages the XmlDiff's Swing GUI
 */
public class GUI {

    private final Logger LOG = LoggerFactory.getLogger(GUI.class);

    // The GUI's singleton instance
    private static GUI gui = null;

    // The GUI window
    private JFrame window = null;

    // The GUI fields
    private List<JTextField> fields = null;

    // The GUI field state labels. They display messages about the content of the field they correspond to
    private List<JLabel> fieldStates = null;

    // The GUI buttons
    private List<JButton> buttons = null;

    // A customized window layout
    private FlowLayout layoutFlowLeft = new FlowLayout();

    // The output of the comparison
    private StringBuilder xmlDiffOutput = null;

    // Window names
    private static final String NAME_WINDOW_MAIN = "XmlDiff - compare XML files";

    // Window menu names
    private static final String NAME_MENU_HELP = "Help";
    private static final String NAME_MENU_ITEM_ABOUT = "About XmlDiff";

    // Label names
    private static final String TEXT_BROWSE_OLD_FILE = "Browse the old XML file: ";
    private static final String TEXT_BROWSE_NEW_FILE = "Browse the new XML file: ";
    private static final String TEXT_BROWSE_CONF_FILE = "Browse a configuration XML file: (optional) ";

    // Button names
    private static final String NAME_BUTTON_PROCESS = "Process";
    private static final String NAME_BUTTON_BROWSE = "Browse";
    private static final String NAME_BUTTON_RESET = "Reset";

    /**
     * Sets the output produced by the last processing
     * @param xmlDiffOutput The output produced by the last processing
     */
    public void setXmlDiffOutput(StringBuilder xmlDiffOutput) {
        this.xmlDiffOutput = xmlDiffOutput;
    }

    /**
     * Gets the output produced by the last processing
     * @return The output produced by the last processing
     */
    public StringBuilder getXmlDiffOutput() {
        return xmlDiffOutput;
    }

    /**
     * Gets the GUI fields
     * @return The GUI fields
     */
    public List<JTextField> getFields() {

        return fields;
    }

    /**
     * Gets the GUI buttons
     * @return The GUI buttons
     */
    public List<JButton> getButtons() {

        return buttons;
    }

    /**
     * Gets the GUI window
     * @return The GUI window
     */
    public JFrame getWindow() {

        return window;
    }

    /**
     * Gets the GUI's singleton instance
     * @return The GUI's singleton instance
     */
    public static GUI getGui() {

        if (gui == null) gui = new GUI();

        return gui;
    }

    // The GUI has private creation access so that all its client code to access & modify to the same window instance
    private GUI() {

        // Get the operating system's look and feel
        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {

            LOG.error(e.getMessage());
        }

        window = new JFrame(NAME_WINDOW_MAIN);
        fields = new ArrayList<JTextField>();
        fieldStates = new ArrayList<JLabel>();
        buttons = new ArrayList<JButton>();

        // Customize the layout manager
        layoutFlowLeft = new FlowLayout();
        layoutFlowLeft.setAlignment(FlowLayout.LEFT);
    }

    /**
     * Builds the XmlDiff's Swing GUI
     */
    public void buildGUI() {

        // Build the GUI menu
        JMenuBar menuBar = new JMenuBar();
        JMenu helpMenu = new JMenu(NAME_MENU_HELP);
        JMenuItem aboutMenuItem = new JMenuItem(NAME_MENU_ITEM_ABOUT);

        aboutMenuItem.addMouseListener(new NewWindowMouseListener(this));
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);

        // Build the GUI content
        JPanel windowContent = new JPanel();
        windowContent.setLayout(new BoxLayout(windowContent, BoxLayout.Y_AXIS));
        windowContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add grouped components to the GUI, eg: a field with its state label and its button
        addComponentGroup(windowContent, TEXT_BROWSE_OLD_FILE, NAME_BUTTON_BROWSE);
        addComponentGroup(windowContent, TEXT_BROWSE_NEW_FILE, NAME_BUTTON_BROWSE);
        addComponentGroup(windowContent, TEXT_BROWSE_CONF_FILE, NAME_BUTTON_BROWSE);
        addComponentGroup(windowContent, TEXT_NO_VALUE, NAME_BUTTON_PROCESS);

        // Setup the GUI window
        window.addWindowListener(new ExitWindowListener(this));
        window.getContentPane().add(BorderLayout.NORTH, menuBar);
        window.getContentPane().add(BorderLayout.CENTER, windowContent);

        SwingUtilities.updateComponentTreeUI(window);
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.setSize(430, 470);  // width, height
        //window.setResizable(false);
        window.setIconImage(IMG_XMLDIFF_LOGO);
        window.setVisible(true);
    }

    // Add grouped components to the GUI, eg: a field with its state label and its button
    private void addComponentGroup(JPanel windowContent, String fieldLabelText, String buttonName) {

        // If a button has no label then it won't be given a field,
        // and it will be considered to be the processing button.
        // The processing button gets an extra state label to link to the generated result.
        boolean hasLabel = !fieldLabelText.trim().equals(TEXT_NO_VALUE);

        JTextField field = new JTextField(20);
        field.setEditable(false);
        field.addMouseListener(new FieldListeners(this).new FieldMouseListener());
        field.addKeyListener(new FieldListeners(this).new FieldKeyListener());

        JLabel fieldLabel = new JLabel(fieldLabelText);

        JLabel fieldState = new JLabel(TEXT_DEFAULT_VALUE);
        fieldStates.add(fieldState);

        JButton button = new JButton(buttonName);
        buttons.add(button);

        // A reset button is added only for the field that receives the configuration file
        JButton resetButton = new JButton(NAME_BUTTON_RESET);

        // Build a link that will point to the result of the processing
        JLabel resultLink = new JLabel();
        resultLink.addMouseListener(new NewWindowMouseListener(this));

        if (hasLabel) {

            addComponents(windowContent, fieldLabel);

            fields.add(field);

            button.addActionListener(new InputButtonActionListener(this));
            resetButton.addActionListener(new ResetButtonActionListener(this));

            if (fieldLabelText.equals(TEXT_BROWSE_CONF_FILE)) {

                buttons.add(resetButton);
                addComponents(windowContent, field, button, resetButton);
            }
            else {

                addComponents(windowContent, field, button);
            }

            addComponents(windowContent, fieldState);
        }
        else {

            // If the button has no label it is considered to be the processing button
            button.addActionListener(new ProcessButtonActionListener(this, new XmlDiffServiceImpl()));
            addComponents(windowContent, button);

            fieldStates.add(resultLink);
            addComponents(windowContent, fieldState, resultLink);
        }
    }

    // Add a variable number of components to the given panel with a customized layout
    private void addComponents(JPanel windowContent, JComponent... components) {

        // Use a panel with a customized layout to keep the size and flow of the components
        JPanel content = new JPanel();
        content.setLayout(layoutFlowLeft);

        for(JComponent component : components) {

            content.add(component);
        }

        windowContent.add(content);
    }

    /**
     * Resets the value of all field state labels
     */
    public void resetAllFieldStates() {

        if (fieldStates == null) return;

        for(JLabel state : fieldStates)
            state.setText(TEXT_DEFAULT_VALUE);
    }

    /**
     * Sets the value of the field state label which has the given index
     * @param fieldStateIndex The index of the field state label to be set
     * @param msgColor The color of the message
     * @param msg The new message to set the field state label to
     */
    public void setFieldState(int fieldStateIndex, Color msgColor, String msg) {

        if (fieldStates == null) return;

        fieldStates.get(fieldStateIndex).setForeground(msgColor);
        fieldStates.get(fieldStateIndex).setText(msg);
    }

    /**
     * Sets focus on the button with the given index. <br />
     * If the index if out of the range then the focus will se set on the first button in the GUI.
     * @param buttonIndex The index of the button to be set focus on
     */
    public void setFocusOnButton(int buttonIndex) {

        if (buttons == null) return;

        if (buttonIndex < 0 || buttonIndex > buttons.size()-1)
            buttonIndex = 0;

        buttons.get(buttonIndex).requestFocus();
    }

    /**
     * Disable the button with the given index
     * @param buttonIndex The index of the button to be disabled
     */
    public void disableButton(int buttonIndex) {

        if (buttons == null) return;

        buttons.get(buttonIndex).setEnabled(false);
    }

    /**
     * Enable the button with the given index
     * @param buttonIndex The index of the button to be enabled
     */
    public void enableButton(int buttonIndex) {

        if (buttons == null) return;

        buttons.get(buttonIndex).setEnabled(true);
    }
}