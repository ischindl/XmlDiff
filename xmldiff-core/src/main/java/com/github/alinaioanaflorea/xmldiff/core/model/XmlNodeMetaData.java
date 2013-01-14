/*
Copyright (C) 2012 Alina Ioana Florea (alina.ioana.florea@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

http://www.opensource.org/licenses/mit-license.php
*/

package com.github.alinaioanaflorea.xmldiff.core.model;

/**
 * This class provides meta data for an XmlNode structure: <br />
 * - how did its content change <br />
 * - the indexes of the XmlNode's parent structures, if it was built from other XmlNodes, <br />
 *   and the percentage at which they matched <br />
 * <br />
 * The indexes refer to the XML structures created from the application's input files. <br />
 */
public class XmlNodeMetaData {

    // How did the content of the XmlNode modified
    private ModificationType modificationType = ModificationType.SAME;

    // Flag marking if the XmlNode got matched or not
    private boolean matched = false;

    // The percentage at which the XmlNode got matched
    private int matchedPercentage = NO_VALUE;

    // The index of the XmlNode's parent structure mapping the first input file
    private int indexParent1 = NO_VALUE;

    // The index of the XmlNode's parent structure mapping the second input file
    private int indexParent2 = NO_VALUE;

    /**
     * Default value to reflect that an XmlNode did not got matched
     */
    public static final int NO_VALUE = -1;

    /**
     * Default value to reflect that an XmlNode got a 100% matched
     */
    public static final int FULLY_MATCHED = 100;

    /**
     * Default value to reflect that an XmlNode got a 0% matched
     */
    public static final int NOT_MATCHED = 0;

    /**
     * Sets the XmlNode's modification type
     */
    public void setModificationType(ModificationType modificationType) {

        this.modificationType = modificationType;
    }

    /**
     * Sets the XmlNode as mandatory
     */
    public void setMandatory() {

        this.modificationType = ModificationType.MANDATORY;
    }

    /**
     * Sets the XmlNode as changed
     */
    public void setChanged()
    {
        modificationType = ModificationType.CHANGED;
    }

    /**
     * Gets the XmlNode's modification type
     */
    public ModificationType getModificationType() {

        return modificationType;
    }

    /**
     * Returns true if the XmlNode is mandatory
     */
    public boolean isMandatory() {

        return modificationType == ModificationType.MANDATORY;
    }

    /**
     * Returns true if the XmlNode represents a change and is not a mandatory element
     */
    public boolean hasModified() {

        return (modificationType != ModificationType.SAME && modificationType != ModificationType.MANDATORY);
    }

    /**
     * Returns true if the XmlNode has fully matched but is not a mandatory element
     */
    public boolean isFullNonMandatoryMatch() {

        return (matchedPercentage == FULLY_MATCHED && modificationType != ModificationType.MANDATORY);
    }

    /**
     * Returns true if the XmlNode got matched
     */
    public boolean isMatched() {

        return matched;
    }

    /**
     * Sets the XmlNode as matched
     */
    public void setMatched() {

        this.matched = true;
    }

    /**
     * Sets the XmlNode to not matched
     */
    public void setNotMatched() {

        this.matched = false;
    }

    /**
     * Sets the percentage at which the parent structures of the XmlNode got matched
     */
    public void setMatchedPercentage(int matchedPercentage) {

        this.matchedPercentage = matchedPercentage;
    }

    /**
     * Gets the percentage at which the parent structures of the XmlNode got matched
     */
    public int getMatchedPercentage() {

        return matchedPercentage;
    }

    /**
     * Sets the index of the XmlNode's parent structure from the first input file
     */
    public void setIndexParent1(int indexParent1) {

        this.indexParent1 = indexParent1;
    }

    /**
     * Gets the index of the XmlNode's parent structure from the first input file
     */
    public int getIndexParent1() {

        return indexParent1;
    }

    /**
     * Sets the index of the XmlNode's parent structure from the second input file
     */
    public void setIndexParent2(int indexParent2) {

        this.indexParent2 = indexParent2;
    }

    /**
     * Gets the index of the XmlNode's parent structure from the second input file
     */
    public int getIndexParent2() {

        return indexParent2;
    }
}