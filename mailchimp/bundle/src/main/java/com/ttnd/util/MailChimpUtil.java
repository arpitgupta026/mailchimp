package com.ttnd.util;

import com.ttnd.cms.Constants;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import java.util.Dictionary;

public class MailChimpUtil {

	public static JSONObject getMailChimpList(Dictionary config){
		JSONObject jsonResponse = null;
		if(config != null){
			Object domain = config.get(Constants.METADATA_MAILCHIMP_DOMAIN);
			Object apikey = config.get(Constants.METADATA_MAILCHIMP_APIKEY);
			Object username = config.get(Constants.METADATA_MAILCHIMP_USERNAME);
			if(domain != null && apikey != null && username != null){
				String listURL = Constants.HTTPS_PROTOCOL + domain.toString() + Constants.API_ENDPOINT + Constants.LIST_URL;
				try{
					String response = HttpUtil.getHttpResponse(listURL, username.toString(), apikey.toString(), null, null);
					if(response != null){
						jsonResponse = new JSONObject(response);
					}
				}catch(JSONException je){
					je.printStackTrace();
				}
			}
        }
    	return jsonResponse;
    }
	
/*
	public static String subscribeUser(String emailD, String[] lists, Dictionary config){
		try {
			JSONObject params = new JSONObject();
			String s = HttpUtil.getHashString(emailD, "MD5");
			if(s != null && lists != null && lists.length > 0 ){
				JSONArray jsonArray = new JSONArray();
				for (int i=0 ;i < lists.length; i++){
					JSONObject obj = new JSONObject();
					obj.put("method", "PUT");
					String relativePath = "lists/" + lists[i] + "/members/" + s;
					obj.put("path", relativePath);
					JSONObject info = new JSONObject();
					info.put("email_address", emailD);
					info.put("status_if_new", Constants.SUSBSCRIBER_STATUS_PENDING);
					obj.put("body", info.toString());
					jsonArray.put(obj);
				}
				params.put("operations", jsonArray);
				String apiEndpoint = config.get(Constants.API_ENDPOINT_PRPOPERTY).toString();
				String apikey = config.get(Constants.API_KEY_PRPOPERTY).toString();
				String username = config.get(Constants.USERNAME_PRPOPERTY).toString();
				String response = HttpUtil.getHttpResponse(apiEndpoint + Constants.BATCH_URL, username, apikey, "POST", params);
				return response;
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
*/

}
