package com.github.alinaioanaflorea.xmldiff.core;

import com.github.alinaioanaflorea.xmldiff.core.model.ModificationType;
import com.github.alinaioanaflorea.xmldiff.core.model.XmlAttribute;
import com.github.alinaioanaflorea.xmldiff.core.model.XmlElem;
import com.github.alinaioanaflorea.xmldiff.core.service.impl.ServiceAccess;
import org.testng.Assert;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class DataSource {

    public static final String PATH_DIR_TEST = DataSource.class.getProtectionDomain().getCodeSource().getLocation().getFile();

    public static final String PATH_FILE_XML_OLD = PATH_DIR_TEST + "xml/old.xml";
    public static final String PATH_FILE_XML_NEW = PATH_DIR_TEST + "xml/new.xml";
    public static final String PATH_FILE_XML_EMPTY_ROOT = PATH_DIR_TEST + "xml/empty_root.xml";
    public static final String PATH_FILE_XML_INVALID = PATH_DIR_TEST + "xml/invalidXML.xml";
    public static final String PATH_FILE_XML_INVALID2 = PATH_DIR_TEST + "xml/invalidXML2.xml";
    public static final String PATH_FILE_XML_INVALID3 = PATH_DIR_TEST + "xml/invalidXML3.xml";
    public static final String PATH_FILE_DOC = PATH_DIR_TEST + "doc/test.txt";

    public static final String PATH_FILE_XML_CONF_EMPTY = PATH_DIR_TEST + "xml/conf_empty.xml";
    public static final String PATH_FILE_XML_CONF_EMPTY2 = PATH_DIR_TEST + "xml/conf_empty2.xml";
    public static final String PATH_FILE_XML_CONF_INVALID = PATH_DIR_TEST + "xml/conf_invalid.xml";
    public static final String PATH_FILE_XML_CONF_INVALID2 = PATH_DIR_TEST + "xml/conf_invalid2.xml";
    public static final String PATH_FILE_XML_CONF_REMOVE_ROOT = PATH_DIR_TEST + "xml/conf_remove_root.xml";
    public static final String PATH_FILE_XML_CONF_WRONG_CONF_TAG_NAME = PATH_DIR_TEST + "xml/conf_wrong_conf_tag_name.xml";
    // Configuration file for the old.xml and new.xml files, that determines what has more priority: <toKeep> or <toRemove>
    public static final String PATH_FILE_XML_CONF_PRIORITY = PATH_DIR_TEST + "xml/conf_priority.xml";

    // The difference between the old.xml and new.xml files without any configuration applied
    public static final String PATH_FILE_XML_DIFF_NO_CONF = PATH_DIR_TEST + "xml/expectedDiffXml_noConf.xml";
    //  The difference between the old.xml and new.xml files configured with xml/conf_old_priority.xml
    public static final String PATH_FILE_XML_DIFF_CONF = PATH_DIR_TEST + "xml/expectedDiffXml_priorityConf.xml";

    private static XmlElem oldXml = buildOldXml();
    private static StringBuilder oldXmlString = buildFileAsString(PATH_FILE_XML_OLD);
    private static XmlElem oldXmlConf = buildOldXmlConf();

    private static XmlElem newXml = buildNewXml();

    private static XmlElem diffXmlNoConf = buildDiffXmlNoConf();
    private static XmlElem diffXmlConf = buildDiffXmlConf();

    private DataSource() {}

    // Gets a copy of the content of the old.xml file as an XmlElem
    public static XmlElem getOldXml() {
        return oldXml.copy();
    }

    // Gets a copy of the content of the old.xml file as an XmlElem configured with PATH_FILE_XML_CONF_PRIORITY
    public static XmlElem getOldXmlConf() {
        return oldXmlConf.copy();
    }

    // Gets a copy of the content of the old.xml file as a StringBuilder
    public static StringBuilder getOldXmlString() {
        return oldXmlString;
    }

    // Gets a copy of the content of the new.xml file as an XmlElem
    public static XmlElem getNewXml() {
        return newXml.copy();
    }

    // Gets a copy of the difference of the not configured old.xml and new.xml
    public static XmlElem getDiffXmlNoConf() {
        return diffXmlNoConf.copy();
    }

    // Gets a copy of the difference of the configured old.xml and new.xml
    public static XmlElem getDiffXmlConf() {
        return diffXmlConf.copy();
    }

    public static StringBuilder buildFileAsString(String pathToFile) {

        StringBuilder output = new StringBuilder();
        BufferedReader br = null;

        try {

            String currLine;
            br = new BufferedReader(new InputStreamReader(new FileInputStream(pathToFile), "UTF-16"));

            while ((currLine = br.readLine()) != null) {
                output.append(currLine);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return output;
    }

    public static XmlElem buildOldXml() {

        // Create the root node
        XmlElem output = new XmlElem("messages", "");

        // Create the "to" & "from" nodes
        XmlElem from1 = new XmlElem("from","Jani");
        from1.setAttributes(new ArrayList<XmlAttribute>());
        from1.getAttributes().add(new XmlAttribute("sex", "male"));

        XmlElem to1 = new XmlElem("to","Elena");
        to1.setAttributes(new ArrayList<XmlAttribute>());
        to1.getAttributes().add(new XmlAttribute("sex", "female"));

        XmlElem to2 = new XmlElem("to","Jani");
        to2.setAttributes(new ArrayList<XmlAttribute>());
        to2.getAttributes().add(new XmlAttribute("sex", "male"));

        XmlElem from2 = new XmlElem("from","Elena");
        from2.setAttributes(new ArrayList<XmlAttribute>());
        from2.getAttributes().add(new XmlAttribute("sex", "female"));

        // Create a "date" node
        XmlElem date = new XmlElem("date", "");
        date.setChildXmlElems(new ArrayList<XmlElem>());
        date.getChildXmlElems().add(new XmlElem("day", "10"));
        date.getChildXmlElems().add(new XmlElem("month", "01"));
        date.getChildXmlElems().add(new XmlElem("year", "2008"));

        // Create the child nodes
        XmlElem childNode1 = new XmlElem("note","7");

        XmlElem childNode2 = new XmlElem("note", "");
        childNode2.setAttributes(new ArrayList<XmlAttribute>());
        childNode2.getAttributes().add(new XmlAttribute("id", "501"));
        childNode2.setChildXmlElems(new ArrayList<XmlElem>());
        childNode2.getChildXmlElems().add(new XmlElem("date", "10/01/2008"));
        childNode2.getChildXmlElems().add(to1);
        childNode2.getChildXmlElems().add(from1);
        childNode2.getChildXmlElems().add(new XmlElem("heading", "Reminder"));
        childNode2.getChildXmlElems().add(new XmlElem("body", "Don't forget me this weekend!"));

        XmlElem childNode3 = new XmlElem("note", "");
        childNode3.setAttributes(new ArrayList<XmlAttribute>());
        childNode3.getAttributes().add(new XmlAttribute("id", "502"));
        childNode3.setChildXmlElems(new ArrayList<XmlElem>());
        childNode3.getChildXmlElems().add(date);
        childNode3.getChildXmlElems().add(to2);
        childNode3.getChildXmlElems().add(from2);
        childNode3.getChildXmlElems().add(new XmlElem("heading", "Re: Reminder"));
        childNode3.getChildXmlElems().add(new XmlElem("body", "I will not"));

        // Add the child nodes
        output.setChildXmlElems(new ArrayList<XmlElem>());
        output.getChildXmlElems().add(childNode1);
        output.getChildXmlElems().add(childNode2);
        output.getChildXmlElems().add(childNode3);

        // Add attributes
        output.setAttributes(new ArrayList<XmlAttribute>());
        output.getAttributes().add(new XmlAttribute("noteNr", "3"));

        return output;
    }

    public static XmlElem buildOldXmlConf() {

        // Create an XmlElem configured with PATH_FILE_XML_CONF_PRIORITY
        XmlElem output = getOldXml();

        output.getMetaData().setMandatory();   // Bc of mandatory children

        // Apply the configuration: remove or mark mandatory the elements
        for (XmlElem childElem : output.getChildXmlElems())  {

            if (childElem.getChildXmlElems() != null && childElem.getChildXmlElems().size() != 0) {

                childElem.getMetaData().setMandatory();

                int nrOfGrandChildElems = childElem.getChildXmlElems().size();
                for (int i = 0; i < nrOfGrandChildElems; i++) {

                    if (i == 0) markModification(childElem.getChildXmlElems().get(i).getChildXmlElems(), ModificationType.MANDATORY);

                    if (i == 4) {

                        childElem.getChildXmlElems().remove(i);
                        continue;
                    }

                    if (i != 2) childElem.getChildXmlElems().get(i).getMetaData().setMandatory();
                }
            }
        }

        return output;
    }

    public static XmlElem buildNewXml() {

        // Create the root node
        XmlElem output = getOldXml();

        // Remove the first <note>
        output.getChildXmlElems().remove(0);

        // Change the <date> of the new first <note>
        XmlElem date = output.getChildXmlElems().get(0).getChildXmlElems().get(0);
        date.setValue("");
        date.setChildXmlElems(new ArrayList<XmlElem>());
        date.getChildXmlElems().add(new XmlElem("day", "10"));
        date.getChildXmlElems().add(new XmlElem("month", "01"));
        date.getChildXmlElems().add(new XmlElem("year", "2008"));

        // Change the <body> of the first <note>
        output.getChildXmlElems().get(0).getChildXmlElems().get(4).setValue("Don't forget me this weekend !!!");

        // Add a <report>
        XmlElem report = new XmlElem("report", "");
        report.setAttributes(new ArrayList<XmlAttribute>());
        report.getAttributes().add(new XmlAttribute("id", "503"));
        output.getChildXmlElems().add(report);

        // Change the attributes
        output.getAttributes().get(0).setValue("2");
        output.getAttributes().add(new XmlAttribute("rptNr", "1"));

        return output;
    }

    // Create the difference between the old.xml and new.xml files without they being configured
    public static XmlElem buildDiffXmlNoConf() {

        // Create the root node
        XmlElem diffXml = ServiceAccess.getFileToXmlElem(PATH_FILE_XML_DIFF_NO_CONF, null);

        diffXml.getMetaData().setChanged();

        diffXml.getChildXmlElems().get(0).getMetaData().setModificationType(ModificationType.DELETED);

        diffXml.getChildXmlElems().get(1).getMetaData().setChanged();
        markModification(diffXml.getChildXmlElems().get(1).getChildXmlElems(), ModificationType.CHANGED);
        markModification(diffXml.getChildXmlElems().get(1).getChildXmlElems().get(0).getChildXmlElems(), ModificationType.NEW);

        diffXml.getChildXmlElems().get(2).getMetaData().setModificationType(ModificationType.NEW);

        // Reposition the elements in the highest matched order:
        XmlElem elem0 = diffXml.getChildXmlElems().get(0);

        XmlElem elem11 = diffXml.getChildXmlElems().get(1).getChildXmlElems().get(1);
        XmlElem elem12 = diffXml.getChildXmlElems().get(1).getChildXmlElems().get(0);
        XmlElem elem1 = diffXml.getChildXmlElems().get(1);
        elem1.getChildXmlElems().clear();
        elem1.getChildXmlElems().add(elem11);
        elem1.getChildXmlElems().add(elem12);

        XmlElem elem2 = diffXml.getChildXmlElems().get(2);

        diffXml.getChildXmlElems().clear();
        diffXml.getChildXmlElems().add(elem1);    // changed
        diffXml.getChildXmlElems().add(elem0);    // deleted
        diffXml.getChildXmlElems().add(elem2);    // new

        return diffXml;
    }

    // Create the difference between the old.xml and new.xml files after they have been configured with PATH_FILE_XML_CONF_PRIORITY
    public static XmlElem buildDiffXmlConf() {

        // Create the root node
        XmlElem diffXml = ServiceAccess.getFileToXmlElem(PATH_FILE_XML_DIFF_CONF, null);

        diffXml.getMetaData().setChanged();

        diffXml.getChildXmlElems().get(0).getMetaData().setModificationType(ModificationType.DELETED);

        diffXml.getChildXmlElems().get(1).getMetaData().setChanged();
        markModification(diffXml.getChildXmlElems().get(1).getChildXmlElems(), ModificationType.MANDATORY);
        diffXml.getChildXmlElems().get(1).getChildXmlElems().get(0).getMetaData().setChanged();
        markModification(diffXml.getChildXmlElems().get(1).getChildXmlElems().get(0).getChildXmlElems(), ModificationType.NEW);

        diffXml.getChildXmlElems().get(2).getMetaData().setMandatory();
        markModification(diffXml.getChildXmlElems().get(2).getChildXmlElems(), ModificationType.MANDATORY);
        markModification(diffXml.getChildXmlElems().get(2).getChildXmlElems().get(0).getChildXmlElems(), ModificationType.MANDATORY);

        diffXml.getChildXmlElems().get(3).getMetaData().setModificationType(ModificationType.NEW);

        // Reposition the elements in the highest matched order:
        XmlElem elem0 = diffXml.getChildXmlElems().get(0);

        XmlElem elem11 = diffXml.getChildXmlElems().get(1).getChildXmlElems().get(1);
        XmlElem elem12 = diffXml.getChildXmlElems().get(1).getChildXmlElems().get(2);
        XmlElem elem13 = diffXml.getChildXmlElems().get(1).getChildXmlElems().get(0);
        XmlElem elem1 = diffXml.getChildXmlElems().get(1);
        elem1.getChildXmlElems().clear();
        elem1.getChildXmlElems().add(elem11);
        elem1.getChildXmlElems().add(elem12);
        elem1.getChildXmlElems().add(elem13);

        XmlElem elem2 = diffXml.getChildXmlElems().get(2);
        XmlElem elem3 = diffXml.getChildXmlElems().get(3);

        diffXml.getChildXmlElems().clear();
        diffXml.getChildXmlElems().add(elem2);     // 100% mandatory
        diffXml.getChildXmlElems().add(elem1);     // changed
        diffXml.getChildXmlElems().add(elem0);     // deleted
        diffXml.getChildXmlElems().add(elem3);     // new

        return diffXml;
    }

    private static void markModification(List<XmlElem> elemsToMark, ModificationType modification) {

        for(XmlElem elem : elemsToMark) {

            elem.getMetaData().setModificationType(modification);
        }
    }

    public static void testData(XmlElem output, XmlElem expected) {

        // Test the root name & value
        Assert.assertEquals(output.getName(), expected.getName());
        Assert.assertEquals(output.getValue(), expected.getValue());

        Assert.assertEquals(output.getMetaData().getModificationType(), expected.getMetaData().getModificationType());

        // Test the root attributes
        if (expected.getAttributes() != null && expected.getAttributes().size() != 0) {

            Assert.assertEquals(output.getAttributes().size(), expected.getAttributes().size());

            int nrOfAttrs =  expected.getAttributes().size();
            for (int i = 0; i < nrOfAttrs; i++) {

                Assert.assertEquals(output.getAttributes().get(i).getName(), expected.getAttributes().get(i).getName());
                Assert.assertEquals(output.getAttributes().get(i).getValue(), expected.getAttributes().get(i).getValue());
            }
        }

        // Test the root child elements
        if (expected.getChildXmlElems() != null && expected.getChildXmlElems().size() != 0) {

            Assert.assertEquals(output.getChildXmlElems().size(), expected.getChildXmlElems().size());

            int nrOfChildElems =  expected.getChildXmlElems().size();
            for (int i = 0; i < nrOfChildElems; i++) {

                testData(output.getChildXmlElems().get(i), expected.getChildXmlElems().get(i));
            }
        }
    }
}
