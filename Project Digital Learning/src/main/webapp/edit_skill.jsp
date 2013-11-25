<!--
@author Shahin Mokhtar
-->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<link rel="Shortcut Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
<link rel="Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>

<html>
    <head>
        <link rel="stylesheet" type="text/css" href="resources/css/style.css">
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>${update == true ? 'Edit' : 'Create'} Skill - Info Support</title>
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
                            <label for="userId"></label>
                        </td>
                        <td>
                            <input type="hidden" id="user" name="user" value="${loggedInUserId}"></input>
                        </td>
                    </tr>
                </table>
                <p>
                    <input class="submit" id="close" onclick="window.close()" type="submit" value="Save">
                </p>
            </form>
            <p>
                <a href="/Project%20Digital%20Learning/profile?id=${loggedInUserId}">
                    <button id="close" onclick="window.close()">Cancel</button>
                </a>
            </p>
            <script type="text/javascript">
                window.onunload = function() {
                    window.opener.location.reload();
                };
            </script>
    </body>
</html>