layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.on("submit(addOrUpdateRole)", function (obj){
        //loading
        var index=top.layer.msg("数据正在加载中...",{icon:16,time:false,shade:0.8})
        /*发送ajax添加*/
        var url=ctx+"/role/save";
        //修改
        if ($("input[name=id]").val()){
            url=ctx+"/role/update";
        }
        //$.post()发送
        $.post(url,obj.field,function (result){
            if (result.code==200){
                setTimeout(function (){
                    //关闭
                    top.layer.close(index);
                    //提示一下
                    top.layer.msg("添加OK",{icon:6});
                    //关闭iframe
                    top.layer.closeAll(index);
                    //刷新
                    parent.location.reload();
                },500)
            }else{
                layer.msg(result.msg,{icon: 5});
            }
        },"json");
        //取消跳转
        return false;
    });

    /*取消*/
    $("closeBtn").click(function (){
        //假设这是iframe
        var index=parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    });
});