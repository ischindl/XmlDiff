/*
Copyright (C) 2012 Alina Ioana Florea (alina.ioana.florea@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

http://www.opensource.org/licenses/mit-license.php
*/
package com.github.alinaioanaflorea.xmldiff.ui.swing.common;

import com.github.alinaioanaflorea.xmldiff.ui.swing.XmlDiff;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;

/**
 * The class contains common functionality
 */
public final class Utilities
{
    // Get the operating system's directory separator: / on Unix systems, \ on Windows
    public static final String DIR_SEPARATOR = File.separator;

    // Label for the XmlDiff out
    public static final String XMLDIFF_OUTPUT = "XmlDiff output";

    // File names
    public static final String NAME_FILE_LOG = "XmlDiff.log";
    public static final String NAME_FILE_HELP = "XmlDiff.txt";
    public static final String NAME_FILE_IMG_LOGO = "XmlDiffLogo.jpg";

    // Paths
    // Get the current working directory
    private static final String PATH_DIR_OUTPUT = System.getProperty("user.dir");
    public static final String PATH_FILE_LOG = PATH_DIR_OUTPUT + DIR_SEPARATOR + NAME_FILE_LOG;

    // The logo image
    private static URL logoURL =  XmlDiff.class.getClassLoader().getResource(NAME_FILE_IMG_LOGO);
    public static final Image IMG_XMLDIFF_LOGO = Toolkit.getDefaultToolkit().getImage(logoURL);

    // Label values
    public static final String TEXT_DEFAULT_VALUE = " ";    // It must be min 1 space else the label holding it won't be displayed
    public static final String TEXT_NO_VALUE = "";
    public static final String TEXT_BROWSE_FILE = "Use the button to select an XML file";

    // GUI components indexes
    public static final int INDEX_FILE_OLD_XML = 0;
    public static final int INDEX_FILE_NEW_XML = 1;
    public static final int INDEX_FILE_CONF_XML = 2;
    public static final int INDEX_BUTTON_PROCESS = 3;
    public static final int INDEX_BUTTON_LINK = 4;

    private Utilities() {}

    /**
     * Resets the content of the given file
     * @param pathToFile The path to the file to reset
     */
    public static void resetFileContent(String pathToFile) {

        PrintWriter writer = null;
        try {

            writer = new PrintWriter(pathToFile);
            writer.print("");

        } catch (FileNotFoundException e) {

            // Log nothing as this might be the log file itself
        }
        finally {

            if (writer != null) writer.close();
        }
    }
}
