<%-- 
    Document   : virtualclassroom
    Created on : 2-dec-2013, 10:56:56
    Author     : Joshua
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
        <link rel="stylesheet" type="text/css" href="../resources/css/virtualclassroom.css">
        <link rel="icon" href="../resources/images/favicon.ico" type="image/x-icon"></link>
        <!-- Bootstrap-->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="../resources/bootstrap/dist/css/bootstrap.min.css">
        <script src="../resources/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="../resources/bootstrap/dist/js/alert.js"></script>
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
                <a class="navbar-brand" href="/PDL/homepage"><img src="../resources/images/Logo.png"></a>
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
            </div>
        </nav>
        <div id="stream" style="margin-left:5px">
           <embed width="768" height="456" src="http://www.focusonthefamily.com/family/JWPlayer/mediaplayer.swf" flashvars="allowfullscreen=true&allowscriptaccess=always&autostart=true&shownavigation=true&enablejs=true&volume=50&file=test.flv&streamer=rtmp://31.186.175.82/live" />
        <div id="chat">
            <div class="panel panel-default" style="width:40%;margin-left:750px;height:300px; overflow: scroll;overflow-x: hidden">
                <div class="panel-body" style="width:106%;margin-left:-15px;margin-top:-16px;">
                    <table class="table table-striped" id="chatOutput" name="chatOutput">
                    </table>
                </div>
            </div>
            <form class="form-inline" role="form">
                <div class="form-group" id="formGroupChatInput" style="width:40%;margin-left:750px;height:300px;">
                    <input type="text" class="form-control" id="chatInput" name="chatInput" onkeyup="toggleSentButton()" style="width:88%" placeholder="Enter a message">
                    <button type="button" disabled class="btn btn-primary" id="buttonSent" name="buttonSent" onClick="sentMessage()">Sent</button>
                </div>
            </form>
            
             <div class="panel panel-default" style="width:14%;margin-left:350px;height:300px; overflow: scroll;overflow-x: hidden">
                <table class="table table-condensed" id="userList" style="width:100%;margin-top:-2px;">
            </table>
             </div>
            
        </div>
        <script>
            try {
                var socket = io.connect('http://31.186.175.82:5001');
            }
            catch (error) {
                // this error states that a connection cannot be established
                // disable the chatInput to show this
                if (error.toString() === 'ReferenceError: io is not defined') {
                    document.getElementById('chatInput').disabled = true;
                    document.getElementById('chatInput').placeholder = 'Cannot establish connection to the chat server';
                }
            }
            // join the room on connect
            socket.on('connect', function(data) {
                var courseId = ${courseId};
                console.log('welcome to chatroom: ' + courseId);

                socket.emit('join room', 'room ' + courseId);
                socket.emit('userJoined', '${loggedInUsername}');
                
                console.log('room joined');
            });
            
             //receiving userlist
            var users = new Array; // this variable holds all the users that are currently connected to this room
            socket.on('userList', function(data) {
                console.log('users ' + data);
                
                //first make sure that we don't add duplicate users to the list
                for (var i=0;i<data.length;i++){
                    var found = false;
                    for (var j=0;j<users.length;j++){
                        if (data[i] === users[j]){
                            found = true;
                            console.log('found duplicate');
                        }
                    }
                    if (!found){    // if not found it means that this user is not already in our userslist
                        users.push(data[i]);
                        found=false;
                        console.log('found no duplicate');
                    }
                }
                for (var i=0;i<users.length;i++){
                    addRowUserList(users[i]);
                }
                
                
                console.log('userList received: ');
            });

            // receiving a join
            socket.on('userJoined', function(data) {
                //update the output box
                //$("#chatOutput").append(data + ' joined the chat\n');
            });

            // receiving a message
            socket.on('message', function(data) {
                //update the output box
                addRowChatOutput(data);

                console.log('message received');
            });

            function sentMessage() {
                var message = '${loggedInUsername}' + ':' + document.getElementById('chatInput').value;
                // emit the message
                socket.emit('message', message);

                // update the output and input boxes
                addRowChatOutput(message);
                document.getElementById('chatInput').value = '';
                toggleSentButton();
                console.log('message sent');
            }

            // add a row to the table which contains the messages
            function addRowChatOutput(data) {
                var table = document.getElementById('chatOutput');

                var rowCount = table.rows.length;
                var row = table.insertRow(rowCount);
                var cell1 = row.insertCell(0);
                cell1.innerHTML = data;
            }

            // add a row to the table which contains the users
            function addRowUserList(data) {
                var table = document.getElementById('userList');

                var rowCount = table.rows.length;
                
                //prevent duplicate entries
                var found = false;
                for (var i=0;i<rowCount;i++){
                    var rowData = table.rows[i].cells[0].innerHTML;
                    if (data === rowData){
                        found = true;
                    }
                }
                if (!found){
                    var row = table.insertRow(rowCount);
                    row.className = "success"; // make the row green
                    var cell1 = row.insertCell(0);
                    cell1.innerHTML = data;
                }
            }
            // block the sent button if there is no input in the chatInput box
            function toggleSentButton() {
                if (document.getElementById('chatInput').value.length > 0) {
                    document.getElementById('buttonSent').disabled = false;
                }
                else {
                    document.getElementById('buttonSent').disabled = true;
                }
            }
        </script>
    </body>
</html>
