/*
Copyright (C) 2012 Alina Ioana Florea (alina.ioana.florea@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

http://www.opensource.org/licenses/mit-license.php
*/

package com.github.alinaioanaflorea.xmldiff.core.model;

import java.util.Comparator;

/**
 *  This class represents an XML node
 */
public abstract class XmlNode {

    // The name of the XmlNode
    protected String name;

    // The value of the XmlNode
    protected String value;

    // Information about the metaData of the XmlNode
    protected XmlNodeMetaData metaData = null;

    protected XmlNode(String name, String value, XmlNodeMetaData metaData) {

        this.name = name;
        this.value = value;
        this.metaData = metaData;
    }

    /**
     * Sets the name of the XmlNode
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Gets the name of the XmlNode
     * @return the name of the XmlNode
     */
    public String getName() {

        return name;
    }

    /**
     * Sets the value of the XmlNode
     * @param value the value of the XmlNode
     */
    public void setValue(String value) {

        this.value = value;
    }

    /**
     * Gets the value of the XmlNode
     * @return the value of the XmlNode
     */
    public String getValue() {

        return value;
    }

    /**
     * Sets the metaData of the XmlNode
     */
    public void setMetaData(XmlNodeMetaData metaData) {

        this.metaData = metaData;
    }

    /**
     * Gets information about the metaData of the XmlNode
     * @return the XmlNode's metaData
     */
    public XmlNodeMetaData getMetaData() {

        return metaData;
    }

    /**
     * This class is used to sort XmlNode structures in a descending order according to their match percentage values
     */
    public static class MatchPercentageComparator implements Comparator<XmlNode> {

        public int compare(XmlNode node1, XmlNode node2) {

            int matchPercentage1 = node1.getMetaData().getMatchedPercentage();
            int matchPercentage2 = node2.getMetaData().getMatchedPercentage();

            if (matchPercentage1 < matchPercentage2)
                return 1;    // Swap position

            else if (matchPercentage1 > matchPercentage2)
                return -1;   // Swap position

            else return 0;   // Equal
        }
    }
}