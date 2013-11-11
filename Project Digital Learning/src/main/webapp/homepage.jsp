<%-- 
    Document   : homepage
    Created on : Nov 11, 2013, 2:25:25 PM
    Author     : wesley
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Homepage</title>
    </head>
    <body>
        <h1>Homepage Navigation</h1>
        <!-- show admin buttons -->
        <c:if test="${isAdmin == true}">
            <a href="/Project Digital Learning/users">User management</a></br>
        </c:if>
            <a href="#">Courses</a></br>
            <a href="#">Usershit</a></br>






    </body>
</html>
