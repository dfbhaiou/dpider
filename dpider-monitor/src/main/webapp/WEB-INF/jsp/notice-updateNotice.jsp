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
    <title>公告管理</title>
    <jsp:include page="common/header.jsp"/>
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <jsp:include page="nav.jsp"/>
    <div class="layui-body">
        <div class="site-text site-block " style="padding: 15px;">
            <div style="padding: 15px">
                <h1>公告编辑</h1>
            </div>
            <div class="layui-form">
                <form style="width: 70%" class="layui-form layui-form-pane" lay-filter="noticeForm"
                      action="/notice/updates" method="post">
                    <input type="hidden" name="id" value="${notice.id}">
                    <div class="layui-form-item">
                        <label class="layui-form-label">标题</label>
                        <div class="layui-input-block">
                            <input type="text" name="title" required lay-verify="required" placeholder="请输入标题"
                                   autocomplete="off" class="layui-input" value="${notice.title}">
                        </div>
                    </div>
                    
                    <div class="layui-form-item layui-form-text">
                        <label class="layui-form-label">公告内容</label>
                        <div class="layui-input-block">
                            <textarea name="content" placeholder="请输入内容" style="height: 399px;" class="layui-textarea">${notice.content}</textarea>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button class="layui-btn" lay-submit lay-filter="noticeForm">立即提交</button>
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

        form.on('submit(noticeForm)', function (data) {
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