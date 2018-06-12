<%@ page import="Accounting.Model.Account" %>
<%--
  Created by IntelliJ IDEA.
  User: rogavactive
  Date: 5/29/18
  Time: 10:55 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>main page</title>
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
    <link rel="shortcut icon" href="src/favicon.ico">
    <link rel="stylesheet" href="css/main.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="css/style.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="css/animate.css"/>
    <script type="text/javascript" src="js/main.js"></script>
    <!--google font and icons-->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Lato:300">
    <%--<meta http-equiv="Refresh" content="3;url=game.jsp">--%>
    <%
        //before the code loads, checks if user is authorised.
        if(request.getSession().getAttribute("Account") ==null){
            response.sendRedirect("index.html");
            return;//this is to redirect immediately and not execute code bellow (which causes an error)
        }else{
        }
    %>
</head>
<body>
    <nav class="top-navbar-container">
        <table width="99%" align="center" class = "top-navbar-table">
            <tbody>
                <tr>
                    <td>
                        <a class="top-navbar-anchors" href="index.html"><span class="top-navbar-left">ProChess</span></a>
                    </td>
                    <td colspan="2">
                        <a class="top-navbar-anchors" href="main.jsp"><span class="top-navbar-elem">Game</span></a>
                    </td>
                    <td colspan="2">
                        <a class="top-navbar-anchors" href="main.jsp"><span class="top-navbar-elem">Friends</span></a>
                    </td>
                    <td colspan="2">
                        <a class="top-navbar-anchors" href="main.jsp"><span class="top-navbar-elem">Profile</span></a>
                    </td>
                    <td>
                        <a class="top-navbar-anchors" href="main.jsp"><span class="top-navbar-right">Logout</span></a>
                    </td>
                </tr>
            </tbody>
        </table>
    </nav>

    <%--<table>--%>
        <%--<tr>--%>
            <%--<td><p>username:</p></td>--%>
            <%--<td><p><%=((Account)request.getSession().getAttribute("Account")).getUsername() %></p></td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
            <%--<td><p>email:</p></td>--%>
            <%--<td><p><%=((Account)request.getSession().getAttribute("Account")).getEmail() %></p></td>--%>
        <%--</tr>--%>
    <%--</table>--%>
    <%--<div class="container" style="align-content: center">--%>
        <%--<p style="text-align: center">Waiting for opponent...</p>--%>
    <%--</div>--%>

    <div class="game-search-outer">
        <div class="game-search-middle">
            <div class="game-search-inner">
                <p>find game here</p>
            </div>
        </div>
    </div>
</body>
</html>
