package com.mmy.pisp.serviceImpl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mmy.pisp.entity.Cards;
import com.mmy.pisp.mapper.CardsMapper;
import com.mmy.pisp.service.CardsService;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 马鸣宇
 * @since 2022-01-17
 */
@Service
public class CardsServiceImpl extends ServiceImpl<CardsMapper, Cards> implements CardsService {

    private final CardsMapper cardsMapper;

    @Autowired
    public CardsServiceImpl(CardsMapper cardsMapper){
        this.cardsMapper=cardsMapper;
    }

    @Override
    public List<Cards> getCardsByUserId() {
        Integer userId= StpUtil.getLoginIdAsInt();
        return cardsMapper.selectList(new QueryWrapper<Cards>().eq("user_id",userId));
    }

    @Override
    public Page<Cards> getCardsPageByUserId(int currentPage, int pageSize){
        Integer userId= StpUtil.getLoginIdAsInt();
        Page<Cards> page=new Page<>(currentPage,pageSize);
        cardsMapper.selectPage(page,new QueryWrapper<Cards>().eq("user_id",userId));
        return page;
    }

    @Override
    public int addCard(Cards card) {
        return cardsMapper.insert(card);
    }

    @Override
    public int editCard(Cards card) {
        return cardsMapper.updateById(card);
    }

    @Override
    public int deleteCardByCardId(Integer cardId) {
        return cardsMapper.deleteById(cardId);
    }

    @Override
    public Page<Cards> searchCards(int currentPage, int pageSize,String cardName, String cardNumber, String cardPassword) {
        Integer userId= StpUtil.getLoginIdAsInt();
        QueryWrapper<Cards> qw=new QueryWrapper<>();
        qw.eq("user_id",userId);
        if(!StringUtil.isNullOrEmpty(cardName)){
            qw.like("card_name",cardName);
        }
        if(!StringUtil.isNullOrEmpty(cardNumber)){
            qw.like("card_number",cardNumber);
        }
        if(!StringUtil.isNullOrEmpty(cardPassword)){
            qw.like("card_password",cardPassword);
        }
        Page<Cards> page = new Page<>(currentPage, pageSize);
        cardsMapper.selectPage(page, qw);
        return page;
    }

}
