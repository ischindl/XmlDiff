package com.github.alinaioanaflorea.xmldiff.ui.jsp.service.impl;

import com.github.alinaioanaflorea.xmldiff.ui.jsp.common.Utilities;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.List;

// This class uploads files in a specific folder on the server
class FileUploader {

    // Returns a list with the paths to the files it uploaded on the server
    public List<String> process(HttpServletRequest request, String uploadDir, int maxMemFileSize)
            throws Exception {

        List<String> inputFilePaths = new ArrayList<String>();

        // Check if this is a file upload request
        if (!ServletFileUpload.isMultipartContent(request)) {

            throw new UnsupportedOperationException("Only upload requests are supported!");
        }

        File outputDir = new File(uploadDir + Utilities.DIR_SEPARATOR + "input");
        if (!outputDir.exists() && !outputDir.mkdir()) {

            throw new NotDirectoryException("Could not create the upload directory!");
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();
        // Set the size threshold (in bytes) beyond which files are written directly to disk
        factory.setSizeThreshold(maxMemFileSize);
        // Set the directory in which to store the data/files that are larger than maxMemFileSize
        factory.setRepository(outputDir);

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        // Set the maximum file size to be uploaded
        upload.setSizeMax(maxMemFileSize);

        // Get the unique session ID to be able to distinguish between multiple users
        // that send files, may be with the same name, on the same server
        String sessionId = request.getSession().getId();

        // Parse the request to get the file items
        List fileItems = upload.parseRequest(request);
        for (Object fi : fileItems)
        {
            FileItem field = (FileItem)fi;

            if ( !field.isFormField() && !field.getName().equals(""))
            {
                String fileName = sessionId + field.getName();
                String fileWithPath = uploadDir + Utilities.DIR_SEPARATOR + fileName;
                inputFilePaths.add(fileWithPath);

                File file = new File(fileWithPath);
                field.write(file);
            }
            else {

                inputFilePaths.add(null);
            }
        }

        return inputFilePaths;
    }
}