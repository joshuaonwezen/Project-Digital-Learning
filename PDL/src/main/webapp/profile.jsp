<!--
@author Shahin Mokhtar
-->
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="index" />
<html  lang="${language}">
    <head>
        <meta charset="UTF-8">
        <title>Profile - Info Support</title>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
        <!-- Bootstrap-->
        <!-- Company Style-->
        <link rel="stylesheet" type="text/css" href="resources/css/profile.css">
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="resources/bootstrap/dist/css/bootstrap.min.css">
        <script src="resources/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="resources/bootstrap/dist/js/alert.js"></script>
        <link rel="stylesheet" href="resources/bootstrap/dist/css/bootstrap.min.css">
        <link rel="Shortcut Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
        <link rel="Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>

        <style type="text/css">
            .editHidden {
                display: none;
            }
            .editUnhidden {
                display: block;
            }
        </style>
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
                    <li><a href="/PDL/courses"><fmt:message key="navbar.course"/></a></li>
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
                    <li class="dropdown active">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><fmt:message key="navbar.profile"/> <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="cv?id=${userId}" target="_blank"><fmt:message key="navbar.cv"/></a></li>
                        </ul>
                    </li>
                    <c:if test="${loggedInIsAdmin || loggedInIsManager == true}">
                        <li><a href="/PDL/vga">VGA</a></li>
                        </c:if>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><fmt:message key="navbar.settings"/> <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="index.jsp"><fmt:message key="navbar.logout"/></a></li>
                                <c:if test="${loggedInUserId == userId}">
                                <li><a id="toggle"><fmt:message key="profile.edit"/></a></li>
                                </c:if>
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
                <form class="navbar-form navbar-right" role="search" id="searchUser" action="searchUser">
                    <div class="form-group">
                        <input type="text" name="searchQuery" id="searchQuery" class="form-control" placeholder="<fmt:message key="searchbar.search.user"/>">
                    </div>
                    <button type="submit" class="btn btn-default"><fmt:message key="navbar.search"/></button>
                </form>
            </div><!-- /.navbar-collapse -->
        </nav>

        <div id="main">
            <div id="main_left">
                <div class="container_profile">
                    <div class="editHidden rightButton">
                        <c:if test="${loggedInUserId == userId}">
                            <button class="btn btn-default" target="_blank" onclick="return windowpop('users/edit?id=${userId}', 800, 500)"><fmt:message key="profile.edit"/></button>
                        </c:if>
                    </div>
                    <h2 style="margin-left: 20px;">
                        ${username}
                    </h2>
                    <div class="container_profile_left">
                        <img id="profileImage" src="$tempGebruiker.imageURL}">
                    </div>
                    <div class="container_profile_right" style="font-size: 19px;">
                        <table>
                            <tbody>
                                <tr>
                                    <td class="tableTitle"></td>
                                    <td class="rowName"><fmt:message key="profile.name"/>:</td>
                                    <td class="rowInfo">${firstname} ${lastname}</td>
                                </tr>
                                <tr>
                                    <td class="tableTitle"></td>
                                    <td class="rowName"><fmt:message key="profile.email"/>:</td>
                                    <td class="rowInfo">${emailAddress}</td>
                                </tr>
                                <tr>
                                    <td class="tableTitle"></td>
                                    <td class="rowName"><fmt:message key="profile.position"/>:</td>
                                    <td class="rowInfo">${position}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                <c:if test="${workList.size() < 1}">
                    <div class="editHidden container_profile">
                        <div class="editHidden rightButton">
                            <c:if test="${loggedInUserId == userId}">
                                <button class="btn btn-default" target="_blank" onclick="return windowpop('work/edit?userId=', 800, 500)">+</button>
                            </c:if>
                        </div>
                        <h3><fmt:message key="profile.workexperience"/></h3>
                    </div>
                </c:if>
                <c:if test="${workList.size() > 0}">
                    <div class="container_profile">
                        <div class="editHidden rightButton">
                            <c:if test="${loggedInUserId == userId}">
                                <button class="btn btn-default" target="_blank" onclick="return windowpop('work/edit?userId=', 800, 500)">+</button>
                            </c:if>
                        </div>
                        <h3><fmt:message key="profile.workexperience"/></h3>
                        <c:forEach var="tempWork" items="${workList}">
                            <div class="box">
                                <div class="box_left">
                                    <div class="top">
                                        <div class="top_left">
                                            <fmt:formatDate value="${tempWork.dateFrom}" pattern="dd-MM-yyyy" /> - <fmt:formatDate value="${tempWork.dateTill}" pattern="dd-MM-yyyy" />
                                        </div>
                                        <div class="top_right">
                                            ${tempWork.name} - 
                                            ${tempWork.profession}
                                        </div>
                                    </div>
                                    <div class="bottom">
                                        <div class="bottom_left"></div>
                                        <div class="bottom_right">
                                            ${tempWork.description}
                                        </div>
                                    </div>
                                </div>
                                <div class="box_right">
                                    <div class="editHidden">
                                        <c:if test="${loggedInUserId == userId}">
                                            <button class="btn btn-default" target="_blank" onclick="return windowpop('work/edit?id=${tempWork.workId}', 800, 500)">Edit</button>
                                            <a href="javascript:if(confirm('Delete?'))
                                               window.location='work/delete?id=${tempWork.workId}';">
                                                <button class="btn btn-default">x</button>
                                            </a>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>

                <c:if test="${educationList.size() < 1}">
                    <div class="editHidden container_profile">
                        <div class="editHidden rightButton">
                            <c:if test="${loggedInUserId == userId}">
                                <button class="btn btn-default" target="_blank" onclick="return windowpop('education/edit?userId=', 800, 500)">+</button>
                            </c:if>
                        </div>
                        <h3><fmt:message key="profile.education"/></h3>
                    </div>
                </c:if>
                <c:if test="${educationList.size() > 0}">
                    <div class="container_profile">
                        <div class="editHidden rightButton">
                            <c:if test="${loggedInUserId == userId}">
                                <button class="btn btn-default" target="_blank" onclick="return windowpop('education/edit?userId=', 800, 500)">+</button>
                            </c:if>
                        </div>
                        <h3><fmt:message key="profile.education"/></h3>
                        <c:forEach var="tempEducation" items="${educationList}">
                            <div class="box">
                                <div class="box_left">
                                    <div class="top">
                                        <div class="top_left">
                                            <fmt:formatDate value="${tempEducation.dateFrom}" pattern="dd-MM-yyyy" /> - <fmt:formatDate value="${tempEducation.dateTill}" pattern="dd-MM-yyyy" />
                                        </div>
                                        <div class="top_right">
                                            ${tempEducation.name} - 
                                            ${tempEducation.profession}
                                        </div>
                                    </div>
                                    <div class="bottom">
                                        <div class="bottom_left"></div>
                                        <div class="bottom_right">
                                            ${tempEducation.description}
                                        </div>
                                    </div>
                                </div>
                                <div class="box_right">
                                    <div class="editHidden">
                                        <c:if test="${loggedInUserId == userId}">
                                            <button class="btn btn-default" target="_blank" onclick="return windowpop('education/edit?id=${tempEducation.educationId}', 800, 500)">Edit</button>
                                            <a href="javascript:if(confirm('Delete?'))
                                               window.location='education/delete?id=${tempEducation.educationId}';">
                                                <button class="btn btn-default">x</button>
                                            </a>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>

                <c:if test="${projectList.size() < 1}">
                    <div class="editHidden container_profile">
                        <div class="editHidden rightButton">
                            <c:if test="${loggedInUserId == userId}">
                                <button class="btn btn-default" target="_blank" onclick="return windowpop('project/edit?userId=', 800, 500)">+</button>
                            </c:if>
                        </div>
                        <h3><fmt:message key="profile.projects"/></h3>
                    </div>
                </c:if>
                <c:if test="${projectList.size() > 0}">
                    <div class="container_profile">
                        <div class="editHidden rightButton">
                            <c:if test="${loggedInUserId == userId}">
                                <button class="btn btn-default" target="_blank" onclick="return windowpop('project/edit?userId=', 800, 500)">+</button>
                            </c:if>
                        </div>
                        <h3><fmt:message key="profile.projects"/></h3>
                        <c:forEach var="tempProject" items="${projectList}">
                            <div class="box">
                                <div class="box_left">
                                    <div class="top">
                                        <div class="top_left">
                                            <fmt:formatDate value="${tempProject.dateFrom}" pattern="dd-MM-yyyy" /> - <fmt:formatDate value="${tempProject.dateTill}" pattern="dd-MM-yyyy" />
                                        </div>
                                        <div class="top_right">
                                            ${tempProject.name} - 
                                            ${tempProject.profession}
                                        </div>
                                    </div>
                                    <div class="bottom">
                                        <div class="bottom_left"></div>
                                        <div class="bottom_right">
                                            ${tempProject.description}
                                            <p>
                                                <a href="${tempProject.URL}">${tempProject.URL}</a>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                                <div class="box_right">
                                    <div class="editHidden">
                                        <c:if test="${loggedInUserId == userId}">
                                            <button class="btn btn-default" target="_blank" onclick="return windowpop('project/edit?id=${tempProject.projectId}', 800, 500)">Edit</button>
                                            <a href="javascript:if(confirm('Delete?'))
                                               window.location='project/delete?id=${tempProject.projectId}';">
                                                <button class="btn btn-default">x</button>
                                            </a>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
            </div>

            <div id="main_right">
                <c:if test="${1 > 2}">
                    <div class="container_profile">
                        <h3><fmt:message key="profile.completed.courses"/></h3>
                    </div>
                </c:if>
                <c:if test="${1 > 2}">
                    <div class="container_profile">
                        <h3><fmt:message key="profile.enrolled.courses"/></h3>
                    </div>
                </c:if>

                <c:if test="${skillList.size() < 1}">
                    <div class="editHidden container_profile">
                        <div class="editHidden rightButton">
                            <c:if test="${loggedInUserId == userId}">
                                <button class="btn btn-default" target="_blank" onclick="return windowpop('skill/edit?userId=', 800, 500)">+</button>
                            </c:if>
                        </div>
                        <h3><fmt:message key="profile.skills"/></h3>
                    </div>
                </c:if>
                <c:if test="${skillList.size() > 0}">
                    <div class="container_profile">
                        <div class="editHidden rightButton">
                            <c:if test="${loggedInUserId == userId}">
                                <button class="btn btn-default" target="_blank" onclick="return windowpop('skill/edit?userId=', 800, 500)">+</button>
                            </c:if>
                        </div>
                        <h3><fmt:message key="profile.skills"/></h3>
                        <c:forEach var="tempSkill" items="${skillList}">
                            <div class="side_box">
                                <div class="side_left">
                                    ${tempSkill.name}
                                </div>
                                <div class="side_right">
                                    <div class="editHidden">
                                        <c:if test="${loggedInUserId == userId}">
                                            <a href="javascript:if(confirm('Delete?'))
                                               window.location='skill/delete?id=${tempSkill.skillId}';">
                                                <button class="btn btn-default skillButtonSize"><span class="skillButtonPlace">x</span></button>
                                            </a>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
            </div>           
        </div>

        <script type="text/javascript">
            $(document).ready(function() {
                $("#toggle").click(function() {
                    $("div").toggleClass("editHidden editUnhidden");
                });
            });

            function windowpop(url, width, height) {
                var leftPosition, topPosition;
                //Allow for borders.
                leftPosition = (window.screen.width / 2) - ((width / 2) + 10);
                //Allow for title and status bars.
                topPosition = (window.screen.height / 2) - ((height / 2) + 50);
                //Open the window.
                window.open(url, "Window2", "status=no,height=" + height + ",width=" + width + ",resizable=yes,left=" + leftPosition + ",top=" + topPosition + ",screenX=" + leftPosition + ",screenY=" + topPosition + ",toolbar=no,menubar=no,scrollbars=no,location=no,directories=no");
            }
        </script>
    </body>
</html>
