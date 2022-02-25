package cn.zhukai.shiro.controller;

import cn.zhukai.shiro.entity.ResultBean;
import cn.zhukai.shiro.infrastructure.enums.TypeEnum;
import cn.zhukai.shiro.infrastructure.exception.CuteException;
import cn.zhukai.shiro.infrastructure.util.JwtUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *
 * </p>
 *
 * @author zhukai
 * @date 2022/2/24 17:58
 **/

@RestController
@RequestMapping("/shiro")
public class ShiroController {
    private static Map<String, Map<String, String>> data;
    static {
        data = new ConcurrentHashMap<>();
        Map<String, String> admin = new HashMap<>();
        admin.put("account", "admin");
        admin.put("password", "zhukai");
        admin.put("id", "0");
        admin.put("role", "admin");
        admin.put("permission", "ALL");
        data.put("admin", admin);
    }

    @PostMapping("register")
    @ResponseBody
    public ResultBean<String> register(@RequestBody Map<String, String> param) {
        String account = param.get("account");
        String password = param.get("password");
        if(account == null || account.equals("") || password == null || password.equals("")) {
            throw new CuteException(TypeEnum.BAD_PARAM, "缺少 account/password 参数");
        }
        Map<String, String> user = new HashMap<>();
        user.put("account", account);
        user.put("password", password);
        user.put("id", "1");
        user.put("role", "user");
        user.put("permission", "1");
        data.put(account, user);
        return new ResultBean<>(TypeEnum.SUCCESS);
    }


    @ResponseBody
    @PostMapping("login")
    public ResultBean<String> login(@RequestBody Map<String, String> param) {
        String account = param.get("account");
        String password = param.get("password");
        if(account == null || account.equals("") || password == null || password.equals("")) {
            throw new CuteException(TypeEnum.BAD_PARAM, "缺少 account/password 参数");
        }
        boolean tag = false;
        String token = null;

        Iterator<Map.Entry<String, Map<String, String>>> entries = data.entrySet().iterator();
        while(entries.hasNext()) {
            Map.Entry<String, Map<String, String>> entry = entries.next();
            if(! account.equals(entry.getKey())) {
                continue;
            }
            Map<String, String> map = entry.getValue();
            tag = account.equals(map.get("account")) && password.equals(map.get("password"));
            token = JwtUtil.sign("salt", map.get("id"), 1000 * 60 * 60 * 24L);
            break;
        }
        if(! tag) {
            throw new CuteException(TypeEnum.ERROR_ACCOUNT);
        }

        return new ResultBean<>(token);
    }



    @ResponseBody
    @RequiresRoles(value = "admin")
    @GetMapping("admin")
    public ResultBean<String> admin() {
        return new ResultBean<String>(TypeEnum.SUCCESS);
    }

    @ResponseBody
    @RequiresRoles(value = {"admin", "user"}, logical = Logical.OR)
    @RequiresPermissions(value = {"ALL", "1"}, logical = Logical.OR)
    @GetMapping("level")
    public ResultBean<String> level() {
        return new ResultBean<String>(TypeEnum.SUCCESS);
    }

    @ResponseBody
    @RequiresRoles(value = {"admin", "user"}, logical = Logical.OR)
    @RequiresPermissions(value = {"ALL", "2"}, logical = Logical.OR)
    @GetMapping("level2")
    public ResultBean<String> level2() {
        return new ResultBean<String>(TypeEnum.SUCCESS);
    }


}
