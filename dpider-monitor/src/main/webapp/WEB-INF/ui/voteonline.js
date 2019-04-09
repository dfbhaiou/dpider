function loadPage() {
    $.get("/topic/list",null,function (res) {
        var data = res.data;
        var tmp = [];// tabel 构建
        for ( var i = 0; i < data.length; i++) {
            var item = data[i];
            tmp.push("<div class=\"layui-card\">\n" +
                "                <div class=\"layui-card-header\">" + item.name + "</div>\n" +
                "                <div class=\"layui-card-body\">" +
                "<div>" + item.content + "</div>" +
                "<span style='display:block; width: 98%;text-align:right;'>" + "<a href='/topic/view/" + item.id + "'>查看详情...</a>" +  "</span>" +
                "<span style='display:block; width: 100%;text-align:right;'>开始时间：" + item.starttime + "&nbsp;&nbsp;截止日期：" + item.endtime +"&nbsp;&nbsp;创建者：" + item.bywho + "</span>" +
                "                </div>" +
                "            </div>");
        }
        $("#topicContainer").html(tmp.join(""));
        // console.log(JSON.stringify(res));
    });
}
function loadNotice() {
    $.get("/notice/list",null,function (res) {
        var data = res.data;
        var tmp = [];// tabel 构建
        for ( var i = 0; i < data.length; i++) {
            var item = data[i];
            tmp.push("<div class=\"layui-card\">\n" +
                "                <div class=\"layui-card-header\">" + item.title + "</div>\n" +
                "                <div class=\"layui-card-body\">" +
                "<div>" + item.content + "</div>" +
                // "<span style='display:block; width: 98%;text-align:right;'>" + "<a href='/topic/view/" + item.id + "'>查看详情...</a>" +  "</span>" +
                "<span style='display:block; width: 100%;text-align:right;'>创建时间：" + item.createtime + "&nbsp;&nbsp;创建者：" + item.username + "</span>" +
                "                </div>" +
                "            </div>");
        }
        $("#noticeContainer").html(tmp.join(""));
        // console.log(JSON.stringify(res));
    });
}
function addAnOption() {
    var count = $("#currentOptionCount").val();
    count = parseInt(count);
    if (count < 10) {
        $("#currentOptionCount").val((count+1));
        var newId = parseInt($("#currentOptionId").val())+1;
        $("#currentOptionId").val(newId);
        var newOptionHtml = "<div id=\""+ newId +"\" class=\"layui-input-block\" style=\"padding: 5px\">\n" +
            "                            <input id=\"option-input-" + newId + "\" type=\"text\" name=\"option\" placeholder=\"填写选项内容\" autocomplete=\"off\" class=\"layui-input\">\n" +
            "                            <button class=\"layui-btn layui-btn-sm\" onclick=\"deleteAnOption(" + newId + ")\">\n" +
            "                                <i class=\"layui-icon\">&#xe640;删除这个选项↑</i>\n" +
            "                            </button>\n" +
            "                        </div>";
        $("#chooseOtherOptions").append(newOptionHtml);
        $("#option-input-"+newId).focus();
    } else {
        var layer = layui.layer;
        layer.msg('最多支持十个');
    }
}
function deleteAnOption(id) {
    $("#" + id).remove();
    var count = $("#currentOptionCount").val();
    count = parseInt(count)
    $("#currentOptionCount").val((count-1));
}
function changeTopicStat(id,stat) {
    $.get("/topic/changeStat/" + id + "/"+stat,null,function (data) {
        window.location.reload();
    });
}
function delUser(id) {
    $.get("/user/deletion/" + id,null,function (data) {
        window.location.reload();
        // var layer = layui.layer;
        // layer.msg(data);
    });
}
function gotoUpdateUser(id) {
    window.location.href='/user/gotoUpdateNotice/' + id;
}
function delTopic(id) {
    layui.use('layer', function(){
        var layer = layui.layer;
        layer.msg("删除中。。。");
    })

    $.get("/topic/deletion/" + id,null,function (data) {
        window.location.reload();
    });
}
function exportJson(id) {
    layui.use('layer', function(){
        var layer = layui.layer;
        $.get("/topic/exportJson/" + id,null,function (data) {
            layer.open({
                title: '导出为JSON'
                ,content: JSON.stringify(data)
            });
        });
    });

}
function exportXlsx(id) {
    window.open("/topic/downloadExcel/" + id);
}
function delNotice(id) {
    $.get("/notice/deletion/" + id,null,function (data) {
        window.location.reload();
        // var layer = layui.layer;
        // layer.msg(data);
    });
}
function gotoUpdateNotice(id) {
    window.location.href='/notice/gotoUpdateNotice/' + id;
}