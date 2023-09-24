package cn.zwz.data.utils;

import cn.zwz.data.entity.Permission;
import cn.zwz.data.vo.MenuVo;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiOperation;

/**
 * 菜单转换VO类
 * Created by XinChen, find me by https://www.ixinjiu.cn
 */
public class VoUtil {

    @ApiOperation(value = "菜单转换VO类")
    public static MenuVo permissionToMenuVo(Permission permission){
        MenuVo vo = new MenuVo();
        BeanUtil.copyProperties(permission, vo);
        return vo;
    }
}
