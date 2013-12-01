<!--
@author Shahin Mokhtar
-->
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="index" />
<!DOCTYPE html>
<html  lang="${language}">
    <head>
        <meta charset="UTF-8">
        <title>Profile - Info Support</title>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
        <!-- Bootstrap-->
        <!-- Company Style-->
        <link rel="stylesheet" type="text/css" href="resources/css/profile.css">
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="resources/bootstrap/dist/css/bootstrap.min.css">
        <script src="resources/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="resources/bootstrap/dist/js/alert.js"></script>
        <link rel="stylesheet" href="resources/bootstrap/dist/css/bootstrap.min.css">
        <link rel="Shortcut Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
        <link rel="Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
    </head>
    <body>
        <!--Start nav bar-->
        <nav class="navbar navbar-inverse" role="navigation">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="/PDL/homepage"><img src="resources/images/Logo.png"></a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" style="margin-top:12px">
                <ul class="nav navbar-nav">
                    <li><a href="/PDL/homepage">Home</a></li>
                    <li><a href="/PDL/courses"><fmt:message key="navbar.course"/></a></li>
                        <c:if test="${loggedInIsAdmin == true}">
                        <li><a href="/PDL/management">Management</a></li>
                        </c:if>
                    <li><a href="/PDL/profile?id=${loggedInUserId}"><fmt:message key="navbar.profile"/></a></li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Settings <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="index.jsp">Logout</a></li>
                                <c:if test="${loggedInUserId == userId}">
                                <li><a id="toggle"><fmt:message key="profile.edit"/></a></li>
                                </c:if>
                            <li><a href="cv?id=${userId}" target="_blank"><fmt:message key="navbar.cv"/></a></li>
                            <li class="divider"></li>
                            <li><a href="#">Help</a></li>
                            <li><a href="#">Report a Problem</a></li>
                            <li>
                                <a >
                                    <form>
                                        <select id="language" name="language" onchange="submit()">
                                            <option value="en_US" ${language == 'en_US' ? 'selected' : ''}>English</option>
                                            <option value="nl_NL" ${language == 'nl_NL' ? 'selected' : ''}>Nederlands</option>
                                        </select>
                                    </form>
                                </a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div><!-- /.navbar-collapse -->
        </nav>
        <div id="main">
            <!--end of header-->

            <script>
                var usersSize = '${usersSize}';
                console.log('userssize debugsdkjfs: ' + '${usersSize}');
            </script>


            <!--<div class="container">-->
            <table class="table table-hover table-bordered" end="5">
                <tr>
                    <th colspan="4" class="TableHeader">
                <form class="navbar-form navbar-left" role="search" id="searchUser" action="searchUser">
                    <div class="form-group">
                        <input type="text" name="searchQuery" id="searchQuery" class="form-control" placeholder="Search User">
                    </div>
                    <button type="submit" class="btn btn-default">Search</button>
                </form>
                </th>
                </tr>
                <tr>
                    <td><fmt:message key="search.firstname"/></td>
                    <td><fmt:message key="search.lastname"/></td>
                    <td><fmt:message key="search.username"/></td>
                    <td><fmt:message key="search.email"/></td>
                </tr>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td><a href="/PDL/profile?id=${user.userId}">${user.firstname}</a></td>
                        <td><a href="/PDL/profile?id=${user.userId}">${user.lastname}</a></td>
                        <td><a href="/PDL/profile?id=${user.userId}">${user.username}</a></td>
                        <td><a href="/PDL/profile?id=${user.userId}">${user.emailAddress}</a></td>
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
