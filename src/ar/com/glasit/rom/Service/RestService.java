package ar.com.glasit.rom.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import android.os.AsyncTask;
import org.apache.http.NameValuePair;


public class RestService extends AsyncTask<Void, Void, ServiceResponse> {

    private List<ServiceListener> serviceListeners;
    private IServiceRequest serviceRequest;
    private String error;

    private RestService(IServiceRequest serviceRequest){
        this.serviceRequest = serviceRequest;
        this.serviceListeners = new Vector<ServiceListener>();
    }

    private void addListener(ServiceListener serviceListener) {
        this.serviceListeners.add(serviceListener);
    }


	@Override
	protected ServiceResponse doInBackground(Void... ht) {
		HttpResponse response = null;
		ServiceResponse jsonResponse = null;
		response = serviceRequest.execute();
		jsonResponse = parseResponse(response);
		return jsonResponse;
	}

	protected void onCancelled() {

	}

	protected void onPostExecute(ServiceResponse result) {
         for (ServiceListener serviceListener: serviceListeners){
             if (this.isCancelled() == true || result == null) {
                 serviceListener.onError(error);
             } else {
                 serviceListener.onServiceCompleted(this.serviceRequest, result);
             }
         }
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String cutEnter = sb.toString();
		return cutEnter.replaceAll("\n", "");
	}
	
	private ServiceResponse parseResponse(HttpResponse response){
		try {
			ServiceResponse jsonResponse = null;
			HttpEntity entity = null;
			entity = response.getEntity();
	
			if (entity != null) {
				InputStream instream = entity.getContent();
				String r = RestService.convertStreamToString(instream);
	
				jsonResponse = new ServiceResponse(r);
	
				int code = response.getStatusLine().getStatusCode();
				if (code != 200) {
					if(code == 403){
						this.cancel(true);
						return null;
					}else if(code == 201){
						return jsonResponse;
					}else{
						this.cancel(true);
						return null;
					}
				}
	
				return jsonResponse;
			}
		} catch (Exception ex) {
			error = ex.getMessage();
			this.cancel(true);
			return null;
		}
		return null;
	}

    public static void callService(ServiceListener serviceListener, IServiceRequest requestStrategy) {
		RestService serviceThread = new RestService(requestStrategy);
        serviceThread.addListener(serviceListener);
		serviceThread.execute();
	}

    public static void callGetService(ServiceListener serviceListener, String url, String method, List<NameValuePair> parameters) {
        IServiceRequest serviceRequest = new ServiceGetRequest(url);
        serviceRequest.setMethod(method);
        serviceRequest.addParameters(parameters);
        RestService.callService(serviceListener, serviceRequest);
    }

    public static void callPostService(ServiceListener serviceListener, String url, String method, List<NameValuePair> parameters) {
        IServiceRequest serviceRequest = new ServicePostRequest(url);
        serviceRequest.setMethod(method);
        serviceRequest.addParameters(parameters);
        RestService.callService(serviceListener, serviceRequest);
    }
}