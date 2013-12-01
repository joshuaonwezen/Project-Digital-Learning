<%-- 
    Document   : homepage
    Created on : Nov 11, 2013, 2:25:25 PM
    Author     : wesley
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
        <!--Company Style-->
        <link rel="stylesheet" type="text/css" href="resources/css/homepage.css">
        <link rel="icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
        <!-- Bootstrap-->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="resources/bootstrap/dist/css/bootstrap.min.css">
        <script src="resources/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="resources/bootstrap/dist/js/alert.js"></script>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home - Info Support</title>
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
                    <li class="active"><a href="/PDL/homepage">Home</a></li>
                    <li><a href="/PDL/courses"><fmt:message key="navbar.course"/></a></li>
                        <c:if test="${loggedInIsAdmin == true}">
                        <li><a href="/PDL/management">Management</a></li>
                        </c:if>
                    <li><a href="/PDL/profile?id=${loggedInUserId}"><fmt:message key="navbar.profile"/></a></li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Settings <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="index.jsp">Logout</a></li>
                            <li class="divider"></li>
                            <li><a href="#">Help</a></li>
                            <li><a href="#">Report a Problem</a></li>
                        </ul>
                    </li>
                </ul>
                <form class="navbar-form navbar-right" role="search" id="searchUser" action="searchUser">
                    <div class="form-group">
                        <input type="text" name="searchQuery" id="searchQuery" class="form-control" placeholder="Search User">
                    </div>
                    <button type="submit" class="btn btn-default">Search</button>
                </form>
            </div><!-- /.navbar-collapse -->
        </nav>
        <!-- eof navbar-->
        <!-- Profile Information -->
        <div class="ProfileInformation">
            <table class="profileinformationtable123">
                <tr class="ProfilePictureHomepage"><td><input type="image" src="resources/images/users_icon.png"</td></tr>
                <tr class="ProfileNameHomepage"><td>${loggedInFirstname}&nbsp;${loggedInLastname}</td></tr>
                <c:if test="${loggedInIsAdmin == true}">
                    <tr class="ProfileRoleHomepage"><td>Admin</td></tr>
                </c:if>
                <tr class="ChatIconHomepage"><td><input type="image" src="resources/images/ChatIconHomepage.png"></td></tr>
            </table>
        </div>    

        <!-- Activity Feed -->
        <div class="ActivityFeed">
            <table class="table table-hover table-bordered">
                <tr>
                    <td colspan="4" class="TableHeader"><fmt:message key="activity.feed"/></th>
                </tr>
                <tr>
                    <td class="Date"><fmt:message key="activity.date"/></td>
                    <td><fmt:message key="activity.title"/></td>
                    <td><fmt:message key="activity.message"/></td>
                </tr>
                <c:forEach var="tempActivity" items="${activityList}" begin="0" end="4">   
                    <tr>
                        <td>${tempActivity.sent}</td>
                        <td>${tempActivity.title}</td>
                        <td class="Message">${tempActivity.message}</td>
                    </tr>
                    </tbody>
                </c:forEach>
            </table>
        </div>
    </div>
    <!-- News Feed -->
    <div class="NewsFeed">
        <table class="table table-hover table-bordered">
            <tr>
                <td colspan="4" class="TableHeader"><fmt:message key="news.feed"/></th>
            </tr>
            <tr>
                <td class="Date"><fmt:message key="activity.date"/></td>
                <td><fmt:message key="news.title"/></td>
                <td><fmt:message key="activity.message"/></td>
            </tr>
            <c:forEach var="tempNewsItem" items="${newsitemList}" begin="0" end="4"> 
                <tr>
                    <td>${tempNewsItem.updated}</td>
                    <td>${tempNewsItem.title}</td>
                    <td class="Message">${tempNewsItem.description}</td>
                </tr>
                </tbody>
            </c:forEach>
        </table>
    </div>
</div>
</body>
</html>
