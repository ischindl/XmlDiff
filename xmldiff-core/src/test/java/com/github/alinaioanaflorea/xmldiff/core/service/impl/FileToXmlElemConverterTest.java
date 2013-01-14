package com.github.alinaioanaflorea.xmldiff.core.service.impl;

import com.github.alinaioanaflorea.xmldiff.core.DataSource;
import com.github.alinaioanaflorea.xmldiff.core.model.XmlElem;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FileToXmlElemConverterTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testProcess() {

        new FileToXmlElemConverter().convertToXmlElem(null, null);
    }

    @Test
    public void testProcess2() {

        // Test without a configuration file
        XmlElem output = new FileToXmlElemConverter().convertToXmlElem(DataSource.PATH_FILE_XML_OLD, null);
        DataSource.testData(output, DataSource.getOldXml());
    }

    @Test
    public void testProcess2_1() {

        // Test without an empty configuration file
        XmlElem output = new FileToXmlElemConverter().convertToXmlElem(DataSource.PATH_FILE_XML_OLD, DataSource.PATH_FILE_XML_CONF_EMPTY);
        DataSource.testData(output, DataSource.getOldXml());
    }

    @Test
    public void testProcess2_2() {

        // Test without an empty configuration file
        XmlElem output = new FileToXmlElemConverter().convertToXmlElem(DataSource.PATH_FILE_XML_OLD, DataSource.PATH_FILE_XML_CONF_EMPTY2);
        DataSource.testData(output, DataSource.getOldXml());
    }

    @Test
    public void testProcess2_3() {

        // Test without an empty configuration file
        XmlElem output = new FileToXmlElemConverter().convertToXmlElem(DataSource.PATH_FILE_XML_OLD, DataSource.PATH_FILE_XML_CONF_WRONG_CONF_TAG_NAME);
        DataSource.testData(output, DataSource.getOldXml());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testProcess3() {

        // Test with an invalid configuration file
        new FileToXmlElemConverter().convertToXmlElem(DataSource.PATH_FILE_XML_OLD, DataSource.PATH_FILE_XML_CONF_INVALID);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testProcess3_1() {

        // Test with an invalid configuration file
        new FileToXmlElemConverter().convertToXmlElem(DataSource.PATH_FILE_XML_OLD, DataSource.PATH_FILE_XML_CONF_INVALID2);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testProcess3_2() {

        // Test with an invalid input file
        new FileToXmlElemConverter().convertToXmlElem(DataSource.PATH_FILE_XML_INVALID, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testProcess3_3() {

        // Test with an invalid input file
        new FileToXmlElemConverter().convertToXmlElem(DataSource.PATH_FILE_XML_INVALID2, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testProcess3_4() {

        // Test with an invalid input file
        new FileToXmlElemConverter().convertToXmlElem(DataSource.PATH_FILE_XML_INVALID3, null);
    }

    @Test
    public void testProcess5() {

        // Test priority of the mandatory/obsolete statuses

        XmlElem output = new FileToXmlElemConverter().convertToXmlElem(DataSource.PATH_FILE_XML_OLD, DataSource.PATH_FILE_XML_CONF_PRIORITY);

        testStatus(output, DataSource.getOldXmlConf());
    }

    private void testStatus(XmlElem output, XmlElem expected) {

        // Identify the root element
        Assert.assertEquals(output.getName(), expected.getName());
        Assert.assertEquals(output.getValue(), expected.getValue());

        // Test the root element
        Assert.assertEquals(output.getMetaData().getModificationType(), expected.getMetaData().getModificationType());

        // Test the root's child elements
        if (expected.getChildXmlElems() != null && expected.getChildXmlElems().size() != 0) {

            Assert.assertEquals(output.getChildXmlElems().size(), expected.getChildXmlElems().size());

            int nrOfChildElems =  expected.getChildXmlElems().size();
            for (int i = 0; i < nrOfChildElems; i++) {

                testStatus(output.getChildXmlElems().get(i), expected.getChildXmlElems().get(i));
            }
        }
    }

    @Test
    public void testProcess6() {

        // Test with a configuration file that removes the root node:

        XmlElem output = new FileToXmlElemConverter().convertToXmlElem(DataSource.PATH_FILE_XML_OLD, DataSource.PATH_FILE_XML_CONF_REMOVE_ROOT);

        Assert.assertEquals(output, null);
    }

    @Test
    public void testProcess7() {

        // Test an XML file that holds only an empty root tag

        XmlElem expected = DataSource.getOldXml();
        XmlElem output = new FileToXmlElemConverter().convertToXmlElem(DataSource.PATH_FILE_XML_EMPTY_ROOT, null);

        Assert.assertEquals(output.getName(), expected.getName());
        Assert.assertEquals(output.getAttributes().get(0).getName(), expected.getAttributes().get(0).getName());
        Assert.assertEquals(output.getAttributes().get(0).getValue(), expected.getAttributes().get(0).getValue());
    }
}