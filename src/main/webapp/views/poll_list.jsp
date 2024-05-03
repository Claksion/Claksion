<%--
  Created by IntelliJ IDEA.
  User: hayoung
  Date: 2024/05/03
  Time: 10:10 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
    let poll_list = {
        init: function () {
            console.log('${pollList}');
        }
    };
    $(function () {
        poll_list.init();
    });
</script>

<div class="content-wrapper">
    <!-- Content -->

    <div class="container-xxl flex-grow-1 container-p-y">
        <!-- Hoverable Table rows -->
        <div class="card">
            <h5 class="card-header">투표</h5>
            <div class="table-responsive text-nowrap">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>투표</th>
                        <th>진행상황</th>
                        <th>액션</th>
                    </tr>
                    </thead>
                    <tbody class="table-border-bottom-0">
                    <c:forEach var="p" items="${pollList}">
                        <tr>
                            <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${p.title}</strong></td>
                            <td><span class="badge bg-label-primary me-1">투표중</span></td>
                            <td>
                                <div class="dropdown">
                                    <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                        <i class="bx bx-dots-vertical-rounded"></i>
                                    </button>
                                    <div class="dropdown-menu">
                                        <a class="dropdown-item" href="javascript:void(0);"
                                        ><i class="bx bx-edit-alt me-1"></i> Edit</a
                                        >
                                        <a class="dropdown-item" href="javascript:void(0);"
                                        ><i class="bx bx-trash me-1"></i> Delete</a
                                        >
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <!--/ Hoverable Table rows -->

        <hr class="my-5" />

    </div>
    <!-- / Content -->

    <!-- Footer -->
    <footer class="content-footer footer bg-footer-theme">
        <div class="container-xxl d-flex flex-wrap justify-content-between py-2 flex-md-row flex-column">
            <div class="mb-2 mb-md-0">
                ©
                <script>
                    document.write(new Date().getFullYear());
                </script>
                , made with ❤️ by
                <a href="https://themeselection.com" target="_blank" class="footer-link fw-bolder">ThemeSelection</a>
            </div>
            <div>
                <a href="https://themeselection.com/license/" class="footer-link me-4" target="_blank">License</a>
                <a href="https://themeselection.com/" target="_blank" class="footer-link me-4">More Themes</a>

                <a
                        href="https://themeselection.com/demo/sneat-bootstrap-html-admin-template/documentation/"
                        target="_blank"
                        class="footer-link me-4"
                >Documentation</a
                >

                <a
                        href="https://github.com/themeselection/sneat-html-admin-template-free/issues"
                        target="_blank"
                        class="footer-link me-4"
                >Support</a
                >
            </div>
        </div>
    </footer>
    <!-- / Footer -->

    <div class="content-backdrop fade"></div>
</div>