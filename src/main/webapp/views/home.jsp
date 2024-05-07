<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>


<script>
    let poll_form = {
        init: function () {
            const eventSource = new EventSource('<c:url value="/user/get"/>?classroomId=${classroom.id}');
            eventSource.onmessage = function (event) {
                console.log('New event:', event.data);
                const data = JSON.parse(event.data);

                for (key in data) {
                    console.log(key + ", " + data[key]);

                    const mateId = 'mate' + key;

                    if(data[key]){ // 로그인 상태
                        document.getElementById(mateId + "_profile").className = "avatar small avatar-online";
                        document.getElementById(mateId + "_status").innerHTML = "<span class='badge bg-label-success me-1'>ACTIVE</span>";
                    } else { // 로그아웃 상태
                        document.getElementById(mateId + "_profile").className = "avatar small avatar-offline";
                        document.getElementById(mateId + "_status").innerHTML = "<span class='badge bg-label-secondary'>Inactive</span>";
                    }
                }

            };
            eventSource.onerror = function (error) {
                console.log('Error:', error);
                eventSource.close();
            };
        }
    };

    $(function () {
        poll_form.init();
    });
</script>

<div class="row">

    <!-- About Me -->
    <div class="col-lg">
        <div class="card mb-4">
            <h5 class="card-header">About Me</h5>
            <div class="card-body">
                <h3 class="card-title text-primary">반갑습니다! <span
                        class="fw-bold">${user.name}</span>님 👋
                </h3>
                <div class="row">
                    <div class="p-2">
                        <span class="badge bg-label-warning rounded">Name</span> ${user.name}
                    </div>
                    <div class="p-2">
                        <span class="badge bg-label-INFO rounded">Email</span> ${user.email}
                    </div>
                    <div class="p-2">
                        <span class="badge bg-label-SUCCESS rounded">Type</span> ${user.type}
                    </div>
                    <div class="p-2">
                        <span class="badge bg-label-PRIMARY rounded">Class</span> ${classroom.name}

                    </div>
                    <div class="card-body ">
                        <img
                                src="<c:url value="/assets/img/illustrations/man-with-laptop-light.png"/>"
                                height="140"
                                alt="View Badge User"
                                style="float:right;"
                                data-app-dark-img="illustrations/man-with-laptop-dark.png"
                                data-app-light-img="illustrations/man-with-laptop-light.png"
                        />
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Classroom Mates -->
    <div class="col-lg">
        <div class="card mb-4">
            <h5 class="card-header">Classroom Mates</h5>
            <div class="card-body">
                <div class="table-responsive text-nowrap">
                    <table class="table">
                        <thead class="table-light">
                        <tr>
                            <th>Profile</th>
                            <th>Email</th>
                            <th>Status</th>
                        </tr>
                        </thead>
                        <tbody class="table-border-bottom-0">
                        <c:forEach var="mate" items="${classMates}">
                        <tr>
                            <td>
                                <ul class="navbar-nav flex-row align-items-center ms-auto">
                                    <li class="nav-item lh-1 me-3">
                                        <div id="mate${mate.id}_profile"
                                             <c:if test="${mate.online}">class="avatar small avatar-online"</c:if>
                                             <c:if test="${!mate.online}">class="avatar small avatar-offline"</c:if>
                                             style="width: 30px; height: 30px;">
                                            <img
                                                    src="${mate.profileImg}" alt=""
                                                    class="w-px-30 rounded-circle prifile-img-full"
                                            >
                                        </div>
                                    </li>
                                    <li class="nav-item lh-1 me-3">
                                        <span>${mate.name}</span>
                                    </li>
                                </ul>
                            </td>
                            <td>${mate.email}</td>
                            <td>
                                <div id="mate${mate.id}_status">
                                    <c:if test="${mate.online}"><span
                                            class="badge bg-label-success me-1">Active</span></c:if>
                                    <c:if test="${!mate.online}"><span
                                            class="badge bg-label-secondary">Inactive</span></c:if>
                                </div>
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

</div>