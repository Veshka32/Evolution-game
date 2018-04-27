<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Game" %>

<html>
<body>
<h1>Welcome, <%=session.getAttribute("player")%>
</h1><br>

Active players: <span><%=((Game)session.getAttribute("game")).getAllPlayers() %></span><br/>

${message}<br>
<form action="start">
    <input type="submit" value="Join game"/>
</form>
</body>
</html>
