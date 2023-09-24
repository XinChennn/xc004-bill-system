package cn.zwz.basics.log;

import java.lang.annotation.*;

/**
 * 日志注解
 * Created by XinChen, find me by https://www.ixinjiu.cn
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {

        /**
         * 日志名称
         */
        public String about() default "默认备注内容";

        /**
         * 日志类型
         * @return
         */
        public LogType type() default LogType.USER_LOGIN;
}
