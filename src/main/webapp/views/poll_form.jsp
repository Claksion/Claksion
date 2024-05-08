<%--
  Created by IntelliJ IDEA.
  User: hayoung
  Date: 2024/04/30
  Time: 3:35 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
        init: function() {}
    };
    $(function () {
        poll_form.init();
    });

    const pollId = ${poll.id};
    function btn_poll() {
        let contents = $('.content');

        if(check(contents)) {
            let poll_contents = Array.from(contents).map(c => ({
                "id": c.id,
                "pollId": ${poll.id},
                "selected": c.checked
            }));

            $.ajax({
                url:'<c:url value="/poll/formimpl"/>?pollId='+pollId,
                type:'post',
                contentType:'application/json',
                data: JSON.stringify(poll_contents),
                success: (res) => {
                    console.log(res)
                    location.href='<c:url value="/poll/result"/>?pollId='+pollId
                }
            });
        }

    }

    function check(contents) {
        let checked = false;
        Array.from(contents).forEach(c => {
            if(c.checked) checked = true;
        });
        if(!checked) {
            $('#modalScrollableTitle').text("경고");
            $('.modal-body p').text("투표할 항목을 선택하세요.");
            $('#modalWarning').modal('show');
            return false;
        }
        return true;
    }
</script>


<div class="card mb-4">
    <div class="card-body">
        <h2 class="card-title text-primary">투표</h2>
        <hr class="m-0"><br/>
        <div class="col-lg-12 mb-1 order-0">
            <div class="mb-3 col-md-6">
                <h3 for="title" class="form-label">제목</h3>
                <h1 class="mb-0"><small class="text-muted">${poll.title}</small></h1>
            </div>
        </div>

        <hr class="m-0"><br/>

        <label class="col-md-2 col-form-label">투표 항목</label>
        <form id="contents">
            <c:forEach var="p" items="${pollContents}">
                <div class="form-check">
                    <input name="poll"
                           class="form-check-input content"
                           id="${p.id}"
                           type="${poll.selectType == 'SINGLE' ? 'radio' : 'checkbox'}"
                            ${p.selected ? 'checked' : ''}/>
                    <label class="form-check-label" for="${p.id}">${p.name}</label>
                </div>
            </c:forEach>
        </form>

        <hr class="m-0"><br/>

        <div class="mb-3">
            <label class="col-md-2 col-form-label">
                <c:choose>
                    <c:when test="${poll.anonymity == false}">유기명 | </c:when>
                    <c:otherwise>무기명 | </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${poll.selectType == 'SINGLE'}">단일 선택</c:when>
                    <c:otherwise>다중 선택</c:otherwise>
                </c:choose>
            </label>
            <br/>
            <label class="form-label">마감</label> &nbsp;
            <small class="text-muted">
                <fmt:parseDate value="${poll.deadline}"
                               pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                <fmt:formatDate pattern="yyyy년 MM월 dd일 HH시 mm분" value="${ parsedDateTime }" />
            </small>
            <br/>
            <label class="form-label">게시자</label> &nbsp;
            <small class="text-muted"> @${poll.madeById} | ${poll.madeByName} </small>
        </div>

        <hr class="m-0"><br/>

        <button type="button" class="btn btn-primary" onclick="btn_poll()">투표</button>

    </div>
</div>


<div class="modal fade" id="modalWarning" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="modalScrollableTitle">title</h5>
                <button
                        type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"
                        aria-label="Close"
                ></button>
            </div>
            <div class="modal-body">
                <p>
                    text
                </p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary"
                        data-bs-dismiss="modal">
                    Close
                </button>
            </div>
        </div>
    </div>
</div>