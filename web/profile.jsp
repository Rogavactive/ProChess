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
    <title>Pro Chess</title>

    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
    <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>

    <%@ page import="Accounting.Model.Account" %>
    <%
        Account acc = (Account)request.getSession().getAttribute("Account");
        if (acc == null) {
            response.sendRedirect("index.html");
            return ;
        }

        String username = acc.getUsername();
        String email = acc.getEmail();

        int bulletRating = acc.getBulletRaiting();
        int blitzRating = acc.getBlitzRaiting();
        int classicalRating = acc.getClassicalRaiting();

        int bulletMatches = acc.getBulletMatches();
        int blitzMatches = acc.getBlitzMatches();
        int classicalMatches = acc.getClassicalMatches();
    %>
</head>

<body>
<div class="ratings" align="left">
    <h1>User Ratings</h1>

    <div class="bullet" title="Very fast games: less than 3 minutes">
        <h3>Bullet</h3>
        <strong> <h2> <%= bulletRating %> </h2> </strong>
        <h5> <%= bulletMatches%> games</h5>
    </div>

    <div class="blitz" title="Fast games: 3 to 8 minutes">
        <h3>Blitz</h3>
        <strong> <h2> <%= blitzRating %> </h2> </strong>
        <h5> <%= blitzMatches%> games</h5>
    </div>

    <div class="classical" title="Long games: more than 8 minutes">
        <h3>Classical</h3>
        <strong> <h2> <%= classicalRating %> </h2> </strong>
        <h5> <%= classicalMatches%> games</h5>
    </div>
</div>

<div class="information" align="center">
    <h3> <%= username %> </h3>
    <h3> <%= email %> </h3>
</div>

</body>

</html>