layui.use(['form', 'layer','formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        formSelects=layui.formSelects;
    /**
     * 监听submit事件
     * 实现营销机会的添加与更新
     */
    form.on("submit(addOrUpdateSaleChance)", function (obj) {

        //判断是添加还是修改，id==null，添加，id！=null修改
        var url=ctx+"/sale_chance/save";

        //判断当前的页面隐藏域有没有id,有id做修改，否则做添加
        if($("input[name=id]").val()){
            url=ctx+"/sale_chance/update"
        }
        //发送ajax请求
        $.ajax({
            type:"post",
            url:url,
            data:obj.field,
            dataType:"json",
            success:function (obj){
                if (obj.code==200){
                    layer.msg("添加成功");
                    //刷新页面
                    window.parent.location.reload()
                }else {
                    layer.msg(obj.msg,{icon:6});
                }
            }
        });
        //取消跳转
        return false;
    });


    //取消功能
    $("#closeBtn").click(function (){
        //获取当前弹出层的索引
        var idx=parent.layer.getFrameIndex(window.name);
        //根据索引关闭
        parent.layer.close(idx);
    });


    //添加下拉框
    var assignMan=$("input[name='man']").val();
    $.ajax({
        type: "post",
        url:ctx+"/user/sales",
        dataType: "json",
        success:function(data){
            //遍历
            for (var x in data){
                if (data[x].id==assignMan){
                    $("#assignMan").append("<option selected value='"+data[x].id+"'>"+data[x].uname+"</option>");
                }else {
                    $("#assignMan").append("<option value='"+data[x].id+"'>"+data[x].uname+"</option>");
                }

            }
            //重新渲染
            layui.form.render("select");
        }
    });

    /**
     * 用户模块的选择角色的下拉框内容
     */
    formSelects.config('selectId',{
        type:"post",
        searchUrl:ctx + "/role/findRoles",
        //自定义返回数据中name的key, 默认 name
        keyName: 'roleName',
        //自定义返回数据中value的key, 默认 value
        keyVal: 'id'
    },true);

});