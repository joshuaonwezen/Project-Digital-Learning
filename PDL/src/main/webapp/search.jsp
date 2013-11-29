<%-- 
    Document   : courses
    Created on : 18-nov-2013, 11:21:07
    Author     : Martijn
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="index_nl_NL" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Bootstrap-->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="resources/bootstrap/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="resources/css/profile.css">
        <link rel="stylesheet" href="resources/bootstrap/dist/css/bootstrap.min.css">
        <script src="resources/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="resources/bootstrap/dist/js/alert.js"></script>
        <!-- Company Style -->
        <!--        <link rel="Shortcut Icon" href="resources/images/favicon.ico" type="image/x-icon">
                <link rel="Icon" href="resources/images/favicon.ico" type="image/x-icon">
                <link rel="stylesheet" type="text/css" href="resources/css/style.css">
                <title>Courses</title>
            </head>
            <body>
        <!--Header-->
    <div id="header">
        <div id="header_logo">
            <img src="resources/images/Logo.png">
        </div>
        <div id="header_nav">
            <ul>
                <li><a href="homepage.jsp">Home</a></li>
                <li><a href="/PDL/courses"><fmt:message key="navbar.course"/></a></li>
                    <c:if test="${loggedInIsAdmin == true}">
                    <li><a href="/PDL/management">Management</a></li>
                    </c:if>
                <li><a href="/PDL/profile?id=${loggedInUserId}"><fmt:message key="navbar.profile"/></a></li>
                <li>
                    <a href="#"><fmt:message key="navbar.settings"/></a>
                    <ul>
                        <li><a href="#">Help</a></li>
                        <li><a href="#"><fmt:message key="navbar.problem"/></a></li>
                        <li><a href="index.jsp"><fmt:message key="navbar.logout"/></a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
    <div id="main">
        <!--end of header-->
        <form class="navbar-form navbar-left" role="search" id="searchUser" action="searchUser">
            <div class="form-group">
                <input type="text" name="searchQuery" id="searchQuery" class="form-control" placeholder="Search User">
            </div>
            <button type="submit" class="btn btn-default">Search</button>
        </form>
        <script>
            var usersSize = '${usersSize}';
            console.log('userssize debugsdkjfs: ' + '${usersSize}');
        </script>


        <!--<div class="container">-->
            <table class="table table-hover table-bordered" end="5">
                <tr>
                    <td colspan="4" class="TableHeader"><fmt:message key="search.result"/></th>
                </tr>
                <tr>
                    <td><fmt:message key="search.firstname"/></td>
                    <td><fmt:message key="search.lastname"/></td>
                    <td><fmt:message key="search.username"/></td>
                    <td><fmt:message key="search.email"/></td>
                </tr>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td>${user.firstname}</td>
                        <td>${user.lastname}</td>
                        <td>${user.username}</td>
                        <td>${user.emailAddress}</td>
                    </tr>
                </c:forEach>
            </table>


        <!--</div>-->
    </div>
    <script>
        //give feedback to the user about the results
        console.log('usersSize: ' + usersSize);
        if (usersSize == 0) {
            console.log('showing melding');
            document.getElementById('validationAlert').innerHTML = '<div class="alert alert-danger"><a class="close" data-dismiss="alert">×</a><strong>Oh snap!</strong> Your search did not match any users.</div>';
        }
        else if ('${usersSizeResults}' > 0) {
            document.getElementById('validationAlert').innerHTML = '<div class="alert alert-info"><a class="close" data-dismiss="alert">×</a><strong>Results:</strong> <u>' + usersSize + '</u> User(s) match your search.</div>';
        }
    </script>

</body>
</html>
