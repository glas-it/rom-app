package ar.com.glasit.rom.Service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by pablo on 26/03/14.
 */
public class ServicePostRequest extends ServiceRequest {

    public ServicePostRequest(String baseUrl) {
        super(baseUrl);
    }

    @Override
    public HttpResponse execute() {
        HttpResponse response = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(getUrl());
            if (parameters != null) {
                httpPost.setEntity(new UrlEncodedFormEntity(parameters));
            }
            httpClient.getParams().setParameter("http.socket.timeout", REQUEST_TIMEOUT);
            response = httpClient.execute(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public RequestType getType() {
        return RequestType.POST;
    }

}
