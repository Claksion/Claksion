<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11.4.10/dist/sweetalert2.min.css">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.4.10/dist/sweetalert2.min.js"></script>

<script>
    let success_modal = function () {
        Swal.fire({
            icon: 'success',
            title: '성공',
            text: '자리 선택에 성공했습니다!',
        });
    };
    let fail_modal = function () {
        Swal.fire({
            icon: 'error',
            title: '실패',
            text: '이미 선택된 좌석입니다.',
        });
    };
    let error_modal = function () {
        Swal.fire({
            icon: 'warning',
            title: '오류',
            text: '잠시 후 다시 시도해주세요.',
        });
    };
    let alert_modal = function () {
        Swal.fire({
            icon: 'warning',
            title: '오류',
            text: '이미 선택한 좌석이 있습니다.',
        });
    };

    $().ready(function () {
        $("#success").click(success_modal);
        $("#fail").click(fail_modal);

        $(".seat").click(function () {

            let canSelect = ${canSelect};
            let seatId = $(this).attr("seatId");


            if (canSelect == false) {
                alert_modal();
            } else {
            console.log("start ajax")
                $.ajax({
                    url: '<c:url value="/seat/checkselect"/>',
                    type: 'POST',
                    data: {seatId: seatId},
                    success: function (response) {
                        if(!response) {

                            $.ajax({
                                url: '<c:url value="/seat/firstselect"/>',
                                type: 'POST',
                                data: {seatId: seatId},
                                success: function (response) {

                                    if (response) {
                                        document.getElementById(response).disabled = true;
                                        success_modal();
                                        canSelect = false;
                                        // location.reload(true);
                                    } else {
                                        fail_modal();
                                    }
                                    console.log('Server response:', response);
                                },
                                error: function (xhr, status, error) {
                                    console.error('Error:', status, error);
                                }
                            });
                        } else {
                            alert("already clicked")
                        }
                    },
                    error: function (error) {

                    }
                })

            }
        }
        );

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
                    }).then(() => {
                        location.reload(true)
                    });
                }
            })
        })

        $("#btn-result").click(function () {
            location.href = "<c:url value="/seat/result"/> ";
        })
    })
</script>


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
                                        canSelect="${canSelect}"
                                        id="task-${seat.id}"
                                        seatId="${seat.id}">
                                        ${seat.zone}${seat.number}
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button class="btn seat-selected btn-lg"
                                        type="button">
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
                                <button class="btn btn-primary seat btn-lg"
                                        type="button"
                                        canSelect="${canSelect}"
                                        id="task-${seat.id}"
                                        seatId="${seat.id}">
                                        ${seat.zone}${seat.number}
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button class="btn seat-selected btn-lg"
                                        type="button">
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
<%--<input id="canSelect" value="${canSelect}"/>--%>

<%--<button id="success">Success Test</button>--%>
<%--<button id="fail">Fail Test</button>--%>
