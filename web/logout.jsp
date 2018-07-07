<%@ page import="Game.Model.GameManager" %>
<%@ page import="Accounting.Model.Account" %>
<%@ page import="Game.Model.Game" %>
<%@ page import="java.sql.SQLException" %>
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
        Account acc = (Account) request.getSession().getAttribute("Account");
        if(acc==null) {
            response.sendRedirect("index.html");
            return;
        }
        if(request.getSession().getAttribute("gameID")!=null){
            String gameID = (String) request.getSession().getAttribute("gameID");
            GameManager manager = (GameManager) request.getServletContext().getAttribute("GameManager");
            Game game = manager.getGameByID(gameID);
            try {
                game.leaveGame(acc.getID());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            request.getSession().removeAttribute("gameID");
        }
        request.getSession().removeAttribute("Account");
        response.sendRedirect("index.html");
    %>
</head>
<body>
</body>
</html>
