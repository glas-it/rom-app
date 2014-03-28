package ar.com.glasit.rom.Service;

import org.apache.http.NameValuePair;

import java.util.List;

public abstract class ServiceRequest implements IServiceRequest {

    protected static final Integer REQUEST_TIMEOUT = 30000;
    protected String baseUrl, method;
    protected List<NameValuePair> parameters;

    public ServiceRequest(String baseUrl) {
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        this.baseUrl = baseUrl;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void addParameters(List<NameValuePair> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String getUrl() {
        return baseUrl+method;
    }

    public String getMethod() {
        return this.method;
    }

    public List<NameValuePair> getParameters() {
        return this.parameters;
    }
}
