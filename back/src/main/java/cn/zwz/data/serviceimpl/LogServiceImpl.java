package cn.zwz.data.serviceimpl;

import cn.zwz.data.dao.LogDao;
import cn.zwz.data.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 日志接口实现
 * Created by XinChen, find me by https://www.ixinjiu.cn
 */
@Service
@Transactional
public class LogServiceImpl implements LogService {

    @Autowired
    private LogDao logDao;

    @Override
    public LogDao getRepository() {
        return logDao;
    }
}
