<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/18
  Time: 16:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>后台管理-用户编辑</title>
    <jsp:include page="common/header.jsp"/>
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <jsp:include page="nav.jsp"/>
    <div class="layui-body">
        <div class="site-text site-block " style="padding: 15px;">
            <div style="padding: 15px">
                <h1>用户编辑</h1>
            </div>
            <div class="layui-form">
                <form style="width: 70%" class="layui-form layui-form-pane" lay-filter="userForm"
                      action="/user/updates" method="post">
                    <input type="hidden" name="id" value="${user.id}">
                    <div class="layui-form-item">
                        <label class="layui-form-label">用户名</label>
                        <div class="layui-input-block">
                            <input type="text" name="name" required lay-verify="required" placeholder="请输入用户名"
                                   autocomplete="off" class="layui-input" value="${user.name}">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">密码</label>
                        <div class="layui-input-block">
                            <input type="password" name="password" required lay-verify="required" placeholder="请输入密码" autocomplete="off" class="layui-input"
                            value="${user.password}">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">角色</label>
                        <div class="layui-input-block">
                            <c:if test="${user.role == 0}">
                                <input type="radio" name="role" value="0" title="普通用户" checked>
                                <input type="radio" name="role" value="1" title="管理员">
                            </c:if>
                            <c:if test="${user.role == 1}">
                                <input type="radio" name="role" value="0" title="普通用户">
                                <input type="radio" name="role" value="1" title="管理员" checked>
                            </c:if>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button class="layui-btn" lay-submit lay-filter="userForm">立即提交</button>
                            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
<script>
    layui.use('form', function () {
        var form = layui.form;

        form.on('submit(userForm)', function (data) {
            console.log(JSON.stringify(data.field)) //当前容器的全部表单字段，名值对形式：{name: value}
            return true;
        });

    });

    layui.use('laydate', function () {
        var laydate = layui.laydate;
        laydate.render({
            elem: '#endTime', //指定元素
            // type: 'datetime',
            // format: 'yyyy-MM-dd HH:mm:ss'
        });
    });
</script>