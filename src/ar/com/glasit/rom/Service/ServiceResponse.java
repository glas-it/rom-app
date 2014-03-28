package ar.com.glasit.rom.Service;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ServiceResponse{
	private JSONObject jsonObject;
	private JSONArray jsonArray;
	
	public ServiceResponse(){

	}
	
	public ServiceResponse(JSONObject object){
		setJsonObject(object);
	}
	
	public ServiceResponse(JSONArray array){
		setJsonArray(array);
	}
	
	public ServiceResponse(String response){
		Object json;
		try {
			json = new JSONTokener(response).nextValue();
			if (json instanceof JSONObject){
				setJsonObject(new JSONObject(response));
			}
			else if (json instanceof JSONArray){
				setJsonArray(new JSONArray(response));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	public JSONArray getJsonArray() {
		return jsonArray;
	}
	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
	
	public String getErrorMessage() throws JSONException{
		return getJsonObject().getString("message");
	}
	
	public Boolean getSuccess() throws JSONException{
		return getJsonObject().getBoolean("success");
	}
}