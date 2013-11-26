<%-- 
    Document   : homepage
    Created on : Nov 11, 2013, 2:25:25 PM
    Author     : wesley
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="Shortcut Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
<link rel="Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="resources/css/style.css">
        <link rel="stylesheet" type="text/css" href="resources/css/homepage.css">
        <link rel="stylesheet" type="text/css" href="resources/bootstrap/dist/css/bootstrapm.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home - Info Support</title>
    </head>
    <body>
        <div id="header">
            <div id="header_logo">
                <img src="resources/images/Logo.png">
            </div>
            <div id="header_nav">
                <ul>
                    <li><a href="homepage.jsp">Home</a></li>
                    <li><a href="/PDL/courses">Courses</a></li>
                        <c:if test="${loggedInIsAdmin == true}">
                        <li><a href="/PDL/management">Management</a></li>
                        </c:if>
                        <li><a href="/PDL/profile?id=${loggedInUserId}">My Profile</a></li>
                    <li>
                        <a href="#">Settings</a>
                        <ul>
                            <li><a href="#">Help</a></li>
                            <li><a href="#">Report a Problem</a></li>
                            <li><a href="index.jsp">Log Out</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
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
        <table class="table table-hover table-bordered" end="5">
            <tr>
                <td colspan="4" class="TableHeader">Activity Feed</th>
            </tr>
            <tr>
                <td class="Date">Date</td>
                <td>Title</td>
                <td colspan="2">Message</td>
            </tr>
            <c:forEach var="tempActivity" items="${activityList}">           
            <tr>
                <td>${tempActivity.sent}</td>
                <td>${tempActivity.title}</td>
                <td class="Message">${tempActivity.message}</td>
                <td class="gotoMessage">
                    <input type="image" src="resources/images/gotoMessage.png" href="PDL/activity/message?id=${tempActivity.activityId}';">
                </td>
            </tr>
          </c:forEach>
        </table>
        </div>
        <!-- News Feed -->
        <div class="NewsFeed">
        <table class="table table-hover table-bordered" end="5">
            <tr>
                <td colspan="4" class="TableHeader">News Feed</th>
            </tr>
            <tr>
                <td class="Date">Date</td>
                <td>Title</td>
                <td colspan="2">Message</td>
            </tr>
            <c:forEach var="tempActivity" items="${activityList}">           
            <tr>
                <td>${tempActivity.sent}</td>
                <td>${tempActivity.title}</td>
                <td class="Message">${tempActivity.message}</td>
                <td class="gotoMessage">
                    <input type="image" src="resources/images/gotoMessage.png" href="PDL/activity/message?id=${tempActivity.activityId}';">
                </td>
            </tr>
          </c:forEach>
        </table>
        </div>



    </body>
</html>
