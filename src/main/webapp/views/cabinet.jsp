<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="game.controller.GameManager" %>
<%@ page import="java.lang.String" %>

<html>
<body>
<h1>Welcome, <%=session.getAttribute("player")%>
</h1><br>

<% GameManager gh=(GameManager)application.getAttribute("gameManager");
 String id;
 String name=(String)session.getAttribute("player");
 if (session.getAttribute("gameId")==null ) id="no game";
 else id=String.valueOf(session.getAttribute("gameId"));%>

Your current game: <span><%=id%></span><br/>
Games you participate:<span><%=gh.getUserGames(name)%></span><br/>
Games you can join: <span><%=gh.getNewGames(name) %></span><br/>

${message}<br>
<form method="post" action="start">
    Input number of game:<input type="text" name="gameId"><button type="submit">Join game</button>
</form>

${createError}<br/>
<form action="create">
    <input type="submit" value="Create new game"/>
</form>

<form action="signOut">
    <input type="submit" value="Sign Out"/>
</form>
</body>
</html>
