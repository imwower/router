package com.example.router;

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
                    if (node == null)
                        break;
                }
                if (node != null)
                    return node.path;
            }
        }
        return null;
    }

    private Node addNode(Node node, String segment) {
        Node childNode = new Node();
        childNode.segment = segment;

        if ("**".equals(segment)) {
            node.wildcard = childNode;
        } else if (segment.startsWith("{") && segment.endsWith("}")) {
            node.dynamicRouter = childNode;
        } else {
            if (node.staticRouters == null)
                node.staticRouters = new HashMap<>();
            if (node.staticRouters.containsKey(segment))
                childNode = node.staticRouters.get(segment);
            else
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
        if (node.wildcard != null)
            return node.wildcard;
        return null;
    }


    private class Node {
        private String path;
        private String segment;
        private Map<String, Node> staticRouters;
        private Node dynamicRouter;
        private Node wildcard;
    }
}
