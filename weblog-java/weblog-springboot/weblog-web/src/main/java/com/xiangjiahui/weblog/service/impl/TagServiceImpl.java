package com.xiangjiahui.weblog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiangjiahui.weblog.common.domain.dos.TagDO;
import com.xiangjiahui.weblog.common.domain.mapper.TagMapper;
import com.xiangjiahui.weblog.model.vo.tag.FindTagListRspVO;
import com.xiangjiahui.weblog.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service("webTagService")
@Slf4j
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    public TagServiceImpl(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }


    @Override
    public List<FindTagListRspVO> findTagList() {
        // 查询所有标签
        List<TagDO> tagDOS = tagMapper.selectList(Wrappers.emptyWrapper());

        // DO 转 VO
        List<FindTagListRspVO> vos = Collections.emptyList();
        if (!CollectionUtils.isEmpty(tagDOS)) {
            vos = tagDOS.stream()
                    .map(tagDO -> FindTagListRspVO.builder()
                            .id(tagDO.getId())
                            .name(tagDO.getName())
                            .build())
                    .collect(Collectors.toList());
        }
        return vos;
    }
}
