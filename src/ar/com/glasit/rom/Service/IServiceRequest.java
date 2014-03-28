package ar.com.glasit.rom.Service;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import java.util.List;

public interface IServiceRequest {

    public enum RequestType {
        GET,
        POST;
        @Override
        public String toString() {
            return (this == RequestType.GET) ? "GET" : "POST";
        }
    }
	HttpResponse execute();
    String getUrl();
    String getMethod();
    List<NameValuePair> getParameters();
    RequestType getType();
    void setMethod(String method);
    void addParameters(List<NameValuePair> parameters);
}
