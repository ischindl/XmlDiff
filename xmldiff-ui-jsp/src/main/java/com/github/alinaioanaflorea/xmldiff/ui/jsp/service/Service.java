package com.github.alinaioanaflorea.xmldiff.ui.jsp.service;

import javax.servlet.http.HttpServletRequest;

/**
 * This class is a template that forces the its implementations <br />
 * to provide all the data needed to be displayed in the output page.
 */
public abstract class Service {

    private static final String PAGE_TITLE = "title";
    private static final String PAGE_CONTENT = "content";

    protected HttpServletRequest request = null;

    public Service(HttpServletRequest request) {
        this.request = request;
    }

    public final void process() {

        request.setAttribute(PAGE_TITLE, getWindowTitle());
        request.setAttribute(PAGE_CONTENT, getWindowContent());
    }

    protected abstract String getWindowTitle();
    protected abstract StringBuilder getWindowContent();
}
