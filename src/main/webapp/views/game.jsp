<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="model.Game" %>
<html>
<body>
<h2>Active game</h2>
${message}<br>
<% Game game=(Game)application.getAttribute("game");%>

Active players: <%=game.playersList() %><br>
Your session ID: <%=request.getSession().getId() %>


</body>
</html>
