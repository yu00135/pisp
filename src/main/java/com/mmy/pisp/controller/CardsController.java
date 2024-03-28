package com.mmy.pisp.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mmy.pisp.entity.Cards;
import com.mmy.pisp.result.ResultVO;
import com.mmy.pisp.service.CardsService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 马鸣宇
 * @since 2022-01-17
 */
@Api(tags="Card API接口文档")
@RestController
@RequestMapping("/card")
public class CardsController {

    @Value("${rsa.publicKey}")
    private String publicKey;

    @Value("${rsa.privateKey}")
    private String privateKey;

    @Value("${aes.key}")
    private String aesKey;

    private final CardsService cardsService;

    @Autowired
    public CardsController(CardsService cardsService){
        this.cardsService=cardsService;
    }

    @PostMapping("/getList")
    public ResultVO getList() {
        List<Cards> cards=cardsService.getCardsByUserId();
        return ResultVO.success(200, "获取卡片列表成功", cards);
    }

    @ApiOperation(value = "分页查找卡包列表")
    @PostMapping("/getPage")
    public ResultVO getPage(@RequestBody Map<String,Object> map) {
        int currentPage=(int) map.get("currentPage");
        int pageSize=(int) map.get("pageSize");
        Page<Cards> cards=cardsService.getCardsPageByUserId(currentPage,pageSize);
        return ResultVO.success(200, "获取卡片列表成功", cards);
    }

    @ApiOperation(value = "批量删除选中卡片")
    @PostMapping("/deleteSelected")
    public ResultVO deleteSelected(@RequestBody List<Integer> list){
        if(list.isEmpty()){
            return ResultVO.fail(101,"请先选择要删除的卡片",null);
        }
        for(Integer i:list){
            if(cardsService.deleteCardByCardId(i)<0){
                return ResultVO.fail(201,"删除卡片失败",null);
            }
        }
        return ResultVO.success(200,"删除卡片成功",null);
    }

    @ApiOperation(value = "删除卡片")
    @PostMapping("/delete")
    public ResultVO delete(@RequestBody Map<String, Object> map){
        Integer cardId=(Integer) map.get("cardId");
        System.out.println(cardId);
        if(cardsService.deleteCardByCardId(cardId)>0){
            return ResultVO.success(200,"删除卡片成功",null);
        }else {
            return ResultVO.fail(201,"删除卡片失败",null);
        }
    }

    @ApiOperation(value = "添加卡片")
    @PostMapping("/add")
    public ResultVO add(@RequestBody Map<String, Object> map){
        Integer userId=StpUtil.getLoginIdAsInt();
        String cardName=(String) map.get("cardName");
        String cardNumber=(String) map.get("cardNumber");
        String encryptString=(String) map.get("cardPassword");
        RSA rsa=new RSA(privateKey,publicKey);
        byte[] decrypt = rsa.decrypt(encryptString, KeyType.PrivateKey);
        String rawPassword=new String(decrypt);
        AES aes = new AES("ECB", "PKCS7Padding",
                // 密钥，可以自定义
                aesKey.getBytes());
        // iv加盐，按照实际需求添加
        //"DYgjCEIMVrj2W9xN".getBytes()
        String cardPassword = aes.encryptBase64(rawPassword);
        Cards card=new Cards();
        card.setCardName(cardName);
        card.setCardNumber(cardNumber);
        card.setCardPassword(cardPassword);
        card.setUserId(userId);
        if(cardsService.addCard(card)>0){
            return ResultVO.success(200,"添加卡片成功",null);
        }else {
            return ResultVO.fail(201,"添加卡片失败",null);
        }
    }

    @ApiOperation(value = "更改卡片信息")
    @PostMapping("/edit")
    public ResultVO edit(@RequestBody Map<String, Object> map){
        Integer userId=StpUtil.getLoginIdAsInt();
        Integer cardId=(Integer) map.get("cardId");
        String cardName=(String) map.get("cardName");
        String cardNumber=(String) map.get("cardNumber");
        String encryptString=(String) map.get("cardPassword");
        RSA rsa=new RSA(privateKey,publicKey);
        byte[] decrypt = rsa.decrypt(encryptString, KeyType.PrivateKey);
        String rawPassword=new String(decrypt);
        AES aes = new AES("ECB", "PKCS7Padding",
                // 密钥，可以自定义
                aesKey.getBytes());
        // iv加盐，按照实际需求添加
        //"DYgjCEIMVrj2W9xN".getBytes()
        String cardPassword = aes.encryptBase64(rawPassword);
        Cards card=new Cards();
        card.setCardId(cardId);
        card.setCardName(cardName);
        card.setCardNumber(cardNumber);
        card.setCardPassword(cardPassword);
        card.setUserId(userId);
        if(cardsService.editCard(card)>0){
            return ResultVO.success(200,"修改卡片成功",null);
        }else {
            return ResultVO.fail(201,"修改卡片失败",null);
        }
    }

    @ApiOperation(value = "查找卡片")
    @PostMapping("/search")
    public ResultVO search(@RequestBody Map<String,Object> map) {
        String cardName=(String) map.get("cardName");
        String cardNumber=(String) map.get("cardNumber");
        String cardPassword=(String) map.get("cardPassword");
        int currentPage=(int) map.get("currentPage");
        int pageSize=(int) map.get("pageSize");
        Page<Cards> cards=cardsService.searchCards(currentPage,pageSize,cardName,cardNumber,cardPassword);
        return ResultVO.success(200, "查找成功", cards);
    }
}
