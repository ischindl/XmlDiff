package com.github.alinaioanaflorea.xmldiff.ui.jsp.controller;

import com.github.alinaioanaflorea.xmldiff.ui.jsp.service.impl.CompareService;
import com.github.alinaioanaflorea.xmldiff.ui.jsp.service.impl.HelpService;
import com.github.alinaioanaflorea.xmldiff.ui.jsp.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class dispatches the incoming requests to the appropriate services for handling
 */
public class XmlDiffController extends HttpServlet {

    // The name of the web.xml parameter that holds the size (in bytes) beyond which files are written directly to disk
    private static final String NAME_PARAM_MAX_MEM_FILE_SIZE = "maxMemByteSize";
    private static final int DEFAULT_MEM_FILE_SIZE = 50 * 1024;         // ~50 kilobytes

    private String uploadDir = null;
    private static int maxMemFileSize = 0;

    @Override
    public void init() {

        uploadDir = getServletContext().getRealPath("/WEB-INF");

        try {

            // Get data from web.xml
            maxMemFileSize = Integer.parseInt(getServletContext().getInitParameter(NAME_PARAM_MAX_MEM_FILE_SIZE)) * 1024;
        }
        catch (NumberFormatException e) {

            maxMemFileSize = DEFAULT_MEM_FILE_SIZE;
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        processRequest(request, response, new CompareService(request, uploadDir, maxMemFileSize));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response, new HelpService(request));
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response, Service service)
        throws ServletException, IOException {

        service.process();

        request.getRequestDispatcher("jsp/result.jsp").forward(request, response);
    }
}