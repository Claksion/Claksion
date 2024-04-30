<%--
  Created by IntelliJ IDEA.
  User: campus2H052
  Date: 2024-04-28
  Time: 오후 7:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html
        lang="en"
        class="light-style layout-menu-fixed"
        dir="ltr"
        data-theme="theme-default"
        data-assets-path="../assets/"
        data-template="vertical-menu-template-free"
>
<head>
    <meta charset="utf-8" />
    <meta
            name="viewport"
            content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"
    />

    <title>소셜로그인</title>

    <meta name="description" content="" />

    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="<c:url value="/assets/img/favicon/favicon.ico"/>" />

    <!-- Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
            href="https://fonts.googleapis.com/css2?family=Public+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&display=swap"
            rel="stylesheet"
    />

    <!-- Icons. Uncomment required icon fonts -->
    <link rel="stylesheet" href="<c:url value="/assets/vendor/fonts/boxicons.css"/>" />

    <!-- Core CSS -->
    <link rel="stylesheet" href="<c:url value="/assets/vendor/css/core.css"/>" class="template-customizer-core-css" />
    <link rel="stylesheet" href="<c:url value="/assets/vendor/css/theme-default.css"/>" class="template-customizer-theme-css" />
    <link rel="stylesheet" href="<c:url value="/assets/css/demo.css"/>" />

    <!-- Vendors CSS -->
    <link rel="stylesheet" href="<c:url value="/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css"/>" />

    <!-- Page CSS -->

    <!-- Helpers -->
    <script src="<c:url value="/assets/vendor/js/helpers.js"/>"></script>

    <!--! Template customizer & Theme config files MUST be included after core stylesheets and helpers.js in the <head> section -->
    <!--? Config:  Mandatory theme config file contain global vars & default theme options, Set your preferred theme option in this file.  -->
    <script src="<c:url value="/assets/js/config.js"/>"></script>
</head>

<body>
<!-- Layout wrapper -->
<div class="layout-wrapper layout-content-navbar">
    <div class="layout-container">
        <!-- Layout container -->
            <!-- Content wrapper -->
            <div class="content-wrapper">
                <!-- Content -->
                <div class="container-xxl flex-grow-1 container-p-y">
                    <div class="row">
                        <!-- Button Options -->
                        <div class="col-12 mx-auto">
                            <div class="container d-flex justify-content-center align-items-center" style="height: 100vh;">
                                <div class="card-deck">
                                    <div class="card" style="width: 320px; height: 430px;">
                                        <div class="card-body d-flex justify-content-center align-items-center">
                                            <div class="row text-center">
                                                <img src="<c:url value="/assets/img/claklogo.png"/>"
                                                     width="120px" height="70px">
                                                <h5>간편하게 로그인</h5>
                                                <div class="d-grid gap-4">
                                                    <button class="btn btn-warning" type="button">
                                                        <img src="<c:url value="/assets/img/kakao.png"/>"
                                                             width="20px" height="20px">
                                                        카카오로그인</button>
                                                    <button class="btn btn-success" type="button">
                                                        <img src="<c:url value="/assets/img/naver.png"/>"
                                                             width="20px" height="20px">
                                                        네이버로그인</button>
                                                </div>
                                                <div>
                                                    <a href="<c:url value="loginother.jsp"/> ">다른방법으로 로그인</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- / Content -->
                <div class="content-backdrop fade"></div>
            </div>
            <!-- Content wrapper -->
        <!-- / Layout page -->
    </div>

    <!-- Overlay -->
    <div class="layout-overlay layout-menu-toggle"></div>
</div>
<!-- / Layout wrapper -->
<!-- Core JS -->
<!-- build:js assets/vendor/js/core.js -->
<script src="<c:url value="/assets/vendor/libs/jquery/jquery.js"/>"></script>
<script src="<c:url value="/assets/vendor/libs/popper/popper.js"/>"></script>
<script src="<c:url value="/assets/vendor/js/bootstrap.js"/>"></script>
<script src="<c:url value="/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"/>"></script>

<script src="<c:url value="/assets/vendor/js/menu.js"/>"></script>
<!-- endbuild -->

<!-- Vendors JS -->

<!-- Main JS -->
<script src="<c:url value="/assets/js/main.js"/>"></script>

<!-- Page JS -->

<!-- Place this tag in your head or just before your close body tag. -->
<script async defer src="https://buttons.github.io/buttons.js"></script>
</body>
</html>

