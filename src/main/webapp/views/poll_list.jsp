<%--
  Created by IntelliJ IDEA.
  User: hayoung
  Date: 2024/05/03
  Time: 10:10 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script>

let poll_list = {
        init: function () {
        }
    };
    $(function () {
        poll_list.init();
    });
</script>

<div class="card mb-4">
    <div class="card-body">
        <h2 class="card-title text-primary">투표 목록</h2>
        <hr class="m-0"><br/>
        <div class="table-responsive text-nowrap">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>투표</th>
                        <th>마감기한</th>
                        <th>진행상황</th>
                    </tr>
                    </thead>
                    <tbody class="table-border-bottom-0">
                    <c:forEach var="p" items="${pollList}">
                        <c:choose>
                            <c:when test="${p.finished}">
                                <tr onclick="location.href='<c:url value="/poll/finalresult"/>?pollId=${p.id}'">
                            </c:when>
                            <c:otherwise>
                                <tr onclick="location.href='<c:url value="/poll/result"/>?pollId=${p.id}'">
                            </c:otherwise>
                        </c:choose>
                            <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${p.title}</strong></td>
                            <td>
                                <fmt:parseDate value="${p.deadline}"
                                               pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                                <fmt:formatDate pattern="yyyy년 MM월 dd일 HH시 mm분" value="${ parsedDateTime }" />
                            </td>
                            <c:choose>
                                <c:when test="${p.finished}">
                                    <td><span class="badge bg-label-success me-1">마감</span></td>
                                </c:when>
                                <c:otherwise>
                                    <td><span class="badge bg-label-primary me-1">투표 진행중</span></td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

        <hr class="m-0"><br/>

        <button type="button" class="btn btn-primary" onclick="location.href='/poll/creation'">투표 생성</button>

    </div>
</div>