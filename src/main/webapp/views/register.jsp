<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>

<!-- Core CSS -->
<link rel="stylesheet" href="<c:url value="/assets/vendor/css/core.css"/>" class="template-customizer-core-css"/>
<link rel="stylesheet" href="<c:url value="/assets/vendor/css/theme-default.css"/>"
      class="template-customizer-core-css"/>
<link rel="stylesheet" href="<c:url value="/assets/css/demo.css"/>"/>
<link rel="stylesheet" href="<c:url value="/assets/vendor/css/pages/page-auth.css"/>"/>

<!-- build:js assets/vendor/js/core.js -->
<script src="<c:url value="/assets/vendor/libs/jquery/jquery.js"/>"></script>
<script src="<c:url value="/assets/vendor/libs/popper/popper.js"/>"></script>
<script src="<c:url value="/assets/vendor/js/bootstrap.js"/>"></script>
<script src="<c:url value="/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"/>"></script>

<div class="container-xxl">
    <div class="authentication-wrapper authentication-basic container-p-y">
        <div class="authentication-inner">
            <!-- Register Card -->
            <div class="card">
                <div class="card-body">
                    <!-- Logo -->
                    <div></div>
                    <!-- /Logo -->
                    <h4 class="mb-2">처음이시군요! 🚀</h4>
                    <p class="mb-4">아래 정보를 입력하고 Claksion을 시작해보세요.</p>

                    <form id="formAuthentication" class="mb-3" action="<c:url value="/user/register"/> " method="POST">

                        <div class="card-body d-flex align-items-center align-items-sm-center gap-4 mx-auto">
                            <img
                                    src="${userInfo.profileImg}"
                                    alt="user-avatar"
                                    class="d-block rounded img-fluid d-flex mx-auto"
                                    height="100"
                                    width="100"
                            />
                        </div>


                        <div class="mb-3">
                            <label for="name" class="form-label">이름</label>
                            <input
                                    type="text"
                                    class="form-control"
                                    id="name"
                                    name="name"
                                    placeholder="Enter your name"
                                    autofocus
                            />
                        </div>
                        <div class="mb-3">
                            <label for="email" class="form-label">이메일</label>
                            <input type="text" class="form-control" id="email" name="email"
                                   placeholder="Enter your email" value="${userInfo.email}" readonly/>
                        </div>
                        <div class="mb-3">
                            <label for="type" class="form-label">유형</label>
                            <select id="type" name="type" class="select2 form-select">
                                <option value="STUDENT">학생</option>
                                <option value="TEACHER">선생님</option>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="classroomId" class="form-label">반</label>
                            <select id="classroomId" name="classroomId" class="select2 form-select">
                                <c:forEach var="classroom" items="${classrooms}">
                                    <option value="${classroom.id}">${classroom.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <input type="text" name="oauthId" value="${userInfo.oauthId}" hidden/>
                        <input type="text" name="profileImg" value="${userInfo.profileImg}" hidden/>

                        <button class="btn btn-primary d-grid w-100">회원가입</button>
                    </form>
                </div>
            </div>
            <!-- Register Card -->
        </div>
    </div>
</div>

<!-- / Content -->
