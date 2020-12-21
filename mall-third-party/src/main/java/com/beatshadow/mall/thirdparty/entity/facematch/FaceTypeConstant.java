package com.beatshadow.mall.thirdparty.entity.facematch;

/**
 * 人脸的类型
 *      默认LIVE
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/12/18 15:59
 */
public enum FaceTypeConstant {

    LIVE("LIVE","表示生活照：通常为手机、相机拍摄的人像图片、或从网络获取的人像图片等，"),
    IDCARD("IDCARD","表示身份证芯片照：二代身份证内置芯片中的人像照片"),
    WATERMARK("WATERMARK","表示带水印证件照：一般为带水印的小图，如公安网小图"),
    CERT("CERT","表示证件照片：如拍摄的身份证、工卡、护照、学生证等证件图片"),
    INFRARED("INFRARED","表示红外照片：使用红外相机拍摄的照片");
    /**
     * 类型
     */
    private String type ;
    /**
     * 描述
     * @return
     */
    private String description ;

    FaceTypeConstant(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
