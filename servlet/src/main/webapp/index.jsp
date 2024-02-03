<%--/**--%>
<%--* Author: George Saito (takatoms)--%>
<%--* Last Modified: November 28th, 2022--%>
<%--*--%>
<%--* This is the dashboard web page jsp file--%>
<%--*/--%>
<%@ page import="java.lang.reflect.Field" %>
<%@ page import="ds.UserLog" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<%--Analytics part--%>
<div class="container">
    <h2><%= "Analytics" %></h2>
    <br/>
    <h3>Number of Requests Received: <%= request.getAttribute("totalRequest")%></h3>
    <h3>Top Searched Word: <%= request.getAttribute("topSearchedWord") %></h3>
    <h3>Most Common Device: <%= request.getAttribute("mostCommonDevice") %></h3>
    <h3>Average Search Time Spent(ms): <%= request.getAttribute("averageLatency")%></h3>
</div>


<br/>
<%--Log table--%>
<div class="container">
    <h2><%= "Logs"%></h2>
    <table class="table">
        <thead>
            <tr>
                <th>ID</th>
                <th>search word</th>
                <th>timestamp</th>
                <th>mobile model</th>
                <th>num result found</th>
                <th>time spent (ms)</th>
                <th>first API request</th>
                <th>second API request</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<UserLog> userLogs = (List<UserLog>) request.getAttribute("userLogs");
                for (UserLog userLog : userLogs) {
            %>
                    <tr>
                        <td><%= userLog.getId() %></td>
                        <td><%= userLog.getSearchWord() %></td>
                        <td><%= userLog.getTimeStamp() %></td>
                        <td><%= userLog.getMobileModel() %></td>
                        <td><%= userLog.getNumResultFound() %></td>
                        <td><%= userLog.getSearchTimeSpent() %></td>
                        <td><%= userLog.getFirstApiRequest() %></td>
                        <td><%= userLog.getSecondApiRequest() %></td>
                    </tr>
            <%
                }
            %>
        </tbody>
    </table>
</div>
</body>
</html>