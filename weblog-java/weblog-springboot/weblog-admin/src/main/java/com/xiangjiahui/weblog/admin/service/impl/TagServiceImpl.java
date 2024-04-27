package com.xiangjiahui.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiangjiahui.weblog.admin.domains.vo.tag.TagReqVO;
import com.xiangjiahui.weblog.admin.service.TagService;
import com.xiangjiahui.weblog.common.domain.dos.TagDO;
import com.xiangjiahui.weblog.common.domain.mapper.TagMapper;
import com.xiangjiahui.weblog.common.exception.BusinessException;
import com.xiangjiahui.weblog.common.model.TagPageListReqVO;
import com.xiangjiahui.weblog.common.model.TagPageListRspVO;
import com.xiangjiahui.weblog.common.model.vo.TagSelectVO;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("tagService")
@Slf4j
public class TagServiceImpl extends ServiceImpl<TagMapper, TagDO> implements TagService {

    private final TagMapper tagMapper;

    public TagServiceImpl(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }


    @Override
    public boolean addTags(TagReqVO vo) {
        List<TagDO> tagDOS = vo.getTags().stream().map(tag -> {
            TagDO tagDO = new TagDO();
            tagDO.setName(tag);
            tagDO.setCreateTime(LocalDateTime.now());
            tagDO.setUpdateTime(LocalDateTime.now());
            return tagDO;
        }).collect(Collectors.toList());

        // 捕获异常，标签数据已存在的就忽略掉，不影响其他标签的添加
        try {
            saveBatch(tagDOS);
        }catch (Exception e){
            log.warn("该标签已存在",e);
        }
        return true;
    }



    @Override
    public PageResponse getPageList(TagPageListReqVO vo) {
        Long currentPage = vo.getCurrentPage();
        Long size = vo.getSize();
        String name = vo.getName();
        LocalDate startDate = vo.getStartDate();
        LocalDate endDate = vo.getEndDate();

        Page<TagDO> tagDOPage = tagMapper.getPageList(currentPage, size, name, startDate, endDate);

        List<TagPageListRspVO> collect = tagDOPage.getRecords().stream().map(tagDO -> {
            TagPageListRspVO rspVO = new TagPageListRspVO();
            rspVO.setId(tagDO.getId());
            rspVO.setName(tagDO.getName());
            rspVO.setCreateTime(tagDO.getCreateTime());
            return rspVO;
        }).collect(Collectors.toList());

        return PageResponse.success(tagDOPage,collect);
    }


    @Override
    public int deleteTagByID(Long id) {
        if (Objects.isNull(id) || id == 0L){
            throw new BusinessException("标签ID不能为空");
        }
        return tagMapper.deleteById(id);
    }



    @Override
    public List<TagSelectVO> getAllTag() {
        LambdaQueryWrapper<TagDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(TagDO::getId,TagDO::getName).orderByDesc(TagDO::getCreateTime);
        List<TagDO> tagDOS = tagMapper.selectList(wrapper);
        return tagDOS.stream().map(tag -> {
            TagSelectVO tagSelectVO = new TagSelectVO();
            tagSelectVO.setLabel(tag.getName());
            tagSelectVO.setId(tag.getId());
            return tagSelectVO;
        }).collect(Collectors.toList());
    }
}
