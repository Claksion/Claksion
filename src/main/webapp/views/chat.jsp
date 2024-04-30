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
    #all {
        width: 400px;
        height: 200px;
        overflow: auto;
        border: 2px solid red;
    }

    #me {
        width: 400px;
        height: 200px;
        overflow: auto;
        border: 2px solid blue;
    }

    #to {
        width: 400px;
        height: 200px;
        overflow: auto;
        border: 2px solid green;
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
            $('#connect').click(()=>{
                this.connect()
            });
            $('#disconnect').click(()=>{
                this.disconnect();
            });
            $('#sendall').click(()=>{
                var msg = JSON.stringify({
                    'sendid' : this.id,
                    'content1' : $("#alltext").val()
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
                    console.log(JSON.parse(msg.body))
                    $("#all").prepend("<h4>" + JSON.parse(msg.body).sendid +":"+JSON.parse(msg.body).content1+ "</h4>");
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

<div class="container">
    <h2>Chat Page</h2>
    <div class="col-sm-5">
        <h1 id="adm_id">${sessionScope.id}</h1>
        <H1 id="status">Status</H1>
        <button id="connect">Connect</button>
        <button id="disconnect">Disconnect</button>
        <h3>All</h3>
        <input type="text" id="alltext"><button id="sendall">Send</button>
        <div id="all"></div>
    </div>
</div>