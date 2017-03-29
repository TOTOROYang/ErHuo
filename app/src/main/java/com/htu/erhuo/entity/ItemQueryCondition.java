package com.htu.erhuo.entity;

import java.math.BigDecimal;

/**
 * Description
 * Created by yzw on 2017/3/29.
 */

public class ItemQueryCondition {
    /**
     * 商品分类编号
     */
    public String sid;

    /**
     * 商品标题
     */
    public String title;

    /**
     * 创建人
     */
    public String creator;

    /**
     * 商品状态
     */
    public String status;

    /**
     * 价格下限
     */
    public BigDecimal priceLow;

    /**
     * 价格上限
     */
    public BigDecimal priceHigh;

    /**
     * 排序规则
     */
    public String rule;

    /**
     * 查询页数
     */
    public int page;

    /**
     * 偏移量（从第几行开始取）：(page-1)*limit
     */
    public int offset;

    /**
     * 查询数量
     */
    public int limit = 20;

    private ItemQueryCondition(Builder builder) {
        sid = builder.sid;
        title = builder.title;
        creator = builder.creator;
        status = builder.status;
        priceLow = builder.priceLow;
        priceHigh = builder.priceHigh;
        rule = builder.rule;
        page = builder.page;
        offset = builder.offset;
        limit = builder.limit;
    }


    public static final class Builder {
        private String sid;
        private String title;
        private String creator;
        private String status;
        private BigDecimal priceLow;
        private BigDecimal priceHigh;
        private String rule;
        private int page = 0;
        private int offset = 0;
        private int limit = 20;

        public Builder() {
        }

        public Builder sid(String val) {
            sid = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder creator(String val) {
            creator = val;
            return this;
        }

        public Builder status(String val) {
            status = val;
            return this;
        }

        public Builder priceLow(BigDecimal val) {
            priceLow = val;
            return this;
        }

        public Builder priceHigh(BigDecimal val) {
            priceHigh = val;
            return this;
        }

        public Builder rule(String val) {
            rule = val;
            return this;
        }

        public Builder page(int val) {
            page = val;
            return this;
        }

        public Builder offset(int val) {
            offset = val;
            return this;
        }

        public Builder limit(int val) {
            limit = val;
            return this;
        }

        public ItemQueryCondition build() {
            return new ItemQueryCondition(this);
        }
    }
}
