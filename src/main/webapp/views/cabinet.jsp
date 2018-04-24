<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Game" %>

<html>
<body>
<h1>Welcome, <%=session.getAttribute("player")%>
</h1><br>

<% Game game = (Game) application.getAttribute("game"); %>

Active players: <span><%=game.playersList() %></span><br/>

${message}<br>
<form action="start">
    <input type="submit" value="Join game"/>
</form>
</body>
</html>
