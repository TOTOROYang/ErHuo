package com.htu.erhuo.entity.enums;

/**
 * 商品分类排序规则
 */
public enum ItemRankEnum {
    PRICE_DESC("pd", "price desc, "),// 按价格降序
    PRICE_ASC("pa", "price asc, "),// 按价格升序
    SORTID_DESC("sid", "sort_id desc, "),// 按分类降序
    SORTID_ASC("sia", "sort_id asc, "),// 按分类升序
    WATCH_DESC("wd", "watch desc, "),// 按点击量降序
    WATCH_ASC("wa", "watch asc, "),// 按点击量升序
    STAR_DESC("sd", "star desc, "),// 按收藏量降序
    STAR_ASC("sa", "star asc, ");// 按收藏量升序


    private String code;

    private String rank;

    ItemRankEnum(String code, String rank) {
        this.code = code;
        this.rank = rank;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    /**
     * 根据sortid代号获取对应的排序语句
     * @param code 排序规则代号
     * @return 排序规则sql语句。没有对应规则的话返回""
     */
    public static String rankOf(String code) {
        for (ItemRankEnum itemRankEnum : values()) {
            if (itemRankEnum.getCode().equals(code)) {
                return itemRankEnum.getRank();
            }
        }
        return "";
    }
}
