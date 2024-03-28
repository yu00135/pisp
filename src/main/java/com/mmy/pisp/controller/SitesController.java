package com.mmy.pisp.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mmy.pisp.entity.Sites;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mmy.pisp.result.ResultVO;
import com.mmy.pisp.service.SitesService;

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
@Api(tags="Site API接口文档")
@RestController
@RequestMapping("/site")
public class SitesController {

    @Value("${rsa.publicKey}")
    private String publicKey;

    @Value("${rsa.privateKey}")
    private String privateKey;

    @Value("${aes.key}")
    private String aesKey;

    private final SitesService sitesService;

    @Autowired
    public SitesController(SitesService sitesService){
        this.sitesService=sitesService;
    }

    @PostMapping("/getList")
    public ResultVO getList() {
        List<Sites> sites=sitesService.getSitesByUserId();
        return ResultVO.success(200, "获取网站列表成功", sites);
    }

    @ApiOperation(value = "分页获取网站列表")
    @PostMapping("/getPage")
    public ResultVO getPage(@RequestBody Map<String,Object> map) {
        int currentPage=(int) map.get("currentPage");
        int pageSize=(int) map.get("pageSize");
        Page<Sites> sites=sitesService.getSitesPageByUserId(currentPage,pageSize);
        return ResultVO.success(200, "获取网站列表成功", sites);
    }


    @ApiOperation(value = "批量删除选中网站")
    @PostMapping("/deleteSelected")
    public ResultVO deleteSelected(@RequestBody List<Integer> list){
        if(list.isEmpty()){
            return ResultVO.fail(400,"请先选择要删除的网站",null);
        }
        for(Integer i:list){
            if(sitesService.deleteSiteBySiteId(i)<0){
                return ResultVO.fail(500,"删除网站失败",null);
            }
        }
        return ResultVO.success(200,"删除网站成功",null);
    }

    @ApiOperation(value = "删除网站")
    @PostMapping("/delete")
    public ResultVO delete(@RequestBody Map<String, Object> map){
        Integer siteId=(Integer) map.get("siteId");
        System.out.println(siteId);
        if(sitesService.deleteSiteBySiteId(siteId)>0){
            return ResultVO.success(200,"删除网站成功",null);
        }else {
            return ResultVO.fail(201,"删除网站失败",null);
        }
    }


    @ApiOperation(value = "添加网站")
    @PostMapping("/add")
    public ResultVO add(@RequestBody Map<String, Object> map){
        Integer userId=StpUtil.getLoginIdAsInt();
        String siteName=(String) map.get("siteName");
        String siteUrl=(String) map.get("siteUrl");
        String siteDescription=(String) map.get("siteDescription");
        String siteAccount=(String) map.get("siteAccount");
        String encryptString=(String) map.get("sitePassword");
        RSA rsa=new RSA(privateKey,publicKey);
        byte[] decrypt = rsa.decrypt(encryptString, KeyType.PrivateKey);
        String rawPassword=new String(decrypt);
        AES aes = new AES("ECB", "PKCS7Padding",
                // 密钥，可以自定义
                aesKey.getBytes());
        // iv加盐，按照实际需求添加
        //"DYgjCEIMVrj2W9xN".getBytes()
        String sitePassword = aes.encryptBase64(rawPassword);
        Sites site=new Sites();
        site.setSiteName(siteName);
        site.setSiteUrl(siteUrl);
        site.setSiteDescription(siteDescription);
        site.setSiteAccount(siteAccount);
        site.setSitePassword(sitePassword);
        site.setUserId(userId);
        if(sitesService.addSite(site)>0){
            return ResultVO.success(200,"添加网站成功",null);
        }else {
            return ResultVO.fail(201,"添加网站失败",null);
        }
    }

    @ApiOperation(value = "更改网站信息")
    @PostMapping("/edit")
    public ResultVO edit(@RequestBody Map<String, Object> map){
        Integer userId=StpUtil.getLoginIdAsInt();
        Integer siteId=(Integer) map.get("siteId");
        String siteName=(String) map.get("siteName");
        String siteUrl=(String) map.get("siteUrl");
        String siteDescription=(String) map.get("siteDescription");
        String siteAccount=(String) map.get("siteAccount");
        String encryptString=(String) map.get("sitePassword");
        RSA rsa=new RSA(privateKey,publicKey);
        byte[] decrypt = rsa.decrypt(encryptString, KeyType.PrivateKey);
        String rawPassword=new String(decrypt);
        AES aes = new AES("ECB", "PKCS7Padding",
                // 密钥，可以自定义
                aesKey.getBytes());
                // iv加盐，按照实际需求添加
                //"DYgjCEIMVrj2W9xN".getBytes()
        String sitePassword = aes.encryptBase64(rawPassword);
        Sites site=new Sites();
        site.setSiteId(siteId);
        site.setSiteName(siteName);
        site.setSiteUrl(siteUrl);
        site.setSiteDescription(siteDescription);
        site.setSiteAccount(siteAccount);
        site.setSitePassword(sitePassword);
        site.setUserId(userId);
        if(sitesService.editSite(site)>0){
            return ResultVO.success(200,"修改网站成功",null);
        }else {
            return ResultVO.fail(201,"修改网站失败",null);
        }
    }


    @ApiOperation(value = "查找网站")
    @PostMapping("/search")
    public ResultVO search(@RequestBody Map<String,Object> map) {
        String siteName=(String) map.get("siteName");
        String siteUrl=(String) map.get("siteUrl");
        String siteDescription=(String) map.get("siteDescription");
        String siteAccount=(String) map.get("siteAccount");
        String sitePassword=(String) map.get("sitePassword");
        int currentPage=(int) map.get("currentPage");
        int pageSize=(int) map.get("pageSize");
        Page<Sites> sites=sitesService.searchSites(currentPage,pageSize,siteName,siteUrl,siteDescription,siteAccount,sitePassword);
        return ResultVO.success(200, "查找成功", sites);
    }
}
