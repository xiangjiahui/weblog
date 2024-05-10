package com.xiangjiahui.weblog.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
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
    public PageResponse getPageMenuList(MenuPageReqVO vo) {
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
        List<MenuDO> childrenList = menuDOS.stream().filter(menuDO -> menuDO.getParentId() > 0).collect(Collectors.toList());
        List<MenuDO> parents = menuDOS.stream().filter(menuDO -> menuDO.getParentId() == 0).collect(Collectors.toList());

        parents.forEach(parent -> {
            List<MenuDO> treeMenu = Lists.newArrayList();
            childrenList.forEach(children -> {
                if (parent.getId().equals(children.getParentId())) {
                    treeMenu.add(children);
                    parent.setChildren(treeMenu);
                }
            });
        });
        return parents;


    }

    @Override
    public void addMenu(MenuReqVO vo) {
        MenuDO menuDO = new MenuDO();
        MenuReqVO.vo2do(menuDO,vo);
        mapper.insert(menuDO);
    }


    @Override
    public List<MenuDO> getAllMenu() {
        return mapper.selectList(null);
    }
}
