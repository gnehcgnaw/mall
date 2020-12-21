package com.beatshadow.mall.thirdparty.entity.facematch;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/12/18 15:20
 */
@NoArgsConstructor
@Data
public class BaiduFaceMatchRequestEntity{

    /**
     * image : sfasq35sadvsvqwr5q...
     * image_type : BASE64
     * face_type : LIVE
     * quality_control : LOW
     * liveness_control : HIGH
     */

    private String image;
    private String image_type;
    private String face_type;
    private String quality_control;
    private String liveness_control;

}
