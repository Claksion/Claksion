<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11.4.10/dist/sweetalert2.min.css">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.4.10/dist/sweetalert2.min.js"></script>

<script>
    $().ready(function () {

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

        $("#btn-back").click(function () {
            location.href = "<c:url value="/seat"/> ";
        })
    })
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
                        <c:if test="${detail == 'seat_select'}">
                            <a type="button" class="btn btn-outline-secondary" id="btn-reset"><i
                                    class="tf-icons bx bx-reset"></i> 초기화</a>
                            <a type="button" class="btn btn-outline-secondary" id="btn-result"><i
                                    class="tf-icons bx bx-receipt"></i> 결과화면</a>
                        </c:if>
                        <c:if test="${detail == 'seat_result'}">
                            <a type="button" class="btn btn-outline-secondary" id="btn-back"><i
                                    class="tf-icons bx bx-arrow-back"></i> 선택화면</a>
                        </c:if>
                    </div>

                    <div class="p-5" style="margin: 20px 100px;">
                        <jsp:include page="${detail}.jsp"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>