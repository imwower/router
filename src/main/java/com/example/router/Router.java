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

    public void addRoute(String path) {
        if (!StringUtils.isEmpty(path)) {
            path = StringUtils.strip(path, "/");
            String[] strings = StringUtils.split(path, "/");
            if (strings.length != 0) {
                Node node = root;
                for (int i = 0; i < strings.length; i++) {
                    String segment = strings[i];
                    node = addNode(node, segment);
                }
            }
        }

    }

    private Node addNode(Node node, String segment) {
        Node childNode = new Node();
        childNode.segment = segment;

        if ("*".equals(segment)) {
            node.singleWildcard = childNode;
        } else if ("**".equals(segment)) {
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


    public class Node {
        private String path;
        private String segment;
        private Map<String, Node> staticRouters;
        private Node dynamicRouter;
        private Node singleWildcard;
        private Node wildcard;
    }
}
