<%-- 
    Document   : courses
    Created on : 18-nov-2013, 11:21:07
    Author     : Martijn
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="resources/css/style.css" rel="stylesheet" type="text/css">
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Courses</title>
    </head>
    <body>
        <h1>Courses</h1>
        <c:choose>
            <c:when test="${coursesSize == 0}">
                No course available
            </c:when>
            <c:otherwise>


                <c:forEach var="course" items="${courses}">
                    <div class="box-course effect1">

                        <div class="box-name">${course.name}</div>  <div class="box-level">${course.level}</div>
                        <div class="box-owner">Teacher: ${course.owner.firstname} ${course.owner.lastname}</div>
                        <li><div class="box-image"><img src="/ímages/NoImageAvailable"/></div></li>
                        <p>Description: <br> ${course.description}</p>
                        <div class="box-level"></div>

                        <li><a class="button" href="#">Enroll</a></li>

                    </div>
                </c:forEach>

            </c:otherwise>
        </c:choose> 

    </body>
</html>
