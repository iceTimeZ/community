function like(btn, entityType, entityId) {
    $.post(
        CONTEXT_PATH + "/like",
        {"entityType": entityType, "entityId": entityId},
        function (data) {
            // 将服务器返回的模型数据标准字符串形式，转成js对象
            data = $.parseJSON(data);
            if (data.code == 0) {
                $(btn).children("i").text(data.likeCount);
                $(btn).children("b").text(data.likeStatus == 1 ? '已赞':'赞');
            } else {
                alert(data.msg);
            }
        }
    )
}