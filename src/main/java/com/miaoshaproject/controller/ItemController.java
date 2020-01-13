package com.miaoshaproject.controller;


import com.miaoshaproject.controller.viewobject.ItemVO;
import com.miaoshaproject.dataObject.ItemDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.response.CommenReturnType;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.PromoModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", origins = {"*"})   // 处理跨域共享，session数据共享问题
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "/createItem", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    @ResponseBody
    public CommenReturnType createItem(@RequestParam(name = "title")String title,
                                       @RequestParam(name = "description")String description,
                                       @RequestParam(name = "price")BigDecimal price,
                                       @RequestParam(name = "stock")Integer stock,
                                       @RequestParam(name = "imgUrl")String imgUrl) throws BusinessException {
        ItemModel itemModel = new ItemModel();
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setDescription(description);
        itemModel.setImgUrl(imgUrl);
        itemModel.setTitle(title);
        ItemModel itemModelForRet = itemService.createItem(itemModel);
        ItemVO itemVO = convertFromModelToVO(itemModelForRet);
        return CommenReturnType.create(itemVO);
    }

    @RequestMapping(value = "/getitem", method = RequestMethod.GET)
    @ResponseBody
    public CommenReturnType getItem(@RequestParam(name = "id")Integer id){
        ItemModel itemModel = itemService.getItemById(id);
        ItemVO itemVO = convertFromModelToVO(itemModel);
        return CommenReturnType.create(itemVO);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommenReturnType listItem(){
        List<ItemModel> modelList = itemService.listItem();
        List<ItemVO> voList = new ArrayList<>();
        for(ItemModel model : modelList){
            ItemVO itemVO = convertFromModelToVO(model);
            voList.add(itemVO);
        }
        return CommenReturnType.create(voList);
    }

    private ItemVO convertFromModelToVO(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);
        if(itemModel.getPromoModel() != null){
            PromoModel pm = itemModel.getPromoModel();
            itemVO.setPromoId(pm.getId());
            itemVO.setPromoPrice(pm.getPromoItemPrice());
            itemVO.setPromoStatus(pm.getStatus());
            itemVO.setStartDate(pm.getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        }else {
            itemVO.setPromoStatus(0);
        }

        return itemVO;
    }

}
