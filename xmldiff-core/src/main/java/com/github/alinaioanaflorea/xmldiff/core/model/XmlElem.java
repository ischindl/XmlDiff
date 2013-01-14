/*
Copyright (C) 2012 Alina Ioana Florea (alina.ioana.florea@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

http://www.opensource.org/licenses/mit-license.php
*/

package com.github.alinaioanaflorea.xmldiff.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an XML element
 */
public class XmlElem extends XmlNode {

    // The attributes of the XmlElem
    private List<XmlAttribute> attributes = null;

    // The child elements of the XmlElem
    private List<XmlElem> childXmlElems = null;

    public XmlElem(String elemName, String elemValue) {

        super(elemName, elemValue, new XmlNodeMetaData());
    }

    public XmlElem(XmlElem xmlElem) {

        super(xmlElem.getName(), xmlElem.getValue(), new XmlNodeMetaData());
        copy(xmlElem);
    }

    /**
     * Sets the attributes of the XmlElem to the given ones
     * @param attributes the attributes of the XmlElem
     */
    public void setAttributes(List<XmlAttribute> attributes) {

        this.attributes = attributes;
    }

    /**
     * Gets the attributes of the XmlElem
     * @return the attributes of the XmlElem
     */
    public List<XmlAttribute> getAttributes() {

        return attributes;
    }

    /**
     * Sets the child XmlElem structures of the XmlElem
     * @param childXmlElems the child XmlElem structures of this XmlElem
     */
    public void setChildXmlElems(List<XmlElem> childXmlElems) {

        this.childXmlElems = childXmlElems;
    }

    /**
     * Gets the child XmlElem structures of the XmlElem
     * @return the child XmlElem structures of this XmlElem
     */
    public List<XmlElem> getChildXmlElems() {

        return childXmlElems;
    }

    /**
     * Returns a does a deep copy of this object: name, value, modification type, attributes and child elements
     * @return A deep copy of this object
     */
    public XmlElem copy() {

        XmlElem output = new XmlElem(name, value);
        output.getMetaData().setModificationType(metaData.getModificationType());

        output.setAttributes(new ArrayList<XmlAttribute>());
        if (attributes != null && attributes.size() != 0) {

           for (XmlAttribute attr : attributes) {

               output.getAttributes().add(new XmlAttribute(attr.getName(), attr.getValue()));
           }
        }

        output.setChildXmlElems(new ArrayList<XmlElem>());
        if (childXmlElems != null && childXmlElems.size() != 0) {

            for (XmlElem childElem : childXmlElems) {

                XmlElem newElem = childElem.copy();
                output.getChildXmlElems().add(newElem);
            }
        }

        return output;
    }

    // Builds this object with the data from the given XmlElem
    private void copy(XmlElem source) {

        name = source.getName();
        value = source.getValue();
        metaData.setModificationType(source.getMetaData().getModificationType());

        attributes = new ArrayList<XmlAttribute>();
        if (source.getAttributes() != null && source.getAttributes().size() != 0) {

            for (XmlAttribute attr : source.getAttributes()) {

                attributes.add(new XmlAttribute(attr.getName(), attr.getValue()));
            }
        }

        childXmlElems = new ArrayList<XmlElem>();
        if (source.getChildXmlElems() != null && source.getChildXmlElems().size() != 0) {

            for (XmlElem sourceElem : source.getChildXmlElems()) {

                XmlElem copyElem = sourceElem.copy();
                childXmlElems.add(copyElem);
            }
        }
    }
}