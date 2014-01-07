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
<fmt:setBundle basename="index" />
<!DOCTYPE html>
<html  lang="${language}">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Bootstrap-->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="resources/bootstrap/dist/css/bootstrap.min.css">
        <script src="resources/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="resources/bootstrap/dist/js/alert.js"></script>
        <title>Courses - Info Support</title>
    </head>
    <body>
        <!--Start nav bar-->
        <nav class="navbar navbar-inverse" role="navigation">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="/PDL/homepage"><img src="resources/images/Logo.png"></a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" style="margin-top:12px">
                <ul class="nav navbar-nav">
                    <li><a href="/PDL/homepage">Home</a></li>
                    <li class="active"><a href="/PDL/courses"><fmt:message key="navbar.course"/></a></li>
                        <c:if test="${loggedInIsAdmin || loggedInIsTeacher || loggedInIsManager == true}">
                        <li class="dropdown" class="active">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">Management <b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><a href="/PDL/management">Management</a></li>
                                <li class="divider"></li>
                                <li><a href="/PDL/i18n_nl">Internationalisation</a></li>
                            </ul>
                        </li> 
                    </c:if>
                    <li><a href="/PDL/profile?id=${loggedInUserId}"><fmt:message key="navbar.profile"/></a></li>
                        <c:if test="${loggedInIsAdmin || loggedInIsManager == true}">
                        <li><a href="/PDL/vga">VGA</a></li>
                        </c:if>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><fmt:message key="navbar.settings"/> <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="index.jsp"><fmt:message key="navbar.logout"/></a></li>
                            <li class="divider"></li>
                            <li>
                                <a >
                                    <form>
                                        <select id="language" name="language" onchange="submit()">
                                            <option value="en_US" ${language == 'en_US' ? 'selected' : ''}>English</option>
                                            <option value="nl_NL" ${language == 'nl_NL' ? 'selected' : ''}>Nederlands</option>
                                        </select>
                                    </form>
                                </a>
                            </li>
                        </ul>
                    </li>
                </ul>
                <form class="navbar-form navbar-right" role="search" id="searchCourse" action="searchCourse">
                    <div class="form-group">
                        <input type="text" name="searchQuery" id="searchQuery" class="form-control" placeholder="<fmt:message key="searchbar.search.course"/>">
                    </div>
                    <button type="submit" class="btn btn-default"><fmt:message key="navbar.search"/></button>
                </form>
            </div><!-- /.navbar-collapse -->
        </nav>
        <!-- eof navbar-->

        <div id="validationAlert" style=margin-left:200px;margin-right:200px;"></div>
        <c:if test="${enrolledIn != null}">
            <div class="alert alert-success" style="margin-left:200px;margin-right:200px;">
                <a class="close" data-dismiss="alert">×</a>
                <strong><fmt:message key="popup.done"/></strong> <fmt:message key="course.enroll.succes"/> "${enrolledIn}"
            </div>
        </c:if>
        <c:if test="${withdrawedFrom != null}">
            <div class="alert alert-success" style="margin-left:200px;margin-right:200px;">
                <a class="close" data-dismiss="alert">×</a>
                <strong><fmt:message key="popup.done"/></strong> <fmt:message key="course.remove"/> "${withdrawedFrom}"
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
                    <c:if test="${course.isVisible}">

                        <div class=".col-md-6 .col-md-offset-3" style="margin-left:200px;margin-right:200px">
                            <div class="thumbnail" >
                                <div class="caption">
                                    <div style="float:right">
                                        <small>
                                            <script>var skills = '';</script>
                                            <c:forEach var="skill" items="${course.skills}">
                                                <script>skills += '${skill.name}, ';</script>
                                            </c:forEach>
                                            <script>document.write(skills.substring(0, skills.length - 2));</script>
                                        </small>
                                    </div>
                                    <c:forEach var="courseSuggestion" items="${suggestedCourses}">
                                        <c:if test="${courseSuggestion.course.courseId == course.courseId}">
                                            <img src="resources/images/starfull.png" title="Suggested Course" data-placement="bottom" data-toggle="tooltip"/>
                                        </c:if>
                                    </c:forEach>
                                    <h3 style="margin-left:0px;display:inline;vertical-align:middle;">${course.name} ${course.level}</h3>
                                     
                                    <h4><small><fmt:message key="course.teacher"/> ${course.owner.firstname} ${course.owner.lastname}</small></h4>
                                    <p>${course.description}</p>
                                    <form action="enroll" id="enroll">
                                        <input type="hidden" id="courseId" name="courseId" value="${course.courseId}"/>
                                        <button id="buttonCourseEnroll${course.courseId}" type="submit" class="btn btn-primary"><fmt:message key="course.enroll"/></button>
                                    </form>
                                    <form action="withdraw" id="withdraw">
                                        <input type="hidden" id="courseId" name="courseId" value="${course.courseId}"/>
                                        <button id="buttonCourseWithdraw${course.courseId}" type="submit" class="btn btn-warning" style="display:none"><fmt:message key="course.withdraw"/></button>
                                    </form>
                                    <div id="courseActions${course.courseId}" style="float:right;margin-top:-63px;margin-right:-20px;display:none">
                                        <div class="row">
                                            <div class="col-xs-6 col-s-3">
                                                <a href="/PDL/documents?courseId=${course.courseId}" class="thumbnail" style="width:85px;height:64px;">
                                                    <img src="resources/images/folder_full.png" data-src="holder.js/100%x180" alt="...">
                                                </a>
                                            </div>
                                            <div class="col-xs-6 col-s-3">
                                                <a href="/PDL/courses/virtualclassroom?courseId=${course.courseId}" class="thumbnail" style="width:64px;height:64px;margin-left:-20px">
                                                    <img src="resources/images/chalk_board.png" data-src="holder.js/100%x180" alt="...">
                                                </a>
                                            </div>   </div>
                                    </div>
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
                                                    document.getElementById('buttonCourseWithdraw${course.courseId}').style.display = 'block';
                                                    document.getElementById('courseActions${course.courseId}').style.display = 'block';
                                                    document.getElementById('buttonCourseEnroll${course.courseId}').style.display = 'none';
                                                }
                                            </script>
                                        </c:when>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                        </br>

                    </c:if>


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
                console.log('showing melding no courses found');
                document.getElementById('validationAlert').innerHTML = '<div class="alert alert-danger"><a class="close" data-dismiss="alert">×</a><strong>Oh snap!</strong> <fmt:message key="search.course.none"/></div>';
            }
            else if ('${coursesSizeResults}' > 0) {
                document.getElementById('validationAlert').innerHTML = '<div class="alert alert-info"><a class="close" data-dismiss="alert">×</a><strong>Results:</strong> <u>' + coursesSize + '</u> <fmt:message key="search.course.match"/></div>';
            }

            //function for opening the course page
            function openCourse(courseId) {
                window.open('/PDL/courses/course?courseId=' + courseId, '_self', false);
            }

        </script>
    </body>
</html>
