<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="en">
<head>
    <title>Websocket Chat</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" href="<c:url value='/webjars/bootstrap/4.3.1/css/bootstrap.min.css'/>">
</head>
<body>
<div class="container" id="app">
    <div class="row">
        <div class="col-md-12">
            <h3>채팅방 리스트</h3>
        </div>
    </div>
    <div class="input-group">
        <div class="input-group-prepend">
            <label class="input-group-text">방제목</label>
        </div>
        <input type="text" class="form-control" id="room_name" onkeyup="if(event.keyCode === 13) createRoom();">
        <div class="input-group-append">
            <button class="btn btn-primary" type="button" onclick="createRoom()">채팅방 개설</button>
        </div>
    </div>
    <ul class="list-group" id="chatrooms">
        <!-- 채팅방 목록은 서버로부터 받아옴 -->
    </ul>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        findAllRoom();
    });

    function findAllRoom() {
        fetch('/chat/rooms')
            .then(response => response.json())
            .then(data => {
                const chatroomsElement = document.getElementById('chatrooms');
                chatroomsElement.innerHTML = '';
                data.forEach(item => {
                    const li = document.createElement('li');
                    li.appendChild(document.createTextNode(item.name));
                    li.classList.add('list-group-item', 'list-group-item-action');
                    li.onclick = function() { enterRoom(item.roomId); };
                    chatroomsElement.appendChild(li);
                });
            })
            .catch(error => {
                console.error('Error fetching chat rooms:', error);
            });
    }

    function createRoom() {
        const roomName = document.getElementById('room_name').value;
        if(roomName === "") {
            alert("방 제목을 입력해 주십시오.");
            return;
        }
        const params = new URLSearchParams();
        params.append("name", roomName);
        fetch('/chat/room', {
            method: 'POST',
            body: params
        })
            .then(response => response.json())
            .then(data => {
                alert(data.name + " 방 개설에 성공하였습니다.");
                document.getElementById('room_name').value = '';
                findAllRoom();
            })
            .catch(error => {
                alert("채팅방 개설에 실패하였습니다.");
            });
    }

    function enterRoom(roomId) {
        const sender = prompt('대화명을 입력해 주세요.');
        if(sender !== "") {
            localStorage.setItem('wschat.sender', sender);
            localStorage.setItem('wschat.roomId', roomId);
            location.href = "/chat/room/enter/" + roomId;
        }
    }
</script>
</body>
</html>
