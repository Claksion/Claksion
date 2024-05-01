<%--
  Created by IntelliJ IDEA.
  User: COM
  Date: 2024-05-01
  Time: 오후 11:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- JSTL -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %><html>
<head>
    <title>Title</title>
</head>
<div class="container-xxl flex-grow-1 container-p-y">
    <div class="row">
        <div class="col-lg-12 mb-4 order-0">
            <div class="card">
                <div class="d-flex align-items-end row">
                    <div class="col-sm-7">
                        <div class="card-body">
                            <h5 class="card-title text-primary">반갑습니다 <span class="fw-bold">하나로</span>님</h5>
                            <p class="mb-4">
                                <span class="fw-bold">이름 : </span> 하나로
                            </p>
                            <p class="mb-4">
                                <span class="fw-bold">소속 : </span> 하나로반
                            </p>
                            <p class="mb-4">
                                <span class="fw-bold">담임선생님 : </span> 이리로
                            </p>

                            <a href="<c:url value="/javascript:;"/>" class="btn btn-sm btn-outline-primary">View Badges</a>
                        </div>
                    </div>
                    <div class="col-sm-5 text-center text-sm-left">
                        <div class="card-body pb-0 px-0 px-md-4">
                            <img
                                    src="<c:url value="/assets/img/illustrations/man-with-laptop-light.png"/>"
                                    height="140"
                                    alt="View Badge User"
                                    data-app-dark-img="illustrations/man-with-laptop-dark.png"
                                    data-app-light-img="illustrations/man-with-laptop-light.png"
                            />
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-4 col-md-4 order-1">
            <div class="row">
                <div class="col-lg-6 col-md-12 col-6 mb-4">
                </div>
            </div>
        </div>
        <!-- Total Revenue -->
        <!--/ Total Revenue -->

    </div>

</div>

</html>
