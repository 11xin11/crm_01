package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.exception.ParamsException;
import com.yjxxt.crm.model.UserModel;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @RequestMapping("toPasswordPage")
    public String updatePwd(){
        return "user/password";
    }

    @RequestMapping("index")
    public String index(){
        return "user/user";
    }


    @RequestMapping("addOrUpdatePage")
    public String addOrUpdatePage(Integer id, Model model){
        if (id!=null){
            User user=userService.selectByPrimaryKey(id);
            model.addAttribute("user",user);
        }
        return "user/add_update";
    }


    @RequestMapping("toSettingPage")
    public String setting(HttpServletRequest req){
        //获取用户的ID
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //调用方法
        User user = userService.selectByPrimaryKey(userId);
        //存储
        req.setAttribute("user",user);
        //转发
        return "user/setting";
    }



    @RequestMapping("login")
    @ResponseBody
    public ResultInfo say(User user){
        ResultInfo resultInfo=new ResultInfo();
            UserModel userModel=userService.userLogin(user.getUserName(), user.getUserPwd());
            resultInfo.setResult(userModel);
            return resultInfo;
    }

    @PostMapping("setting")
    @ResponseBody
    public ResultInfo sayUpdate(User user){
        ResultInfo resultInfo=new ResultInfo();
        //修改信息的操作
        userService.updateByPrimaryKeySelective(user);
        //返回目标数据的对象
        return resultInfo;
    }
    //头部工具栏的添加
    @RequestMapping ("save")
    @ResponseBody
    public ResultInfo save(User user){
        ResultInfo resultInfo=new ResultInfo();
        //添加用户的操作
        userService.addUser(user);
        //返回目标数据的对象
        return success("用户添加成功！");
    }
    //行内工具栏的修改
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(User user){
        //修改用户的操作
        userService.changeUser(user);
        //返回目标数据的对象
        return success("用户修改成功！");
    }


    //用户模块的批量删除
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer[] ids){
        //删除用户的操作
        userService.removeUserIds(ids);
        //返回目标数据的对象
        return success("用户删除成功！");
    }


    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updatePwd(HttpServletRequest req,String oldPassword,String newPassword,String confirmPwd){
        ResultInfo resultInfo=new ResultInfo();
        //获取cookie中userId
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //修改密码的操作
            userService.changeUserPwd(userId,oldPassword,newPassword,confirmPwd);
        return resultInfo;
    }

    //查询所有的销售人员
    @RequestMapping("sales")
    @ResponseBody
    public List<Map<String,Object>> findSales(){
        List<Map<String, Object>> list = userService.querySales();
        return list;
    }

    //用户模块的列表查询
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> list(UserQuery userQuery){
        return  userService.findUserByParams(userQuery);

    }
}
