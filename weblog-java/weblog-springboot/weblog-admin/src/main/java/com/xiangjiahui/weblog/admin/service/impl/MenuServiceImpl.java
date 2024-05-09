package com.xiangjiahui.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangjiahui.weblog.admin.service.MenuService;
import com.xiangjiahui.weblog.common.domain.dos.MenuDO;
import com.xiangjiahui.weblog.common.domain.mapper.MenuMapper;
import com.xiangjiahui.weblog.common.model.MenuPageReqVO;
import com.xiangjiahui.weblog.common.model.MenuReqVO;
import com.xiangjiahui.weblog.common.model.MenuVO;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service("menuService")
public class MenuServiceImpl implements MenuService {

    private final MenuMapper mapper;

    public MenuServiceImpl(MenuMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public PageResponse getAllMenu(MenuPageReqVO vo) {
        String name = vo.getName();
        Long currentPage = vo.getCurrentPage();
        Long size = vo.getSize();
        LocalDate startDate = vo.getStartDate();
        LocalDate endDate = vo.getEndDate();
        Page<MenuDO> menuDOPage = mapper.selectPageList(name,currentPage, size, startDate, endDate);
        List<MenuVO> collect = menuDOPage.getRecords().stream().map(menuDO -> {
            MenuVO menuVO = new MenuVO();
            MenuVO.do2vo(menuDO, menuVO);
            return menuVO;
        }).collect(Collectors.toList());
        return PageResponse.success(menuDOPage, collect);
    }

    @Override
    public List<MenuDO> getTreeMenu() {
        List<MenuDO> menuDOS = mapper.selectList(null);
        return menuDOS.stream().filter(menu -> menu.getParentId() == 0L)
                .peek(menu -> menu.setChildren(getChildren(menu))).collect(Collectors.toList());

    }


    @Override
    public void addMenu(MenuReqVO vo) {
        MenuDO menuDO = new MenuDO();
        MenuReqVO.vo2do(menuDO,vo);
        mapper.insert(menuDO);
    }

    public List<MenuDO> getChildren(MenuDO menuDO){
        return mapper.selectList(new LambdaQueryWrapper<MenuDO>().eq(MenuDO::getParentId,menuDO.getId()));
    }
}
