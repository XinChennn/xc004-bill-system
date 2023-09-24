package cn.zwz.data.serviceimpl;

import cn.zwz.data.dao.mapper.DictMapper;
import cn.zwz.data.entity.Dict;
import cn.zwz.data.service.IDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 数据字典 服务层实现
 * Created by XinChen, find me by https://www.ixinjiu.cn
 */
@Service
public class IDictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

}
