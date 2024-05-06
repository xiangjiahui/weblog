package com.xiangjiahui.weblog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.xiangjiahui.weblog.common.domain.dos.ArticleDO;
import com.xiangjiahui.weblog.common.domain.mapper.ArticleMapper;
import com.xiangjiahui.weblog.common.utils.PageResponse;
import com.xiangjiahui.weblog.model.vo.archive.FindArchiveArticlePageListReqVO;
import com.xiangjiahui.weblog.model.vo.archive.FindArchiveArticlePageListRspVO;
import com.xiangjiahui.weblog.model.vo.archive.FindArchiveArticleRspVO;
import com.xiangjiahui.weblog.service.ArchiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Month;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service("archiveService")
@Slf4j
public class ArchiveServiceImpl implements ArchiveService {

    private final ArticleMapper articleMapper;

    public ArchiveServiceImpl(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    @Override
    public PageResponse findArchivePageList(FindArchiveArticlePageListReqVO findArchiveArticlePageListReqVO) {
        Long current = findArchiveArticlePageListReqVO.getCurrentPage();
        Long size = findArchiveArticlePageListReqVO.getSize();

        // 分页查询
        IPage<ArticleDO> page = articleMapper.selectPageList(current, size, null, null, null);
        List<ArticleDO> articleDOS = page.getRecords();

        List<FindArchiveArticlePageListRspVO> vos = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(articleDOS)) {
            // DO 转 VO
            List<FindArchiveArticleRspVO> archiveArticleRspVOS =  articleDOS.stream()
                    .map(articleDO -> {
                        return FindArchiveArticleRspVO.builder()
                                .id(articleDO.getId())
                                .title(articleDO.getTitle())
                                .cover(articleDO.getCover())
                                .createDate(articleDO.getCreateTime().toLocalDate())
                                .createMonth(YearMonth.of(articleDO.getCreateTime().getYear(), Month.of(articleDO.getCreateTime().getMonthValue())))
                                .build();
                    })
                    .collect(Collectors.toList());

            // 按创建的月份进行分组
            Map<YearMonth, List<FindArchiveArticleRspVO>> map = archiveArticleRspVOS.stream().collect(Collectors.groupingBy(FindArchiveArticleRspVO::getCreateMonth));
            // 使用 TreeMap 按月份倒序排列
            Map<YearMonth, List<FindArchiveArticleRspVO>> sortedMap = new TreeMap<>(Collections.reverseOrder());
            sortedMap.putAll(map);

            // 遍历排序后的 Map，将其转换为归档 VO
            sortedMap.forEach((k, v) -> vos.add(FindArchiveArticlePageListRspVO.builder().month(k).articles(v).build()));
        }
        return PageResponse.success(page, vos);
    }
}
