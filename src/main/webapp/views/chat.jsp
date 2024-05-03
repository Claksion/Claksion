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

    <style>
        body {
            font-family: Arial, sans-serif;
        }
        #status-indicator {
            width: 20px;
            height: 20px;
            border-radius: 50%; /* 원 모양으로 만들기 위해 */
            display: inline-block;
            float: right; /* 오른쪽으로 정렬 */
            margin-left: 10px; /* 오른쪽 여백 추가 */
        }

        .connected {
            background-color: green;
        }

        .disconnected {
            background-color: red;
        }

        .container {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        #chat-window {
            border: 1px solid #ccc;
            padding: 10px;
            height: 400px;
            overflow-y: scroll;
            margin-bottom: 10px;
        }

        #chat-messages {
            overflow-y: auto;
            border: 2px solid gray;
            border-radius: 10px;
            flex-direction: column-reverse; /* 컨텐츠를 역순으로 정렬 */
            height: 350px;
        }

        #input-area {
            display: flex;
            justify-content: flex-end;

        }

        #message-input {
            flex: 1;
            padding: 5px;
            margin-right: 5px;
        }
    </style>
    <script>
        let websocket = {
            stompClient:null, //웹소켓의 클라이언트 소켓이된다. connect 누를때 만들어짐.
            init:function (){
                this.connect()
                let statusIndicator = $('#status-indicator')[0];
                statusIndicator.classList.remove("disconnected");
                statusIndicator.classList.add("connected");
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
                        let chatMessages = $("#chat-messages");
                        chatMessages.append("<h5>" +msg.sendid +":"+msg.message+ "</h5>");
                        chatMessages.scrollTop(chatMessages[0].scrollHeight);
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
<div class=".container height: 100vh;
    width: 100%;" >
    <h2>Chat Room</h2>
    <div>
        <div id="chat-window">
            <div>
                <span style="vertical-align: middle;">실시간</span>
                <div id="status-indicator" class="disconnected"></div>
            </div>
            <div id="chat-messages" style="display: flex; flex-direction: column-reverse; max-height: 300px; overflow-y: auto;"></div>
        </div>
        <div id="input-area">
            <input type="text" id="message-input" placeholder="메시지를 입력하세요...">
            <button id="send-btn" type="button" class="btn btn-primary">전송</button>
        </div>
    </div>
</div>

</body>
</html>
