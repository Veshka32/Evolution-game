
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<%java.lang.String id=request.getSession().getId(); %><br>
<div id="sessionID"><%=id%></div>
<script src="js/websocket.js"></script>


</body>
</html>
