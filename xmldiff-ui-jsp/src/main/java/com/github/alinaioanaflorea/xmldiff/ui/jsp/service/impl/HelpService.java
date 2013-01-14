package com.github.alinaioanaflorea.xmldiff.ui.jsp.service.impl;

import com.github.alinaioanaflorea.xmldiff.core.service.XmlDiffService;
import com.github.alinaioanaflorea.xmldiff.ui.jsp.common.Utilities;
import com.github.alinaioanaflorea.xmldiff.ui.jsp.service.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * This class services the requests to view the help/documentation file
 */
public class HelpService extends Service {

    private static final String NAME_FILE_HELP = "doc" + Utilities.DIR_SEPARATOR + "XmlDiff.txt";
    private static final String TITLE_WINDOW_HELP = "XmlDiff - Help";

    // Read the documentation file only at application start-up as it's not going to change
    private static StringBuilder windowContent = buildWindowContent();

    public HelpService(HttpServletRequest request) {
        super(request);
    }

    @Override
    protected String getWindowTitle() {
        return TITLE_WINDOW_HELP;
    }

    @Override
    protected StringBuilder getWindowContent() {
        return windowContent;
    }

    private static StringBuilder buildWindowContent() {

        // Get the help file from the xmldiff-core package
        InputStream content = XmlDiffService.class.getClassLoader().getResourceAsStream(NAME_FILE_HELP);
        BufferedReader inputContent = new BufferedReader(new InputStreamReader(content));

        StringBuilder outputContent = new StringBuilder();
        String line;

        try {

            while ((line = inputContent.readLine()) != null) {

                outputContent.append(line).append("\n");
            }
        }
        catch (IOException e) {

            outputContent.replace(0, outputContent.length(), e.getMessage());
        }
        finally {

            try {

                if (inputContent != null) {

                    inputContent.close();
                }
            }
            catch (IOException e) {

                outputContent.replace(0, outputContent.length(), e.getMessage());
            }
        }

        return outputContent;
    }
}