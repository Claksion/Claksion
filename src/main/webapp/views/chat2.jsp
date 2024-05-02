<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chat Room</title>
    <link rel="stylesheet" href="<c:url value="//maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"/>">
    <script src="<c:url value="//cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.min.js"/>"></script>
    <script src="<c:url value="//cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"/>"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

    <script>
        let websocket = {
            stompClient:null, //웹소켓의 클라이언트 소켓이된다. connect 누를때 만들어짐.
            init:function (){
                this.connect()
                $('#send-btn').click(function() {
                    let messageContent = $('#message-input').val();
                    if (messageContent && websocket.stompClient) {
                        let chatMessage = {
                            sendid: "${sessionScope.userName}",
                            message: messageContent,
                            type: 'TALK'
                        };
                        websocket.stompClient.send("/pub/chat/send", {}, JSON.stringify(chatMessage));
                        $('#message-input').val('');
                    }
                });
            },
            connect:function (){
                // 소켓생성
                let socket = new SockJS('${serverurl}/ws'); // 서버의 WebSocket 경로
                websocket.stompClient = Stomp.over(socket);
                websocket.stompClient.connect({}, function(frame) {
                    console.log('Connected: ' + frame);
                    websocket.stompClient.subscribe('/sub/chat/room/1', function(message) {
                        let msg = JSON.parse(message.body);
                        console.log("Received message:", msg);
                        $("#chat-messages").prepend("<h5>" +msg.sendid +":"+msg.message+ "</h5>");
                    });
                });
            },
        };
        $(function (){
            websocket.init();
        });
    </script>

</head>
<body>
<div class="container mt-5">
    <h2>Chat Room</h2>
    <div id="chat-messages" class="border p-3 mb-2" style="height: 300px; overflow-y: scroll;"></div>
    <textarea id="message-input" class="form-control mb-2" rows="3"></textarea>
    <button id="send-btn" type="button" class="btn btn-primary">Send Message</button>
</div>

</body>
</html>
