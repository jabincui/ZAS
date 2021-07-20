package com.risefalcon.zasgateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.risefalcon.zasgateway.service.AuthorityServiceImpl;
import com.risefalcon.zasgateway.service.MicroserviceServiceImpl;
import com.risefalcon.zasgateway.service.RoleServiceImpl;
import com.risefalcon.zasgateway.util.Constant;
import com.risefalcon.zasgateway.security_model.Authority;
import com.risefalcon.zasgateway.security_model.Role;
import com.risefalcon.zasgateway.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/role")
public class RoleControllerImpl implements  RoleController{

    @Autowired
    private RoleServiceImpl roleService;
    @Autowired
    private AuthorityServiceImpl authorityService;
    @Autowired
    private MicroserviceServiceImpl microserviceService;

    /**
     * 新建角色
     *
     * @param role key       | value
     *             ------------------------
     *             id        | 不用传，会忽略
     *             msId      | 角色所属微服务id
     *             name      | 角色名
     * @return INVALID：找不到msId，name，或msId，name是空串
     * EXIST：name已存在
     * PASS：正常通过
     */
    @Override
    public JSONObject ins(Role role) {
        log.info(role.getId());
        JSONObject jsonObject = new JSONObject();
        if (role.getMsId() == null || role.getMsId().equals("")
                || role.getName() == null || role.getName().equals("")) {
            jsonObject.put(Constant.RESULT_KEY, Constant.INVALID);
            return jsonObject;
        }
        /**
         * todo: 这里有一个疑似bug
         */
        for (Role r: roleService.list()) {
            if (r.getMsId().equals(role.getMsId())
                    && r.getName().equals(role.getName())) {
                jsonObject.put(Constant.RESULT_KEY, Constant.EXIST);
                return jsonObject;
            }
        }

        Role r = new Role(role.getMsId(), role.getName());
        roleService.save(r);

        jsonObject.put(Constant.RESULT_KEY, Constant.PASS);
        jsonObject.put(Constant.OBJ, r);
        return jsonObject;
    }

    /**
     * 修改角色名
     *
     * @param role key      | value
     *             -----------------------
     *             id       | 要更新的角色id
     *             msId     | 不用传，会忽略
     *             name     | 要改成的角色名
     * @return INVALID：找不到id，name，或id，name是空串
     * EXIST：name已存在
     * PASS：正常通过
     */
    @Override
    public String upd(Role role) {
        if (role.getId() == null || role.getId().equals("")) {
            return Constant.INVALID;
        }
        Role role1 = roleService.getById(role.getId());
        if (null == role1) {
            return Constant.NOT_EXIST;
        }
        for (Role r: roleService.list()) {
            if (r.getMsId().equals(role.getMsId())
                    && r.getName().equals(role.getName())) {
                return Constant.EXIST;
            }
        }
        role.setMsId(role1.getMsId());
        roleService.saveOrUpdate(role);
        return Constant.PASS;
    }

    /**
     * 删除角色
     *
     * @param id 要删除的角色id
     * @return INVALID：找不到id，或id是空串，或id以"pa"开头（PERMIT_ALL）
     * NOT_EXIST: id不存在
     * PASS：正常通过
     */
    @Override
    public String del(String id) {
        log.info(id);
        if (id == null || id.equals("") || id.startsWith(Constant.PA)) {
            return Constant.INVALID;
        }
        Role role = roleService.getById(id);
        if (null == role) {
            return Constant.NOT_EXIST;
        }
        // 删除有关权限项
        authorityService.remove(new QueryWrapper<Authority>().eq(Authority.ROLEID, id));
        roleService.removeById(id);
        return Constant.PASS;
    }

    /**
     * 获取全部角色
     * @return 全部角色
     */
    @Override
    public List<Role> getAll() {
        return roleService.list();
    }


//    @GetMapping("/keys")
//    public Set<Object> getKeys() {
//        return redisService.getKeys(Constant.ROLE);
//    }

    @Override
    public JSONObject getByMsId(String msId) {
        JSONObject jsonObject = new JSONObject();
        if (msId == null || msId.equals("")) {
            jsonObject.put(Constant.RESULT_KEY, Constant.INVALID);
        }
        if (null == microserviceService.getById(msId)) {
            jsonObject.put(Constant.RESULT_KEY, Constant.NOT_EXIST);
            return jsonObject;
        }
        List<Role> roles = roleService.list(new QueryWrapper<Role>().eq(Role.MSID, msId));
        jsonObject.put(Constant.RESULT_KEY, Constant.PASS);
        jsonObject.put(Constant.OBJ, roles);
        return jsonObject;
    }
}
