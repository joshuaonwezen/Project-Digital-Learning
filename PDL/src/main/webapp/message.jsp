<%-- 
    Document   : message
    Created on : Dec 6, 2013, 9:06:11 PM
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
        <link rel="stylesheet" type="text/css" href="resources/css/chat.css">
        <link rel="icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
        <!-- Bootstrap-->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="resources/bootstrap/dist/css/bootstrap.min.css">
        <script src="resources/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="resources/bootstrap/dist/js/alert.js"></script>
        <!-- Chat -->
        <script src="http://31.186.175.82:5001/socket.io/socket.io.js"></script>
        <!-- Select2 -->
        <link href="resources/select2/select2.css" rel="stylesheet"/>
        <script src="resources/select2/select2.js"></script>
        <!-- Moment JS-->
        <script src="resources/moment/moment-m.js" type="text/javascript"></script>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <!-- scripts used for video-conferencing -->
        <script src="js/socket.io.js"></script>
        <script src="js/RTCPeerConnection-v1.5.js"></script>
        <script src="js/conference.js"></script>
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
                <ul class="nav nav-pills nav-stacked">
                    <li>
                        <a href="/PDL/homepage">
                            Home
                        </a>
                    </li>
                    <li>
                        <a href="messages">
                            <fmt:message key="message.messages.message"/>
                        </a>
                    </li>
                    <li class="active">
                        <a href="#">
                            ${chat.subject}
                        </a>
                    </li>
                </ul>
            </div>

            <div id="main_right">
                <div id="chat">
                      <!-- just copy this <section> and next script -->
                <section class="experiment">                
                    <section>
                    <span>
                         <a href="/video-conferencing/" target="_blank" title="Open this link in new tab. Then your conference room will be private!"></a>
                    </span>
                    <section>
                        Want to start a videoconference?  <button id="setup-new-room" class="setup">Switch to video!</button><br>
                    </section>

                    <!-- list of all available conferencing rooms -->
                    <table style="width: 100%;" id="rooms-list"></table>

                     <!-- local/remote videos container -->
                     <div id="videos-container"></div>
                     </section>
                    <div id="chatLeft">
                        <div class="panel panel-default chatOutputStyle">
                            <div class="panel-body">
                                <table class="table" id="chatOutput" name="chatOutput" style="width:725px;margin-left:-15px;margin-top:-16px;">
                                </table>
                            </div>
                        </div>
                    </div>
                    <div id="userRight">
                        <div class="panel panel-default users">
                            <table class="table table-condensed" id="userList">
                                <c:forEach var="user" items="${chat.users}">
                                    <tr class="warning" id="${user.username}"><td>${user.username}</td></tr> 
                                        </c:forEach>
                            </table>
                        </div>
                    </div>
                </div>
                <div id="maint_bot">
                    <form class="form-inline" role="form">
                        <div class="" id="formGroupChatInput">
                            <div class="chatInput">
                                <input type="text" class="form-control" id="chatInput" name="chatInput" onkeyup="toggleSentButton()" placeholder="Enter a message">
                            </div>
                            <div class="chatSend">
                                <button type="button" class="btn btn-primary buttonStyle" disabled id="buttonSent" name="buttonSent" onClick="sentMessage()">Send</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

        </div>
           <script>
            var config = {
                openSocket: function(config) {
                    var SIGNALING_SERVER = 'http://webrtc-signaling.jit.su:80/',
                            defaultChannel = location.hash.substr(1) || 'video-conferencing-hangout';

                    var channel = config.channel || defaultChannel;
                    var sender = Math.round(Math.random() * 999999999) + 999999999;

                    io.connect(SIGNALING_SERVER).emit('new-channel', {
                        channel: channel,
                        sender: sender
                    });

                    var socket = io.connect(SIGNALING_SERVER + channel);
                    socket.channel = channel;
                    socket.on('connect', function() {
                        if (config.callback)
                            config.callback(socket);
                    });

                    socket.send = function(message) {
                        socket.emit('message', {
                            sender: sender,
                            data: message
                        });
                    };

                    socket.on('message', config.onmessage);
                },
                onRemoteStream: function(media) {
                    var video = media.video;

                    video.setAttribute('width', 600);
                    video.setAttribute('controls', true);
                    video.setAttribute('id', media.stream.id);

                    videosContainer.insertBefore(video, videosContainer.firstChild);

                    video.play();
                    rotateVideo(video);

                    setTimeout(function() {
                        // unmute audio stream for echo-cancellation
                        // config.attachStream.getAudioTracks()[0].enabled = true;
                    }, 2000);
                    scaleVideos();
                },
                onRemoteStreamEnded: function(stream) {
                    var video = document.getElementById(stream.id);
                    if (video) {
                        video.style.opacity = 0;
                        rotateVideo(video);
                        setTimeout(function() {
                            video.parentNode.removeChild(video);
                            scaleVideos();
                        }, 1000);
                    }
                },
                onRoomFound: function(room) {
                    var alreadyExist = document.querySelector('button[data-broadcaster="' + room.broadcaster + '"]');
                    if (alreadyExist)
                        return;

                    if (typeof roomsList === 'undefined')
                        roomsList = document.body;

                    var tr = document.createElement('tr');
                    if(room.roomName === '${chat.subject}'){
                    tr.innerHTML = '<td>The chatroom <strong>' + room.roomName + '</strong> has started a videoconference, click here to join:</td>' +
                            '<td><button class="join">Join</button></td>';
                    roomsList.insertBefore(tr, roomsList.firstChild);
                }

                    var joinRoomButton = tr.querySelector('.join');
                    joinRoomButton.setAttribute('data-broadcaster', room.broadcaster);
                    joinRoomButton.setAttribute('data-roomToken', room.roomToken);
                    joinRoomButton.onclick = function() {
                        this.disabled = true;

                        var broadcaster = this.getAttribute('data-broadcaster');
                        var roomToken = this.getAttribute('data-roomToken');
                        captureUserMedia(function() {
                            conferenceUI.joinRoom({
                                roomToken: roomToken,
                                joinUser: broadcaster
                            });
                        });
                    };
                },
                onRoomClosed: function(room) {
                    var joinButton = document.querySelector('button[data-roomToken="' + room.roomToken + '"]');
                    if (joinButton) {
                        // joinButton.parentNode === <li>
                        // joinButton.parentNode.parentNode === <td>
                        // joinButton.parentNode.parentNode.parentNode === <tr>
                        // joinButton.parentNode.parentNode.parentNode.parentNode === <table>
                        joinButton.parentNode.parentNode.parentNode.parentNode.removeChild(joinButton.parentNode.parentNode.parentNode);
                    }
                }
            };
            
            function setupNewRoomButtonClickHandler() {
                btnSetupNewRoom.disabled = true;
                captureUserMedia(function() {
                    var roomId = '${chat.subject}';
                    conferenceUI.createRoom({
                        roomName: ({}).value || '${chat.subject}'                      
                    });
                });
            }

            function captureUserMedia(callback) {
                var video = document.createElement('video');
                video.setAttribute('width', 300);
                video.setAttribute('autoplay', true);
                video.setAttribute('controls', true);
                videosContainer.insertBefore(video, videosContainer.firstChild);

                getUserMedia({
                    video: video,
                    onsuccess: function(stream) {
                        config.attachStream = stream;
                        callback && callback();

                        video.setAttribute('muted', true);
                        rotateVideo(video);
                        scaleVideos();
                    },
                    onerror: function() {
                        alert('unable to get access to your webcam');
                        callback && callback();
                    }
                });
            }

            var conferenceUI = conference(config);

            /* UI specific */
            var videosContainer = document.getElementById('videos-container') || document.body;
            var btnSetupNewRoom = document.getElementById('setup-new-room');
            var roomsList = document.getElementById('rooms-list');

            if (btnSetupNewRoom)
                btnSetupNewRoom.onclick = setupNewRoomButtonClickHandler;

            function rotateVideo(video) {
                video.style[navigator.mozGetUserMedia ? 'transform' : '-webkit-transform'] = 'rotate(0deg)';
                setTimeout(function() {
                    video.style[navigator.mozGetUserMedia ? 'transform' : '-webkit-transform'] = 'rotate(360deg)';
                }, 1000);
            }


            function scaleVideos() {
                var videos = document.querySelectorAll('video'),
                        length = videos.length, video;

                var minus = 130;
                var windowHeight = 700;
                var windowWidth = 600;
                var windowAspectRatio = windowWidth / windowHeight;
                var videoAspectRatio = 4 / 3;
                var blockAspectRatio;
                var tempVideoWidth = 0;
                var maxVideoWidth = 0;

                for (var i = length; i > 0; i--) {
                    blockAspectRatio = i * videoAspectRatio / Math.ceil(length / i);
                    if (blockAspectRatio <= windowAspectRatio) {
                        tempVideoWidth = videoAspectRatio * windowHeight / Math.ceil(length / i);
                    } else {
                        tempVideoWidth = windowWidth / i;
                    }
                    if (tempVideoWidth > maxVideoWidth)
                        maxVideoWidth = tempVideoWidth;
                }
                for (var i = 0; i < length; i++) {
                    video = videos[i];
                    if (video)
                        video.width = maxVideoWidth - minus;
                }
            }

            window.onresize = scaleVideos;

        </script>
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
                var chatId = ${chat.chatId};
                console.log('welcome to chatroom: ' + chatId);

                socket.emit('join room', 'privateRoom ' + chatId);
                socket.emit('userJoined', '${loggedInUsername}');
            });

            //receiving userlist
            socket.on('userList', function(data) {
                refreshUserList(data);
                console.log('userList received: ');
            });

            // receiving a join
            socket.on('userJoined', function(data) {
                //update the output box
                //$("#chatOutput").append(data + ' joined the chat\n');

                //play a sound
                var userJoinedSound = new Audio('resources/sounds/01_-_Warm_Interface_Sound_1.wav');
                userJoinedSound.play();
            });

            // receiving a message
            socket.on('message', function(data) {
                //update the output box
                addRowChatOutput(data);
                //play a sound
                var messageReceivedSound = new Audio('resources/sounds/Interface Alert Sound 3.wav');
                messageReceivedSound.play();

                console.log('message received');
            });

            //receiving the offline messages
            socket.on('offline_messages', function(docs) {
                console.log('received offline message');
                for (var i = 0; i < docs.length; i++) {
                    //update the output box
                    addRowChatOutput(docs[i].msg);
                }
                if (docs.length === 0) {
                    //alway emit a first message in the room so we can display the latest message
                    var message = 'Chat created on ' + moment().format('MMMM Do YYYY, HH:mm');
                    socket.emit('message', message);
                    addRowChatOutput(message);
                    console.log('room joined');
                }
            });

            function sentMessage() {
                var date = new Date();
                var message = '${loggedInUsername}' + '||' + date + '||' + document.getElementById('chatInput').value;
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
                var row;
                var rowCount = table.rows.length;
                var rowPrecedingCount = rowCount - 1;
                var rowPreceding = table.rows[rowPrecedingCount];
                var rowPrecedingClass;
                //data
                var from = '<b>' + data.substring(0, data.indexOf('||')) + '</b>';
                var date;

                //first message doesnt have a user from which its sent
                var message = '';
                if (rowCount === 0) {
                    message = data;
                }
                else {
                    data = data.substring(data.indexOf('||') + 2, data.length);
                    date = new Date(data.substring(0, data.indexOf('||')));
                    message = data.substring(data.indexOf('||') + 2, data.length);
                }

                //add newlines to large messages so they will fit in the row
                if (message.length > 60) {
                    var result = '';
                    while (message.length > 0) {
                        result += message.substring(0, 60) + '\n';
                        message = message.substring(60);
                    }
                    message = result;
                }

                //create row blocks
                var newRowBlock = false;
                if (rowPrecedingCount !== -1) {
                    var precedingUser = rowPreceding.getAttribute('name');
                    var precedingDate = new Date(rowPreceding.getAttribute('received'));
                    var currentDate = new Date();

                    //add to existing block if was sent within on hour by the same user
                    if ((precedingUser === from) && (moment(precedingDate).add('hours', 1) > date)) {
                        console.log('samedate');
                        row = rowPreceding;
                        row.cells[0].innerHTML = row.cells[0].innerHTML + '</br>' + message;
                    }
                    //message from other user then preceding user or time sent is larger than one hour
                    //create new 'block'
                    else {
                        row = table.insertRow(rowCount);
                        row.setAttribute('name', from);
                        row.setAttribute('received', date);
                        var cell1 = row.insertCell(0);
                        cell1.innerHTML = from + '</br>' + message;
                        cell1.style.width='93%';
                        newRowBlock = true;
                        var cell2 = row.insertCell(1);
                        cell2.innerHTML = '<small class="text-muted">' + moment(date).format('HH:mm') + '</small>';
                        cell2.style.width='7%';
                    }
                }
                //first occurence
                else {
                    //create new block if it's the first row
                    row = table.insertRow(rowCount);
                    var cell1 = row.insertCell(0);
                    cell1.innerHTML = message;
                }

                //create new date block
                var dateBlockAdded = false;
                if (rowCount > 1 && (moment(precedingDate).add('days', 1) < date)) {//check if it's a new day since a message was sent
                    console.log('should create new block');
                    //create new tr with date of today
                    var row2 = table.insertRow(rowCount);
                    var cell1 = row2.insertCell(0);
                    cell1.setAttribute('align', 'center');
                    cell1.setAttribute('colSpan', '2');
                    row2.setAttribute('received', date);
                    cell1.innerHTML = '<div class="text-info">' + moment(date).format('ll') + '</div>';
                    dateBlockAdded = true;
                }

                //set row styling
                if (newRowBlock) {
                    if (rowCount === 0) {//base case
                        row.className = 'active';
                    }
                    else {

                        rowPrecedingClass = rowPreceding.className;
                        if (rowPrecedingClass === 'active') {
                            row.className = '';
                        }
                        else {
                            row.className = 'active';
                        }
                    }
                }
                $('div').scrollTop(1000000); // scroll to end of table
            }

            // update the list with user
            function refreshUserList(data) {

                //set which users are online and offline
            <c:forEach var="user" items="${chat.users}">
                var userOnline = false;
                for (var i = 0; i < data.length; i++) {
                    if (data[i] === '${user.username}') {
                        userOnline = true;
                    }
                }
                if (!userOnline) {
                    document.getElementById('${user.username}').className = 'warning';
                }
                else {
                    document.getElementById('${user.username}').className = 'success';
                }
            </c:forEach>
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
