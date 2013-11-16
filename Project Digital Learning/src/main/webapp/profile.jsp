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
        <div class="Header">
        <ul>
            <li><a class="button" href="homepage.jsp">Home</a></li>
            <li><a class="button" href="#profile">My Profile</a></li>
            <li><a class="button" href="#courses">Courses</a></li>
            <c:if test="${loggedInIsAdmin == true}">
            <li><a class="button" href="/Project Digital Learning/management">Management</a></li>
             </c:if>
            <li><a class="button" href="index.jsp">LogOut</a></li>
        </ul>
        </div>
    <div id="header">
        <table border="0">
            <tr>
                <td>
                    <h2>Profile</h2>
                </td>
                <td>
                    <input type="text" name="search" value="search..">
                </td>
                <td>
                    <p>
                        <a href="users">Users</a>
                    </p>
                </td>
            </tr>
        </table>
    </div>
    <div id="main">
        <div id="main_left">
            <div class="container">
                <div id="main_inner_left">
                    <!--<img src="$tempGebruiker.imageURL}" width="150" height="150">-->
                </div>
                <div id="main_inner_right">
                    <table border="0">
                        <tr>
                            <td>
                                ${firstname} ${lastname}
                            </td>
                        </tr>
                        <tr>
                            <td>
                                E-mail: 
                            </td>
                            <td>
                                ${emailAddress}
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td>
                                <a href="users/edit?id=${userId}">Edit profile</a>
                            </td>
                        </tr>
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
                            <td>
                                <a href="work/edit?id=${tempWork.workId}">edit</a> |
                                <a href="javascript:if(confirm('Weet u het zeker dat u deze item wil verwijderen?'))
                                   window.location='work/delete?id=${tempWork.workId}';">x</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <p>
                    <a href="work/edit?userId="">+</a>
                </p>
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
                            <td>
                                <a href="project/edit?id=${tempProject.projectId}">edit</a> |
                                <a href="javascript:if(confirm('Weet u het zeker dat u deze item wil verwijderen?'))
                                   window.location='project/delete?id=${tempProject.projectId}';">x</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <p>
                    <a href="project/edit?userId=">+</a>
                </p>
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
                            <td>
                                <a href="skill/edit?id=${tempSkill.skillId}">edit</a> |
                                <a href="javascript:if(confirm('Weet u het zeker dat u deze item wil verwijderen?'))
                                   window.location='skill/delete?id=${tempSkill.skillId}';">x</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <p>
                    <a href="skill/edit?userId=">+</a>
                </p>
            </div>
        </div>
    </div>
    <div id="footer">

    </div>
</body>
</html>
