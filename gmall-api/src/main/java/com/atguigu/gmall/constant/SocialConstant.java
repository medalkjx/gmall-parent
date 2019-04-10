package com.atguigu.gmall.constant;

/**
 * @author ：mei
 * @date ：Created in 2019/4/1 0001 下午 22:29
 * @description：社交使用的常量信息
 * @modified By：
 * @version: $
 */
public class SocialConstant {

    public enum SocialTypeEnum {
        QQ("1", "qq"), WEIBO("2", "weibo");
        private String id;
        private String type;

        SocialTypeEnum(String id, String type) {
            this.id = id;
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }
    }
}
