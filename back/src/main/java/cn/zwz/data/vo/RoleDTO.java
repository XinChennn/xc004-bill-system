package cn.zwz.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by XinChen, find me by https://www.ixinjiu.cn
 */
@Data
@Accessors(chain = true)
public class RoleDTO {

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色ID")
    private String id;

    @ApiModelProperty(value = "角色备注")
    private String description;
}
