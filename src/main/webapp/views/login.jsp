<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>

<!-- Core CSS -->
<link rel="stylesheet" href="../assets/vendor/css/core.css" class="template-customizer-core-css"/>
<link rel="stylesheet" href="../assets/vendor/css/theme-default.css" class="template-customizer-theme-css"/>
<link rel="stylesheet" href="../assets/css/demo.css"/>
<link rel="stylesheet" href="../assets/vendor/css/pages/page-auth.css"/>

<!-- build:js assets/vendor/js/core.js -->
<script src="../assets/vendor/libs/jquery/jquery.js"></script>
<script src="../assets/vendor/libs/popper/popper.js"></script>
<script src="../assets/vendor/js/bootstrap.js"></script>
<script src="../assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>

<div class="container-xxl">
    <div class="authentication-wrapper authentication-basic container-p-y">
        <div class="authentication-inner">
            <!-- Register -->
            <div class="card">
                <div class="card-body">
                    <!-- Logo -->
                    <div class="app-brand justify-content-center">
                        <a href="<c:url value="/"/>" class="app-brand-link gap-2">
                            <img src="../assets/img/logo.png" alt="" class="w-px-40 h-auto rounded-circle">
                            <span class="app-brand-text demo text-body fw-bolder">Claksion</span>
                        </a>
                    </div>
                    <!-- /Logo -->
                    <div class="row text-center">

                        <h4 class="mb-2">Welcome! 👋</h4>
                        <p class="mb-4">간편 로그인으로 Claksion을 시작하세요!</p>
                        <!-- 카카오 로그인 -->
                        <a class="p-2"
                           href="https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${kakaoRestApiKey}&redirect_uri=http://localhost:80/user/login/kakao/oauth" >
                            <img src="/assets/img/login_btn_kakao.png" style="height:40px">
                        </a>

                        <!-- 네이버 로그인 -->
                        <a class="p-2"
                           href="https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=525aa8be9e038a0dccd36d35dca909d8&redirect_uri=http://localhost:80/member/login/kakao/oauth">
                            <img src="/assets/img/login_btn_naver.png" style="height:40px">
                        </a>
                    </div>
                </div>
            </div>
            <!-- /Register -->
        </div>
    </div>
</div>