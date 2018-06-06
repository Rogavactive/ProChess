<%@ page import="Accounting.Model.AccountManager" %>
<%@ page import="Accounting.Model.Account" %><%--
  Created by IntelliJ IDEA.
  User: rogavactive
  Date: 6/6/18
  Time: 1:56 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <!--scale to the device parameters-->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!--icon-->
    <link rel="shortcut icon" href="src/favicon.ico">
    <!--bootstrap-->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css"
          integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js"
            integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb"
            crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js"
            integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn"
            crossorigin="anonymous"></script>
    <!--bootstrap-->
    <title>Validation Required</title>
    <link rel="stylesheet" href="css/style.css" type="text/css" media="screen"/>
    <meta http-equiv="Refresh" content="5;url=main.jsp">
</head>
<body style="display: flex;">
    <div style="background-color:white; width:80%; text-align:center; margin: auto; border-radius:5px;padding:20px;font-size:20px">
        <p><%
            String code = request.getParameter("code");
            String email = request.getParameter("email");
            AccountManager manager = (AccountManager) request.getServletContext().getAttribute("AccManager");
            Account acc = manager.checkValidate(email,code);
            if(acc!=null){
                request.getSession().setAttribute("Account", acc);
                out.print("Registration successful. If not redirected automatically click <a href=\"/main.jsp\">here</a>");
            }else{
                out.print("Oops! Something went wrong!");
            }
        %></p>
    </div>
</body>
</html>
