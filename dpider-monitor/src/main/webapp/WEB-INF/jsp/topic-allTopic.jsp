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
                <h1>投票管理列表</h1>
            </div>
            <div>
                <table id="topicTable" lay-filter="test"></table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
<script>

    layui.use('layer', function(){
    });

    layui.use('table', function() {
        var table = layui.table;

        table.render({
            elem: '#topicTable'
            , height: 500
            , url: '/topic/list' //数据接口
            , page: true //开启分页
            , cols: [[ //表头
                {field: 'name', title: '主题', width: 120, fixed: 'left'}
                , {field: 'choosenum', title: '可选择数', width: 90, sort: true}
                , {field: 'content', title: '内容', width: 200}
                // , {field: 'optionlist', title: '选项Json', width: 200}
                , {field: 'anonymous', title: '是否匿名', width: 100, sort: true}
                , {field: 'stat', title: '状态', width: 80, sort: true, sort: true}
                , {field: 'bywho', title: '创建者', width: 80, sort: true, sort: true}
                , {field: 'countpeople', title: '已投票人数', width: 120, sort: true, sort: true}
                , {field: 'starttime', title: '开始时间', width: 120, sort: true, sort: true}
                , {field: 'endtime', title: '截止时间', width: 120, sort: true}
                , {field: 'adminAction', title: '操作', width: 250}
            ]]
        });
    })
</script>