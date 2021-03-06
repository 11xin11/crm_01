package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseQuery;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.Permission;
import com.yjxxt.crm.bean.Role;
import com.yjxxt.crm.mapper.ModuleMapper;
import com.yjxxt.crm.mapper.PermissionMapper;
import com.yjxxt.crm.mapper.RoleMapper;
import com.yjxxt.crm.query.RoleQuery;
import com.yjxxt.crm.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RoleService extends BaseService<Role,Integer> {

    @Autowired(required = false)
    private RoleMapper roleMapper;

    @Autowired(required = false)
    private PermissionMapper permissionMapper;

    @Autowired(required = false)
    private ModuleMapper moduleMapper;

    //查询所有的角色信息
    public List<Map<String,Object>> findRoles(Integer userId){

        return roleMapper.selectRoles(userId);
    }

    /**
     * 角色的条件查询以及分页
     * @param roleQuery
     * @return
     */
    public Map<String,Object> findRoleByParam(RoleQuery roleQuery){
        //实例化Map
        Map<String,Object> map=new HashMap<String,Object>();
        //开启分页单位
        PageHelper.startPage(roleQuery.getPage(),roleQuery.getLimit());
        PageInfo<Role> rlist=new PageInfo<>(selectByParams(roleQuery));
        //准备数据
        map.put("code",0);
        map.put("msg","success");
        map.put("count",rlist.getTotal());
        map.put("data",rlist.getList());
        //返回目标map
        return map;
    }

    /**
     * 一.验证
     * 1.角色名非空
     * 2.角色名唯一
     * 二.默认参数
     * is_valid=1
     * createDtae
     * updateDate
     * 三。添加成功是否
     */
    public void addRole(Role role){
        //1.角色名非空
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名称");
        //2.角色名唯一
        Role temp=roleMapper.selectRoleByName(role.getRoleName());
        AssertUtil.isTrue(temp!=null,"角色已经存在");
        //3.设定默认值
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        //4.添加成功是否
        AssertUtil.isTrue(insertHasKey(role)<1,"添加失败了");
    }

    /**
     * 授权
     * @param roleId
     * @param mids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer roleId, Integer[] mids) {
        AssertUtil.isTrue(roleId==null||roleMapper.selectByPrimaryKey(roleId)==null,"请选择角色");
        AssertUtil.isTrue(mids==null||mids.length==0,"最少选择一个资源");
        //roleId,mid---t-permission
        //统计当前角色的资源数量
        int count=permissionMapper.countRoleModulesByRoleId(roleId);
        if (count>0){
            //删除当前角色的资源信息
            AssertUtil.isTrue(permissionMapper.deleteRoleModulesByRoleId(roleId)!=count,"角色资源分配失败");
        }
        List<Permission> plist=new ArrayList<>();
        if (mids!=null&&mids.length>0){
            //遍历mids
            for (Integer mid:mids){
                //实例化对象
                Permission permission=new Permission();
                permission.setRoleId(roleId);
                permission.setModuleId(mids[0]);
                //权限码
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                plist.add(permission);
            }
        }
        AssertUtil.isTrue(permissionMapper.insertBatch(plist)!=plist.size(),"授权失败");
    }

    /**
     * 一.验证
     *      id验证
     * 1.角色名非空
     * 2.角色名唯一
     * 二.默认参数
     *      is_valid=1
     *      createDtae
     *      updateDate
     * 三.修改成功是否
     */
    public void changeRole(Role role){
        //验证当前对象是否存在
        Role temp=roleMapper.selectByPrimaryKey(role.getId());
        AssertUtil.isTrue(temp==null,"待修改的记录不存在");
        //2.角色名唯一
        Role temp2=roleMapper.selectRoleByName(role.getRoleName());
        AssertUtil.isTrue(temp2!=null &&!(temp2.getId().equals(role.getId())),"角色已经存在");
        //3.设定默认值
        role.setUpdateDate(new Date());
        //4.修改是否成功
        AssertUtil.isTrue(updateByPrimaryKeySelective(role)<1,"修改失败了");

    }
    //角色删除
    public void removeRoleById(Role role) {
        //验证
        AssertUtil.isTrue(role.getId()==null||selectByPrimaryKey(role.getId())==null,"请选择数据");
        //设定默认值
        role.setIsValid(0);
        //判断是否成功
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)<1,"删除失败了");
    }
}
