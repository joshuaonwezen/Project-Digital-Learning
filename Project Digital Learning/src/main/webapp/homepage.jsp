<%-- 
    Document   : homepage
    Created on : Nov 11, 2013, 2:25:25 PM
    Author     : wesley
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="Shortcut Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
<link rel="Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="resources/css/style.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home - Info Support</title>
    </head>
    <body>
        <div class="Header">
        <ul>
            <li><a class="button" href="homepage.jsp">Home</a></li>
            <li><a class="button" href="#profile">My Profile</a></li>
            <li><a class="button" href="#courses">Courses</a></li>
             <c:if test="${loggedInIsAdmin == true}">
            <li><a class="button" href="/Project Digital Learning/management">Management</a></li>
             </c:if>
            <li><a class="button" href="index.jsp">LogOut</a></li>
        </ul>
        </div>
        <h1>Homepage Navigation</h1>
        <!-- show admin buttons -->
            <br><br>
            Logged in as: ${loggedInUsername}




    </body>
</html>
