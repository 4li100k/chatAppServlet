<%--
  Created by IntelliJ IDEA.
  User: :))))))
  Date: 11/19/2017
  Time: 10:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>chat: ${username}</title>
    <link rel="stylesheet" type="text/css" href="chatStyle.css">
</head>
<body>
<h1>
    HELLO, ${username}!
</h1>
<p id="states">readyState: </p>
    <table>
        <tr>
            <td class="tops">
                Message:
            </td>
            <td class="tops">
                <input type="text" name="msg" id="msg" autocomplete="off" onkeydown="if (event.keyCode == 13)document.getElementById('btn').click()" />
            </td>
            <td class="tops">
                <input type="submit" value="Send" id="btn" onclick="postMessage();"/>
            </td>
            <td>
                <div id="message">
                    <% if(application.getAttribute("message")!=null){%>
                    <%=application.getAttribute("message")%>
                    <%}%>
                </div>
            </td>
        </tr>
    </table>
<script src="index.js"></script>
<%--<script>getMessage()</script>--%>
</body>
</html>