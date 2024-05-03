<%--
  Created by IntelliJ IDEA.
  User: hayoung
  Date: 2024/04/30
  Time: 3:35 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>

<!-- Core CSS -->
<link rel="stylesheet" href="../assets/vendor/css/core.css" class="template-customizer-core-css"/>
<link rel="stylesheet" href="../assets/vendor/css/theme-default.css" class="template-customizer-theme-css"/>
<link rel="stylesheet" href="../assets/css/demo.css"/>

<!-- build:js assets/vendor/js/core.js -->
<script src="../assets/vendor/libs/jquery/jquery.js"></script>
<script src="../assets/vendor/libs/popper/popper.js"></script>
<script src="../assets/vendor/js/bootstrap.js"></script>
<script src="../assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>

<script>

    let poll_form = {
        init: function () {
        }
    };
    $(function () {
        poll_form.init();

    });

    function btn_create_poll() {
        let poll_title = $('#title').val().toString();
        let poll_contents = [];
        $('.content').each(function() {
            poll_contents.push($(this).val());
        });
        let poll_multi = $('#multi').is(':checked')?"MULTI":"SINGLE";
        let poll_anonymity = $('#anonymity').is(':checked');
        let poll_datetime = $('#datetime').val();

        console.log(poll_contents);

        const pollData = {
            title: poll_title,
            anonymity: poll_anonymity,
            selectType: poll_multi,
            deadline: poll_datetime
        }
        let pollContentsData = [];
        $('.content').each(function() {
            content = {
                // pollId: ,
                name: $(this).val(),
                selected: false
            }
            pollContentsData.push(content);
        });

        const requestData = {
            "poll": pollData,
            "pollContents": pollContentsData
        };

        console.log(requestData);

        $.ajax({
            url:'<c:url value="/poll/creationimpl"/>',
            type:'post',
            contentType:'application/json',
            data:JSON.stringify({requestData}),
            success: pollId => {
                location.href="<c:url value="/poll/form"/>?pollId="+pollId;
            }
        });
    }
</script>


<h1>투표</h1>
<div class="card mb-4">
    <div class="card-body">
        <div class="col-lg-12 mb-1 order-0">
                <div class="mb-3 col-md-6">
                    <h3 for="title" class="form-label">제목</h3>
                    <input class="form-control"
                            type="text"
                            id="title"
                            name="title"
                            placeholder="Title"
                            autofocus
                    />
                </div>

    <hr class="m-0"><br/>
    <div class="input-group">
        <span class="input-group-text" id="PollContent1">1</span>
        <input
                type="text"
                class="form-control content"
                placeholder="PollContent"
                aria-label="PollContent"
                aria-describedby="basic-addon11"
        />
    </div><br/>
    <div class="input-group">
        <span class="input-group-text" id="PollContent2">2</span>
        <input
                type="text"
                class="form-control content"
                placeholder="PollContent"
                aria-label="PollContent"
                aria-describedby="basic-addon11"
        />
    </div>

    <br/><hr class="m-0"><br/>
        <div class="card-body">
            <div class="form-check form-switch mb-2">
                <input class="form-check-input" type="checkbox" id="multi" checked/>
                <label class="form-check-label" for="multi"
                >다중 선택</label
                >
            </div>
            <div class="form-check form-switch mb-2">
                <input class="form-check-input" type="checkbox" id="anonymity"/>
                <label class="form-check-label" for="anonymity"
                >익명</label
                >
            </div>
        </div>

    <div class="card-body">
        <div class="mb-3 row">
            <label for="datetime" class="col-md-2 col-form-label">마감</label>
            <div class="col-md-10">
                <input class="form-control"
                       type="datetime-local"
                       value="2021-06-18T12:30:00"
                       id="datetime"
                />
            </div>
        </div>
    </div>

    <hr class="m-0"><br/>

    <button type="button" class="btn btn-primary" onclick="btn_create_poll()">등록</button>
        </div>


    </div>
</div>
