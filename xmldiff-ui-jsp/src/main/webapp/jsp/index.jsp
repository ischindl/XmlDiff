<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:set var="pageTitle" scope="page" value="XmlDiff - compare XML files"/>
<%
    request.setAttribute("labels", new String[] {"Browse the older version of the XML file: ",
                                                 "Browse the newer version of the XML file: ",
                                                 "Browse the (optional) configuration XML file: "});

    request.setAttribute("fieldNames", new String[] {"old", "new", "conf"});
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title>${pageTitle}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-16">
        <!-- Paths are relative to the root project directory inside the server -->
        <link rel="icon" type="image/x-icon" href="img/XmlDiffLogo.ico" />
        <link rel="stylesheet" type="text/css" href="css/index.css">
        <script type="text/javascript" src="js/index.js"></script>
    </head>

    <body>

        <!-- The help button retrieves the help documentation -->
        <form action="XmlDiff" method="get" target="_blank">
            <input type="submit" id="help" name="help" value="Help" />
        </form>

        <hr /> <!-- Insert a horizontal line to visually separate the help menu from the comparison components -->

        <h3>${pageTitle}</h3> <br />

        <form action="XmlDiff" method="post" enctype="multipart/form-data" target="_blank">

            <c:forEach items="${labels}" step="1" varStatus="loop">
                <label for="${fieldNames[loop.index]}"><c:out value="${labels[loop.index]}"/></label>
                <input id="${fieldNames[loop.index]}" type="file" name="file" size="50" />
                <br />
            </c:forEach>

            <br />
            <!-- The comparison button retrieves the result of the comparison of the input files -->
            <input type="submit" id="compare" name="compare" value="Compare" />
        </form>
    </body>
</html> 