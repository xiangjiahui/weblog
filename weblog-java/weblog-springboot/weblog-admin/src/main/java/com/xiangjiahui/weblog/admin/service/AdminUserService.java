package com.xiangjiahui.weblog.admin.service;

import com.xiangjiahui.weblog.admin.domains.vo.user.FindUserVO;
import com.xiangjiahui.weblog.admin.domains.vo.user.UpdateAdminUserVO;
import com.xiangjiahui.weblog.common.model.UpdateSaveUserVO;
import com.xiangjiahui.weblog.common.model.UserPageReqVO;
import com.xiangjiahui.weblog.common.utils.PageResponse;

public interface AdminUserService {

    int updatePasswordByUsername(UpdateAdminUserVO updateAdminUserVO);

    FindUserVO findUser();

    PageResponse getPageUserList(UserPageReqVO vo);

    void addUser(UpdateSaveUserVO vo);

    boolean updateUser(UpdateSaveUserVO vo);
}
