/*
Copyright (C) 2012 Alina Ioana Florea (alina.ioana.florea@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

http://www.opensource.org/licenses/mit-license.php
*/

package com.github.alinaioanaflorea.xmldiff.core.service.impl;

import com.github.alinaioanaflorea.xmldiff.core.model.XmlNode;
import com.github.alinaioanaflorea.xmldiff.core.model.XmlElem;
import com.github.alinaioanaflorea.xmldiff.core.model.XmlAttribute;
import com.github.alinaioanaflorea.xmldiff.core.model.ModificationType;
import com.github.alinaioanaflorea.xmldiff.core.model.XmlNodeMetaData;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

// This class builds an XmlElem with the difference between two XmlElem structures
class XmlElemMatcher {

    public static final String DEFAULT_NAME_NO_ROOT = "root";
    public static final String DEFAULT_MSG_NO_CHANGE = "No change!";

    /**
     * Returns the difference between its input XmlElem structures <br />
     * <br />
     * @param oldXml The older version of the XmlElem structure to be compared <br />
     * @param newXml The newer version of the XmlElem structure to be compared <br />
     */
    public static XmlElem matchXmlElem(XmlElem oldXml, XmlElem newXml) {

        // The XmlElem structure to hold the difference between the input XmlElem structures
        XmlElem diffXml;

        if (oldXml != null && newXml != null)
        {
            diffXml = (XmlElem) match(oldXml, newXml);

            if (diffXml.getMetaData().getMatchedPercentage() == XmlNodeMetaData.FULLY_MATCHED) {

                diffXml.setValue(DEFAULT_MSG_NO_CHANGE);
                diffXml.getAttributes().clear();
                diffXml.getChildXmlElems().clear();
            }
        }
        else if (oldXml != null) {

            diffXml = oldXml;
            diffXml.getMetaData().setModificationType(ModificationType.DELETED);
        }
        else if (newXml != null) {

            diffXml = newXml;
            diffXml.getMetaData().setModificationType(ModificationType.NEW);
        }
        else {

            diffXml = new XmlElem(DEFAULT_NAME_NO_ROOT, DEFAULT_MSG_NO_CHANGE);
        }

        return diffXml;
    }

    // Returns the difference between its input XmlNode structures
    private static XmlNode match(XmlNode oldNode, XmlNode newNode) {

        XmlNode diffNode = newNode instanceof XmlElem ?
                           new XmlElem(newNode.getName(), newNode.getValue()) :
                           new XmlAttribute(newNode.getName(), newNode.getValue());

        XmlNodeMetaData diffNodeStatus = diffNode.getMetaData();

        int nameMatchPercentage = match(oldNode.getName(), newNode.getName(), diffNodeStatus);
        int valueMatchPercentage = match(oldNode.getValue(), newNode.getValue(), diffNodeStatus);
        int attrsMatchPercentage = XmlNodeMetaData.FULLY_MATCHED;
        int childNodesMatchPercentage = XmlNodeMetaData.FULLY_MATCHED;

        if (newNode instanceof XmlElem) {
        
            ((XmlElem)diffNode).setAttributes(new ArrayList<XmlAttribute>());
            attrsMatchPercentage = match(((XmlElem) oldNode).getAttributes(), ((XmlElem) newNode).getAttributes(),
                                         ((XmlElem) diffNode).getAttributes(), diffNodeStatus);

            ((XmlElem)diffNode).setChildXmlElems(new ArrayList<XmlElem>());
            childNodesMatchPercentage = match(((XmlElem) oldNode).getChildXmlElems(), ((XmlElem) newNode).getChildXmlElems(),
                                             ((XmlElem) diffNode).getChildXmlElems(), diffNodeStatus);
        }

        int matchedPercentage = (nameMatchPercentage + valueMatchPercentage
                                 + attrsMatchPercentage + childNodesMatchPercentage) / 4;

        diffNodeStatus.setMatchedPercentage(matchedPercentage);

        if (!diffNodeStatus.hasModified() &&
            (newNode.getMetaData().isMandatory() || oldNode.getMetaData().isMandatory())) {

            diffNodeStatus.setMandatory();
        }

        if (matchedPercentage != XmlNodeMetaData.FULLY_MATCHED)
            diffNodeStatus.setChanged();                          // CHANGED has priority over MANDATORY

        return diffNode;
    }

    // Returns the matched percentage of its input values and updates the meta data accordingly
    private static int match(String value1, String value2, XmlNodeMetaData nodeMetaData) {

        if ((value1 == null && value2 == null) ||
            (value1 != null && value2 != null && value1.equals(value2))) {

            return XmlNodeMetaData.FULLY_MATCHED;
        }

        nodeMetaData.setChanged();
        return XmlNodeMetaData.NOT_MATCHED;
    }

    // Updates meta data and triggers the build of a list of XML elements
    // containing the differences between its first two input lists
    private static <T extends XmlNode> int match(List<T> oldXml, List<T> newXml, List<T> diffXml, XmlNodeMetaData diffNodeStatus) {

        int nrOfOldElems = (oldXml != null) ? oldXml.size() : 0;
        int nrOfNewElems = (newXml != null) ? newXml.size() : 0;

        if (nrOfOldElems != 0 && nrOfNewElems != 0) {

            return compare(oldXml, newXml, diffXml);
        }
        else if (nrOfOldElems != 0) {

            diffXml.addAll(oldXml);
            markModification(diffXml, diffNodeStatus, ModificationType.DELETED);

            return XmlNodeMetaData.NOT_MATCHED;
        }
        else if (nrOfNewElems != 0) {

            diffXml.addAll(newXml);
            markModification(diffXml, diffNodeStatus, ModificationType.NEW);

            return XmlNodeMetaData.NOT_MATCHED;
        }

        return XmlNodeMetaData.FULLY_MATCHED;
    }

    // Builds a list of XML elements representing the differences between its first two input lists,
    // and returns the percentage at which they matched
    private static <T extends XmlNode> int compare(List<T> oldXml, List<T> newXml, List<T> diffXml) {

        int nrOfOldXmlElems = oldXml.size();
        int nrOfNewXmlElems = newXml.size();

        for (int i = 0; i < nrOfOldXmlElems; i++) {

            XmlNode oldNode = oldXml.get(i);

            for (int j = 0; j < nrOfNewXmlElems; j++) {

                XmlNode newNode = newXml.get(j);

                if (oldNode.getName().equals(newNode.getName())) {

                    XmlNode diffNode = match(oldNode, newNode);

                    diffNode.getMetaData().setIndexParent1(i);
                    diffNode.getMetaData().setIndexParent2(j);

                    diffXml.add((T) diffNode);
                }
            }
        }

        int matchPercentage = filter(oldXml, newXml, diffXml);

        // Reset the match states for the next round of siblings comparisons
        resetMatchState(oldXml);
        resetMatchState(newXml);

        return matchPercentage;
    }

    // Marks all the elements in the given list with the given ModificationType
    private static <T extends XmlNode> void markModification(List<T> source, XmlNodeMetaData nodeStatus, ModificationType modificationType) {

        for (XmlNode node: source) {

            node.getMetaData().setModificationType(modificationType);
        }

        if (!nodeStatus.hasModified())  // Respect the ModificationType priority rule
            nodeStatus.setChanged();
    }

    // After the match is done this method will choose the final matches
    private static <T extends XmlNode> int filter(List<T> oldXml, List<T> newXml, List<T> diffXml) {

        // Mark the highest% matches in the given structures
        markHighestMatches(oldXml, newXml, diffXml);

        // Remove the XmlNodes which did not matched from the given structure
        removeMatches(diffXml, false);

        // Add the XmlNodes which did not got matched from the oldXml structure as deleted
        addNotMatchedElems(oldXml, diffXml, ModificationType.DELETED);
        // Add the XmlNodes which did not got matched from the newXml structure as new
        addNotMatchedElems(newXml, diffXml, ModificationType.NEW);

        // IMPORTANT: The match percentage must be calculated before removing 100% matches
        // in order to be able to take in consideration the total nr of XmlNode structures on which the calculation is done
        int matchPercentage = calcMatchPercentage(diffXml);

        // Remove 100% not mandatory matches from the given structure.
        // These are XML elements that did not changed and therefor should not be part of the difference/output
        removeMatches(diffXml, true);

        return matchPercentage;
    }

    // Marks a child XmlNode structure and its parents as fully matched if both of the child 's parents are not yet matched.
    private static <T extends XmlNode> void markHighestMatches(List<T> xmlNodeParents1, List<T> xmlNodeParents2, List<T> childXmlNodes) {

        // Place the highest matches first
        Collections.sort(childXmlNodes, new XmlNode.MatchPercentageComparator());

        for (XmlNode childXmlNode : childXmlNodes) {

            int indexParent1 = childXmlNode.getMetaData().getIndexParent1();
            int indexParent2 = childXmlNode.getMetaData().getIndexParent2();

            XmlNodeMetaData parent1MetaData = xmlNodeParents1.get(indexParent1).getMetaData();
            XmlNodeMetaData parent2MetaData = xmlNodeParents2.get(indexParent2).getMetaData();

            if (!parent1MetaData.isMatched() && !parent2MetaData.isMatched()) {

                parent1MetaData.setMatched();
                parent2MetaData.setMatched();

                childXmlNode.getMetaData().setMatched();
            }
        }
    }

    // This method is used to remove the XmlNode structures which either got 100% match percentage AND are NOT mandatory
    // OR to remove the nodes that did not matched at all.
    // The boolean flag is used to choose the kind of deletion.
    private static <T extends XmlNode> void removeMatches(List<T> source, boolean removeMatched) {

        for (int i = 0; i < source.size(); i++) {

            XmlNode elem = source.get(i);

            if ((!removeMatched && !elem.getMetaData().isMatched()) ||
                (removeMatched && elem.getMetaData().isFullNonMandatoryMatch())) {

                source.remove(elem);

                // Reposition the index after the deletion
                i--;
            }
        }
    }

    // Adds the not yet matched XmlNode structures from its input source list to its input destination list,
    // marking them also with the given ModificationType
    private static <T extends XmlNode> void addNotMatchedElems(List<T> source, List<T> destination, ModificationType modification) {

        for (XmlNode xmlNode: source) {

            if (!xmlNode.getMetaData().isMatched()) {

                xmlNode.getMetaData().setModificationType(modification);
                xmlNode.getMetaData().setMatched();

                destination.add((T) xmlNode);
            }
        }
    }

    /*
    * Sets all the structures from the given list to not matched
    *
    * @param source The list of elements to be be marked as not matched
    * @param <T> The type of the elements in the given list. It must extend XmlNode
    */
    private static <T extends XmlNode> void resetMatchState(List<T> source) {

        for (XmlNode xmlNode : source) {

            xmlNode.getMetaData().setNotMatched();
        }
    }

    /*
     * Calculates the average(Pythagorean arithmetic mean) match percentage of all the structures in the given list
     *
     * @param source The list of elements from which the average should be calculated
     * @param <T> The type of the elements in the given list. It must extend XmlNode
     * @return The average match percentage
     */
    private static <T extends XmlNode> int calcMatchPercentage(List<T> source) {

        int matchPercentage = XmlNodeMetaData.NOT_MATCHED;

        for (XmlNode xmlNode : source) {

            matchPercentage += xmlNode.getMetaData().getMatchedPercentage();
        }

        return (matchPercentage != 0) ? matchPercentage/source.size() : XmlNodeMetaData.NOT_MATCHED;
    }
}