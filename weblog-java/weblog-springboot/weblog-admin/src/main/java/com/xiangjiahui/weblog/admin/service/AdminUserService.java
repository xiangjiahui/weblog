package com.xiangjiahui.weblog.admin.service;

import com.xiangjiahui.weblog.admin.domains.vo.user.FindUserVO;
import com.xiangjiahui.weblog.admin.domains.vo.user.UpdateAdminUserVO;

public interface AdminUserService {

    int updatePasswordByUsername(UpdateAdminUserVO updateAdminUserVO);

    FindUserVO findUser();
}
