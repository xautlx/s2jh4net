<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>${name}</title>
    <style>
        img{
            max-width: 100%;
        }
    </style>
    <link rel="shortcut icon" href="${applicationScope.ctx}/assets/apps/img/favicon.ico"/>
    <link href="${applicationScope.ctx}/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
</head>
<body style="margin: 20px">
<div class="row">
    <div class="col-md-10">${mdHtml}</div>
    <div class="col-md-2">
        <ul>
            <c:forEach var="item" items="${files}">
                <c:if test="${item!='images'}">
                    <li><a href="${item}">${item}</a></li>
                </c:if>
            </c:forEach>
        </ul>
    </div>
</div>
</body>
</html>