package com.atguigu.gmall.sms.vo;

import com.atguigu.gmall.sms.entity.FlashPromotionSession;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ：mei
 * @date ：Created in 2019/3/25 0025 下午 21:02
 * @description：可选场次及其数量
 * @modified By：
 * @version: $
 */
@Data
public class FlashPromotionSessionCountResult extends FlashPromotionSession {

    private Integer productCount;
}
