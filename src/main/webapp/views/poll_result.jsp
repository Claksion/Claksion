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
        init: function () {
            const eventSource = new EventSource('<c:url value="/poll/resultimpl"/>?pollId=${poll.id}');
            eventSource.onmessage = function(event) {
                console.log('New event:', event.data);
                const data = JSON.parse(event.data);
                for(d in data) {
                    console.log(d);
                    const cntId = 'cnt'+d;
                    const rankId = 'rank'+d;
                    console.log(cntId, rankId);
                    const parts = data[d].split(',');
                    console.log(parts);
                    console.log(document.getElementById(cntId));
                    document.getElementById(cntId).innerHTML=parts[0]+"표";
                    document.getElementById(rankId).innerHTML=parts[1]+"등";
                }
            };
            eventSource.onerror = function(error) {
                console.log('Error:', error);
                eventSource.close();
            };
        }
    };
    $(function () {
        poll_form.init();
    });

    function show_voted_user(pollId, contentId) {
        $.ajax({
            url: '<c:url value="/poll/voteduser"/>',
            type: 'GET',
            data: {
                pollId: pollId,
                contentId: contentId
            },
            success: function(data) {
                console.log(data);
                $('#modalScrollableTitle').text(data.title);
                $('.modal-body p').text(data.text);

                $('#modalSelected').modal('show');
            },
            error: function(error) {
                console.error('Error fetching data: ', error);
            }
        });
    }
</script>


<h2 class="card-header mb-0">투표 결과 (실시간)</h2>
<div class="card mb-4">
    <div class="card-body">
        <div class="col-lg-12 mb-1 order-0">
            <div class="mb-3 col-md-6">
                <h3 for="title" class="form-label">제목</h3>
                <h1 class="mb-0"><small class="text-muted">${poll.title}</small></h1>
            </div>
        </div>

        <hr class="m-0"><br/>

        <div class="row gy-3">
            <div class="col-md">

                <table class="table table-borderless table-hover">
                    <thead>
                        <tr>
                            <th>투표 항목</th>
                            <th>득표수</th>
                            <th>랭킹</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${pollContents}">
                            <c:choose>
                                <c:when test="${poll.anonymity == false}">
                                    <tr id="${p.id}" onclick="show_voted_user(${poll.id}, ${p.id})"
                                    data-bs-toggle="modal"
                                    data-bs-target="#modalSelected">
                                </c:when>
                                <c:otherwise>
                                    <tr id="${p.id}">
                                </c:otherwise>
                            </c:choose>
                                <td class="align-middle"><h4 class="mb-0">${p.name}</h4></td>
                                <td class="py-3"><h4 id="cnt${p.id}" class="mb-0"></h4></td>
                                <td><h4><small id="rank${p.id}" class="text-muted"></small></h4></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <br/><hr class="m-0"><br/>

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

    </div>
</div>
<h4 class="card-header text-muted">투표에 참여하셨습니다.</h4>


<div class="modal fade" id="modalSelected" tabindex="-1" aria-hidden="true">
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