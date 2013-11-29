<%-- 
    Document   : courses
    Created on : 18-nov-2013, 11:21:07
    Author     : Martijn
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="index_nl_NL" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Bootstrap-->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="resources/bootstrap/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="resources/css/style.css">
        <link rel="stylesheet" href="resources/bootstrap/dist/css/bootstrap.min.css">
        <script src="resources/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="resources/bootstrap/dist/js/alert.js"></script>
        <!-- Company Style -->
<!--        <link rel="Shortcut Icon" href="resources/images/favicon.ico" type="image/x-icon">
        <link rel="Icon" href="resources/images/favicon.ico" type="image/x-icon">
        <link rel="stylesheet" type="text/css" href="resources/css/style.css">
        <title>Courses</title>
    </head>
    <body>
        <!--Header-->
                <div id="header">
            <div id="header_logo">
                <img src="resources/images/Logo.png">
            </div>
            <div id="header_nav">
                <ul>
                    <li><a href="homepage.jsp">Home</a></li>
                    <li><a href="/PDL/courses"><fmt:message key="navbar.course"/></a></li>
                        <c:if test="${loggedInIsAdmin == true}">
                        <li><a href="/PDL/management">Management</a></li>
                        </c:if>
                        <li><a href="/PDL/profile?id=${loggedInUserId}"><fmt:message key="navbar.profile"/></a></li>
                    <li>
                        <a href="#"><fmt:message key="navbar.settings"/></a>
                        <ul>
                            <li><a href="#">Help</a></li>
                            <li><a href="#"><fmt:message key="navbar.problem"/></a></li>
                            <li><a href="index.jsp"><fmt:message key="navbar.logout"/></a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
        <!--eof header-->
        <form class="navbar-form navbar-left" role="search" id="searchCourse" action="searchCourse">
            <div class="form-group">
                <input type="text" name="searchQuery" id="searchQuery" class="form-control" placeholder="Search Course">
            </div>
            <button type="submit" class="btn btn-default">Search</button>
        </form>
        </br></br></br></br>


        <div id="validationAlert" style="margin-left:20px;margin-right:20px"></div>
        <c:if test="${enrolledIn != null}">
            <div class="alert alert-success" style="margin-left:20px;margin-right:20px">
                <a class="close" data-dismiss="alert">×</a>
                <strong><fmt:message key="popup.done"/></strong> <fmt:message key="course.enroll.succes"/> "${enrolledIn}"
            </div>
        </c:if>
        
        <script>
            var coursesSize = '${coursesSize}';
        </script>
        <c:choose>
            <c:when test="${coursesSize == 0}">
            </c:when>
            <c:otherwise>
                <c:forEach var="course" items="${courses}">
                    <form action="enroll" id="enroll">
                        <input type="hidden" id="courseId" name="courseId" value="${course.courseId}"
                               <c:if test="${course.isVisible}">
                                   <div class="row">
                                       <div class=".col-md-6 .col-md-offset-3" style="margin-left:200px;margin-right:200px">
                                       <div class="thumbnail" >
                                           <div class="caption">
                                               <div style="float:right">
                                                   <small>
                                                       hier moeten de skills nog worden opgesomd
                                                   </small>
                                               </div>
                                               <h3>${course.name} ${course.level}</h3>
                                               <h4><small><fmt:message key="course.teacher"/> ${course.owner.firstname} ${course.owner.lastname}</small></h4>
                                               <p>${course.description}</p>
                                               <button id="buttonCourseEnroll${course.courseId}" type="submit" class="btn btn-primary"><fmt:message key="course.enroll"/></buton>
                                                   <button id="buttonCourseOpen${course.courseId}" type="button" class="btn btn-success"><fmt:message key="course.go"/></buton>
                                                       <c:choose>
                                                           <c:when test="${userEnrolledCoursesSize != 0}">
                                                               <script>var found = false;</script>
                                                               <c:forEach var="temp" items="${userEnrolledCourses}">
                                                                   <c:if test="${temp == course.courseId}">
                                                                       <script>
                                                                           found = true;
                                                                       </script>
                                                                   </c:if>

                                                               </c:forEach>
                                                               <script>
                                                                   if (found) {
                                                                       document.getElementById('buttonCourseEnroll${course.courseId}').style.display = 'none';
                                                                   }
                                                                   else {
                                                                       document.getElementById('buttonCourseOpen${course.courseId}').style.display = 'none';
                                                                   }
                                                               </script>
                                                           </c:when>
                                                       </c:choose>
                                                       </div>
                                                       </div>
                                                       </div>
                                                       </div>
                                                       </br>

                                                   </c:if>

                                                   </form>
                                                   <script>
                                                       console.log('the cours ei siil');
                        if (${!course.isVisible}) {
                            coursesSize = coursesSize - 1;
                        }
                                                   </script>
                                               </c:forEach>

                                           </c:otherwise>
                                       </c:choose> 
                                       <script>
                                           //give feedback to the user about the results
                                           console.log('coursesSize: ' + coursesSize);
                                           if (coursesSize == 0) {
                                               console.log('showing melding');
                                               document.getElementById('validationAlert').innerHTML = '<div class="alert alert-danger"><a class="close" data-dismiss="alert">×</a><strong>Oh snap!</strong> Your search did not match any Course.</div>';
                                           }
                                           else if ('${coursesSizeResults}' > 0) {
                                               document.getElementById('validationAlert').innerHTML = '<div class="alert alert-info"><a class="close" data-dismiss="alert">×</a><strong>Results:</strong> <u>' + coursesSize + '</u> Course(s) match your search.</div>';
                                           }
                                       </script>

                                       </body>
                                       </html>
