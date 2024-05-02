<%--
  Created by IntelliJ IDEA.
  User: hayoung
  Date: 2024/04/28
  Time: 2:47 PM
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
    $(document).ready(function() {
        $('#userInfoButton').on('click', function() {
            // 사용자 ID를 세션, 쿠키 또는 로컬 스토리지에서 가져옴
            var userId = sessionStorage.getItem('userId'); // 예시로 세션 스토리지 사용

            $.ajax({
                url: '<c:url value="seat/userinfo"/>',
                type: 'POST',
                data: { userId: userId },
                success: function(response) {
                    console.log('Server response:', response);
                    alert('사용자 정보가 서버로 전송되었습니다.');
                },
                error: function(xhr, status, error) {
                    console.error('Error:', status, error);
                    alert('오류가 발생했습니다. 다시 시도해 주세요.');
                }
            });
        });
    });
</script>
<style>
    .row {

        height: 100%;
    }
    .card {
        align-items: center;
        padding: 50px;
    }
    #classroom {
        text-align: center;
        width: 90%;
        height: 90%;
        padding: 20px;
        margin-top: 20px;
        border-radius: 5px;
    }
    #board {
        background: #255930;
        border: 5px solid #836a4a;
        color: black;
        margin: 0 auto 20px auto;
        width: 200px;
        border-radius: 5px;
        box-shadow: 0 0.125rem 0.25rem 0 rgba(133,146,163,.4);
    }
    #seat_table {
        margin: 0 auto;
    }
    .seat {
        width: 40px;
        height: 40px;
        border: 1px solid transparent;
        border-radius: 5px;
        margin: 5px;
        box-shadow: 0 0.125rem 0.25rem 0 rgba(133,146,163,.4);
    }
    .mine {
        background: #e32323;
    }
    .empty {
        background: white;
    }
    .selected{
        background: #313131;
    }
</style>
<div class="row">
    <div class="col-lg-12 mb-1 order-0">
        <div class="card">
            <h1>자리 뽑기</h1>
            <h3>개발2기</h3>
            <div id="classroom">
                <div id="board">칠판</div>
                <table id="seat_table">
                    <tbody>
                    <tr>
                        <td><button
                                id="userInfoButton"
                                type="button"
                                class="btn seat mine"
                                data-bs-toggle="modal"
                                data-bs-target="#modalMine"
                        >
                        </button></td>
                        <td><button
                                type="button"
                                class="btn seat empty"
                                data-bs-toggle="modal"
                                data-bs-target="#modalEmpty"
                        >
                        </button></td>
                        <td><button
                                type="button"
                                class="btn seat selected"
                                data-bs-toggle="modal"
                                data-bs-target="#modalSelected"
                        >
                        </button></td>
                    </tr>
                    </tbody>
                </table>
            </div>

        </div>
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