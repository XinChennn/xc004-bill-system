package cn.zwz.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by XinChen, find me by https://www.ixinjiu.cn
 */
@Data
@Accessors(chain = true)
public class PermissionDTO {

    @ApiModelProperty(value = "页面路径")
    private String path;

    @ApiModelProperty(value = "菜单标题")
    private String title;
}
