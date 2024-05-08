<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chat Room</title>
    <link rel="stylesheet" href="<c:url value='//cdn.jsdelivr.net/npm/bootstrap@5.3.0/css/bootstrap.min.css'/>">
    <script src="<c:url value='//cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js'/>"></script>
    <script src="<c:url value='//cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.min.js'/>"></script>
    <script src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js'/>"></script>
    <script src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.0/sockjs.min.js'/>"></script>
    <script src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js'/>"></script>

    <style>
        :root {
            --bg-color: #282c34;
            --text-color: #ddd;
            --btn-bg-color: #08a;
            --input-bg-color: #444;
            --msg-in-color: #595f72;
            --msg-out-color: #4a4e69;
        }

        body, html {
            width: 100vw;
            height: 100vh;
            margin: 0;
            font-family: Arial, sans-serif;
        }

        #wrapper {
            border-radius: 10px;
            display: flex;
            height: 100%;
        }

        #sidebar {
            width: 20vw;
            background-color: white;;
            color: white;
            overflow-y: auto;
            border-radius: 15px;
            margin-right: 20px;
            overflow-y: auto;
            height: calc(100vh - 128px);
        }
        #sidebar::-webkit-scrollbar {
            width: 10px;
            background-color: black;
            border-radius: 10px;
        }
        #sidebar::-webkit-scrollbar-thumb {
            background: #696cff;; /* 스크롤바 색상 */
            border-radius: 10px; /* 스크롤바 둥근 테두리 */
        }

        #sidebar::-webkit-scrollbar-track {
            background: white;  /*스크롤바 뒷 배경 색상*/
            border-radius: 10px;
        }

        #search-bar input {
            width: 100%;
            padding: 10px;
            box-sizing: border-box;
            border:  1px solid #7577fc;
            background-color: white;;
            color: white;

        }

        .contact {
            display: flex;
            padding: 10px;
            border-bottom: 2px solid gray;;
            cursor: pointer;
            align-items: center; /* 수직 중앙 정렬 */
            justify-content: center; /* 수평 중앙 정렬 */
            margin-bottom: 2px;

        }


        .contact-info .contact-name {
            margin: 0;
            font-weight: bold;
        }

        .contact-info .contact-last-msg {
            margin: 0;
            font-size: 0.8em;
        }

        #chat-window {
            flex-grow: 1;
            display: flex;
            flex-direction: column;
            background-color: white;
            border-radius: 15px;
        }

        #chat-messages {
            flex-grow: 1; /* This allows the area to expand with the available space */
            overflow-y: auto; /* Enables scrolling */
            padding: 10px;
            height: calc(100vh - 200px);
            background: #ffffff;
            display: flex;
            flex-direction: column-reverse;
            border-radius: 20px;
        }
        #chat-messages::-webkit-scrollbar {
            width: 10px;
            background-color: black;
            border-radius: 10px;
        }
        #chat-messages::-webkit-scrollbar-thumb {
            background: #696cff;; /* 스크롤바 색상 */
            border-radius: 10px; /* 스크롤바 둥근 테두리 */
        }

        #chat-messages::-webkit-scrollbar-track {
            background: white;  /*스크롤바 뒷 배경 색상*/
            border-radius: 10px;
        }

        #input-area {
            display: flex;
            padding: 10px;
            background-color: white;
        }

        #input-area input {
            flex-grow: 1;
            padding: 10px;
            border: none;
            margin-right: 10px;
        }

        #input-area button {
            padding: 10px 20px;
            border: none;
            background-color: #696cff;
            color: white;
            cursor: pointer;
        }

        #input-area button:hover {
            background-color: #2980b9;
        }
        .message {
            padding: 8px 16px;
            border-radius: 20px;
            margin: 5px;
            max-width: 80%;
            display: flex;
            align-items: center; /* 수직 중앙 정렬 */
            justify-content: center; /* 수평 중앙 정렬 */
        }
        .my-message {
            color: white;
            background-color: #696cff;
            align-self: flex-end; /* 오른쪽 정렬 */
        }

        .other-message {
            color:  #696cff;
            background-color: rgba(105, 108, 255, 0.16);
            align-self: flex-start; /* 왼쪽 정렬 */
        }

    </style>
    <script>
        let websocket = {
            stompClient:null, //웹소켓의 클라이언트 소켓이된다. connect 누를때 만들어짐.
            init:function (){
                this.connect()
                function sendMessage() {
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
                }
                $('#send-btn').click(sendMessage);
                // 엔터 키 이벤트 핸들러
                $('#message-input').keypress(function(event) {
                    if (event.which == 13) {
                        event.preventDefault();  // 폼 제출 방지
                        sendMessage();  // 메시지 전송 함수 호출
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
                        if(msg.sendid === "${sessionScope.userName}") {
                            messageElement = "<div class='message my-message'>" + msg.sendid + " : " + msg.message + "</div>";
                        } else {
                            messageElement = "<div class='message other-message'>" + msg.sendid + " : " + msg.message + "</div>";
                        }
                        chatMessages.prepend(messageElement);
                        chatMessages.scrollTop(chatMessages[0].scrollHeight);
                    });
                });
            },
        };
        let fetchPreviousMessages = {
            channelName: 'chatroom',  // 채널 이름을 객체 속성으로 저장
            init: function () {
                console.log("Initializing fetch for previous messages...");
                $.ajax({
                    url: `/api/messages`,  // 서버의 메시지 요청 API
                    type: 'GET',  // HTTP GET 메소드 사용
                    data: {channel: this.channelName},  // 채널 이름을 'channel' 파라미터로 전달
                    success: function (messages) {
                        let chatMessages = $("#chat-messages");
                        messages.reverse().forEach(function (messageString) {
                            // 문자열 메시지를 객체로 파싱
                            let message = parseMsgObject(messageString);
                            // 파싱된 객체에서 필요한 데이터 추출 및 화면에 표시
                            if (message.sendid && message.message) {
                                if(message.sendid === "${sessionScope.userName}") {
                                    messageElement = "<div class='message my-message'>" + message.sendid + " : " + message.message + "</div>";
                                } else {
                                    messageElement = "<div class='message other-message'>" + message.sendid + " : " + message.message + "</div>";
                                }
                                chatMessages.append(messageElement);
                            }
                        });
                        chatMessages.scrollTop(chatMessages[0].scrollHeight);  // 스크롤을 하단으로 이동
                    },
                    error: function (error) {
                        console.log('Error fetching previous messages:', error);
                    }
                });
            }

        };
        $(document).ready(function (){
            fetchPreviousMessages.init();
            websocket.init();
        });
        function parseMsgObject(msgStr) {
            const content = msgStr.slice(msgStr.indexOf('(') + 1, msgStr.lastIndexOf(')'));
            const properties = content.split(', ');
            const msgObject = {};
            properties.forEach(prop => {
                const [key, value] = prop.split('=');
                msgObject[key.trim()] = value ? value.trim() : null;
            });
            return msgObject;
        }
    </script>

</head>
<body>
<div id="wrapper">
    <div id="sidebar">
        <div class="contact">
            <h3 class="card-title text-primary" style="margin: 0">
                <span class="fw-bold" >${classroom.name}</span>
            </h3>
        </div>
        <table class="table">
            <tbody class="table-border-bottom-0">
            <c:forEach var="mate" items="${classMates}">
                <tr >
                    <td>
                        <div class="d-flex justify-content-start align-items-center user-name">
                            <div class="avatar-wrapper" style="margin-right: 10px">
                                <div
                                        <c:if test="${mate.online}">class="avatar small avatar-online"</c:if>
                                        <c:if test="${!mate.online}">class="avatar small avatar-offline"</c:if>
                                        class="avatar avatar-sm me-2">
                                    <img src="${mate.profileImg}" alt=""
                                         class="w-px-35 rounded-circle prifile-img-full"
                                    />
                                </div>
                            </div>
                            <div class="d-flex flex-column" style="margin-right: 10px">
                                <span class="fw-medium">${mate.name}</span>
                            </div>
                            <c:if test="${mate.online}"><span class="badge bg-label-success me-1">Active</span></c:if>
                            <c:if test="${!mate.online}"><span class="badge bg-label-secondary">Inactive</span></c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div id="chat-window">
        <div id="chat-messages" >
        </div>
        <div id="input-area">
            <input type="text" id="message-input" placeholder="메세지를 입력하세요">
            <button id="send-btn" type="button" class="btn btn-primary"><i class="bx bx-paper-plane mx-1"></i></button>
        </div>
    </div>
</div>
</body>
</html>
