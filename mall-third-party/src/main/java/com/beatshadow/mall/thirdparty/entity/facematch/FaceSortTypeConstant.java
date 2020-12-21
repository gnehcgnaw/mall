package com.beatshadow.mall.thirdparty.entity.facematch;

/**
 * 人脸检测排序类型
 *     默认为0
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/12/18 16:35
 */
public enum FaceSortTypeConstant {
    ZERO(0,"代表检测出的人脸按照人脸面积从大到小排列"),
    ONE(1,"代表检测出的人脸按照距离图片中心从近到远排列");;

    /**
     * 类型
     */
    private int type ;

    /**
     * 描述
     * @return
     */
    private String description ;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    FaceSortTypeConstant(int type, String description) {
        this.type = type;
        this.description = description;
    }


}
