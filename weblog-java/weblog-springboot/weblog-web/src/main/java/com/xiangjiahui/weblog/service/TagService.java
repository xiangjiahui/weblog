package com.xiangjiahui.weblog.service;

import com.xiangjiahui.weblog.model.vo.tag.FindTagListRspVO;

import java.util.List;

public interface TagService {

    /**
     * 获取标签列表
     * @return
     */
    List<FindTagListRspVO> findTagList();
}
