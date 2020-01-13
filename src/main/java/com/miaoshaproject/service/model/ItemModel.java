package com.miaoshaproject.service.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ItemModel {

    private Integer id;

    // 商品名称
    @NotBlank(message = "商品名称不能为空")
    private String title;

    // 价格
    @NotNull(message = "必须填写价格")
    @Min(value = 0, message = "最低价格为0")
    @Max(value = 500000, message = "最高价格不能超过50万")
    // 这里必须是BigDecimal类型，因为java后台转到前端会有精度损失，这是一种止损的措施（类似于stringfy()这种函数有损失）
    private BigDecimal price;

    // 库存
    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存最低为0")
    private Integer stock;

    // 文字描述
    @NotBlank(message = "商品必须有文字描述")
    private String description;

    // 销量
    private Integer sales;

    @NotBlank(message = "图片信息不能为空")
    private String imgUrl;


    //适用聚合模型
    private PromoModel promoModel;

    public PromoModel getPromoModel() {
        return promoModel;
    }

    public void setPromoModel(PromoModel promoModel) {
        this.promoModel = promoModel;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }
}
