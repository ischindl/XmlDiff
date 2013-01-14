package com.github.alinaioanaflorea.xmldiff.core.service.impl;

import com.github.alinaioanaflorea.xmldiff.core.DataSource;
import com.github.alinaioanaflorea.xmldiff.core.model.*;

import org.testng.Assert;
import org.testng.annotations.Test;

public class XmlElemMatcherTest {

    @Test
    public void match() {

        XmlElem output = XmlElemMatcher.matchXmlElem(null, null);

        Assert.assertEquals(output.getName(), XmlElemMatcher.DEFAULT_NAME_NO_ROOT);
        Assert.assertEquals(output.getValue(), XmlElemMatcher.DEFAULT_MSG_NO_CHANGE);
    }

    @Test
    public void match1() {

        XmlElem output = XmlElemMatcher.matchXmlElem(DataSource.getOldXml(), DataSource.getOldXml());

        Assert.assertEquals(output.getName(), DataSource.getOldXml().getName());
        Assert.assertEquals(output.getValue(), XmlElemMatcher.DEFAULT_MSG_NO_CHANGE);
        Assert.assertEquals(output.getAttributes().size(), 0);
        Assert.assertEquals(output.getChildXmlElems().size(), 0);
    }

    @Test
    public void match2() {

        XmlElem expected = DataSource.getOldXml();
        expected.getMetaData().setModificationType(ModificationType.DELETED);

        XmlElem output = XmlElemMatcher.matchXmlElem(DataSource.getOldXml(), null);
        DataSource.testData(output, expected);
    }

    @Test
    public void match3() {

        XmlElem expected = DataSource.getOldXml();
        expected.getMetaData().setModificationType(ModificationType.NEW);

        XmlElem output = XmlElemMatcher.matchXmlElem(null, DataSource.getOldXml());
        DataSource.testData(output, expected);
    }

    @Test
    public void match4() {

        // Test with not configured test files
        XmlElem output = XmlElemMatcher.matchXmlElem(DataSource.getOldXml(), DataSource.getNewXml());

        DataSource.testData(output, DataSource.getDiffXmlNoConf());
    }

    @Test
    public void match5() {

        // Configure the test files
        FileToXmlElemConverter fileConv = new FileToXmlElemConverter();

        XmlElem oldXml = fileConv.convertToXmlElem(DataSource.PATH_FILE_XML_OLD, DataSource.PATH_FILE_XML_CONF_PRIORITY);
        XmlElem newXml = fileConv.convertToXmlElem(DataSource.PATH_FILE_XML_NEW, DataSource.PATH_FILE_XML_CONF_PRIORITY);

        // Test
        XmlElem output = XmlElemMatcher.matchXmlElem(oldXml, newXml);

        DataSource.testData(output, DataSource.getDiffXmlConf());
    }
}