<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/20
  Time: 18:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录</title>
    <link rel="stylesheet" href="../../../ui/layui/css/layui.css">
    <link rel="stylesheet" href="../../../ui/voteonline.css">
    <script src="../../../ui/layui/layui.js"></script>
    <script src="../../../ui/voteonline.js"></script>
    <script src="../../../ui/jquery.min.js"></script>
    <script>
        layui.use('element', function(){
        });
        layui.use('form', function () {
            var form = layui.form;

            form.on('submit(loginForm)', function (data) {
                console.log(JSON.stringify(data.field)) //当前容器的全部表单字段，名值对形式：{name: value}
                return true;
            });

        });
    </script>
</head>
<body background="https://area.sinaapp.com/bingImg11">
<%--<body background="https://img.xjh.me/random_img.php">--%>
<h1 align="center" style="padding: 60px">在线投票系统</h1>
<div class="layui-main site-inline">
    <div style="width: 60%;margin-left: auto;margin-right: auto;">
        <div class="layui-form">
            <form class="layui-form layui-form-pane" lay-filter="loginForm" action="/user/login" method="post">
                <div class="layui-form-item">
                    <label class="layui-form-label">用户名</label>
                    <div class="layui-input-block">
                        <input type="text" name="name" required lay-verify="required" placeholder="请输入您的用户名"
                               autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">密码</label>
                    <div class="layui-input-block">
                        <input type="password" name="password" required lay-verify="required" placeholder="请输入密码" autocomplete="off" class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">角色</label>
                    <div class="layui-input-block">
                        <input type="radio" name="role" value="0" title="普通用户" checked>
                        <input type="radio" name="role" value="1" title="管理员">
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-input-block">
                        <button class="layui-btn" lay-submit lay-filter="loginForm">登录</button>
                    </div>
                </div>
            </form>
        </div>
        <div style="padding: 15px;color: #ffae20">${wrongInfo}</div>
    </div>
</div>
</body>
</html>
