package com.risefalcon.zasgateway.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Deprecated
public class RedisService {
    @Resource // todo ?
    private StringRedisTemplate stringRedisTemplate;

    //string 类型
    /*

	新建一个 RedisService，注入 StringRedisTemplate，使用 		stringRedisTemplate.opsForValue()
可以获取 ValueOperations<String, String> 对象，通过该对象即可读写 redis 数据库了。
*/
    public void set(String key, String value){
        ValueOperations<String, String> forValue = stringRedisTemplate.opsForValue();
        forValue.set(key,value);
    }

    public String get(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }

    //String类型存储对象，存储前要把对象转化为json数据类型
    public <T> void setObj(String key, T t){
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        stringStringValueOperations.set(key, JSON.toJSONString(t));
    }

    public <T> T getObj(String key, Class<T> clazz){
//        Class<T> clazz = (Class<T>)
//                (((ParameterizedType)this.getClass()
//                        .getGenericSuperclass()).getActualTypeArguments()[0]);
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        String tStr = stringStringValueOperations.get(key);
        return JSON.parseObject(tStr, clazz);
    }



    //list类型
/*

使用 stringRedisTemplate.opsForList() 可以获取 ListOperations<String, String> listOperations redis 列表对象，
该列表是个简单的字符串列表，可以支持从左侧添加，也可以支持
从右侧添加，一个列表最多包含 2 ^ 32 -1 个元素。

*/
    public void add(String key, Object o) {
        stringRedisTemplate.opsForList().rightPush(key, JSON.toJSONString(o));
    }

    // todo
    public String pop(String key) {
        return stringRedisTemplate.opsForList().leftPop(key);
    }

    // =========================todo==========================

    //hash类型
    /*
hash 类型其实原理和 string 一样的，但是有两个 key，使用 stringRedisTemplate.opsForHash()
可以获取 HashOperations<String, Object, Object> 对象。比如我们要存储订单信息，所有订单
信息都放在 order 下，针对不同用户的订单实体，可以通过用户的 id 来区分，这就相当于两个 key
了。


*/
    public void put(String h, String hk, String hv){
        stringRedisTemplate.opsForHash().put(h, hk, hv);
    }

    public <T> T get(String h, Object hk, Class<T> clazz) {
       return JSON.parseObject((String) stringRedisTemplate.opsForHash().get(h, hk), clazz);
    }

    public <T> Set<T> getKeys(String h, Class<T> clazz) {
        Set<Object> keys = stringRedisTemplate.opsForHash().keys(h);
        Set<T> result = new HashSet<>();
        for (Object o : keys) {
            log.info((String)o);
            result.add(JSON.parseObject((String)o, clazz)) ;
        }
        return result;
    }

    public <T> List<T> getValues(String h, Class<T> clazz) {
        List<Object> values = stringRedisTemplate.opsForHash().values(h);
        List<T> result = new ArrayList<>();
        for (Object o : values) {
            result.add(JSON.parseObject((String)o, clazz)) ;
        }
        return result;
    }

    public boolean exist(String h, String hk) {
        return stringRedisTemplate.opsForHash().hasKey(h, hk);
    }

    public boolean update(String h, Object oldHk, Object newHk) {
        Object obj = stringRedisTemplate.opsForHash().get(h, oldHk);
        if (obj == null) return false;
        stringRedisTemplate.opsForHash().delete(h, oldHk);
        stringRedisTemplate.opsForHash().put(h, newHk, obj);
        return true;
    }


    public void update(String h, Object oldHk, Object newHk, Object newObj) {
        stringRedisTemplate.opsForHash().delete(h, oldHk);
        stringRedisTemplate.opsForHash().put(h, newHk, newObj);
    }


    public void delete(String h, String hk) {
        stringRedisTemplate.opsForHash().delete(h, hk);
    }
    public Set<Object> getKeys(String h) {
        return stringRedisTemplate.opsForHash().keys(h);
    }
}
