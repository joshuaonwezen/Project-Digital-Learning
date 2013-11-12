<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>${paginaTitel}</title>
        <link href="/WEBappMVCMavenSolution/style.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <h2>${paginaTitel}</h2>
        <c:if test="${errors != null}">
            <!-- Mochten er errors zijn, dan worden ze hier getoond -->
            <p>${errors}</p>
        </c:if>
        <c:choose>
            <c:when test="${id == null}">
                <!-- Als er geen id is meegegeven, ga je een product toevoegen -->
                <form id="nieuweWork" action="nieuw" method="post">
                </c:when>
                <c:otherwise>
                    <!-- Anders ga je een experience wijzigen -->
                    <form id="wijzigenWork" action="wijzig" method="post">
                    </c:otherwise>
                </c:choose>
                <p>
                    <c:if test="${id != null}">
                        <!-- Het id wordt meegestuurd, om te bepalen welke gebruiker je gaat wijzigen -->
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
                            <label for="userId">User</label>
                        </td>
                        <td>
                            <select id="user" name="user" >
                                <c:forEach var="tempProject" items="${projectList}">
                                    <option value="${tempProject.userId}">${tempProject.firstname} ${tempProject.lastname}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                </table>
                </p>
                <p>
                    <input class="submit" type="submit" value="Verzenden">
                </p>
            </form>
    </body>
</html>