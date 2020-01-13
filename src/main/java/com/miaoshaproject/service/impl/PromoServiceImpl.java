package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.PromoMapper;
import com.miaoshaproject.dataObject.PromoDO;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoMapper promoMapper;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        PromoDO promoDO = promoMapper.selectByItemId(itemId);
        if(promoDO == null){
            return null;
        }
        PromoModel promoModel = convertFromPromoDO(promoDO);
        if(promoModel == null){
            return null;
        }
        if(promoModel.getStartDate().isAfterNow()){
            promoModel.setStatus(1);
        }else if(promoModel.getEndDate().isBeforeNow()){
            promoModel.setStatus(3);
        }else{
            promoModel.setStatus(2);
        }

        return promoModel;
    }



    private PromoModel convertFromPromoDO(PromoDO promoDo){
        if(promoDo == null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDo, promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDo.getPromoItemPrice()));
        promoModel.setStartDate(new DateTime(promoDo.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDo.getEndDate()));
        return promoModel;
    }

}
