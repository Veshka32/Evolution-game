<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Game" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
<body>
<h2>Active game</h2>
<% Game game=(Game)application.getAttribute("game");%>

Active players: <%=game.playersList() %><br>
Your session ID: <%=request.getSession().getId() %><br>
Moves so far: <%=game.printMoves() %><br>

<script src="https://code.jquery.com/jquery-1.10.2.js"
	type="text/javascript"></script>
<script src="js/app-ajax.js" type="text/javascript"></script>
<body>


</body>
</html>

