package com.htu.erhuo.entity.enums;

/**
 * 商品分类（分类编号及名称可能有变动）
 */
public enum ItemSortEnum {

    BOOK("101", "图书音像"),
    COMPUTER("102", "电脑外设"),
    DIGITAL("103", "手机数码"),
    FOOD("104", "食品"),
    CLOTHES("105", "服装"),
    SHOES("106", "鞋靴"),
    ELECTRIC("107", "家电"),
    SPORT("108", "运动户外"),
    BAG("109", "箱包"),
    COSMETIC("110", "美妆"),
    OTHER("111", "其他");

    private String id;// 分类号

    private String name;// 分类名称

    ItemSortEnum(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static String nameOf(String id) {
        for (ItemSortEnum itemSortEnum : values()) {
            if (itemSortEnum.id.equals(id)) {
                return itemSortEnum.getName();
            }
        }
        return null;
    }

    public static String idOf(String name){
        for (ItemSortEnum itemSortEnum : values()) {
            if (itemSortEnum.name.equals(name)) {
                return itemSortEnum.getId();
            }
        }
        return null;
    }

}
