<%-- 
    Document   : courses
    Created on : 18-nov-2013, 11:21:07
    Author     : Martijn
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="resources/css/style.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="resources/bootstrap/dist/css/bootstrapm.css">
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Courses</title>
    </head>
    <div id="header">
        <div id="header_logo">
            <img src="resources/images/Logo.png">
        </div>
        <div id="header_nav">
            <ul>
                <li><a href="homepage.jsp">Home</a></li>
                <li><a href="#courses">Courses</a></li>
                    <c:if test="${loggedInIsAdmin == true}">
                    <li><a href="/Project Digital Learning/management">Management</a></li>
                    </c:if>
                <li><a href="/Project%20Digital%20Learning/profile?id=${loggedInUserId}">My Profile</a></li>
                <li>
                    <a href="#">Settings</a>
                    <ul>
                        <li><a href="#">Help</a></li>
                        <li><a href="#">Report a Problem</a></li>
                        <li><a href="index.jsp">Log Out</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
    <body>
        <h1 style="margin-left:10px">Courses</h1>
        <c:choose>
            <c:when test="${coursesSize == 0}">
                No course available
            </c:when>
            <c:otherwise>


                <c:forEach var="course" items="${courses}">
                <div class="box-course effect1">
                    <form action="enroll" id="enroll">
                        <input type="hidden" value="${course.courseId}"/>
                    <div class="box-name">${course.name}</div>  <div class="box-level">${course.level}</div>
                    <div class="box-owner">Teacher: ${course.owner.firstname} ${course.owner.lastname}</div>
                    <div class="box-owner">Skills: </div>
                    <br>
                    <img class="box-image" src="http://findready.com/adminCP/graphics/NotAvailable.JPG"/>
                    <br>Description: <br> ${course.description}
                    <div class="box-level"></div>
                    <br>
                   <ul style="text-align: right; margin-right:20px;">
                       <li><button type="submit" class="btn btn-primary" action="enroll" style="text-align: right;">Enroll</button></li>
                   </ul>
                    
                  <!--<div class="box-button"><button type="button" class="btn btn-default" >Enroll</button></div>-->

                </div>
            </form>
            </c:forEach>

            </c:otherwise>
        </c:choose> 

    </body>
</html>
