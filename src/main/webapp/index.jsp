<%@ page contentType="text/html;charset=UTF-8"%>
<%
    String url = request.getContextPath() + "/admin";
    if (request.getQueryString() != null) {
        url = url + "?" + request.getQueryString();
    }
    response.sendRedirect(url);
%>