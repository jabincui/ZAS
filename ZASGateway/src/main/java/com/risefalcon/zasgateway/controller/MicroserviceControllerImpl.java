package com.risefalcon.zasgateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.risefalcon.zasgateway.config.Constant;
import com.risefalcon.zasgateway.model.Microservice;
import com.risefalcon.zasgateway.model.Role;
import com.risefalcon.zasgateway.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/microservice")
public class MicroserviceControllerImpl implements MicroserviceController{
    @Autowired
    RedisService redisService;

    /**
     * 新建微服务
     * @param ms key          | value
     *           ----------------------------
     *           id           | 不用传，会忽略
     *           name         | 新微服务名
     * @return
     *      INVALID：找不到name或name是空串
     *      EXIST：已存在此名称的微服务
     *      PASS：正常通过
     */
    @Override
    public JSONObject ins(Microservice ms) {
        log.info(ms.getId());
        JSONObject jsonObject = new JSONObject();
        if (ms.getName() == null || ms.getName().equals("")) {
            jsonObject.put(Constant.RESULT_KEY, Constant.INVALID);
            return jsonObject;
        }
        for (Microservice m : redisService.getValues(Constant.MICROSERVICE, Microservice.class)) {
            if (m.getName().equals(ms.getName())) {
                jsonObject.put(Constant.RESULT_KEY, Constant.EXIST);
                return jsonObject;
            }
        }
        Microservice m = new Microservice(ms.getName());
        redisService.put(Constant.MICROSERVICE, m.getId(), JSON.toJSONString(m));

        // 初始化PERMIT_ALL
        Role role = new Role(m.getId(), "PERMIT_ALL");
        role.setId(Constant.PA + role.getId());
        redisService.put(Constant.ROLE, role.getId(), JSON.toJSONString(role));

        jsonObject.put(Constant.RESULT_KEY, Constant.PASS);
        jsonObject.put(Constant.OBJ, m);
        return jsonObject;
    }

    /**
     * 修改微服务名
     * @param microservice key        |   value
     *                     ---------------------------
     *                     id         | 要更新的微服务id
     *                     name       | 要修改成的微服务名
     * @return
     *      INVALID：找不到某个key或某个value是空串、
     *      NOT_EXIST：原来的微服务名不存在
     *      EXIST：要改成的微服务名已存在
     *      PASS：正常通过
     */
    @Override
    public String upd(Microservice microservice) {
        if (microservice.getId() == null || microservice.getId().equals("")
                || microservice.getName() == null || microservice.getName().equals("")) {
            return Constant.INVALID;
        }
        if (!redisService.exist(Constant.MICROSERVICE, microservice.getId())) {
            return Constant.NOT_EXIST;
        }
        for (Microservice m : redisService.getValues(Constant.MICROSERVICE, Microservice.class)) {
            if (m.getName().equals(microservice.getName())) return Constant.EXIST;
        }
        redisService.put(Constant.MICROSERVICE, microservice.getId(), JSON.toJSONString(microservice));
        return Constant.PASS;
    }

    /**
     * 删除微服务
     * @param id 要删除的微服务的id
     * @return
     *      INVALID：找不到id或id是空串
     *      NOT_EXIST：id不存在
     *      PASS：正常通过
     */
    @Override
    public String del(String id) {
        log.info(id);
        if (id == null || id.equals("")) {
            return Constant.INVALID;
        }
        if (!redisService.exist(Constant.MICROSERVICE, id)) {
            return Constant.NOT_EXIST;
        }
        redisService.delete(Constant.MICROSERVICE, id);
        return Constant.PASS;
    }

    /**
     * 获取全部微服务
     * @return 全部微服务
     */
    @Override
    public List<Microservice> getAll() {
        return redisService.getValues(Constant.MICROSERVICE, Microservice.class);
    }

    @GetMapping("/keys")
    public Set<Object> getKeys() {
        return redisService.getKeys(Constant.MICROSERVICE);
    }
}
