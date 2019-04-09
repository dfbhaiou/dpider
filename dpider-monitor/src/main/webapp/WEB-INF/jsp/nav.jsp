<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="layui-header">
    <div class="layui-logo">投票系统后台</div>

    <c:if test="${currentUser == null}">
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="/login">
                    登录</a>
            </li>
        </ul>
    </c:if>
    <c:if test="${currentUser != null}">
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="javascript:;">
                    <img src="//tva1.sinaimg.cn/crop.0.0.118.118.180/5db11ff4gw1e77d3nqrv8j203b03cweg.jpg" class="layui-nav-img">
                        ${currentUser.name}
                    <span class="layui-nav-more"></span></a>
                    <%--<dl class="layui-nav-child layui-anim layui-anim-upbit">--%>
                    <%--<dd><a href="">基本资料</a></dd>--%>
                    <%--<dd><a href="">安全设置</a></dd>--%>
                    <%--</dl>--%>
            </li>
            <li class="layui-nav-item"><a href="/user/logout">退出</a></li>
            <span class="layui-nav-bar" style="left: 76px; top: 55px; width: 0px; opacity: 0;"></span>
        </ul>
    </c:if>
</div>


<div class="layui-side layui-bg-black">
    <div class="layui-side-scroll">
        <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
        <ul class="layui-nav layui-nav-tree"  lay-filter="test">
            <li class="layui-nav-item layui-nav-itemed">
                <a class="" href="javascript:;">投票管理</a>
                <dl class="layui-nav-child">
                    <dd><a href="/admin/addTopic">发起投票</a></dd>
                    <dd><a href="/admin/allTopic">投票管理列表</a></dd>
                </dl>
            </li>
            <li class="layui-nav-item">
                <a href="javascript:;">用户管理</a>
                <dl class="layui-nav-child">
                    <dd><a href="/admin/addUser">新增加用户</a></dd>
                    <dd><a href="/admin/allUser">用户管理列表</a></dd>
                </dl>
            </li>
            <li class="layui-nav-item">
                <a href="javascript:;">公告管理</a>
                <dl class="layui-nav-child">
                    <dd><a href="/admin/addNotice">新增加公告</a></dd>
                    <dd><a href="/admin/allNotice">公告管理列表</a></dd>
                </dl>
            </li>
        </ul>
    </div>
</div>