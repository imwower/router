package com.koala.gateway.util;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private Node root;

    public Router() {
        root = new Node();
        root.path = "/";
        root.segment = "/";
    }

    public Router addRoute(String path) {
        if (!StringUtils.isEmpty(path)) {
            String strippedPath = StringUtils.strip(path, "/");
            String[] strings = StringUtils.split(strippedPath, "/");
            if (strings.length != 0) {
                Node node = root;
                for (int i = 0; i < strings.length; i++) {
                    String segment = strings[i];
                    node = addNode(node, segment);
                    if ("**".equals(segment))
                        break;
                }
                node.path = path;
            }
        }
        return this;
    }

    public String matchRoute(String path) {
        if (!StringUtils.isEmpty(path)) {
            String strippedPath = StringUtils.strip(path, "/");
            String[] strings = StringUtils.split(strippedPath, "/");
            if (strings.length != 0) {
                Node node = root;
                for (int i = 0; i < strings.length; i++) {
                    String segment = strings[i];
                    node = matchNode(node, segment);
                    if (node == null || node.isWildcard)
                        break;
                }
                if (node != null)
                    return node.path;
            }
        }
        return null;
    }

    private Node addNode(Node node, String segment) {
        if ("**".equals(segment)) {
            node.isWildcard = true;
            return node;
        }
        if (segment.startsWith("{") && segment.endsWith("}")) {
            Node childNode = new Node();
            childNode.segment = segment;
            node.dynamicRouter = childNode;
            return childNode;
        }

        Node childNode;

        if (node.staticRouters == null)
            node.staticRouters = new HashMap<>();
        if (node.staticRouters.containsKey(segment))
            childNode = node.staticRouters.get(segment);
        else {
            childNode = new Node();
            childNode.segment = segment;
            node.dynamicRouter = childNode;
            node.staticRouters.put(segment, childNode);
        }
        return childNode;
    }

    private Node matchNode(Node node, String segment) {
        if (node.staticRouters != null && node.staticRouters.containsKey(segment)) {
            return node.staticRouters.get(segment);
        }
        if (node.dynamicRouter != null)
            return node.dynamicRouter;
        if (node.isWildcard)
            return node;
        return null;
    }


    private class Node {
        private String path;
        private String segment;
        private Map<String, Node> staticRouters;
        private Node dynamicRouter;
        private boolean isWildcard;
    }
}
