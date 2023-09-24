package cn.zwz.basics.security;

import cn.zwz.basics.exception.ZwzException;
import cn.zwz.data.entity.User;
import cn.zwz.data.service.UserService;
import cn.zwz.data.utils.ZwzNullUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 根据账号/手机号查询用户所有信息
 * Created by XinChen, find me by https://www.ixinjiu.cn
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    private static final String LOGIN_FAIL_DISABLED_PRE = "userLoginDisableFlag:";

    @Override
    @ApiOperation(value = "根据账号/手机号查询用户所有信息")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String loginFailFlag = LOGIN_FAIL_DISABLED_PRE + username;
        String value = redisTemplate.opsForValue().get(loginFailFlag);
        Long timeRest = redisTemplate.getExpire(loginFailFlag, TimeUnit.MINUTES);
        if(!ZwzNullUtils.isNull(value)){
            throw new ZwzException("试错超限，请您在" + timeRest + "分钟后再登");
        }
        User user = userService.findByMobile(username);
        if(user == null) {
            user = userService.findByUsername(username);
        }
        return new SecurityUserDetails(user);
    }
}
