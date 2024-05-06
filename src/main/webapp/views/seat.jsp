<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11.4.10/dist/sweetalert2.min.css">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.4.10/dist/sweetalert2.min.js"></script>

<script>
    $().ready(function () {
        $("#success").click(function () {
            Swal.fire({
                icon: 'success',
                title: '성공',
                text: '자리 선택에 성공했습니다!',
            });
        });

        $("#error").click(function () {
            Swal.fire({
                icon: 'error',
                title: '실패',
                text: '이미 선택된 좌석입니다.',
            });
        });

        $(".seat").click(function () {
            Swal.fire({
                icon: 'error',
                title: '실패',
                text: '이미 선택된 좌석입니다.',
            });
        });

        $("#btn-reset").click(function () {
            Swal.fire({
                title: '좌석 정보를 초기화하겠습니까?',
                showCancelButton: true,
                showLoaderOnConfirm: true,
                preConfirm: () => {
                    return $.ajax({
                        url: '<c:url value="seat/reset"/>',
                        data: {classroomId: ${classroom.id}},
                        type: 'POST'
                    });
                },
                allowOutsideClick: () => !Swal.isLoading()
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire({
                        icon: 'success',
                        title: '좌석이 초기화되었습니다.'
                    }).then(() => {location.reload(true)});
                }
            })
        })
    })
</script>


<script>
    $(document).ready(function () {
        $('#userInfoButton').on('click', function () {
            // 사용자 ID를 세션, 쿠키 또는 로컬 스토리지에서 가져옴
            var userId = sessionStorage.getItem('userId'); // 예시로 세션 스토리지 사용

            $.ajax({
                url: '<c:url value="seat/userinfo"/>',
                type: 'POST',
                data: {userId: userId},
                success: function (response) {
                    console.log('Server response:', response);
                    alert('사용자 정보가 서버로 전송되었습니다.');
                },
                error: function (xhr, status, error) {
                    console.error('Error:', status, error);
                    alert('오류가 발생했습니다. 다시 시도해 주세요.');
                }
            });
        });
    });
</script>

<div class="row">
    <div class="col-lg-12 mb-1 order-0">

        <!-- Seat -->
        <div class="col-lg">
            <div class="card mb-4">
                <h5 class="card-header">Seat</h5>
                <div class="card-body">
                    <h3 class="card-title text-primary">
                        <span class="fw-bold">${classroom.name}</span>의 자리배치도 입니다! 🪑
                    </h3>

                    <div class="btn-group" role="group">
                        <a type="button" class="btn btn-outline-secondary" id="btn-reset"><i
                                class="tf-icons bx bx-reset"></i> 초기화</a>
                        <a type="button" class="btn btn-outline-secondary" id="btn-time"><i
                                class="tf-icons bx bx-time"></i> 시간설정</a>
                    </div>

                    <div class="p-5" style="margin: 20px 100px;">
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
                                                        <button class="btn btn-primary seat btn-lg" type="button"
                                                                seatId="${seat.id}">
                                                                ${seat.zone}${seat.number}
                                                        </button>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <button class="btn seat-selected btn-lg" type="button"
                                                                seatId="${seat.id}">
                                                                ${seat.zone}${seat.number}
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
                                                        <button class="btn btn-primary seat btn-lg" type="button"
                                                                seatId="${seat.id}">
                                                                ${seat.zone}${seat.number}
                                                        </button>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <button class="btn seat-selected btn-lg" type="button"
                                                                seatId="${seat.id}">
                                                                ${seat.zone}${seat.number}
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

                    </div>
                </div>
            </div>
        </div>


        <button id="success">Success</button>
        <button id="error">Error</button>


        <!-- Modal -->
        <div class="modal fade" id="modalMine" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-scrollable modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalScrollableTitle">자리 선택</h5>
                        <button
                                type="button"
                                class="btn-close"
                                data-bs-dismiss="modal"
                                aria-label="Close"
                        ></button>
                    </div>
                    <div class="modal-body">
                        <p>
                            내 자리입니다. 🤡
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
        <div class="modal fade" id="modalEmpty" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-scrollable modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalScrollableTitle">자리 선택</h5>
                        <button
                                type="button"
                                class="btn-close"
                                data-bs-dismiss="modal"
                                aria-label="Close"
                        ></button>
                    </div>
                    <div class="modal-body">
                        <p>
                            자리 선택이 완료되었습니다.
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
        <div class="modal fade" id="modalSelected" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-scrollable modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalScrollableTitle">자리 선택</h5>
                        <button
                                type="button"
                                class="btn-close"
                                data-bs-dismiss="modal"
                                aria-label="Close"
                        ></button>
                    </div>
                    <div class="modal-body">
                        <p>
                            이미 선택된 자리입니다.
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
    </div>
</div>