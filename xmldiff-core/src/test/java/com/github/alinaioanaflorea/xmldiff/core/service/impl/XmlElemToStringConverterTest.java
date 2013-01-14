package com.github.alinaioanaflorea.xmldiff.core.service.impl;

import com.github.alinaioanaflorea.xmldiff.core.DataSource;
import org.testng.Assert;
import org.testng.annotations.Test;

public class XmlElemToStringConverterTest {

    @Test
    public void testProcess() {

        Assert.assertEquals(XmlElemToStringConverter.convertToString(null), null);
    }

    @Test
    public void testProcess2() {

        StringBuilder expected = DataSource.getOldXmlString();
        StringBuilder output = XmlElemToStringConverter.convertToString(DataSource.getOldXml());

        output.replace(0, output.length(), output.toString().replaceAll("\\s", ""));        // "\s" = whitespaces
        expected.replace(0, expected.length(), expected.toString().replaceAll("\\s", ""));;

        Assert.assertTrue(output.toString().equals(expected.toString()));
    }
}