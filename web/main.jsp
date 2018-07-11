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
        if(request.getSession().getAttribute("Account") == null){
            response.sendRedirect("index.html");
            return;//this is to redirect immediately and not execute code bellow (which causes an error)
        }
        String id = request.getParameter("id");
        if(id==null)
            id = "0";
    %>
    <script>
        var opponentID = <%=id%>;
    </script>
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
                String gameid = (String) request.getSession().getAttribute("gameID");
                if(gameid!=null)
                    out.print("?id="+gameid);
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

            <%--<td><p><%=((Account)request.getSession().getAttribute("Account")).getUsername() %></p></td>--%>
            <%--<td><p><%=((Account)request.getSession().getAttribute("Account")).getEmail() %></p></td>--%>

    <div class="game-search-outer">
        <div class="game-search-middle">
            <div class="game-search-inner">
                <p>Choose game type:</p>
                <div style="align-items: center">
                    <select class="choose-type" id="choose-type"> <!--Supplement an id here instead of using 'text'-->
                        <option value="" selected disabled hidden >Choose type</option>
                        <option value="0">Random Opponent</option>
                        <option value="1">Friend</option>
                        <option value="2">Single Play</option>
                    </select>
                </div>
                <p>Choose time:</p>
                <select class="main-time" id="main-time">
                    <option value="" selected disabled hidden >Choose time</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="5">5</option>
                    <option value="10">10</option>
                    <option value="15">15</option>
                </select>
                <p style="display:inline-block; width:3%">+</p>
                <select class="bonus-time" id="bonus-time">
                    <option value="" selected disabled hidden >Bonus time</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="5">5</option>
                    <option value="10">10</option>
                </select>
                <input class="link-holder" id="link-holder" placeholder="Link" type="text" readonly>
                <button onclick="CopyLink()" class="copy-icon-btn" id="copy-btn" disabled>
                    <i class="material-icons copy-icon-custom">file_copy</i>
                </button>
                <button id="search-btn" onclick="SearchRequest()">Search</button>
                <p class="find-game">Waiting for opponent...</p>
                <p class="find-game" id = "find-game-timer">00:00</p>
            </div>
        </div>
    </div>
</body>
</html>
