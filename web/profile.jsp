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

        int bulletRating = acc.getBulletRaiting();
        int blitzRating = acc.getBlitzRaiting();
        int classicalRating = acc.getClassicalRaiting();

        int bulletMatches = acc.getBulletMatches();
        int blitzMatches = acc.getBlitzMatches();
        int classicalMatches = acc.getClassicalMatches();
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
                <a class="top-navbar-anchors" href="puzzles.jsp"><span class="top-navbar-elem">Puzzles</span></a>
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

    <div class="profile-top-inner-coontainer">
        <h1 align="center">Hello, dear <%=username%>!</h1>
    </div>

    <hr>

    <div class="ratings-container" >
        <h2 align="center">Your game statistics</h2>
    </div>

    <table width="99%" align="center" class="profile-stat-table">
        <tr>
            <td>
                <p>Type</p>
            </td>
            <td>
                <p>Rating</p>
            </td>
            <td>
                <p>Games</p>
            </td>
        </tr>
        <tr>
            <td>
                <p>Bullet</p>
            </td>
            <td>
                <p><%=bulletRating%></p>
            </td>
            <td>
                <p><%=blitzMatches%></p>
            </td>
        </tr>
        <tr>
            <td>
                <p>Blitz</p>
            </td>
            <td>
                <p><%=blitzRating%></p>
            </td>
            <td>
                <p><%=blitzMatches%></p>
            </td>
        </tr>
        <tr>
            <td>
                <p>Classical</p>
            </td>
            <td>
                <p><%=classicalRating%></p>
            </td>
            <td>
                <p><%=classicalMatches%></p>
            </td>
        </tr>
    </table>

    <div class="profile-information-container">
        <i class="material-icons material-icons-custom">account_circle</i>
        <p class="profile-info-custom">Username: <%= username %> </p>
        <br>
        <i class="material-icons material-icons-custom">email</i>
        <p class="profile-info-custom">Email: <%= email %> </p>
        <br>
        <i class="material-icons material-icons-custom">account_circle</i>
        <p class="profile-info-custom">Account type: <%
            if(acc.type())
                out.print("Native.");
            else
                out.print("Google.");
        %> </p>
        <br>
        <div align="center">
            <button onclick="ChangeRedirect()" class="change-btn" id="change-btn">
                <i class="material-icons copy-icon-custom">build</i> Change
            </button>
        </div>
    </div>

    <hr>

    <div>
        <h2 align="center">Last 10 games</h2>
        <ul>
            <li>
                <p>Game 1 here</p>
            </li>
            <li>
                <p>Game 2 here</p>
            </li>
        </ul>
        </ul>
    </div>

</div>

</body>

</html>