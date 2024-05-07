package com.xiangjiahui.weblog.markdown.renderer;

import org.apache.commons.lang3.StringUtils;
import org.commonmark.ext.image.attributes.ImageAttributes;
import org.commonmark.node.Image;
import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * 定制化图片渲染
 * 支持图片下方描述文字
 */
public class ImageNodeRenderer implements NodeRenderer {

    private  final HtmlWriter html;

    // 图片宽
    private static final String KEY_WIDTH = "width";
    // 图片高
    private static final String KEY_HEIGHT = "height";

    public ImageNodeRenderer(HtmlNodeRendererContext context) {
        this.html = context.getWriter();
    }


    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return Collections.singleton(Image.class);
    }

    @Override
    public void render(Node node) {
        Image image = (Image) node;
        html.line();

        // 图片链接
        String url = image.getDestination();
        // 图片描述
        String title = image.getTitle();

        //拼接HTML结构
        StringBuilder htmlBuilder = new StringBuilder("<img src=\"");
        htmlBuilder.append(url);
        htmlBuilder.append("\"");

        // 添加 title="图片标题" alt="图片标题" 属性
        if (StringUtils.isNotBlank(title)) {
            htmlBuilder.append(String.format(" title=\"%s\" alt=\"%s\"", title, title));
        }

        // 图片宽高
        Node lastChild = node.getLastChild();
        if (Objects.nonNull(lastChild) && lastChild instanceof ImageAttributes) {
            ImageAttributes imageAttributes = (ImageAttributes) lastChild;
            if (!CollectionUtils.isEmpty(imageAttributes.getAttributes())) {
                String width = imageAttributes.getAttributes().get(KEY_WIDTH);
                String height = imageAttributes.getAttributes().get(KEY_HEIGHT);
                htmlBuilder.append(StringUtils.isBlank(width) ? "" : (" " + KEY_WIDTH + "=" + width + "\""));
                htmlBuilder.append(StringUtils.isBlank(height) ? "" : (" " + KEY_HEIGHT + "=" + height + "\""));
            }
        }

        htmlBuilder.append(">");

        if (StringUtils.isNotBlank(title)) {
            // 图文下方文字
            htmlBuilder.append(String.format("<span class=\"image-caption\">%s</span>", title));
        }

        // 设置 HTML 内容
        html.raw(htmlBuilder.toString());
        html.line();
    }
}
