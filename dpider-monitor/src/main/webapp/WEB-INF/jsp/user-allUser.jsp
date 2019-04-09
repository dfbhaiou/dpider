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
    <title>个人中心</title>
    <jsp:include page="common/header.jsp"/>
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <jsp:include page="nav.jsp"/>
    <div class="layui-body">
        <div class="site-text site-block " style="padding: 15px;">
            <div style="padding: 15px;">
                <h1>用户管理列表</h1>
            </div>
            <div>
                <table id="allUserTable" lay-filter="test"></table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
<script>
    layui.use('table', function() {
        var table = layui.table;

        table.render({
            elem: '#allUserTable'
            , height: 600
            , url: '/user/list' //数据接口
            , page: true //开启分页
            , cols: [[ //表头
                {field: 'name', title: '用户名', width: 200, fixed: 'left'}
                , {field: 'role', title: '角色', width: 100, sort: true}
                , {field: 'action', title: '操作', width: 200}
            ]]
        });
    })
</script>