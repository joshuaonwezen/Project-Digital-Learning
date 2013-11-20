<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="Shortcut Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
<link rel="Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
<head>
    <meta charset="UTF-8">
    <title>Profile - Info Support</title>
    <link href="resources/css/style.css" rel="stylesheet" type="text/css">
    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
    <style type="text/css">
        .hidden {
            display: none;
        }
        .unhidden {
            display: block;
        }
    </style>
</head>
<body>
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
                        <li><a id="toggle">Toggle Edit Mode</a></li>
                        <li><a href="#">Help</a></li>
                        <li><a href="#">Report a Problem</a></li>
                        <li><a href="index.jsp">Log Out</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
    <div id="main">
        <div id="main_left">
            <div class="container">
                <div class="hidden rightButton">
                    <c:if test="${loggedInUserId == userId}">
                        <a href="users/edit?id=${userId}">
                            <button>Edit</button>
                        </a>
                    </c:if>
                </div>
                <h2 style="margin-left: 20px;">
                    ${loggedInUsername}
                </h2>
                <div class="container_left">
                    <img id="profileImage" src="$tempGebruiker.imageURL}">
                </div>
                <div class="container_right">
                    <table>
                        <tbody>
                            <tr>
                                <td class="tableTitle">bio</td>
                                <td class="rowName">name</td>
                                <td class="rowInfo">${firstname} ${lastname}</td>
                            </tr>
                            <tr>
                                <td class="tableTitle"></td>
                                <td class="rowName">e-mail</td>
                                <td class="rowInfo">${emailAddress}</td>
                            </tr>
                            <tr>
                                <td class="tableTitle"></td>
                                <td class="rowName">age</td>
                                <td class="rowInfo">${age}</td>
                            </tr>
                            <tr>
                                <td class="tableTitle"></td>
                                <td class="rowName">position</td>
                                <td class="rowInfo">${position}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="container">
                <div class="hidden rightButton">
                    <c:if test="${loggedInUserId == userId}">
                        <a href="project/edit?userId=">
                            <button>+</button>
                        </a>
                    </c:if>
                </div>
                <h3>Projects</h3>
                <c:forEach var="tempWork" items="${workList}">
                    <div class="box">
                        <div class="box_left">
                            <div class="top">
                                <span class="marginTiny">${tempWork.fromYear} - ${tempProject.tillYear}</span>
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
                                    <a href="work/edit?id=${tempWork.workId}">
                                        <button>Edit</button>
                                    </a>
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

            <div class="container">
                <div class="hidden rightButton">
                    <c:if test="${loggedInUserId == userId}">
                        <a href="education/edit?userId=">
                            <button>+</button>
                        </a>
                    </c:if>
                </div>
                <h3>Education</h3>
                <c:forEach var="tempEducation" items="${educationList}">
                    <div class="box">
                        <div class="box_left">
                            <div class="top">
                                <span class="marginTiny">${tempEducation.fromYear} - ${tempEducation.tillYear}</span>
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
                                    <a href="education/edit?id=${tempEducation.educationId}">
                                        <button>Edit</button>
                                    </a>
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

            <div class="container">
                <div class="hidden rightButton">
                    <c:if test="${loggedInUserId == userId}">
                        <a href="project/edit?userId=">
                            <button>+</button>
                        </a>
                    </c:if>
                </div>
                <h3>Projects</h3>
                <c:forEach var="tempProject" items="${projectList}">
                    <div class="box">
                        <div class="box_left">
                            <div class="top">
                                <span class="marginTiny">${tempProject.fromYear} - ${tempProject.tillYear}</span>
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
                                    <a href="project/edit?id=${tempProject.projectId}">
                                        <button>Edit</button>
                                    </a>
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
            <div class="container">
                <h3>Completed Courses</h3>
            </div>
            <div class="container">
                <h3>Enrolled Courses</h3>
            </div>

            <div class="container">
                <div class="hidden rightButton">
                    <c:if test="${loggedInUserId == userId}">
                        <a href="skill/edit?userId=">
                            <button>+</button>
                        </a>
                    </c:if>
                </div>
                <h3>Skills</h3>
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
                                    <a href="project/edit?id=${tempProject.projectId}">
                                        <button>Edit</button>
                                    </a>
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
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $("#toggle").click(function() {
            $("div").toggleClass("hidden unhidden");
        });
    });
</script>
</body>
</html>
