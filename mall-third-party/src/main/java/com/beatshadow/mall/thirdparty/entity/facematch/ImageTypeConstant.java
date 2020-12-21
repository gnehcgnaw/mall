package com.beatshadow.mall.thirdparty.entity.facematch;

/**
 * 图片类型
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/12/18 15:35
 */
public enum ImageTypeConstant {
    BASE64("BASE64","图片的base64值，base64编码后的图片数据，编码后的图片大小不超过2M"),
    URL("URL","图片的 URL地址( 可能由于网络等原因导致下载图片时间过长)"),
    FACE_TOKEN("FACE_TOKEN","人脸图片的唯一标识，调用人脸检测接口时，会为每个人脸图片赋予一个唯一的FACE_TOKEN，同一张图片多次检测得到的FACE_TOKEN是同一个");

    ImageTypeConstant(String type, String description) {
        this.type = type;
        this.description = description;
    }

    /**
     * 类型
     */
    private String type ;

    /**
     * 描述
     * @return
     */
    private String description ;

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
