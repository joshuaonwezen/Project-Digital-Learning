<!--
@author Shahin Mokhtar
-->
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="index_nl_NL" />
<head>
    <meta charset="UTF-8">
    <title>Profile - Info Support</title>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
        <!-- Bootstrap-->
        <!-- Company Style-->
        <link rel="stylesheet" type="text/css" href="resources/css/profile.css">
        <link rel="stylesheet" href="resources/bootstrap/dist/css/bootstrap.min.css">
        <link rel="Shortcut Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
<link rel="Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>

    <style type="text/css">
        .hidden {
            display: none;
        }
        .unhidden {
            display: block;
        }
        iframe { width: 100%; height: 50%; }
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
                <a class="navbar-brand" href="homepage.jsp"><img src="resources/images/Logo.png"></a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" style="margin-top:12px">
                <ul class="nav navbar-nav">
                    <li><a href="homepage.jsp">Home</a></li>
                    <li class="active"><a href="/PDL/courses"><fmt:message key="navbar.course"/></a></li>
                    <c:if test="${loggedInIsAdmin == true}">
                        <li><a href="/PDL/management">Management</a></li>
                    </c:if>
                    <li><a href="/PDL/profile?id=${loggedInUserId}"><fmt:message key="navbar.profile"/></a></li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Settings <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="index.jsp">Logout</a></li>
                            <li class="divider"></li>
                            <li><a href="#">Help</a></li>
                            <li><a href="#">Report a Problem</a></li>
                        </ul>
                    </li>
                </ul>
                <form class="navbar-form navbar-right" role="search" id="searchCourse" action="searchCourse">
                    <div class="form-group">
                        <input type="text" name="searchQuery" id="searchQuery" class="form-control" placeholder="Search Course">
                    </div>
                    <button type="submit" class="btn btn-default">Search</button>
                </form>
            </div><!-- /.navbar-collapse -->
        </nav>
        <!-- eof navbar-->

    <div id="main">
        <div id="main_left">
            <div class="container_profile">
                <div class="hidden rightButton">
                    <c:if test="${loggedInUserId == userId}">
                        <button style="width:auto; height:auto;" target="_blank" onclick="return windowpop('users/edit?id=${userId}', 800, 500)"><fmt:message key="profile.edit"/></button>
                    </c:if>
                </div>
                <h2 style="margin-left: 20px;">
                    ${loggedInUsername}
                </h2>
                <div class="container_profile_left">
                    <img id="profileImage" src="$tempGebruiker.imageURL}">
                </div>
                <div class="container_profile_right" style="font-size: 19px;">
                    <table>
                        <tbody>
                            <tr>
                                <td class="tableTitle"></td>
                                <td class="rowName"><fmt:message key="profile.name"/></td>
                                <td class="rowInfo">${firstname} ${lastname}</td>
                            </tr>
                            <tr>
                                <td class="tableTitle"></td>
                                <td class="rowName"><fmt:message key="profile.email"/></td>
                                <td class="rowInfo">${emailAddress}</td>
                            </tr>
                            <tr>
                                <td class="tableTitle"></td>
                                <td class="rowName"><fmt:message key="profile.age"/></td>
                                <td class="rowInfo">${age}</td>
                            </tr>
                            <tr>
                                <td class="tableTitle"></td>
                                <td class="rowName"><fmt:message key="profile.position"/></td>
                                <td class="rowInfo">${position}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="container_profile">
                <div class="hidden rightButton">
                    <c:if test="${loggedInUserId == userId}">
                        <button style="width:auto; height:auto;" target="_blank" onclick="return windowpop('work/edit?userId=', 800, 500)">+</button>
                    </c:if>
                </div>
                <h3><fmt:message key="profile.workexperience"/></h3>
                <c:forEach var="tempWork" items="${workList}">
                    <div class="box">
                        <div class="box_left">
                            <div class="top">
                                <span class="marginTiny">${tempWork.dateFrom} - ${tempWork.dateTill}</span>
                                <span class="marginSmall">${tempWork.name}</span>
                                <span class="marginSmall">${tempWork.profession}</span>
                            </div>
                            <div class="bottom">
                                <span class="marginBig">${tempWork.description}</span>
                            </div>
                        </div>
                        <div class="box_right">
                            <div class="hidden">
                                <c:if test="${loggedInUserId == userId}">
                                    <button style="width:auto; height:auto;" target="_blank" onclick="return windowpop('work/edit?id=${tempWork.workId}', 800, 500)">Edit</button>
                                    <a href="javascript:if(confirm('Delete?'))
                                       window.location='work/delete?id=${tempWork.workId}';">
                                        <button>x</button>
                                    </a>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <div class="container_profile">
                <div class="hidden rightButton">
                    <c:if test="${loggedInUserId == userId}">
                        <button style="width:auto; height:auto;" target="_blank" onclick="return windowpop('education/edit?userId=', 800, 500)">+</button>
                    </c:if>
                </div>
                <h3><fmt:message key="profile.education"/></h3>
                <c:forEach var="tempEducation" items="${educationList}">
                    <div class="box">
                        <div class="box_left">
                            <div class="top">
                                <span class="marginTiny">${tempEducation.dateFrom} - ${tempEducation.dateTill}</span>
                                <span class="marginSmall">${tempEducation.name}</span>
                                <span class="marginSmall">${tempEducation.profession}</span>
                            </div>
                            <div class="bottom">
                                <span class="marginBig">${tempEducation.description}</span>
                            </div>
                        </div>
                        <div class="box_right">
                            <div class="hidden">
                                <c:if test="${loggedInUserId == userId}">
                                    <button style="width:auto; height:auto;" target="_blank" onclick="return windowpop('education/edit?id=${tempEducation.educationId}', 800, 500)">Edit</button>
                                    <a href="javascript:if(confirm('Delete?'))
                                       window.location='education/delete?id=${tempEducation.educationId}';">
                                        <button>x</button>
                                    </a>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <div class="container_profile">
                <div class="hidden rightButton">
                    <c:if test="${loggedInUserId == userId}">
                        <button style="width:auto; height:auto;" target="_blank" onclick="return windowpop('project/edit?userId=', 800, 500)">+</button>
                    </c:if>
                </div>
                <h3><fmt:message key="profile.projects"/></h3>
                <c:forEach var="tempProject" items="${projectList}">
                    <div class="box">
                        <div class="box_left">
                            <div class="top">
                                <span class="marginTiny">${tempProject.dateFrom} - ${tempProject.dateTill}</span>
                                <span class="marginSmall">${tempProject.name}</span>
                                <span class="marginSmall">${tempProject.profession}</span>
                            </div>
                            <div class="bottom">
                                <span class="marginBig">${tempProject.description}</span>
                            </div>
                        </div>
                        <div class="box_right">
                            <div class="hidden">
                                <c:if test="${loggedInUserId == userId}">
                                    <button style="width:auto; height:auto;" target="_blank" onclick="return windowpop('project/edit?id=${tempProject.projectId}', 800, 500)">Edit</button>
                                    <a href="javascript:if(confirm('Delete?'))
                                       window.location='project/delete?id=${tempProject.projectId}';">
                                        <button>x</button>
                                    </a>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <div id="main_right">
            <div class="container_profile">
                <h3><fmt:message key="profile.completed.courses"/></h3>
            </div>
            <div class="container_profile">
                <h3><fmt:message key="profile.enrolled.courses"/></h3>
            </div>

            <div class="container_profile">
                <div class="hidden rightButton">
                    <c:if test="${loggedInUserId == userId}">
                        <button style="width:auto; height:auto;" target="_blank" onclick="return windowpop('skill/edit?userId=', 800, 500)">+</button>
                    </c:if>
                </div>
                <h3><fmt:message key="profile.skills"/></h3>
                <c:forEach var="tempSkill" items="${skillList}">
                    <div class="boxRight">
                        <div class="boxRight_left">
                            <div class="top">
                                <span class="marginTiny">${tempSkill.name}</span>
                                <span class="marginSmall">${tempSkill.level}</span>
                            </div>
                        </div>
                        <div class="boxRight_right">
                            <div class="hidden">
                                <c:if test="${loggedInUserId == userId}">

                                    <a href="javascript:if(confirm('Delete?'))
                                       window.location='skill/delete?id=${tempSkill.skillId}';">
                                        <button>x</button>
                                    </a>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

                
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        $("#toggle").click(function() {
            $("div").toggleClass("hidden unhidden");
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
