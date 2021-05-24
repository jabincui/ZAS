package com.risefalcon.zasgateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
    private RedisService redisService;

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
        for (Role r: redisService.getValues(Constant.ROLE, Role.class)) {
            if (r.getMsId().equals(role.getMsId())
                    && r.getName().equals(role.getName())) {
                jsonObject.put(Constant.RESULT_KEY, Constant.EXIST);
                return jsonObject;
            }
        }

        Role r = new Role(role.getMsId(), role.getName());
        redisService.put(Constant.ROLE, r.getId(), JSON.toJSONString(r));

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
        if (!redisService.exist(Constant.ROLE, role.getId())) {
            return Constant.NOT_EXIST;
        }
        for (Role r: redisService.getValues(Constant.ROLE, Role.class)) {
            if (r.getMsId().equals(role.getMsId())
                    && r.getName().equals(role.getName())) {
                return Constant.EXIST;
            }
        }
        role.setMsId(redisService.get(Constant.ROLE, role.getId(), Role.class).getMsId());
        redisService.put(Constant.ROLE, role.getId(), JSON.toJSONString(role));
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
        if (!redisService.exist(Constant.ROLE, id)) {
            return Constant.NOT_EXIST;
        }

        // 删除有关权限项
        for (Authority authority: redisService.getValues(Constant.AUTHORITY, Authority.class)) {
            if (authority.getRoleId().equals(id)) {
                redisService.delete(Constant.AUTHORITY, authority.getId());
            }
        }

        redisService.delete(Constant.ROLE, id);
        return Constant.PASS;
    }

    /**
     * 获取全部角色
     * @return 全部角色
     */
    @Override
    public List<Role> getAll() {
        return redisService.getValues(Constant.ROLE, Role.class);
    }


    @GetMapping("/keys")
    public Set<Object> getKeys() {
        return redisService.getKeys(Constant.ROLE);
    }

    @Override
    public JSONObject getByMsId(String msId) {
        JSONObject jsonObject = new JSONObject();
        if (msId == null || msId.equals("")) {
            jsonObject.put(Constant.RESULT_KEY, Constant.INVALID);
        }
        if (!redisService.exist(Constant.MICROSERVICE, msId)) {
            jsonObject.put(Constant.RESULT_KEY, Constant.NOT_EXIST);
            return jsonObject;
        }
        List<Role> roles = redisService.getValues(Constant.ROLE, Role.class);
        roles.removeIf(role -> !role.getMsId().equals(msId));
        jsonObject.put(Constant.RESULT_KEY, Constant.PASS);
        jsonObject.put(Constant.OBJ, roles);
        return jsonObject;
    }
}
