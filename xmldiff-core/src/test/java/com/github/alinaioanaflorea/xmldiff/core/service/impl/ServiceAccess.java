package com.github.alinaioanaflorea.xmldiff.core.service.impl;

import com.github.alinaioanaflorea.xmldiff.core.model.XmlElem;

public class ServiceAccess {

    public static XmlElem getFileToXmlElem(String pathToFile, String pathToConfFile) {

        return new FileToXmlElemConverter().convertToXmlElem(pathToFile, pathToConfFile);
    }
}
