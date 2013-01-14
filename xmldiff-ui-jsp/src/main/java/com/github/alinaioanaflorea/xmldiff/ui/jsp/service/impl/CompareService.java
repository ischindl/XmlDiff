package com.github.alinaioanaflorea.xmldiff.ui.jsp.service.impl;

import com.github.alinaioanaflorea.xmldiff.core.service.impl.XmlDiffServiceImpl;
import com.github.alinaioanaflorea.xmldiff.ui.jsp.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This class handles the comparison between the input files
 */
public class CompareService extends Service {

    private static final Logger LOG = LoggerFactory.getLogger(CompareService.class);

    private static final String TITLE_WINDOW_RESULT = "XmlDiff - Result";
    private StringBuilder windowContent = null;
    private final static StringBuilder emptyWindowContent = new StringBuilder();

    private String uploadDir = null;
    private int maxMemFileSize = 0;
    private HttpServletRequest request = null;

    public CompareService(HttpServletRequest request, String uploadDir, int maxFileSizeKb) {

        super(request);

        this.request = request;
        this.uploadDir = uploadDir;
        this.maxMemFileSize = maxFileSizeKb;
    }

    @Override
    protected String getWindowTitle() {
        return TITLE_WINDOW_RESULT;
    }

    @Override
    protected StringBuilder getWindowContent() {

        List<String> inputFilePaths = null;

        try {

            inputFilePaths = new FileUploader().process(request, uploadDir, maxMemFileSize);

            windowContent = new XmlDiffServiceImpl().getDiff(inputFilePaths.get(0), inputFilePaths.get(1), inputFilePaths.get(2));

        }
        catch (Exception e) {

            // Print the original file name to the user
            String sessionId = request.getSession().getId();
            String msg = new String(e.getMessage());
            msg = msg.replace(sessionId, "");

            if (windowContent != null) {

                windowContent.replace(0, windowContent.length(), msg);
            }
            else {
                windowContent = new StringBuilder(msg);
            }
        }
        finally {

            // Cleanup
            if (inputFilePaths != null) {

                for (String filePath : inputFilePaths)  {

                    if (filePath == null) continue;

                    File file = new File(filePath);
                    try {

                        if (file.exists() && !file.delete())
                            throw new IOException("Could not delete the file: " + filePath);
                    }
                    catch (Exception e) {

                        LOG.warn(e.getMessage());
                    }
                }
            }
        }

        return (windowContent != null) ? windowContent : emptyWindowContent;
    }
}