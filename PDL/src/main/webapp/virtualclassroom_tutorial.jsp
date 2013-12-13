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
        <link rel="stylesheet" href="../resources/bootstrap/dist/css/bootstrap.min.css">
        <script src="../resources/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="../resources/bootstrap/dist/js/alert.js"></script>
        <title>Tutorial - Info Support</title>
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
                <a class="navbar-brand" href="/PDL/homepage"><img src="../resources/images/Logo.png"></a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" style="margin-top:12px">
                <ul class="nav navbar-nav">
                    <li><a href="/PDL/homepage">Home</a></li>
                    <li class="active"><a href="/PDL/courses"><fmt:message key="navbar.course"/></a></li>
                        <c:if test="${loggedInIsAdmin || loggedInIsTeacher || loggedInIsManager == true}">
                        <li><a href="/PDL/management">Management</a></li>
                        </c:if>
                    <li><a href="/PDL/profile?id=${loggedInUserId}"><fmt:message key="navbar.profile"/></a></li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><fmt:message key="navbar.settings"/> <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="index.jsp"><fmt:message key="navbar.logout"/></a></li>
                            <li class="divider"></li>
                            <li><a href="#">Help</a></li>
                            <li><a href="#"><fmt:message key="navbar.problem"/></a></li>
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
            </div>
        </nav>
        <div style="position: absolute; margin-left: 20%;">
        <c:if test="${courseOwner.username == loggedInUsername}">
            <h3>Your key is:  ${courseKey}<br><br></h3>
        </c:if>
            
        <h1> Step 1</h1><br>
        Download the following program: <a href="http://sourceforge.net/projects/obsproject/files/OBS_0_584b_Installer.exe/download">Open Broadcast Software</a><br>
        This program will allow you to setup your videostream and start a lesson.<br><br>
        <h1> Step 2</h1><br>
        Right click on the "Sources" tab and press "Add".<br>
        <img src="../resources/images/tutorial1.png"><br>
        From here on you can select what you want to be shown on the livestream in the virtual classroom<br>
        - Window Capture:   Captures a select window (like your browser or a word document, only this window will be shown on your stream.<br>
        - Monitor Capture:   Captures your entire screen, everything you do on the selected monitor will be shown!<br>
        - Video Capture Device:   Captures your webcam.<br>
        You can preview your stream by pressing "Preview Stream"<br><br>
        <h1> Step 3</h1><br>
        You can edit your captured windows by pressing "Edit Scene".
        <img src="../resources/images/tutorial3.png"><br><br>
        <h1> Step 4</h1><br>
        Now that you've setup your stream, it's time to connect to our server and start your lesson!<br>
        Press the "Settings" button and go to "Broadcast Settings"<br>
        <img src="../resources/images/tutorial2.png"><br>
        Here fill in the server address "rtmp://31.186.175.82/live"<br>
        Now fill in the stream key of your selected course which is:
        <c:if test="${courseOwner.username == loggedInUsername}">
        ${courseKey}<br>
        </c:if>
        Your done! Press "OK" and "Start Streaming".<br><br>
        
        <a href="/PDL/courses/virtualclassroom?courseId=${courseId}">Back to Virtual Classroom</a>
        
        </div>   
      </body>
</html>
