<%--
  Created by IntelliJ IDEA.
  User: campus2H052
  Date: 2024-03-27
  Time: 오전 9:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        border: 2px solid red;
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

    #sendall {
        padding: 5px 10px;
        background-color: #007bff;
        color: #fff;
        border: none;
        cursor: pointer;
    }

</style>
<!-- Core plugin JavaScript-->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<%-- Web socket lib --%>
<script src="/webjars/sockjs-client/sockjs.min.js"></script>
<script src="/webjars/stomp-websocket/stomp.min.js"></script>
<script>
    let websocket = {
        id:'', // 화면에 입력되어 있는 아이디.(누가 메세지를 보내는지)
        stompClient:null, //웹소켓의 클라이언트 소켓이된다. connect 누를때 만들어짐.
        init:function (){
            this.id = $('#adm_id').text();
            let statusIndicator = $('#status-indicator')[0];
            this.connect()
            statusIndicator.classList.remove("disconnected");
            statusIndicator.classList.add("connected");
            $('#disconnect').click(()=>{
                this.disconnect();
                statusIndicator.classList.remove("connected");
                statusIndicator.classList.add("disconnected");
            });
            $('#sendall').click(()=>{
                var msg = JSON.stringify({
                    'sendid' : this.id,
                    'content1' : $("#message-input").val()
                });
                this.stompClient.send("/receiveall", {}, msg);
            });
        },
        connect:function (){
            // 소켓생성
            let sid = this.id; //보내는사람의 아이디
            let socket = new SockJS('${serverurl}/ws'); //소켓 객체를 준비/ ws 는 채널 역할을 한다.
            this.stompClient = Stomp.over(socket);
            this.stompClient.connect({}, function(frame) {
                // 화면에 커넥트가 되었음을 알림
                websocket.setConnected(true);
                console.log('Connected: ' + frame);
                this.subscribe('/send', function(msg) {
                    console.log("xxxxxxxxx=>")
                    $("#chat-messages").prepend("<h4>" + JSON.parse(msg.body.sendid) +":"+JSON.parse(msg.body.content1)+ "</h4>");
                });
            });
        },
        setConnected:function(connected){
            if (connected) {
                $("#status").text("Connected");
            } else {
                $("#status").text("Disconnected");
            }
        },
        disconnect:function(){
            if (this.stompClient !== null) {
                this.stompClient.disconnect();
            }
            websocket.setConnected(false);
            console.log("Disconnected");
        },
    };
    $(function (){
        websocket.init();
    });
</script>

<div class=".container height: 100vh;
    width: 100%;" >
    <div class="col-sm-5">
        <button id="disconnect">채팅종료</button>
        <div id="all"></div>
        <div id="chat-window">
            <div>
                <span style="vertical-align: middle;">실시간</span>
                <div id="status-indicator" class="disconnected"></div>
            </div>
            <div id="chat-messages"></div>
        </div>
        <div id="input-area">
            <input type="text" id="message-input" placeholder="메시지를 입력하세요...">
            <button id="sendall">전송</button>
        </div>
    </div>
</div>