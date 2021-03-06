package com.risefalcon.zasgateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.risefalcon.zasgateway.util.Constant;
import com.risefalcon.zasgateway.security_model.Signup;
import com.risefalcon.zasgateway.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/signup")
public class SignupControllerImpl implements SignupController{

    @Autowired
    private RedisService redisService;

    /**
     * 新建注册角色池
     *      用于注册时分配角色
     *      一次注册只能使用一个注册角色池
     * @param signup key           | value
     *               ------------------------------
     *               id            | 不用传，会忽略
     *               name          | 角色池的友好名称
     *               rolesId       | 不用传，会忽略
     * @return
     *      INVALID：找不到名字或是空串
     *      PASS：正常通过
     */
    @Override
    public JSONObject ins(Signup signup) {
        JSONObject jsonObject = new JSONObject();
        if (signup.getName() == null || signup.getName().equals("")) {
            jsonObject.put(Constant.RESULT_KEY, Constant.INVALID);
            return jsonObject;
        }
        Signup su = new Signup(signup.getName());
        redisService.put(Constant.SIGNUP, su.getId(),
                JSONObject.toJSONString(su));
        jsonObject.put(Constant.RESULT_KEY, Constant.PASS);
        jsonObject.put(Constant.OBJ, su);
        return jsonObject;
    }

    @Override
    public String upd(Signup signup) {
        if (signup.getId() == null || signup.getId().equals("")
                || signup.getName() == null || signup.getName().equals("")) {
            return Constant.INVALID;
        }
        if (redisService.exist(Constant.SIGNUP, signup.getId())) {
            return Constant.NOT_EXIST;
        }
        redisService.put(Constant.SIGNUP, signup.getId(), signup.getName());
        return Constant.PASS;
    }

    @Override
    public String del(String id) {
        if (id == null || id.equals("")) {
            return Constant.INVALID;
        }
        if (!redisService.exist(Constant.SIGNUP, id)) {
            return Constant.NOT_EXIST;
        }
        redisService.delete(Constant.SIGNUP, id);
        return Constant.PASS;
    }

    @Override
    public List<Signup> getAll() {
        return redisService.getValues(Constant.SIGNUP, Signup.class);
    }

    /**
     * 给signup增加一个role
     * @param jsonObject key        | value
     *                   ------------------------
     *                   id         | signup id
     *                   roleId     | 角色id
     * @return
     *      INVALID：找不到id，roleId或是空串
     *      NOT_EXIST：id或roleId对应的实体不存在
     *      EXIST：roleId已存在
     *      PASS：正常通过
     */
    @PostMapping("/add_role")
    public String addRole(@RequestBody JSONObject jsonObject) {
        String id = (String) jsonObject.get("id");
        String roleId = (String) jsonObject.get("roleId");
        if (id == null || id.equals("")
                || roleId == null || roleId.equals("")) {
            return Constant.INVALID;
        }
        if ((!redisService.exist(Constant.SIGNUP, id))
                || (!redisService.exist(Constant.ROLE, roleId))) {
            return Constant.NOT_EXIST;
        }
        Signup signup = redisService
                .get(Constant.SIGNUP, id, Signup.class);
        if (signup.getRolesId().contains(roleId)) {
            return Constant.EXIST;
        }
        signup.getRolesId().add(roleId);
        log.info(signup.getRolesId().toString());
        redisService.put(Constant.SIGNUP, id,
                JSONObject.toJSONString(signup));
        return Constant.PASS;
    }

    @PostMapping("/remove_role")
    public String removeRole(@RequestBody JSONObject jsonObject) {
        String id = (String) jsonObject.get("id");
        String roleId = (String) jsonObject.get("roleId");
        if (id == null || id.equals("")
                || roleId == null || roleId.equals("")) {
            return Constant.INVALID;
        }
        if ((!redisService.exist(Constant.SIGNUP, id))
                || (!redisService.exist(Constant.ROLE, roleId))) {
            return Constant.NOT_EXIST;
        }
        Signup signup = redisService
                .get(Constant.SIGNUP, id, Signup.class);
        if (!signup.getRolesId().contains(roleId)) {
            return Constant.NOT_EXIST;
        }
        signup.getRolesId().remove(roleId);
        log.info(signup.getRolesId().toString());
        redisService.put(Constant.SIGNUP, id,
                JSONObject.toJSONString(signup));
        return Constant.PASS;
    }
}
