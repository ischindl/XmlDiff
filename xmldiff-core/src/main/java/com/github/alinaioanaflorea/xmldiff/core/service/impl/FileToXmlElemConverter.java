/*
Copyright (C) 2012 Alina Ioana Florea (alina.ioana.florea@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

http://www.opensource.org/licenses/mit-license.php
*/

package com.github.alinaioanaflorea.xmldiff.core.service.impl;

import com.github.alinaioanaflorea.xmldiff.core.common.Utilities;
import com.github.alinaioanaflorea.xmldiff.core.model.XmlElem;

import com.github.alinaioanaflorea.xmldiff.core.model.XmlNode;
import com.github.alinaioanaflorea.xmldiff.core.model.XmlNodeMetaData;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

// This class converts the content of the given XML file to an XmlElem structure.
// If the class also receives a configuration file, it will be used accordingly to delete or mark as mandatory the tags of the processed file.
class FileToXmlElemConverter {

    // The name of the XML elements to be removed from the output
    private static final String NAME_XML_ELEM_TO_KEEP = "toKeep";

    // The name of the XML elements to be removed from the output
    private static final String NAME_XML_ELEM_TO_REMOVE = "toRemove";

    // The names of the XML elements to be marked mandatory
    private Set<String> mandatoryNames;

    // The names of the XML elements to be removed
    private Set<String> obsoleteNames;

    // Converts the content of the given XML file to an XmlElem structure
    // configured with the data provided in the optional configuration file.
    // If no configuration is received then the previous configuration
    // done on this object will be used, if any.
    public XmlElem convertToXmlElem(String pathToXmlFile, String pathToConfXmlFile) {

        if (pathToXmlFile == null)
            throw new IllegalArgumentException(Utilities.MSG_ERROR_FILE_INVALID + ": " + pathToXmlFile);

        // IMPORTANT: the configuration file must be processed first
        // so it can be used to configure the build of the input XML file
        if (pathToConfXmlFile != null) convertToXmlElem(pathToConfXmlFile, true);

        return convertToXmlElem(pathToXmlFile, false);
    }

    // Triggers the conversion convertToXmlElem by setting its pre and post requisites
    private XmlElem convertToXmlElem(String pathToXmlFile, boolean isConfigFile) {

        Node rootNode = Utilities.getXmlRootNode(pathToXmlFile);
        XmlElem rootXmlElem = convertToXmlElem(rootNode, isConfigFile, false);

        if (isConfigFile && rootXmlElem != null) {

            mandatoryNames = getChildNames(NAME_XML_ELEM_TO_KEEP, rootXmlElem.getChildXmlElems());
            obsoleteNames = getChildNames(NAME_XML_ELEM_TO_REMOVE, rootXmlElem.getChildXmlElems());

            return null;
        }

        return rootXmlElem;
    }

    /*
     * Converts the given Node to XmlElem format
     *
     * The XML elements marked to be removed in the configuration file will be skipped.
     * The XML elements marked to be kept in the configuration file will marked as mandatory.
     * An XML element will be marked mandatory also if any of its child elements were marked mandatory.
     * The above configuration is applied only to non-configuration files!
     */
    private XmlElem convertToXmlElem(Node node, boolean isConfigFile, boolean hasMandatoryParent) {

        String xmlElemName = node.getNodeName();

        if (!isConfigFile && toRemove(xmlElemName) && !isMandatory(xmlElemName) && !hasMandatoryParent)
            return null;

        NodeList childNodes = node.getChildNodes();
        int nrOfChildNodes = childNodes.getLength();

        Node nodeValue = (nrOfChildNodes != 0) ? childNodes.item(0) : null;    // The first child Node is the Node's value
        String xmlElemValue = (nodeValue != null && nodeValue.getNodeValue() != null) ? nodeValue.getNodeValue().trim() : "";

        // Build the root XML element
        XmlElem xmlElem = new XmlElem(xmlElemName, xmlElemValue);

        if (!isConfigFile && (isMandatory(xmlElemName) || hasMandatoryParent))
            xmlElem.getMetaData().setMandatory();

        // Gets the Node's child elements
        boolean hasMandatoryChildElems = false;
        if (nrOfChildNodes != 0) {

            List<XmlElem> childXmlElems = new ArrayList<XmlElem>();

            for (int i = 1; i < nrOfChildNodes ; i++) {      // On index 0 was the Node's value

                Node childNode = childNodes.item(i);

                if (childNode.getNodeType() == Node.ELEMENT_NODE) {   // Child nodes are separated by TEXT_NODEs

                    XmlElem childElem = convertToXmlElem(childNode, isConfigFile, xmlElem.getMetaData().isMandatory());
                    if (childElem != null) {

                        childXmlElems.add(childElem);

                        if (!isConfigFile && childElem.getMetaData().isMandatory())
                            hasMandatoryChildElems = true;
                    }
                }
            }

            xmlElem.setChildXmlElems(childXmlElems);
        }

        if (!isConfigFile) {

            if (hasMandatoryChildElems)
                xmlElem.getMetaData().setMandatory();

            // Get the Node's attributes
            xmlElem.setAttributes(Utilities.getXmlAttributes(node));

            if (xmlElem.getMetaData().isMandatory())
                markMandatory(xmlElem.getAttributes());
        }

        return xmlElem;
    }

    /**
     * Get the names of the XmlElem structures whose parents have the given name
     *
     * @param name The name to find in the given list
     * @param xmlElems The list of XmlElem structures to search through for the given name
     * @return The names of the XmlElem structures whose parents have the given name
     */
    private static Set<String> getChildNames(String name, List<XmlElem> xmlElems) {

        if (xmlElems == null) return null;

        Set<String> childNames = new HashSet<String>();

        for (XmlElem parentXmlElem : xmlElems) {

            if (parentXmlElem.getName().equals(name) && parentXmlElem.getChildXmlElems() != null)

                for (XmlElem childXmlElem : parentXmlElem.getChildXmlElems()) {

                    childNames.add(childXmlElem.getName());
                }
        }

        return childNames;
    }

    // Returns true if the given name is marked mandatory, false otherwise
    protected boolean isMandatory(String xmlElemName) {

        return mandatoryNames != null && mandatoryNames.contains(xmlElemName);
    }

    // Returns true if the given name is marked to be removed, false otherwise
    protected boolean toRemove(String xmlElemName) {

        return obsoleteNames != null && obsoleteNames.contains(xmlElemName);
    }

    // Marks as mandatory the XmlNodes from the given list
    private <E extends XmlNode> void markMandatory(List<E> xmlNodes) {

        if (xmlNodes == null) return;

        for(XmlNode xmlNode : xmlNodes) {

            if (xmlNode.getMetaData() == null)
                xmlNode.setMetaData(new XmlNodeMetaData());

                xmlNode.getMetaData().setMandatory();
        }
    }
}