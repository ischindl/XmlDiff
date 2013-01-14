package com.github.alinaioanaflorea.xmldiff.core.common;

import com.github.alinaioanaflorea.xmldiff.core.DataSource;
import com.github.alinaioanaflorea.xmldiff.core.model.XmlAttribute;
import com.github.alinaioanaflorea.xmldiff.core.model.XmlElem;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

public class UtilitiesTest {

    @DataProvider
    public Object[][] invalidPathProvider() {

        return new Object[][] { new Object[]{null}, new Object[]{""}, new Object[]{"xyz.xml"},
                                new Object[]{DataSource.PATH_FILE_DOC},
                                new Object[]{DataSource.PATH_FILE_XML_INVALID},
                                new Object[]{1}, new Object[]{"1"} };
    }

    @Test(dataProvider = "invalidPathProvider", expectedExceptions = IllegalArgumentException.class)
    @Parameters({"pathToXmlFile"})
    public void testGetXmlRootNode(String pathToXmlFile) {

        Utilities.getXmlRootNode(pathToXmlFile);
    }

    @Test
    public void testGetXmlRootNode2() {

        Node output = Utilities.getXmlRootNode(DataSource.PATH_FILE_XML_OLD);

        testData(output, DataSource.getOldXml());
    }

    private void testData(Node data, XmlElem expected) {

        // Test the root name & value
        Assert.assertEquals(data.getNodeName(), expected.getName());
        Assert.assertEquals(data.getChildNodes().item(0).getNodeValue().trim(), expected.getValue());

        // Test the child names & values
        if (expected.getChildXmlElems() != null) {

            NodeList childNodes = data.getChildNodes();
            int nrOfChildNodes = childNodes.getLength();

            int j = 0;
            for (int i = 1; i < nrOfChildNodes ; i++) {

                Node childNode = childNodes.item(i);  // On index 0 was the value of the Node

                if (childNode.getNodeType() == Node.ELEMENT_NODE)  {  // ELEMENT_NODEs are separated by TEXT_NODEs

                    testData(childNode, expected.getChildXmlElems().get(j++));
                }
            }
        }
    }

    @Test
    public void testGetXmlAttributes() {

        Assert.assertEquals(Utilities.getXmlAttributes(null), null);
    }

    @Test
    public void testGetXmlAttributes2() {

        Node output = Utilities.getXmlRootNode(DataSource.PATH_FILE_XML_OLD);

        testAttrData(output, DataSource.getOldXml());
    }

    private void testAttrData(Node data, XmlElem expected) {

        List<XmlAttribute> dataAttrs = Utilities.getXmlAttributes(data);

        if (expected.getAttributes() != null) {

            int nrOfAttrs = expected.getAttributes().size();
            for (int i = 0; i < nrOfAttrs; i++) {

                System.out.print(dataAttrs.get(i).getName());
                Assert.assertEquals(dataAttrs.get(i).getName(), expected.getAttributes().get(i).getName());
                Assert.assertEquals(dataAttrs.get(i).getValue(), expected.getAttributes().get(i).getValue());
            }
        }

        if (expected.getChildXmlElems() != null) {

            NodeList childNodes = data.getChildNodes();
            int nrOfChildNodes = childNodes.getLength();

            int j = 0;
            for (int i = 1; i < nrOfChildNodes ; i++) {

                Node childNode = childNodes.item(i);

                if (childNode.getNodeType() == Node.ELEMENT_NODE) {

                    testAttrData(childNode, expected.getChildXmlElems().get(j++));
                }
            }
        }
    }
}