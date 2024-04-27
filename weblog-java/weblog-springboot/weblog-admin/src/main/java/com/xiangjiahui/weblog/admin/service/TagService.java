package com.xiangjiahui.weblog.admin.service;

import com.xiangjiahui.weblog.admin.domains.vo.tag.TagReqVO;
import com.xiangjiahui.weblog.common.model.TagPageListReqVO;
import com.xiangjiahui.weblog.common.model.vo.TagSelectVO;
import com.xiangjiahui.weblog.common.utils.PageResponse;

import java.util.List;

public interface TagService {

    boolean addTags(TagReqVO vo);

    PageResponse getPageList(TagPageListReqVO vo);

    int deleteTagByID(Long id);

    List<TagSelectVO> getAllTag();
}
