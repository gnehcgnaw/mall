package com.beatshadow.mall.thirdparty.entity.facematch;

/**
 * 活体检测控制
 *      若活体检测结果不满足要求，则返回结果中会提示活体检测失败
 *      默认 NONE
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/12/18 15:59
 */
public enum LivenessControlTypeConstant {
    NONE("NONE","不进行控制"),
    LOW("LOW","较低的活体要求(高通过率 低攻击拒绝率)"),
    NORMAL("NORMAL","一般的活体要求(平衡的攻击拒绝率, 通过率)"),
    HIGH("HIGH","较高的活体要求(高攻击拒绝率 低通过率)");

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

    LivenessControlTypeConstant(String type, String description) {
        this.type = type;
        this.description = description;
    }
}
