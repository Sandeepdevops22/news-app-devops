<html>
<head>
    <meta http-equiv="refresh" content="10">
</head>
<body>
    <h2>Today's News (India) - <%= request.getAttribute("date") %></h2>

    <ul>
        <%
            java.util.List<String> headlines = 
                (java.util.List<String>) request.getAttribute("headlines");

            for (String h : headlines) {
        %>
            <li><%= h %></li>
        <%
            }
        %>
    </ul>

</body>
</html>
