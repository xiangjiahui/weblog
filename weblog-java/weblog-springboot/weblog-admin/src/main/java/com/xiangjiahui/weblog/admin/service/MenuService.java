package com.xiangjiahui.weblog.admin.service;

import com.xiangjiahui.weblog.common.domain.dos.MenuDO;
import com.xiangjiahui.weblog.common.model.MenuPageReqVO;
import com.xiangjiahui.weblog.common.model.MenuReqVO;
import com.xiangjiahui.weblog.common.utils.PageResponse;

import java.util.List;

public interface MenuService {

    PageResponse getAllMenu(MenuPageReqVO vo);

    List<MenuDO> getTreeMenu();

    void addMenu(MenuReqVO vo);
}
