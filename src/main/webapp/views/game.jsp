<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="model.Game" %>
<html>
<head></head>
<script type="text/javascript" src="JS/jquery-1.4.2.min.js"></script>
      <script type="text/javascript">
           var auto = setInterval(    function ()
           {
                $('#players').load('views/reload-window.jsp').fadeIn("slow");
           }, 5000); // refresh every 5000 milliseconds
      </script>
<body>
<h1>${message}</h1><br>
<h2>Active players</h2><br>
<% Game game=(Game)application.getAttribute("game");%>
<div id="players"><%=game.playersList() %></div><br>

Your session ID: <%=request.getSession().getId() %></br>

<button onclick="location.href='/socket.html'">Start game</button>

</body>
</html>
