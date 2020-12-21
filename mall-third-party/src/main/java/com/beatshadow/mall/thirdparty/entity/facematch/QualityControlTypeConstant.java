package com.beatshadow.mall.thirdparty.entity.facematch;

/**
 * 图片质量控制
 *    若图片质量不满足要求，则返回结果中会提示质量检测失败
 *    默认 NONE
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/12/18 15:59
 */
public enum QualityControlTypeConstant {
    NONE("NONE","不进行控制"),
    LOW("LOW","较低的质量要求"),
    NORMAL("NORMAL","一般的质量要求"),
    HIGH("HIGH","较高的质量要求");

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

    /**
     * 类型
     */
    private String type ;

    /**
     * 描述
     * @return
     */
    private String description ;

    QualityControlTypeConstant(String type, String description) {
        this.type = type;
        this.description = description;
    }
}
