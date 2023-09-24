package cn.zwz.data.serviceimpl;

import cn.zwz.data.dao.mapper.UserMapper;
import cn.zwz.data.entity.User;
import cn.zwz.data.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户 服务层实现
 * Created by XinChen, find me by https://www.ixinjiu.cn
 */
@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
}
