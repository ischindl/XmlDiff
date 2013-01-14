<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title>${title}</title>
        <link rel="icon" type="image/x-icon" href="img/XmlDiffLogo.ico" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-16">
    </head>
    <body>
        <pre>${fn:escapeXml(content)}</pre>
    </body>
</html>