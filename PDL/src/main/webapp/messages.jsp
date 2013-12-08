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
        <!-- Select2 -->
        <link href="resources/select2/select2.css" rel="stylesheet"/>
        <script src="resources/select2/select2.js"></script>
        <!-- Chat -->
        <script src="http://31.186.175.82:5001/socket.io/socket.io.js"></script>
        <!-- Moment JS-->
        <script src="resources/moment/moment-m.js" type="text/javascript"></script>

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
            
            <div id="connectionAlert"></div>
                <button type="button" class="btn btn-default" onClick="$('#myModal').modal('show')">New Message</button>
            
                <c:choose>
            <c:when test="${chatsSize == 0}">
                <div class="alert alert-warning" style="margin-left:20px;margin-right:20px">
                        <a class="close" data-dismiss="alert">×</a>
                        <strong>Nothing available</strong> Press the create button to start a new Message.
                </div>
            </c:when>
            <c:otherwise>
                <table class="table" id="messagesOverview" name="messagesOverview">
                    <tr><th>Subject</th><th>Users</th><th>Last Message</th><th>Last Received</th></tr>
                <c:forEach var="chat" items="${userChats}">
                        <tr id="${chat.chatId}">
                            <td><a href="message?chatId=${chat.chatId}">${chat.subject}</a>
                            </td>
                            <td>
                                <c:forEach var="user" items="${chat.users}">
                                   ${user.username}
                                </c:forEach>
                            </td>
                        </tr>
                </c:forEach>
  
                </table>
            </c:otherwise>
                </c:choose>
                
            <!-- Modal Dialog for Creating a new Chat -->
            <div class="modal fade" id="myModal">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title">Compose New Message</h4>
                        </div>
                        <div class="modal-body">
                            <form class="form-horizontal" role="form" id="newChat" action="message" method="post">
                                
                                <div class="form-group">
                                    <label for="subject" class="col-sm-2 control-label">Subject</label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" id="subject" name="subject" onkeyup="toggleCreateButton()" placeholder="Enter a subject">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="subject" class="col-sm-2 control-label">Users</label>
                                    <div class="col-sm-10">
                                        <input type="hidden" id="tagUsers" name="tagUsers" placeholder="&nbsp;Enter Users" style="width:100%">
                                        <script>
                                            //set all available users from the database in the multiselect
                                            var arrUsers = new Array();
                                            <c:forEach var='user' items='${users}'>
                                            arrUsers.push('${user.username}');
                                            </c:forEach>
                                            $('#tagUsers').select2({tags: arrUsers, tokenSeparators: [",", " "], createSearchChoice: false});
                                        </script> 
                                    </div>
                                </div>

                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                            <button type="submit" class="btn btn-primary" id="create" name="create" disabled>Create</button>
                        </div>
                    </form>
                        
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
        <script>
            try {
                var socket = io.connect('http://31.186.175.82:5001');
            }
            catch (error) {
                document.getElementById('connectionAlert').innerHTML = '<div class="alert alert-danger"><a class="close" data-dismiss="alert">×</a><strong>Oh snap!</strong> Cannot establish connection to the chatserver.</div>';
              
            }
            // join the room on connect
            socket.on('connect', function(data) {
                //get for each chat the latest message
                <c:forEach var="chat" items="${userChats}">
                    socket.emit('getLatestMessage', 'privateRoom ' + ${chat.chatId});
                </c:forEach>
                console.log('request for latest message sent');
            });
            
            //receiving the latest messages
            socket.on('latestMessage', function(docs){
                console.log('received latest message: ' + docs[docs.length-1].msg);
                addMessage(docs[docs.length-1]);
                
            });

            function addMessage(data){
                var table = document.getElementById('messagesOverview');
                var row = document.getElementById(data.room.replace('privateRoom ', ''));
                var cellMessage = row.insertCell(2);
                var cellData = row.insertCell(3);
                
                cellMessage.innerHTML=data.msg;
                cellData.innerHTML=moment(data.timeSent).fromNow();
            }
            
            // block the create button if there is no input in the subject feld
            function toggleCreateButton() {
                if (document.getElementById('subject').value.length > 0) {
                    document.getElementById('create').disabled = false;
                }
                else {
                    document.getElementById('create').disabled = true;
                }
            }
        </script>
    </body>
</html>
