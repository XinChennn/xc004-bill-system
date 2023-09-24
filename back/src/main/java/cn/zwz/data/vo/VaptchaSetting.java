package cn.zwz.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by XinChen, find me by https://www.ixinjiu.cn
 */
@Data
public class VaptchaSetting implements Serializable{

    @ApiModelProperty(value = "键")
    private String secretKey;

    @ApiModelProperty(value = "VID")
    private String vid;

    @ApiModelProperty(value = "是否改变secretkey")
    private Boolean changed;

    @ApiModelProperty(value = "场景")
    private String scene;
}
