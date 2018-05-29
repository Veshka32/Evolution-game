<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

<div id="players">${playersList}</div><br>

Your session ID: <%=request.getSession().getId() %></br>

<form action="http://172.16.172.71:8080/evo/socket.html">
    <input type="submit" value="Start game" />
</form>
${message}

</body>
</html>
