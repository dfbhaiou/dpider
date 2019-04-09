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
            <div style="padding: 15px">
                <h1>发起新投票</h1>
            </div>
            <div class="layui-form">
                <form style="width: 70%" class="layui-form layui-form-pane" lay-filter="topicForm" action="/topic/addition" method="post">
                    <div class="layui-form-item">
                        <label class="layui-form-label">标题</label>
                        <div class="layui-input-block">
                            <input type="text" name="name" required lay-verify="required" placeholder="请输入标题"
                                   autocomplete="off" class="layui-input">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label">最多选几项</label>
                        <div class="layui-input-inline">
                            <input type="text" name="choosenum" required lay-verify="required" placeholder="用户最多选择几个选项"
                                   autocomplete="off" class="layui-input">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label">截止日期</label>
                        <div class="layui-input-block">
                            <input id="endtime" type="text" name="endtime" required lay-verify="required"
                                   placeholder="点击选择截止日期" autocomplete="off" class="layui-input">
                        </div>
                    </div>

                    <input id="currentOptionId" type="hidden" value="2">
                    <input id="currentOptionCount" type="hidden" value="2">

                    <div class="layui-form-item">
                        <label class="layui-form-label">可选择选项</label>
                        <div id="chooseOtherOptions">
                            <div class="layui-input-block" style="padding: 5px">
                                <input type="text" name="option" placeholder="填写选项内容" autocomplete="off" class="layui-input">
                            </div>
                            <div class="layui-input-block" style="padding: 5px">
                                <input type="text" name="option" placeholder="填写选项内容" autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-input-block" style="padding: 5px">
                            <%--<button class="layui-btn layui-btn-sm" onclick="addAnOption()">--%>
                                <%--<i class="layui-icon">&#xe654;新增一个选项</i>--%>
                            <%--</button>--%>
                                <input type="text" placeholder="新增加一个选项" onclick="addAnOption()" autocomplete="off" class="layui-input">

                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">开启匿名</label>
                        <div class="layui-input-block">
                            <input type="checkbox" name="anonymous" value="0" lay-skin="switch" lay-text="开启|关闭">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">投票开启</label>
                        <div class="layui-input-block">
                            <input type="checkbox" name="stat" value="0" checked lay-skin="switch" lay-text="开启|关闭">
                        </div>
                    </div>
                    <div class="layui-form-item layui-form-text">
                        <label class="layui-form-label">投票描述</label>
                        <div class="layui-input-block">
                            <textarea name="content" placeholder="请输入内容" class="layui-textarea"></textarea>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button class="layui-btn" lay-submit lay-filter="topicForm">立即提交</button>
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

        form.on('submit(topicForm)', function (data) {
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