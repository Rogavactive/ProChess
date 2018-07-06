<%@ page import="Accounting.Model.Account" %><%--
  Created by IntelliJ IDEA.
  User: rogavactive
  Date: 6/7/18
  Time: 5:06 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<html lang="en">
<head>
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
    <link rel="stylesheet" href="css/game.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="css/style.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="css/animate.css"/>
    <script type="text/javascript" src="js/game.js"></script>
    <!--google-->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Lato:300">
    <%
        String link_id = (String)request.getParameter("id");
        if(link_id==null){
            response.sendRedirect("main.jsp");
            return;
        }
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
                if(id==null||!id.equals(link_id)){
                    //spectator here. before it just redirect.
                    response.sendRedirect("main.jsp");
                    return;
                }
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

<div class="div-main-container">
    <div class="chat-div">
        <div id = "chatBox" class="chat-box" ></div>
        <form onsubmit="chatBtnClick(); return false;" autocomplete="off">
            <input class="chat-message-input" type="text" id="chatMessage" size="50">
            <input class="chat-message-btn" type="submit" value="Send">
        </form>
    </div>
    <div class="chess-board-container-div">
        <div class="container">



            <div class="contentPane">

                <table id="chess_board" cellpadding="0" cellspacing="0">
                    <tr>
                        <td id="70"></td>
                        <td id="71"></td>
                        <td id="72"></td>
                        <td id="73"></td>
                        <td id="74"></td>
                        <td id="75"></td>
                        <td id="76"></td>
                        <td id="77"></td>
                    </tr>
                    <tr>
                        <td id="60"></td>
                        <td id="61"></td>
                        <td id="62"></td>
                        <td id="63"></td>
                        <td id="64"></td>
                        <td id="65"></td>
                        <td id="66"></td>
                        <td id="67"></td>
                    </tr>
                    <tr>
                        <td id="50"></td>
                        <td id="51"></td>
                        <td id="52"></td>
                        <td id="53"></td>
                        <td id="54"></td>
                        <td id="55"></td>
                        <td id="56"></td>
                        <td id="57"></td>
                    </tr>
                    <tr>
                        <td id="40"></td>
                        <td id="41"></td>
                        <td id="42"></td>
                        <td id="43"></td>
                        <td id="44"></td>
                        <td id="45"></td>
                        <td id="46"></td>
                        <td id="47"></td>
                    </tr>
                    <tr>
                        <td id="30"></td>
                        <td id="31"></td>
                        <td id="32"></td>
                        <td id="33"></td>
                        <td id="34"></td>
                        <td id="35"></td>
                        <td id="36"></td>
                        <td id="37"></td>
                    </tr>
                    <tr>
                        <td id="20"></td>
                        <td id="21"></td>
                        <td id="22"></td>
                        <td id="23"></td>
                        <td id="24"></td>
                        <td id="25"></td>
                        <td id="26"></td>
                        <td id="27"></td>
                    </tr>
                    <tr>
                        <td id="10"></td>
                        <td id="11"></td>
                        <td id="12"></td>
                        <td id="13"></td>
                        <td id="14"></td>
                        <td id="15"></td>
                        <td id="16"></td>
                        <td id="17"></td>
                    </tr>
                    <tr>
                        <td id="00" ></td>
                        <td id="01"></td>
                        <td id="02"></td>
                        <td id="03"></td>
                        <td id="04"></td>
                        <td id="05"></td>
                        <td id="06"></td>
                        <td id="07"></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
