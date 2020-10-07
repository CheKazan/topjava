<%--
  Created by IntelliJ IDEA.
  User: Artur
  Date: 07.10.2020
  Time: 13:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<html>
<head>
    <title>Meals table</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<table border="1">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach items="${mealsTable}" var="meal">
        <tr>
            <td style="color: <c:out value="${meal.excess ? '#FF0000' : '#008000'}" />">${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}</td>
            <td style="color: <c:out value="${meal.excess ? '#FF0000' : '#008000'}" />">${meal.description}</td>
            <td style="color: <c:out value="${meal.excess ? '#FF0000' : '#008000'}" />">${meal.calories}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
