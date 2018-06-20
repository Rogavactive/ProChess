<%--
  Created by IntelliJ IDEA.
  User: dimit
  Date: 06/20/18
  Time: 00:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Logout</title>
    <%
        if(request.getSession().getAttribute("Account")!=null)
            request.getSession().removeAttribute("Account");
        //Remove game here also.
        response.sendRedirect("index.html");
    %>
</head>
<body>
</body>
</html>
