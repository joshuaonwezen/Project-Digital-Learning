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
        <title>${update == true ? 'Edit' : 'Create'} Project - Info Support</title>
    </head>
    <body>
        <h2>${update == true ? 'Edit' : 'Create'} Project</h2>
        <c:if test="${errors != null}">
            <!-- Mochten er errors zijn, dan worden ze hier getoond -->
            <p>${errors}</p>
        </c:if>
        <c:choose>
            <c:when test="${id == null}">
                <form id="newProject" action="new" method="post">
                </c:when>
                <c:otherwise>
                    <form id="editProject" action="edit" method="post">
                    </c:otherwise>
                </c:choose>
                <c:if test="${id != null}">
                    <!-- Het id wordt meegestuurd -->
                    <input type="hidden" name="id" id="id" value="${id}"></input>
                </c:if>
                <table border="0">
                    <tr>
                        <td>
                            <label for="fromYear">From</label>
                        </td>
                        <td>
                            <input type="textfield" id="fromYear" name="fromYear" value="${fromYear}"></input>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="tillYear">Till</label>
                        </td>
                        <td>
                            <input type="textfield" id="tillYear" name="tillYear" value="${tillYear}"></input>
                        </td>
                    </tr>
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
                            <label for="profession">Profession</label>
                        </td>
                        <td>
                            <input type="textfield" id="profession" name="profession" value="${profession}"></input>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="description">Description</label>
                        </td>
                        <td>
                            <input type="textfield" id="description" name="description" value="${description}"></input>
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
                    <input class="submit" type="submit" value="Save">
                </p>
            </form>
            <p>
                <a href="/Project%20Digital%20Learning/profile?id=${loggedInUserId}">
                    <button>Cancel</button>
                </a>
            </p>
    </body>
</html>