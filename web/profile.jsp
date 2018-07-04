<%--
  Created by IntelliJ IDEA.
  User: paranoid
  Date: 7/4/18
  Time: 7:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ page import="Accounting.Model.Account" %>
    <title>Pro Chess</title>
    <%
        Account acc = (Account)request.getSession().getAttribute("Account");
        if (acc == null) {
            response.sendRedirect("index.html");
            return ;
        }
        int rating = acc.getDefaultRaiting();
    %>
</head>
<body>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
    <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>

    <div class="ratings">
        <h1>User Ratings</h1>
        <div class="bullets" title="Very fast games: less than 3 minutes">
            <h3>Bullet</h3>
            <span class="rating">
                <strong>X(rating)</strong>
                <span>X(num games) games</span>
                <span class="progress"></span>
            </span>
        </div>



    </div>

</body>
</html>
