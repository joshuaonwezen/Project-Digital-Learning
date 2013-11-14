<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <link href="resources/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
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
                <c:choose>
                    <c:when test="${tempGebruiker.size() != 0}">
                        <!-- Wanneer er gebruikers opgeslagen zijn, worden ze hier getoond -->
                        <c:forEach var="tempGebruiker" items="${userList}">
                            <!-- Per gebruiker wordt nu een rij aangemaakt met daarin zijn gegevens -->
                            <div id="main_inner_left">
                                <!--<img src="$tempGebruiker.imageURL}" width="150" height="150">-->
                            </div>
                            <div id="main_inner_right">
                                <table border="0">
                                    <tr>
                                        <td>
                                            ${tempGebruiker.firstname} ${tempGebruiker.lastname}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            E-mail: 
                                        </td>
                                        <td>
                                            ${tempGebruiker.emailAddress}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td>
                                            <a href="users/edit?id=${tempGebruiker.userId}">Edit profile</a>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <!-- Als er geen gebruikers zijn, wordt deze melding getoond -->
                        Er zijn geen gebruikers gevonden.
                    </c:otherwise>
                </c:choose>
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
