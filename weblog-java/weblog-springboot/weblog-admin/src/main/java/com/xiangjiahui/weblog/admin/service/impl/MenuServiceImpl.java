package com.xiangjiahui.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiangjiahui.weblog.admin.service.MenuService;
import com.xiangjiahui.weblog.common.domain.dos.MenuDO;
import com.xiangjiahui.weblog.common.domain.mapper.MenuMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("menuService")
public class MenuServiceImpl implements MenuService {

    private final MenuMapper mapper;

    public MenuServiceImpl(MenuMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public List<MenuDO> getAllMenu() {
        return mapper.selectList(new LambdaQueryWrapper<MenuDO>().eq(MenuDO::getParentId, 0));
    }

    @Override
    public List<MenuDO> getTreeMenu() {
        List<MenuDO> menuDOS = mapper.selectList(null);
        return menuDOS.stream().filter(menu -> menu.getParentId() == 0L)
                .peek(menu -> menu.setChildren(getChildren(menu))).collect(Collectors.toList());

    }

    public List<MenuDO> getChildren(MenuDO menuDO){
        return mapper.selectList(new LambdaQueryWrapper<MenuDO>().eq(MenuDO::getParentId,menuDO.getId()));
    }
}
