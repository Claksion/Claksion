<%--
  Created by IntelliJ IDEA.
  User: campus2H052
  Date: 2024-05-03
  Time: 오전 9:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
  <title>Websocket ChatRoom</title>
  <!-- Required meta tags -->
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

  <!-- Bootstrap CSS -->
  <link rel="stylesheet" href="<c:url value="//webjars/bootstrap/4.3.1/dist/css/bootstrap.min.css"/>">
</head>
<body>
<div class="container" id="app">
  <div>
    <h2 id="roomName">Loading...</h2>
  </div>
  <div class="input-group">
    <div class="input-group-prepend">
      <label class="input-group-text">내용</label>
    </div>
    <input type="text" class="form-control" id="messageInput" onkeydown="if(event.key === 'Enter') sendMessage()">
    <div class="input-group-append">
      <button class="btn btn-primary" type="button" onclick="sendMessage()">보내기</button>
    </div>
  </div>
  <ul class="list-group" id="messageList">
  </ul>
</div>
<!-- JavaScript Libraries -->
<script src="<c:url value="//webjars/sockjs-client/1.1.2/sockjs.min.js"/>"></script>
<script src="<c:url value="//webjars/stomp-websocket/2.3.3-1/stomp.min.js"/>"></script>
<script>
  let sock = new SockJS("/ws-stomp");
  let ws = Stomp.over(sock);
  let roomId = localStorage.getItem('wschat.roomId');
  let sender = localStorage.getItem('wschat.sender');

  function findRoom() {
    fetch('/chat/room/' + roomId)
            .then(response => response.json())
            .then(data => {
              document.getElementById('roomName').textContent = data.name;
            })
            .catch(error => console.error('Error fetching room:', error));
  }

  function sendMessage() {
    let messageInput = document.getElementById('messageInput');
    let message = messageInput.value;
    ws.send("/pub/chat/message", {}, JSON.stringify({type: 'TALK', roomId: roomId, sender: sender, message: message}));
    messageInput.value = '';
  }

  function recvMessage(recv) {
    let messageList = document.getElementById('messageList');
    let newMessage = document.createElement('li');
    newMessage.classList.add('list-group-item');
    newMessage.textContent = (recv.type === 'ENTER' ? '[알림]' : recv.sender) + " - " + recv.message;
    messageList.insertBefore(newMessage, messageList.firstChild);
  }

  function connect() {
    ws.connect({}, function(frame) {
      ws.subscribe("/sub/chat/room/" + roomId, function(message) {
        let recv = JSON.parse(message.body);
        recvMessage(recv);
      });
      ws.send("/pub/chat/message", {}, JSON.stringify({type: 'ENTER', roomId: roomId, sender: sender}));
    }, function(error) {
      console.log("WebSocket connection error. Attempting to reconnect...");
      setTimeout(connect, 10000); // Try to reconnect every 10 seconds
    });
  }

  connect();
  findRoom();
</script>
</body>
</html>
