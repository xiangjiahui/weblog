package com.xiangjiahui.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiangjiahui.weblog.admin.domains.vo.tag.SearchTagsReqVO;
import com.xiangjiahui.weblog.admin.domains.vo.tag.SelectRspVO;
import com.xiangjiahui.weblog.admin.domains.vo.tag.TagReqVO;
import com.xiangjiahui.weblog.admin.service.TagService;
import com.xiangjiahui.weblog.common.domain.dos.ArticleTagRelDO;
import com.xiangjiahui.weblog.common.domain.dos.TagDO;
import com.xiangjiahui.weblog.common.domain.mapper.ArticleTagRelMapper;
import com.xiangjiahui.weblog.common.domain.mapper.TagMapper;
import com.xiangjiahui.weblog.common.exception.BusinessException;
import com.xiangjiahui.weblog.common.model.TagPageListReqVO;
import com.xiangjiahui.weblog.common.model.TagPageListRspVO;
import com.xiangjiahui.weblog.common.model.vo.TagSelectVO;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("tagService")
@Slf4j
public class TagServiceImpl extends ServiceImpl<TagMapper, TagDO> implements TagService {

    private final TagMapper tagMapper;

    private final ArticleTagRelMapper articleTagRelMapper;

    public TagServiceImpl(TagMapper tagMapper, ArticleTagRelMapper articleTagRelMapper) {
        this.tagMapper = tagMapper;
        this.articleTagRelMapper = articleTagRelMapper;
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
            rspVO.setArticlesTotal(tagDO.getArticlesTotal());
            return rspVO;
        }).collect(Collectors.toList());

        return PageResponse.success(tagDOPage,collect);
    }


    @Override
    public int deleteTagByID(Long id) {
//        if (Objects.isNull(id) || id == 0L){
//            throw new BusinessException("标签ID不能为空");
//        }

        // 校验该标签下是否有关联的文章，若有，则不允许删除，提示用户需要先删除标签下的文章
        ArticleTagRelDO articleTagRelDO = articleTagRelMapper.selectOneByTagId(id);

        if (Objects.nonNull(articleTagRelDO)) {
            log.warn("==> 此标签下包含文章，无法删除，tagId: {}", id);
            throw new BusinessException(" 此标签下包含文章，无法删除");
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

    @Override
    public List<SelectRspVO> searchTags(SearchTagsReqVO searchTagsReqVO) {
        String key = searchTagsReqVO.getKey();

        // 执行模糊查询
        List<TagDO> tagDOS = tagMapper.selectByKey(key);

        // do 转 vo
        List<SelectRspVO> vos = null;
        if (!CollectionUtils.isEmpty(tagDOS)) {
            vos = tagDOS.stream()
                    .map(tagDO -> SelectRspVO.builder()
                            .label(tagDO.getName())
                            .value(tagDO.getId())
                            .build())
                    .collect(Collectors.toList());
        }
        return vos;
    }
}
