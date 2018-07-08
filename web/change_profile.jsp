<%--
  Created by IntelliJ IDEA.
  User: dimit
  Date: 07/07/18
  Time: 18:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title>Pro Chess</title>
    <meta charset="utf-8">
    <title>Pro Chess</title>
    <!--scale to the device parameters-->
    <meta name="viewport" content="width=device-width, initial-scale=1">
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
    <!--icon-->
    <link rel="shortcut icon" href="src/favicon.ico">
    <!--my styles-->
    <link rel="stylesheet" href="css/profile.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="css/style.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="css/animate.css"/>
    <script type="text/javascript" src="js/profile.js"></script>
    <!--google-->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Lato:300">


    <%@ page import="Accounting.Model.Account" %>
    <%
        Account acc = (Account)request.getSession().getAttribute("Account");
        if (acc == null) {
            response.sendRedirect("index.html");
            return ;
        }

        String username = acc.getUsername();
        String email = acc.getEmail();

    %>
</head>

<body>

<nav class="top-navbar-container">
    <table width="99%" align="center" class = "top-navbar-table">
        <tbody>
        <tr>
            <td>
                <a class="top-navbar-anchors" href="main.jsp"><span class="top-navbar-left">ProChess</span></a>
            </td>
            <td colspan="2">
                <a class="top-navbar-anchors" href="game.jsp<%
                String id = (String) request.getSession().getAttribute("gameID");
                if(id!=null)
                    out.print("?id="+id);
                %>">
                    <span class="top-navbar-elem">Game</span>
                </a>
            </td>
            <td colspan="2">
                <a class="top-navbar-anchors" href="main.jsp"><span class="top-navbar-elem">Puzzles</span></a>
            </td>
            <td colspan="2">
                <a class="top-navbar-anchors" href="profile.jsp"><span class="top-navbar-elem">Profile</span></a>
            </td>
            <td>
                <a class="top-navbar-anchors" href="logout.jsp"><span class="top-navbar-right">Logout</span></a>
            </td>
        </tr>
        </tbody>
    </table>
</nav>


<div class="profile-outer-container" align="left">

    <h1 align="center">Modify Your profile.</h1>
    <div class="essential-info-container" align="center">

        <i class="material-icons material-icons-custom">account_circle</i>
        <input id="username-input" placeholder="Username(min:8)" value="<%=username%>" type="text" maxlength="20">
        <br>
        <i class="material-icons material-icons-custom">email</i>
        <input id="email-input" placeholder="Email" value="<%=email%>" type="text">
        <br>
        <button onclick="ChangeEssential()" class="change-btn" id="change-btn">
            <i class="material-icons copy-icon-custom">build</i> Change
        </button>

    </div>

    <hr>

    <div class="pass-info-container" align="center">
        <h1 align="center"><%
            if(acc.type())
                out.print("Change your password");
            else
                out.print("Set your password.");
        %></h1>

        <%
            if(acc.type()) {
                out.print("<i class=\"material-icons material-icons-custom register-icons\">lock</i>\n");
                out.print("<input id=\"old-password-input\" autocomplete=\"new-password\" placeholder=\"Old Password\" type=\"password\" maxlength=\"20\">");
            }
        %>
        <br>
        <i class="material-icons material-icons-custom register-icons">lock</i>
        <input id="new-password-input" autocomplete="new-password" placeholder="New Password" type="password" maxlength="20">
        <br>
        <i class="material-icons material-icons-custom register-icons">lock</i>
        <input id="repeat-password-input" autocomplete="new-password" placeholder="Repeat Password" type="password" maxlength="20">
        <button onclick="<%
            if(acc.type())
                out.print("changePass()");
            else
                out.print("setPass()");
        %>" class="change-btn" id="pass-change-btn">
            <i class="material-icons copy-icon-custom">build</i> <%
                if(acc.type())
                    out.print("Change");
                else
                    out.print("Set");
            %>
        </button>

    </div>

</div>

</body>

</html>