<%-- 
    Document   : homepage
    Created on : Nov 11, 2013, 2:25:25 PM
    Author     : wesley

    @todo the should be one modal alert for creating/editing a chat
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
                        <c:if test="${loggedInIsAdmin || loggedInIsTeacher || loggedInIsManager == true}">
                        <li><a href="/PDL/management">Management</a></li>
                        </c:if>
                    <li><a href="/PDL/profile?id=${loggedInUserId}"><fmt:message key="navbar.profile"/></a></li>
                    <c:if test="${loggedInIsAdmin || loggedInIsTeacher || loggedInIsManager == true}">
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
            </div><!-- /.navbar-collapse -->
        </nav>
        <!-- eof navbar-->
        <div id="main_left">
                <ul class="nav nav-pills nav-stacked" style="width:150px">
                    <li>
                        <a href="/PDL/homepage">
                            Home
                        </a>
                    </li>
                    <li class="active">
                        <a href="messages">
                            Messages<span class="badge pull-right" id="notifications"></span>
                        </a>
                    </li>
                </ul><br/>
             <button type="button" class="btn btn-default" id="createMessage" onClick="$('#createChat').modal('show')" style="width:150px">New Message</button>

            
            </div>
        
        <div id="main_right">

            <div id="connectionAlert"></div>
           
            <c:choose>
                <c:when test="${chatsSize == 0}">
                    <div class="alert alert-warning" style="margin-left:20px;margin-right:20px">
                        <a class="close" data-dismiss="alert">×</a>
                        <strong>Nothing available</strong> Press the create button to start a new Message.
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="table" id="messagesOverview" name="messagesOverview">
                        <tr><th>Subject</th><th>Users</th><th>Last Message</th><th>Last Received</th><th>Manage</th></tr>
                                <c:forEach var="chat" items="${userChats}">
                            <tr id="${chat.chatId}">
                                <td><a href="message?chatId=${chat.chatId}">${chat.subject}</a>
                                </td>
                                <td>
                                    <c:forEach var="user" items="${chat.users}">
                                        ${user.username}
                                    </c:forEach>
                                </td>
                                <td>
                                    <c:if test="${chat.created.userId == loggedInUserId}">
                                        <form id="deleteMessage" action="deleteMessage" method="post" style="margin: 0; padding: 0;display:inline">
                                            <input type="hidden" id="chatToDelete" name="chatToDelete"/>
                                            <button type="button" class="btn btn-default btn-xs" id="btnDelete" onclick="$('#confirmDelete').modal('show');
                                                    document.getElementById('chatToDelete').value =${chat.chatId}">
                                                <span class="glyphicon glyphicon-trash"></span>
                                            </button>
                                        </form>
                                        <button type="button" class="btn btn-default btn-xs" id ="btnManage" onclick="showManageMessage(${chat.chatId}, '${chat.subject}');">
                                            <span class="glyphicon glyphicon-cog"></span>
                                        </button>
                                    </c:if>
                                    <c:if test="${chat.created.userId != loggedInUserId}">
                                        <form id="removeUserFromMessage" action="removeUserFromMessage" method="post" style="margin: 0; padding: 0;display:inline">
                                            <input type="hidden" id="chatToRemoveUser" name="chatToRemoveUser"/>
                                            <button type="button" class="btn btn-default btn-xs" id="btnRemove" onclick="$('#confirmRemove').modal('show');
                                                    document.getElementById('chatToRemoveUser').value =${chat.chatId}">
                                                <span class="glyphicon glyphicon-remove"></span>
                                            </button>
                                        </form>
                                    </c:if>

                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:otherwise>
            </c:choose>

            <!-- Modal Dialog for Creating a new Chat -->
            <div class="modal fade" id="createChat">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title">Compose New Message</h4>
                        </div>
                        <div class="modal-body">
                            <form class="form-horizontal" role="form" id="newMessage" action="createMessage" method="post">

                                <div class="form-group">
                                    <label for="subject" class="col-sm-2 control-label">Subject</label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" id="subject" name="subject" onkeyup="toggleCreateButton()" placeholder="Enter a subject">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="users" class="col-sm-2 control-label">Users</label>
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

            <!-- Modal Dialog for Managing/Editing a new Chat -->
            <div class="modal fade" id="manageChat">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title">Manage Message</h4>
                        </div>
                        <div class="modal-body">
                            <form class="form-horizontal" role="form" id="manageMessage" action="manageMessage" method="post">
                                <input type="hidden" id="chatToManage" name="chatToManage"/>
                                <div class="form-group">
                                    <label for="subjectUpdate" class="col-sm-2 control-label">Subject</label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" id="subjectUpdate" name="subjectUpdate" onkeyup="toggleUpdateButton()" placeholder="Enter a subject">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="users" class="col-sm-2 control-label">Users</label>
                                    <div class="col-sm-10">
                                        <input type="hidden" id="tagManageUsers" name="tagManageUsers" placeholder="&nbsp;Enter Users" style="width:100%">
                                        <script>
                                            //set all available users from the database in the multiselect
                                            var arrUsers = new Array();
                                            <c:forEach var='user' items='${users}'>
                                            arrUsers.push('${user.username}');
                                            </c:forEach>
                                            $('#tagManageUsers').select2({tags: arrUsers, tokenSeparators: [",", " "], createSearchChoice: false});
                                        </script> 
                                    </div>
                                </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                            <button type="submit" class="btn btn-primary" id="update" name="update" disabled>Update</button>
                        </div>
                        </form>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
        <script>
            var messagesUnread=0;
            try {
                var socket = io.connect('http://31.186.175.82:5001');
            }
            catch (error) {
                //block all elements that should not be used when there is no connection and show a message
                document.getElementById('connectionAlert').innerHTML = '<div class="alert alert-danger"><a class="close" data-dismiss="alert">×</a><strong>Oh snap!</strong> Cannot establish connection to the chatserver.</div>';
                document.getElementById('createMessage').style.display = 'none';
                document.getElementById('messagesOverview').style.display = 'none';
            }
            // join the room on connect
            socket.on('connect', function(data) {
                //get for each chat the latest message
            <c:forEach var="chat" items="${userChats}">
                socket.emit('getLatestMessage', 'privateRoom ' + ${chat.chatId});
            </c:forEach>
                socket.emit('getUnreadNotifications', '${loggedInUsername}');
                console.log('request for latest message sent');
            });

            //receiving the latest messages
            socket.on('latestMessage', function(docs) {
                console.log('received latest message: ' + docs[docs.length - 1].msg);
                addMessage(docs[docs.length - 1]);
            });

            //receiving the notifications that are unread
            socket.on('unreadNotifications', function(docs) {
                appendUnreadStyle(docs);
            });

            function addMessage(data) {
                var table = document.getElementById('messagesOverview');
                var row = document.getElementById(data.room.replace('privateRoom ', ''));
                var cellMessage = row.insertCell(2);
                var cellData = row.insertCell(3);
                var cellLastSent = row.insertCell(5);

                cellMessage.innerHTML = data.msg;
                cellData.innerHTML = moment(data.timeSent).fromNow();
                cellLastSent.innerHTML = data.timeSent;
                cellLastSent.style.display = 'none'; // we only need the date from this cell
            }

            //set row as unread
            function appendUnreadStyle(docs) {
                var table = document.getElementById('messagesOverview');
                var rowsCount = table.rows.length-1; //minus 1 for the header
                
                // 1. if there are no docs for any row yet
                if (rowsCount > 0 && docs.length === 0){
                    for (var i=0;i<rowsCount;i++){
                        var rowId = table.rows[i+1].id;
                        document.getElementById(rowId).style.fontWeight = 'bold';
                        messagesUnread++;
                    }
                }
                // if table has rows, then check for every row if it's unread
                
                //2. set the lastseen for every row
                else if (rowsCount > 0){
                    for (var i=0;i<rowsCount;i++){ // iterate over every row in the table
                        var rowId = table.rows[i+1].id;
                        var lastReceived = document.getElementById(rowId).cells[5].innerHTML;
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
                                    document.getElementById(rowId).style.fontWeight = 'bold';
                                    messagesUnread++;
                                }
                           }
                        }
                        if (!docFound){
                            //this means that the row is definentely unread
                            document.getElementById(rowId).style.fontWeight = 'bold';
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


            // block the create button if there is no input in the subject feld
            function toggleCreateButton() {
                if (document.getElementById('subject').value.length > 0) {
                    document.getElementById('create').disabled = false;
                }
                else {
                    document.getElementById('create').disabled = true;
                }
            }
            // block the update button if there is no input in the subject feld
            function toggleUpdateButton() {
                if (document.getElementById('subjectUpdate').value.length > 0) {
                    document.getElementById('update').disabled = false;
                }
                else {
                    document.getElementById('update').disabled = true;
                }
            }

            //delete a chat
            function deleteChat() {
                console.log('lastTime: ' + document.getElementById('chatToDelete').value);
                document.getElementById('deleteMessage').submit();
            }

            //remove a user from the message
            function removeUserFromChat() {
                document.getElementById('removeUserFromMessage').submit();
            }

            //show the dialog for managing a chat
            function showManageMessage(chatId, subject) {
                //set the general values
                document.getElementById("chatToManage").value = chatId;
                document.getElementById('subjectUpdate').value = subject;

                //set the users that are already linked to the chat
                var linkedUsers = new Array();
            <c:forEach var="chat" items="${userChats}">
                if (${chat.chatId} === chatId) {
                <c:forEach var="user" items="${chat.users}">
                    if (${user.userId} !== ${loggedInUserId}) {
                        linkedUsers.push('${user.username}');
                    }
                </c:forEach>
                }
            </c:forEach>
                $('#tagManageUsers').select2('val', [linkedUsers]);

                toggleUpdateButton();
                $('#manageChat').modal('show');
            }
        </script>
        <!-- Modal Dialog for Deleting -->
        <div class="modal fade" id="confirmDelete">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title">Confirm Delete</h4>
                    </div>
                    <div class="modal-body">
                        <p>All Users will be removed from this message. Are you sure you want to delete this message?</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                        <button type="button" class="btn btn-primary" onclick="deleteChat()"><fmt:message key="edit.popup.yes"/></button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
    </body>
    <!-- Modal Dialog for Removing -->
    <div class="modal fade" id="confirmRemove">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">Confirm Leave</h4>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to leave this message?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-primary" onclick="removeUserFromChat()"><fmt:message key="edit.popup.yes"/></button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</body>
</html>
