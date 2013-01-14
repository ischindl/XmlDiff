/*
Copyright (C) 2012 Alina Ioana Florea (alina.ioana.florea@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

http://www.opensource.org/licenses/mit-license.php
*/

package com.github.alinaioanaflorea.xmldiff.core.common;

import com.github.alinaioanaflorea.xmldiff.core.model.XmlAttribute;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

/**
 * This class holds common functionality
 */
public final class Utilities {

    public static final String DIR_SEPARATOR = File.separator;  // "/" on Unix, "\" on Windows

    // Error messages
    public static final String MSG_ERROR_FILE_INVALID = "Invalid file path or XML content provided";

    private Utilities() {}

    /**
     * Gets the root Node of the given XML file
     *
     * @param pathToXmlFile Path to the XML to be processed
     * @return The root node of the given XML file
     * @throws IllegalArgumentException in case the provided file path or its XML content are not valid
     */
    public static Node getXmlRootNode(String pathToXmlFile) {

        try {

            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = docBuilder.parse(new File(pathToXmlFile));
            doc.getDocumentElement().normalize();

            return doc.getChildNodes().item(0);
        }
        catch (Exception e) {

            if (pathToXmlFile != null) {

                // Print only the file name as the path could be on a server
                StringBuilder fileName = new StringBuilder(pathToXmlFile);
                int lastDir = pathToXmlFile.lastIndexOf(DIR_SEPARATOR);

                if (lastDir == -1)
                    lastDir = 0;
                else
                    lastDir++;

                fileName.replace(0, lastDir, "");
                pathToXmlFile = fileName.toString();
            }

            throw new IllegalArgumentException(MSG_ERROR_FILE_INVALID + ": " + pathToXmlFile);
        }
    }

    /**
     * Gets the attributes of the given Node in XmlAttribute format
     *
     * @param node The Node from which to extract the attributes
     * @return The attributes of the given Node in XmlAttribute format, or "null" if the given Node is null or doesn't have attributes
     */
    public static List<XmlAttribute> getXmlAttributes(Node node) {

        if (node == null || node.getAttributes() == null || node.getAttributes().getLength() == 0)
            return null;

        NamedNodeMap attributeNodes = node.getAttributes();
        int nrOfAttributes = attributeNodes.getLength();
        List<XmlAttribute> attributes = new ArrayList<XmlAttribute>();

        for (int i = 0; i < nrOfAttributes; i++) {

            Attr attr = (Attr) attributeNodes.item(i);
            XmlAttribute xmlAttribute = new XmlAttribute(attr.getNodeName(), attr.getNodeValue());

            attributes.add(xmlAttribute);
        }

        return attributes;
    }
}
