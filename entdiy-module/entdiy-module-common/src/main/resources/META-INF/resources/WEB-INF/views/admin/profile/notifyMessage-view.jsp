<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="form-horizontal">
    <div class="form-body">
        <h3 class="margin-bottom-20">${notifyMessage.title}</h3>
        <div class="row">
            <div class="col-md-12">${notifyMessage.message}</div>
        </div>
    </div>
</div>
<c:if test="${param.readed=='false'}">
    <script type="text/javascript">
        $(function () {
            setTimeout(function () {
                AdminIndex.updateMessageCount();
            }, 1000)
        });
    </script>
</c:if>