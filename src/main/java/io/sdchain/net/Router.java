package io.sdchain.net;

public class Router {
    private RequestMethod method;
    private String url;

    public Router() {
        super();
    }

    public Router(RequestMethod method, String url) {
        super();
        this.method = method;
        this.url = url;
    }

    public RequestMethod getMethod() {
        return method;
    }
    public String getUrl() {
        return url;
    }
    public void setMethod(RequestMethod method) {
        this.method = method;
    }
    public void setUrl(String url) {
        this.url = url;
    }

}
