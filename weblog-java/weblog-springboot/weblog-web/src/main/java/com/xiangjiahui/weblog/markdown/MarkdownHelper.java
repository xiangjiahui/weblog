package com.xiangjiahui.weblog.markdown;

import com.xiangjiahui.weblog.markdown.renderer.ImageNodeRenderer;
import com.xiangjiahui.weblog.markdown.renderer.LinkNodeRenderer;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.image.attributes.ImageAttributesExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Arrays;
import java.util.List;


@Slf4j
public class MarkdownHelper {

    /**
     * Markdown解析器
     */
    private static final Parser PARSER;

    /**
     * HTML渲染器
     */
    private static final HtmlRenderer HTML_RENDERER;

    /**
     * 初始化
     */
    static {
        //Markdown 表格拓展, 支持表格
        List<Extension> extensions = Arrays.asList(
                TablesExtension.create(),  // 表格扩展
                HeadingAnchorExtension.create(), // 标题锚定项
                ImageAttributesExtension.create(), // 图片宽高
                TaskListItemsExtension.create() // 任务列表
                 );
        PARSER = Parser.builder().extensions(extensions).build();
        HTML_RENDERER = HtmlRenderer.builder().extensions(extensions)
                //.attributeProviderFactory(context -> new NofollowLinkAttributeProvider())
                .nodeRendererFactory(ImageNodeRenderer::new)
                .nodeRendererFactory(LinkNodeRenderer::new)
                .build();
    }

    /**
     * Markdown转Html
     * @param markdown
     * @return
     */
    public static String markdownToHtml(String markdown) {
        if (markdown == null) {
            log.warn("Markdown文本内容为空");
            return null;
        }
        return HTML_RENDERER.render(PARSER.parse(markdown));
    }

    public static void main(String[] args) {
    	String markdown = "### 测试";
    	log.info(markdownToHtml(markdown));
	}
}
