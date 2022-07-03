package com.atguigu.cmsservice.controller;


import com.atguigu.cmsservice.entity.CrmBanner;
import com.atguigu.cmsservice.service.CrmBannerService;
import com.atguigu.commonutils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author Quan
 * @since 2021-08-05
 */
@RestController
@Api( "网站首页Banner列表-admin端")
@RequestMapping("/cmsservice/admin/banner")
public class CrmBannerAdminController {
    @Resource
    private CrmBannerService bannerService;

    @GetMapping("/pageBanner/{page}/{limit}")
    public R pageBanner(@PathVariable Integer page, @PathVariable Integer limit) {
        Page<CrmBanner> bannerPage = new Page<>(page, limit);
        bannerService.page(bannerPage, null);
        return R.ok().data("items", bannerPage.getRecords()).data("total", bannerPage.getTotal());
    }

    @ApiOperation(value = "获取Banner")
    @GetMapping("get/{id}")
    public R get(@PathVariable String id) {
        CrmBanner banner = bannerService.getById(id);
        return R.ok().data("item", banner);
    }

    @ApiOperation(value = "新增Banner")
    @PostMapping("save")
    @CacheEvict(value = "banner", allEntries=true)
    public R save(@RequestBody CrmBanner banner) {
        bannerService.save(banner);
        return R.ok();
    }

    @ApiOperation(value = "修改Banner")
    @PutMapping("update")
    @CacheEvict(value = "banner", allEntries=true)
    public R updateById(@RequestBody CrmBanner banner) {
        bannerService.updateById(banner);
        return R.ok();
    }

    @ApiOperation(value = "删除Banner")
    @DeleteMapping("remove/{id}")
    @CacheEvict(value = "banner", allEntries=true)
    public R remove(@PathVariable String id) {
        bannerService.removeById(id);
        return R.ok();
    }
}

