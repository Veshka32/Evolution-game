<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="game.controller.Game" %>
<% Game game=(Game)application.getAttribute("game");%>
<%=game.playersList() %>