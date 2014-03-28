package ar.com.glasit.rom.Service;

public interface ServiceListener {
	void onServiceCompleted(IServiceRequest request, ServiceResponse obj);
	void onError(String error);
}
