<%--
  Created by IntelliJ IDEA.
  User: dimit
  Date: 07/12/18
  Time: 10:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%
        String gameID = request.getParameter("id");
        if(gameID==null){
            response.sendRedirect("profile.jsp");
            return;
        }
    %>
    <title>Game <%=gameID%></title>
</head>
<body>

</body>
</html>
