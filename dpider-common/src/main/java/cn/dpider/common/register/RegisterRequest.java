package cn.dpider.common.register;

public class RegisterRequest {

    private String subPath;

    private String host;

    private String port;

    private String name;

    private String nodeInfoJson;

    public String getNodeInfoJson() {
        return nodeInfoJson;
    }

    public void setNodeInfoJson(String nodeInfoJson) {
        this.nodeInfoJson = nodeInfoJson;
    }

    public String getSubPath() {
        return subPath;
    }

    public void setSubPath(String subPath) {
        this.subPath = subPath;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
