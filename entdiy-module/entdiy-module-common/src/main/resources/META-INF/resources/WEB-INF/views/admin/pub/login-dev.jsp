<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="form-info">
    <p id="devModeTips" style="padding: 10px">
        <b> 开发/测试/演示登录快速入口:
            <a href="javascript:void(0)" onclick="setupDevUser('root','123456')">
                root/123456
            </a>
            &nbsp;|&nbsp;
            <a href="javascript:void(0)" onclick="setupDevUser('manager','123456')">
                manager/123456
            </a>
        </b>
    </p>
    <script type="text/javascript">
        var $form = $("#login-form");

        function setupDevUser(user, password) {
            $("input[name='username']", $form).val(user);
            $("input[name='password']", $form).val(password);
            $form.submit();
        }

        jQuery(document).ready(function () {
            $("#devModeTips").pulsate({
                color: "#bf1c56",
                repeat: 10
            });
        });
    </script>
</div>