package cn.zwz.basics.code.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 子项VO
 * Created by XinChen, find me by https://www.ixinjiu.cn
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @ApiModelProperty(value = "子项标题")
    private String title;

    @ApiModelProperty(value = "子项类别")
    private String type;

    @ApiModelProperty(value = "子项大写")
    private String upperName;

    @ApiModelProperty(value = "子项小写")
    private String lowerName;

    @ApiModelProperty(value = "子项行名")
    private String lineName;
}
