package com.mmy.pisp.serviceImpl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmy.pisp.entity.Sites;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mmy.pisp.mapper.SitesMapper;
import com.mmy.pisp.service.SitesService;

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
public class SitesServiceImpl extends ServiceImpl<SitesMapper, Sites> implements SitesService {

    private final SitesMapper sitesMapper;

    @Autowired
    public SitesServiceImpl(SitesMapper sitesMapper){
        this.sitesMapper=sitesMapper;
    }

    @Override
    public List<Sites> getSitesByUserId(){
        Integer userId= StpUtil.getLoginIdAsInt();
        return sitesMapper.selectList(new QueryWrapper<Sites>().eq("user_id",userId));
    }

    @Override
    public Page<Sites> getSitesPageByUserId(int currentPage, int pageSize){
        Integer userId= StpUtil.getLoginIdAsInt();
        Page<Sites> page=new Page<>(currentPage,pageSize);
        sitesMapper.selectPage(page,new QueryWrapper<Sites>().eq("user_id",userId));
        return page;
    }

    @Override
    public int addSite(Sites site){
        return sitesMapper.insert(site);
    }

    @Override
    public int editSite(Sites site){return sitesMapper.updateById(site);}

    @Override
    public int deleteSiteBySiteId(Integer siteId){ return sitesMapper.deleteById(siteId); }

    @Override
    public Page<Sites> searchSites(int currentPage, int pageSize,String siteName,String siteUrl,String siteDescription,String siteAccount,String sitePassword){
        Integer userId= StpUtil.getLoginIdAsInt();
        QueryWrapper<Sites> qw=new QueryWrapper<>();
        qw.eq("user_id",userId);
        if(!StringUtil.isNullOrEmpty(siteName)){
            qw.like("site_name",siteName);
        }
        if(!StringUtil.isNullOrEmpty(siteUrl)){
            qw.like("site_url",siteUrl);
        }
        if(!StringUtil.isNullOrEmpty(siteDescription)){
            qw.like("site_description",siteDescription);
        }
        if(!StringUtil.isNullOrEmpty(siteAccount)){
            qw.like("site_account",siteAccount);
        }
        if(!StringUtil.isNullOrEmpty(sitePassword)){
            qw.like("site_password",sitePassword);
        }
        Page<Sites> page = new Page<>(currentPage, pageSize);
        sitesMapper.selectPage(page, qw);
        return page;
    }

}
