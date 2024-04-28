package com.xiangjiahui.weblog.admin.service.impl;

import com.xiangjiahui.weblog.admin.service.MenuService;
import com.xiangjiahui.weblog.common.domain.dos.MenuDO;
import com.xiangjiahui.weblog.common.domain.mapper.MenuMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("menuService")
public class MenuServiceImpl implements MenuService {

    private final MenuMapper mapper;

    public MenuServiceImpl(MenuMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public List<MenuDO> getAllMenu() {
        return mapper.selectList(null);
    }
}
