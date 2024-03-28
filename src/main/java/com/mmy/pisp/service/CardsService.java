package com.mmy.pisp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mmy.pisp.entity.Cards;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 马鸣宇
 * @since 2022-01-17
 */
public interface CardsService extends IService<Cards> {

    List<Cards> getCardsByUserId();

    Page<Cards> getCardsPageByUserId(int currentPage, int pageSize);

    int addCard(Cards card);

    int editCard(Cards card);

    int deleteCardByCardId(Integer cardId);

    Page<Cards> searchCards(int currentPage, int pageSize,String cardName, String cardNumber, String cardPassword);
}
