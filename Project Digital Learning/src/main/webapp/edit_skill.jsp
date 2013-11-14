<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>${update == true ? 'Edit' : 'Create'} Skill</title>
        <link href="/WEBappMVCMavenSolution/style.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <h2>${update == true ? 'Edit' : 'Create'} Skill</h2>
        <c:if test="${errors != null}">
            <!-- Mochten er errors zijn, dan worden ze hier getoond -->
            <p>${errors}</p>
        </c:if>
        <c:choose>
            <c:when test="${id == null}">
                <form id="newSkill" action="new" method="post">
                </c:when>
                <c:otherwise>
                    <form id="editSkill" action="edit" method="post">
                    </c:otherwise>
                </c:choose>
                <c:if test="${id != null}">
                    <!-- Het id wordt meegestuurd -->
                    <input type="hidden" name="id" id="id" value="${id}"></input>
                </c:if>
                <table border="0">
                    <tr>
                        <td>
                            <label for="name">Name</label>
                        </td>
                        <td>
                            <input type="textfield" id="name" name="name" value="${name}"></input>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="level">Level</label>
                        </td>
                        <td>
                            <select id="level" name="level" >
                                <option value="Beginner">Beginner</option>
                                <option value="Intermediate">Intermediate</option>  
                                <option value="Advanced">Advanced</option>  
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="userId">User</label>
                        </td>
                        <td>
                            <select id="user" name="user" >
                                <c:forEach var="tempSkill" items="${skillList}">
                                    <option value="${tempSkill.userId}">${tempSkill.firstname} ${tempSkill.lastname}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                </table>
                <p>
                    <input class="submit" type="submit" value="Verzenden">
                </p>
            </form>
    </body>
</html>