<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="${ctx}/assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/assets/plugins/jquery-mousewheel/jquery.mousewheel.min.js"></script>
</head>
<body>
	<div id="wrapper">
		<div id="scroller">
			<ul>
				<li class="row">Row 1</li>
				<li class="row">Row 2</li>
				<li class="row">Row 3</li>
				<li class="row">Row 4</li>
				<li class="row">Row 5</li>
				<li class="row">Row 6</li>
				<li class="row">Row 7</li>
				<li class="row">Row 8</li>
				<li class="row">Row 9</li>
				<li class="row">Row 10</li>
				<li class="row">Row 11</li>
				<li class="row">Row 12</li>
				<li class="row">Row 13</li>
				<li class="row">Row 14</li>
				<li class="row">Row 15</li>

				<li class="row">Row 16</li>
				<li class="row">Row 17</li>
				<li class="row">Row 18</li>
				<li class="row">Row 19</li>
				<li class="row">Row 20</li>
				<li class="row">Row 21</li>
				<li class="row">Row 22</li>
				<li class="row">Row 23</li>
				<li class="row">Row 24</li>
				<li class="row">Row 25</li>
				<li class="row">Row 26</li>
				<li class="row">Row 27</li>
				<li class="row">Row 28</li>
				<li class="row">Row 29</li>
				<li class="row">Row 30</li>
			</ul>
		</div>
	</div>
	<script type="text/javascript">
        $(function() {
            $(window).on('mousewheel', function(event) {
                alert("aa");
            });
            
        });
    </script>
</body>
</html>