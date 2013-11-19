<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="Shortcut Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
<link rel="Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
<head>
    <meta charset="UTF-8">
    <title>Profile - Info Support</title>
    <link href="resources/css/style.css" rel="stylesheet" type="text/css">
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
                    <c:if test="${loggedInIsAdmin == false}">
                    <li><a href="/Project%20Digital%20Learning/profile?id=${loggedInUserId}">My Profile</a></li>
                    </c:if>
                <li>
                    <a href="#">Settings</a>
                    <ul>
                        <li><a href="#">Account Settings</a></li>
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
                            <c:if test="${loggedInUserId == userId}">
                                <tr>        
                                    <td>
                                        <a href="users/edit?id=${userId}">
                                            <button>Edit</button>
                                        </a>
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="container">
                <h3>Work Experience</h3>
                <table border="0">
                    <c:forEach var="tempWork" items="${workList}">
                        <tr>
                            <td>
                                ${tempWork.fromYear}-${tempWork.tillYear}
                            </td>
                            <td>
                                ${tempWork.name}
                            </td>
                            <td>
                                ${tempWork.profession}
                            </td>
                        </tr>
                        <tr>
                            <td>
                                ${tempWork.description}
                            </td>
                            <c:if test="${loggedInUserId == userId}">
                                <td>
                                    <a href="work/edit?id=${tempWork.workId}">
                                        <button>Edit</button>
                                    </a>
                                </td>
                                <td>
                                    <a href="javascript:if(confirm('Delete?'))
                                       window.location='work/delete?id=${tempWork.workId}';">
                                        <button>x</button>
                                    </a>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </table>
                <c:if test="${loggedInUserId == userId}">
                    <p>
                        <a href="work/edit?userId=">
                            <button>+</button>
                        </a>
                    </p>
                </c:if>
            </div>
            <div class="container">
                <h3>Education</h3>
                <table border="0">
                    <c:forEach var="tempEducation" items="${educationList}">
                        <tr>
                            <td>
                                ${tempEducation.fromYear}-${tempProject.tillYear}
                            </td>
                            <td>
                                ${tempEducation.name}
                            </td>
                            <td>
                                ${tempEducation.profession}
                            </td>
                        </tr>
                        <tr>
                            <td>
                                ${tempEducation.description}
                            </td>
                            <c:if test="${loggedInUserId == userId}">
                                <td>
                                    <a href="education/edit?id=${tempEducation.educationId}">
                                        <button>Edit</button>
                                    </a>
                                </td>
                                <td>
                                    <a href="javascript:if(confirm('Delete?'))
                                       window.location='education/delete?id=${tempEducation.educationId}';">
                                        <button>x</button>
                                    </a>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </table>
                <c:if test="${loggedInUserId == userId}">
                    <p>
                        <a href="education/edit?userId=">
                            <button>+</button>
                        </a>
                    </p>
                </c:if>
            </div>
            <div class="container">
                <h3>Projects</h3>
                <table border="0">
                    <c:forEach var="tempProject" items="${projectList}">
                        <tr>
                            <td>
                                ${tempProject.fromYear}-${tempProject.tillYear}
                            </td>
                            <td>
                                ${tempProject.name}
                            </td>
                            <td>
                                ${tempProject.profession}
                            </td>
                        </tr>
                        <tr>
                            <td>
                                ${tempProject.description}
                            </td>
                            <c:if test="${loggedInUserId == userId}">
                                <td>
                                    <a href="project/edit?id=${tempProject.projectId}">
                                        <button>Edit</button>
                                    </a>
                                </td>
                                <td>
                                    <a href="javascript:if(confirm('Delete?'))
                                       window.location='project/delete?id=${tempProject.projectId}';">
                                        <button>x</button>
                                    </a>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </table>
                <c:if test="${loggedInUserId == userId}">
                    <p>
                        <a href="project/edit?userId=">
                            <button>+</button>
                        </a>
                    </p>
                </c:if>
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
                <h3>Skills</h3>
                <table border="0">
                    <c:forEach var="tempSkill" items="${skillList}">
                        <tr>
                            <td>
                                ${tempSkill.name}
                            </td>
                            <td>
                                Level: ${tempSkill.level}
                            </td>
                            <c:if test="${loggedInUserId == userId}">
                                <td>
                                    <a href="skill/edit?id=${tempSkill.skillId}">
                                        <button>Edit</button>
                                    </a>
                                </td>
                                <td>
                                    <a href="javascript:if(confirm('Delete?'))
                                       window.location='skill/delete?id=${tempSkill.skillId}';">
                                        <button>x</button>
                                    </a>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </table>
                <c:if test="${loggedInUserId == userId}">
                    <p>
                        <a href="skill/edit?userId=">
                            <button>+</button>
                        </a>
                    </p>
                </c:if>
            </div>
        </div>
    </div>
</body>
</html>
