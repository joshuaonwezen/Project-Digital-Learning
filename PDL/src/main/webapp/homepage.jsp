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
<fmt:setBundle basename="index" />

<!DOCTYPE html>
<html  lang="${language}">
    <head>
        <!--Company Style-->
        <link rel="stylesheet" type="text/css" href="resources/css/homepage.css">
        <link rel="icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
        <!-- Bootstrap-->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="resources/bootstrap/dist/css/bootstrap.min.css">
        <script src="resources/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="resources/bootstrap/dist/js/alert.js"></script>
        <!-- Moment JS-->
        <script src="resources/moment/moment-m.js" type="text/javascript"></script>
        <!-- Chat -->
        <script src="http://31.186.175.82:5001/socket.io/socket.io.js"></script>

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
                        <c:if test="${loggedInIsAdmin || loggedInIsTeacher || loggedInIsManager == true}">
                        <li><a href="/PDL/management">Management</a></li>
                        </c:if>
                    <li><a href="/PDL/profile?id=${loggedInUserId}"><fmt:message key="navbar.profile"/></a></li>
                    <c:if test="${loggedInIsAdmin || loggedInIsManager == true}">
                    <li><a href="/PDL/vga">VGA</a></li>
                    </c:if>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><fmt:message key="navbar.settings"/> <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="index.jsp"><fmt:message key="navbar.logout"/></a></li>
                            <li class="divider"></li>
                            <li><a href="#">Help</a></li>
                            <li><a href="#"><fmt:message key="navbar.problem"/></a></li>
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
                <form class="navbar-form navbar-right" role="search" id="searchUser" action="searchUser">
                    <div class="form-group">
                        <input type="text" name="searchQuery" id="searchQuery" class="form-control" placeholder="<fmt:message key="searchbar.search.user"/>">
                    </div>
                    <button type="submit" class="btn btn-default"><fmt:message key="navbar.search"/></button>
                </form>
            </div><!-- /.navbar-collapse -->
        </nav>
        <!-- eof navbar-->
        <div id="main">
            <div id="main_left">
                <ul class="nav nav-pills nav-stacked" style="width:150px">
                    <li class="active">
                        <a href="/PDL/homepage">
                            Home
                        </a>
                    </li>
                    <li>
                        <a href="messages">
                            Messages<span class="badge pull-right" id="notifications"></span>
                        </a>
                    </li>
                </ul>
            </div>

            <div id="main_right">
                <!-- Activity Feed -->
                <div class="conainer_homepage">
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
                                <td>
                                    <script>document.write(moment('${tempActivity.sent}').fromNow());</script>
                                </td>
                                <td>${tempActivity.title}</td>
                                <td class="Message">${tempActivity.message}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
                <!-- News Feed -->
                <div class="conainer_homepage">
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
                                <td>
                                    <script>document.write(moment('${tempNewsItem.updated}').fromNow());</script>
                                </td>
                                <td>${tempNewsItem.title}</td>
                                <td class="Message">${tempNewsItem.description}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
                        <table class="table" id="messagesOverview" name="messagesOverview" style="display:none">
                                <c:forEach var="chat" items="${userChats}">
                            <tr id="${chat.chatId}">
                            </tr>
                                </c:forEach>
                        </table>
        </div>
                        <script>
                            var messagesUnread=0;
            try {
                var socket = io.connect('http://31.186.175.82:5001');
            }
            catch (error) {
                console.log(error);
            }
            // join the room on connect
            socket.on('connect', function(data) {
                //get for each chat the latest message
            <c:forEach var="chat" items="${userChats}">
                socket.emit('getLatestMessage', 'privateRoom ' + ${chat.chatId});
            </c:forEach>
                socket.emit('getUnreadNotifications', '${loggedInUsername}');
            });

            //receiving the latest messages
            socket.on('latestMessage', function(docs) {
                addMessage(docs[docs.length - 1]);
            });

            //receiving the notifications that are unread
            socket.on('unreadNotifications', function(docs) {
                appendUnreadStyle(docs);
            });

            function addMessage(data) {
                var table = document.getElementById('messagesOverview');
                var row = document.getElementById(data.room.replace('privateRoom ', ''));
                var cellLastSent = row.insertCell(0);

                cellLastSent.innerHTML = data.timeSent;
                //cellLastSent.style.display = 'none'; // we only need the date from this cell
            }

            //set row as unread
            function appendUnreadStyle(docs) {
                var table = document.getElementById('messagesOverview');
                var rowsCount = table.rows.length;
                // 1. if there are no docs for any row yet
                if (rowsCount > 0 && docs.length === 0){
                    for (var i=0;i<rowsCount;i++){
                        var rowId = table.rows[i].id;
                        messagesUnread++;
                    }
                }
                // if table has rows, then check for every row if it's unread
                
                //2. set the lastseen for every row
                else if (rowsCount > 0){
                    console.log('rowscount');
                    for (var i=0;i<rowsCount;i++){ // iterate over every row in the table
                        var rowId = table.rows[i].id;
                        var lastReceived = document.getElementById(rowId).cells[0].innerHTML;
                        var dateLastReceived = new Date(lastReceived);
                        var docFound = false;
                        for (var j=0;j<docs.length;j++){
                            var docsRoomId = docs[j].room.replace('privateRoom ', '');
                            if (rowId === docsRoomId){
                                docFound = true;
                               
                                //make sure that  lastseen is later than  last received
                                var lastSeen = docs[j].timeLastSeen;
                                var dateLastSeen = new Date(lastSeen);
                                if (dateLastSeen < dateLastReceived){
                                    //last message is not seen yet; make row unread
                                    messagesUnread++;
                                }
                           }
                        }
                        if (!docFound){
                            //this means that the row is definentely unread
                            messagesUnread++;
                        }
                    }
                }
                if (messagesUnread === 0){
                    document.getElementById('notifications').style.display = 'none';
                }
                else{
                    document.getElementById('notifications').innerHTML = messagesUnread;
                }
                
            }
            
        </script>
    </body>
</html>
