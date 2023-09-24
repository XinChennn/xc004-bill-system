package cn.zwz.data.serviceimpl;

import cn.zwz.data.dao.mapper.DepartmentMapper;
import cn.zwz.data.entity.Department;
import cn.zwz.data.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 部门 服务层实现
 * Created by XinChen, find me by https://www.ixinjiu.cn
 */
@Service
public class IDepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

}
