<!DOCTYPE html>
<html lang="en">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="index" />
<html  lang="${language}">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Bootstrap-->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="../resources/bootstrap/dist/css/bootstrap.min.css">
        <script src="../resources/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="../resources/bootstrap/dist/js/alert.js"></script>
        <!-- Select2 -->
        <link href="../resources/select2/select2.css" rel="stylesheet"/>
        <script src="../resources/select2/select2.js"></script>
        <!-- Company Style -->
        <link rel="Shortcut Icon" href="../resources/images/favicon.ico" type="image/x-icon">
        <link rel="Icon" href="../resources/images/favicon.ico" type="image/x-icon">
        <link rel="stylesheet" type="text/css" href="../resources/css/editmode.css">
        <title>Conference - Info Support</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

        <!-- scripts used for video-conferencing -->
        <script src="js/socket.io.js"></script>
        <script src="js/RTCPeerConnection-v1.5.js"></script>
        <script src="js/conference.js"></script>
    </head>
    <body>
        <div class="header">
            <h1>Conference</h1>
        </div>
        <!-- just copy this <section> and next script -->
        <section class="experiment">                
            <section>
                <button id="setup-new-room" class="setup">Join</button>
            </section>

            <!-- list of all available conferencing rooms -->
            <table style="width: 100%;" id="rooms-list"></table>

            <!-- local/remote videos container -->
            <div id="videos-container"></div>
        </section>
        <script>
            var config = {
                openSocket: function(config) {
                    var SIGNALING_SERVER = 'https://www.webrtc-experiment.com:2013/',
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
                    tr.innerHTML = '<td><strong>' + room.roomName + '</strong> shared a conferencing room with you!</td>' +
                            '<td><button class="join">Join</button></td>';
                    roomsList.insertBefore(tr, roomsList.firstChild);

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
                document.getElementById('conference-name').disabled = true;
                captureUserMedia(function() {
                    conferenceUI.createRoom({
                        roomName: (document.getElementById('conference-name') || {}).value || 'Anonymous'
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

            (function() {
                var uniqueToken = document.getElementById('unique-token');
                if (uniqueToken)
                    if (location.hash.length > 2)
                        uniqueToken.parentNode.parentNode.parentNode.innerHTML = '<h2 style="text-align:center;"><a href="' + location.href + '" target="_blank">Share this link</a></h2>';
                    else
                        uniqueToken.innerHTML = uniqueToken.parentNode.parentNode.href = '#' + (Math.random() * new Date().getTime()).toString(36).toUpperCase().replace(/\./g, '-');
            })();

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
    </body>
</html>