layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);


    //监听提交
    form.on('submit(saveBtn)',function (data){

        //发送ajax请求
        $.ajax({
            type:"post",
            url:ctx+"/user/setting",
            data:{
                userName: data.field.userName,
                phone:data.field.phone,
                email:data.field.email,
                trueName:data.field.trueName,
                id:data.field.id
            },
            dataType:"json",
            success:function (msg){
                if (msg.code==200){
                    layer.msg("保存成功了！！！",function (){
                        //清空Cookie
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"});
                        //跳转页面
                        window.parent.location.href=ctx+"/index";
                    });
                }else{
                    //修改失败提示
                    layer.msg(msg.msg);
                }
            }
        });
        return false;
    });
});