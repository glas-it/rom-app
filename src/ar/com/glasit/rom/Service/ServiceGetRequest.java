package ar.com.glasit.rom.Service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by pablo on 26/03/14.
 */
public class ServiceGetRequest extends ServiceRequest {

    public ServiceGetRequest(String baseUrl) {
        super(baseUrl);
    }

    @Override
    public HttpResponse execute() {
        HttpResponse response = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            String url = getUrl();
            if (parameters != null){
                String paramString = URLEncodedUtils.format(parameters, "utf-8");
                if (!paramString.isEmpty()) {
                    url += "?" + paramString;
                }
            }
            HttpGet request = new HttpGet(url);
            httpClient.getParams().setParameter("http.socket.timeout", REQUEST_TIMEOUT);
            response = httpClient.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public RequestType getType() {
        return RequestType.GET;
    }

}
