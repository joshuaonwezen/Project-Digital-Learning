<!--
@author Shahin Mokhtar
-->
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
        iframe { width: 100%; height: 50%; }
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
                <li><a href="courses.jsp">Courses</a></li>
                    <c:if test="${loggedInIsAdmin == true}">
                    <li><a href="management">Management</a></li>
                    </c:if>
                <li><a href="profile?id=${loggedInUserId}">My Profile</a></li>
                <li>
                    <a href="#">Settings</a>
                    <ul>
                        <c:if test="${loggedInUserId == userId}">
                            <li><a id="toggle">Toggle Edit Mode</a></li>
                            </c:if>
                        <li><a href="#">Help</a></li>
                        <li><a href="#">Report a Problem</a></li>
                        <li><a href="index.jsp">Log Out</a></li>
                        <li><a href="cv?id=${userId}" target="_blank">Generate CV</a></li>
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
                        <button style="width:auto; height:auto;" target="_blank" onclick="return windowpop('users/edit?id=${userId}', 800, 500)">Edit</button>
                    </c:if>
                </div>
                <h2 style="margin-left: 20px;">
                    ${loggedInUsername}
                </h2>
                <div class="container_left">
                    <img id="profileImage" src="$tempGebruiker.imageURL}">
                </div>
                <div class="container_right" style="font-size: 19px;">
                    <table>
                        <tbody>
                            <tr>
                                <td class="tableTitle"></td>
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
                        <button style="width:auto; height:auto;" target="_blank" onclick="return windowpop('work/edit?userId=', 800, 500)">+</button>
                    </c:if>
                </div>
                <h3>Work experience</h3>
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

            <div class="container">
                <div class="hidden rightButton">
                    <c:if test="${loggedInUserId == userId}">
                        <button style="width:auto; height:auto;" target="_blank" onclick="return windowpop('education/edit?userId=', 800, 500)">+</button>
                    </c:if>
                </div>
                <h3>Education</h3>
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

            <div class="container">
                <div class="hidden rightButton">
                    <c:if test="${loggedInUserId == userId}">
                        <button style="width:auto; height:auto;" target="_blank" onclick="return windowpop('project/edit?userId=', 800, 500)">+</button>
                    </c:if>
                </div>
                <h3>Projects</h3>
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
            <div class="container">
                <h3>Completed Courses</h3>
            </div>
            <div class="container">
                <h3>Enrolled Courses</h3>
            </div>

            <div class="container">
                <div class="hidden rightButton">
                    <c:if test="${loggedInUserId == userId}">
                        <button style="width:auto; height:auto;" target="_blank" onclick="return windowpop('skill/edit?userId=', 800, 500)">+</button>
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
