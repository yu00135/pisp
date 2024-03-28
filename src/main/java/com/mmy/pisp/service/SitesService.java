package com.mmy.pisp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mmy.pisp.entity.Sites;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 马鸣宇
 * @since 2022-01-17
 */
public interface SitesService extends IService<Sites> {

    List<Sites> getSitesByUserId();

    Page<Sites> getSitesPageByUserId(int currentPage, int pageSize);

    int addSite(Sites site);

    int editSite(Sites site);

    int deleteSiteBySiteId(Integer siteId);

    Page<Sites> searchSites(int currentPage, int pageSize,String siteName,String siteUrl,String siteDescription,String siteAccount,String sitePassword);

}
