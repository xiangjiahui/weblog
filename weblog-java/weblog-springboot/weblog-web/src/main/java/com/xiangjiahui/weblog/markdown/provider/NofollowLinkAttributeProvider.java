package com.xiangjiahui.weblog.markdown.provider;

import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.AttributeProvider;

import java.util.Map;

public class NofollowLinkAttributeProvider implements AttributeProvider {

    /**
     * 域名
     */
    private static final String DOMAIN = "www.xjh-log.cn";


    @Override
    public void setAttributes(Node node, String s, Map<String, String> map) {
        if (node instanceof Link) {
            Link link = (Link) node;
            // 获取链接地址,判断是否是本站链接
            String url = link.getDestination();
            // 如果链接不是自己域名,则添加nofollow属性
            if (!url.contains(DOMAIN)) {
                map.put("rel", "nofollow");
            }
        }
    }
}
