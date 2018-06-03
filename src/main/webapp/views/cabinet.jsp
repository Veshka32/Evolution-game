<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="game.controller.GameHandler" %>
<%@ page import="java.lang.Integer" %>

<html>
<body>
<h1>Welcome, <%=session.getAttribute("player")%>
</h1><br>

<% GameHandler gh=(GameHandler)application.getAttribute("gameHandler");
String id=String.valueOf(session.getAttribute("gameId"));
if (id==null) id="no game";%>

Your current game: <span><%=id%></span><br/>
Games you participate:<span><%=gh.getUserGames((String)session.getAttribute("player"))%></span><br/>
Games you can join: <span><%=gh.getNewGames() %></span><br/>

${message}<br>
<form method="post" action="start">
    Input number of game:<input type="text" name="gameId"><button type="submit">Join game</button>
</form>

<form action="create">
    <input type="submit" value="Create new game"/>
</form>

<form action="signOut">
    <input type="submit" value="Sign Out"/>
</form>
</body>
</html>
