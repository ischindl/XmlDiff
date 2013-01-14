/*
Copyright (C) 2012 Alina Ioana Florea (alina.ioana.florea@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

http://www.opensource.org/licenses/mit-license.php
*/

package com.github.alinaioanaflorea.xmldiff.core.service.impl;

import com.github.alinaioanaflorea.xmldiff.core.model.XmlElem;
import com.github.alinaioanaflorea.xmldiff.core.model.XmlAttribute;

// This class writes an XmlElem structure to the output file
class XmlElemToStringConverter {

    // Indentation measures for the data in the output stream
    private static final String INDENTATION_INCREMENT = "    ";
    private static final String NO_INDENTATION = "";

    // The encoding used for the data in the output stream
    private static final String XML_CONTENT_ENCODING = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>";

    /**
     * Converts the content of an XmlElem structure to its String representation,
     * indented as in an XML file
     *
     * @param xmlElem The XmlElem structure to be converted
     */
    public static StringBuilder convertToString(XmlElem xmlElem) {

        if (xmlElem == null) return null;

        StringBuilder xmlContent = new StringBuilder(XML_CONTENT_ENCODING);
        xmlContent.append("\n").append(buildXml(xmlElem, NO_INDENTATION));

        return xmlContent;
    }

    // Creates a StringBuilder representation from the given XML element structure
    // with the given indentation size
    private static StringBuilder buildXml(XmlElem xmlElem, String indentation) {

        StringBuilder output = new StringBuilder();

        // Build the start XML element
        output.append(indentation).append("<").append(xmlElem.getName());

        // Insert the XML element's modification type
        if (xmlElem.getMetaData().hasModified())
            output.append(" mod=\"").append(xmlElem.getMetaData().getModificationType()).append("\"");

        // Insert the XML element's attributes
        if (xmlElem.getAttributes() != null)
            for (XmlAttribute attr : xmlElem.getAttributes()) {

                output.append(" ").append(attr.getName()).append("=\"").append(attr.getValue()).append("\"");

                if (attr.getMetaData().hasModified()) {

                    output.append(" mod_").append(attr.getName()).append("=\"")
                          .append(attr.getMetaData().getModificationType()).append("\"");
                }
            }

        // Close the start XML element
        output.append(">");

        // Build the XML element's content
        if (!xmlElem.getValue().isEmpty()) {

            output.append(xmlElem.getValue());
        }

        if (xmlElem.getChildXmlElems() != null)
            for(XmlElem chilTag : xmlElem.getChildXmlElems()) {

                output.append("\n").append(buildXml(chilTag, indentation + INDENTATION_INCREMENT));
            }

        // Build the end XML element
        output.append("\n").append(indentation).append("</").append(xmlElem.getName()).append("> \n");

        return output;
    }
}