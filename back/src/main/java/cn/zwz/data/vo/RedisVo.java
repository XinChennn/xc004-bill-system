package cn.zwz.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by XinChen, find me by https://www.ixinjiu.cn
 */
@Data
@AllArgsConstructor
public class RedisVo {

    @ApiModelProperty(value = "Redis键")
    private String key;

    @ApiModelProperty(value = "Redis值")
    private String value;

    @ApiModelProperty(value = "保存秒数")
    private Long expireTime;
}
