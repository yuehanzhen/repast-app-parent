package com.aaa.lee.app.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @Company AAA软件教育
 * @Author Seven Lee
 * @Date Create in 2019/11/21 11:05
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ShopInfoVo implements Serializable {

    private Long shopId;
    private String name;
    private String province;
    private String city;
    private String borough;
    private String lng;
    private String lat;
    private String phone;
    private Long facilityId;
    private String title;
    private String description;

}
