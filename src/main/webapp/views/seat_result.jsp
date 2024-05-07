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
    const modal = document.getElementById("resultModal");

    $().ready(function () {
        $(".resultBtn").click(function () {
            let seatId = $(this).attr("seatId");
            let seatName = $(this).attr("seatName");

            $.ajax({
                url: '<c:url value="/seat/result/detail"/>',
                type: 'GET',
                data: {seatId: seatId},
                success: function (response) {
                    alert(response);
                    console.log('Server response:', response);
                },
                error: function (xhr, status, error) {
                    console.error('Error:', status, error);
                }
            });

            $("#modalTitle").html(seatName+"자리 선택 현황입니다.");
            $("#modalScrollable").modal("show");
        })
    })
</script>

<h5>* 좌석 클릭 시 좌석을 선택한 멤버 리스트를 확인할 수 있습니다.</h5>

<table class="table" style="text-align: center;">
    <thead>
    <tr class="">
        <th colspan="2"><h3>칠판 👨🏻‍🏫</h3></th>

    </tr>
    </thead>
    <tbody class="table-border-bottom-0">
    <c:forEach var="seat" items="${seatList}" varStatus="status" step="2">
        <tr>
            <td>
                <div class="p-3">
                    <c:forEach var="seat"
                               items="${seat}">
                        <c:choose>
                            <c:when test="${seat.userId == 0}">
                                <button class="btn btn-primary seat btn-lg"
                                        type="button"
                                        style="width: 100px; height: 100px;"
                                        disabled
                                >
                                    <p class="card-text">
                                            ${seat.zone}${seat.number}
                                    </p>
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button class="btn btn-primary btn-lg resultBtn"
                                        type="button"
                                        style="width: 100px; height: 100px;"
                                        seatId="${seat.id}"
                                        seatName="${seat.zone}${seat.number}"
                                >
                                    <p class="card-text">
                                            ${seat.zone}${seat.number}
                                    </p>
                                    <p class="card-text">
                                        <span class="badge bg-white text-gray ms-1">${seat.userName}</span>
                                    </p>
                                </button>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>
            </td>
            <td>
                <div class="p-3">
                    <c:forEach var="seat"
                               items="${seatList[status.index+1]}">
                        <c:choose>
                            <c:when test="${seat.userId == 0}">
                                <button class="btn btn-primary seat btn-lg"
                                        type="button"
                                        style="width: 100px; height: 100px;"
                                        disabled
                                >
                                    <p class="card-text">
                                            ${seat.zone}${seat.number}
                                    </p>
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button class="btn btn-primary btn-lg resultBtn"
                                        type="button"
                                        style="width: 100px; height: 100px;"
                                        seatId="${seat.id}"
                                        seatName="${seat.zone}${seat.number}"
                                >
                                <p class="card-text">
                                            ${seat.zone}${seat.number}
                                    </p>
                                    <p class="card-text">
                                        <span class="badge bg-white text-gray ms-1">${seat.userName}</span>
                                    </p>
                                </button>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<div class="modal fade" id="resultModal">
    dd
</div>

<!-- Modal -->
<div class="modal fade" id="modalScrollable" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="modalScrollableTitle">좌석 선택 현황</h5>
                <button
                        type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"
                        aria-label="Close"
                ></button>
            </div>
            <div class="modal-body">
                <p id="modalTitle"></p>
                <ul class="p-0 m-0">
                    <li class="d-flex mb-4 pb-1">
                        <div class="avatar flex-shrink-0 me-3">
                            <span class="avatar-initial rounded bg-label-primary"><i
                                    class="bx bx-mobile-alt"></i></span>
                        </div>
                        <div class="d-flex w-100 flex-wrap align-items-center justify-content-between gap-2">
                            <div class="me-2">
                                <h6 class="mb-0">황혜림</h6>
                                <small class="text-muted">2024.04.28 13:23:02</small>
                            </div>
                            <div class="user-progress">
                                <span class="badge bg-success">성공</span>
                            </div>
                        </div>
                    </li>
                    <li class="d-flex mb-4 pb-1">
                        <div class="avatar flex-shrink-0 me-3">
                            <span class="avatar-initial rounded bg-label-success"><i class="bx bx-closet"></i></span>
                        </div>
                        <div class="d-flex w-100 flex-wrap align-items-center justify-content-between gap-2">
                            <div class="me-2">
                                <h6 class="mb-0">김민수</h6>
                                <small class="text-muted">2024.04.28 13:23:06</small>
                            </div>
                            <div class="user-progress">
                                <span class="badge bg-danger rounded-pill">실패</span>
                            </div>
                        </div>
                    </li>
                    <li class="d-flex mb-4 pb-1">
                        <div class="avatar flex-shrink-0 me-3">
                            <span class="avatar-initial rounded bg-label-info"><i class="bx bx-home-alt"></i></span>
                        </div>
                        <div class="d-flex w-100 flex-wrap align-items-center justify-content-between gap-2">
                            <div class="me-2">
                                <h6 class="mb-0">이지은</h6>
                                <small class="text-muted">2024.04.28 13:23:17</small>
                            </div>
                            <div class="user-progress">
                                <span class="badge bg-danger rounded-pill">실패</span>
                            </div>
                        </div>
                    </li>
                    <li class="d-flex">
                        <div class="avatar flex-shrink-0 me-3">
                            <span class="avatar-initial rounded bg-label-secondary"><i
                                    class="bx bx-football"></i></span>
                        </div>
                        <div class="d-flex w-100 flex-wrap align-items-center justify-content-between gap-2">
                            <div class="me-2">
                                <h6 class="mb-0">박준호</h6>
                                <small class="text-muted">2024.04.28 13:23:29</small>
                            </div>
                            <div class="user-progress">
                                <span class="badge bg-danger rounded-pill">실패</span>
                            </div>
                        </div>
                    </li>
                </ul>
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