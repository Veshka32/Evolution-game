<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="model.Game" %>
<html>
<head><script type="text/javascript" src="JS/jquery-1.4.2.min.js"></script>
      <script type="text/javascript">
           var auto = setInterval(    function ()
           {
                $('#moves').load('views/reload-window.jsp').fadeIn("slow");
           }, 5000); // refresh every 5000 milliseconds
      </script>
      </head>
<body onload="getMessages();">
<h2>Active game</h2>
${message}<br>
<% Game game=(Game)application.getAttribute("game");%>

Active players: <%=game.playersList() %><br>
Your session ID: <%=request.getSession().getId() %></br>

<div id="moves">Moves so far: <%=game.printMoves() %></div>


<form method="post" action="/move">
<label>Enter you move:
<input type="text" name="move"><br />
</label>
<button type="submit">Move!</button>
</form>

</body>
</html>
