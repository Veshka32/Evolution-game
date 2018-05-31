<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="game.controller.GameHandler" %>

<html>
<body>
<h1>Welcome, <%=session.getAttribute("player")%>
</h1><br>

Games you can join: <span><%=((GameHandler)application.getAttribute("gameHandler")).getAllGames() %></span><br/>


${message}<br>
<form method="post" action="start">
    Input number of game:<input type="text" name="gameId"><button type="submit">Join game</button>
</form>

<form action="create">
    <input type="submit" value="Create game"/>
</form>

<form action="signOut">
    <input type="submit" value="Sign Out"/>
</form>
</body>
</html>
