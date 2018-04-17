<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="model.Game" %>
<% Game game=(Game)application.getAttribute("game");%>
Moves so far: <%=game.printMoves() %>