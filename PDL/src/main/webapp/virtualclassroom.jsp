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
        <!-- Player -->
        <link href="http://vjs.zencdn.net/4.3/video-js.css" rel="stylesheet">
        <script src="http://vjs.zencdn.net/4.3/video.js"></script>
        <style type="text/css">
            .vjs-default-skin .vjs-play-progress,
            .vjs-default-skin .vjs-volume-level { background-color: #596063 }
            .vjs-default-skin .vjs-control-bar,
            .vjs-default-skin .vjs-big-play-button { background: rgba(8,7,7,0.7) }
            .vjs-default-skin .vjs-slider { background: rgba(8,7,7,0.2333333333333333) }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Virtual Classroom - Info Support</title>

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
                    <li><a href="/PDL/homepage">Home</a></li>
                    <li class="active"><a href="/PDL/courses"><fmt:message key="navbar.course"/></a></li>
                        <c:if test="${loggedInIsAdmin || loggedInIsTeacher || loggedInIsManager == true}">
                        <li><a href="/PDL/management">Management</a></li>
                        </c:if>
                    <li><a href="/PDL/profile?id=${loggedInUserId}"><fmt:message key="navbar.profile"/></a></li>
                    <c:if test="${courseOwner.username == loggedInUsername}">
                    <c:if test="${loggedInIsAdmin || loggedInIsTeacher || loggedInIsManager == true}">
                    <li><a href="/PDL/vga">VGA</a></li>
                    </c:if>
                    <li><a href="/PDL/courses/tutorial?courseId=${courseId}">Help</a></li>
                    </c:if>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><fmt:message key="navbar.settings"/> <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="/PDL/index.jsp"><fmt:message key="navbar.logout"/></a></li>
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
        <c:if test="${courseOwner.username == loggedInUsername}">
        <p style="text-align: center;">
        Press the "Help" button for a detailed tutorial on how to start your lesson.
        </p>
        </c:if>
        <div id="main">
            <div id="main_top">
                <div id="stream">
                          <embed width="100%" height="500px" src="http://www.focusonthefamily.com/family/JWPlayer/mediaplayer.swf" flashvars="allowfullscreen=true&allowscriptaccess=always&autostart=true&shownavigation=true&enablejs=true&volume=50&file=${courseKey}.flv&streamer=rtmp://31.186.175.82/live" />
                </div>
            </div>
            <div id="main_left">
                <div class="panel panel-default chatOutputStyle">
                    <div class="panel-body" style="width:106%;margin-left:-15px;margin-top:-16px;">
                        <table class="table table-striped" id="chatOutput" name="chatOutput">
                        </table>
                    </div>
                </div>
            </div>
            <div id="main_right">
                <div class="panel panel-default users">
                    <table class="table table-condensed" id="userList" style="width:100%;margin-top:-2px;">
                    </table>
                </div>
            </div>
            <div id="maint_bot">
                <form class="form-inline" role="form">
                    <div class="" id="formGroupChatInput">
                        <div class="chatInput">
                            <input type="text" class="form-control" id="chatInput" name="chatInput" onkeyup="toggleSentButton()" placeholder="Enter a message">
                        </div>
                        <div class="chatSend">
                            <button type="button" style="width: 190px;" class="btn btn-default" disabled id="buttonSent" name="buttonSent" onClick="sentMessage()">Send</button>
                        </div>
                    </div>
                </form>
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
            
            socket.on('userList', function(data) {
                console.log('users ' + data);
                
                //add users to the userlist
                for (var i=0;i<data.length;i++){
                    addRowUserList(data[i], i);
                }
                
                
                console.log('userList received: ');
            });

            // receiving a join
            socket.on('userJoined', function(data) {
                //update the output box
                //$("#chatOutput").append(data + ' joined the chat\n');
                
                //play a sound
                var userJoinedSound = new Audio('../resources/sounds/01_-_Warm_Interface_Sound_1.wav');
                userJoinedSound.play();
            });

            // receiving a message
            socket.on('message', function(data) {
                //update the output box
                addRowChatOutput(data);

                console.log('message received');
            });
            
            //receiving the offline messages
            socket.on('offline_messages', function(docs){
                console.log('received offline message');
                for (var i=0; i<docs.length;i++){
                    //update the output box
                    addRowChatOutput(docs[i].msg);
                }
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
            function addRowUserList(data, i) {
                var table = document.getElementById('userList');
                var rowCount = table.rows.length;
                //reset the userlist if we received a new one
                if (i === 0){
                    for (var j=0;j<rowCount;j++){
                       table.deleteRow(0);
                    }
                    rowCount = 0;
                }
                
                var row = table.insertRow(rowCount);
                row.className = "success"; // make the row green
                var cell1 = row.insertCell(0);
                cell1.innerHTML = data;
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
