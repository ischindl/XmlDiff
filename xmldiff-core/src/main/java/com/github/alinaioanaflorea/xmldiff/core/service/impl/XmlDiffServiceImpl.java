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
import com.github.alinaioanaflorea.xmldiff.core.service.XmlDiffService;

/**
 * This class defines the XmlDiff's business flow,
 * building the difference between two XML files in a String format.
 */
public class XmlDiffServiceImpl implements XmlDiffService {

    /**
     * Gets the difference between the input XML files configured with the data in the configuration file
     *
     * @param pathToOldXmlFile The older version of the XML file to be compared
     * @param pathToNewXmlFile The newer version of the XML file to be compared
     * @param pathToConfigXmlFile The configuration file (optional)
     * @return The XML difference between the input XML file in String format
     */
    public StringBuilder getDiff(String pathToOldXmlFile, String pathToNewXmlFile, String pathToConfigXmlFile) {

        FileToXmlElemConverter fileConverter = new FileToXmlElemConverter();
        XmlElem oldXml = fileConverter.convertToXmlElem(pathToOldXmlFile, pathToConfigXmlFile);
        XmlElem newXml = fileConverter.convertToXmlElem(pathToNewXmlFile, null);  // A null config file path determines the usage of the previously sent configuration

        XmlElem diffXml = XmlElemMatcher.matchXmlElem(oldXml, newXml);

        return XmlElemToStringConverter.convertToString(diffXml);
    }
}