package com.ttnd.util;

import com.ttnd.cms.Constants;
import com.ttnd.mailchimp.model.SubscriptionList;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

public final class MailChimpUtil {

	public static JSONObject getMailChimpList(ValueMap config){
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

	public static JSONObject getCampaigns(ValueMap config){
		JSONObject jsonResponse = null;
		if(config != null){
			Object domain = config.get(Constants.METADATA_MAILCHIMP_DOMAIN);
			Object apikey = config.get(Constants.METADATA_MAILCHIMP_APIKEY);
			Object username = config.get(Constants.METADATA_MAILCHIMP_USERNAME);
			if(domain != null && apikey != null && username != null){
				String fechALLCampaignURL = Constants.HTTPS_PROTOCOL + domain.toString() + Constants.API_ENDPOINT + Constants.CAMPAIGN_URL;
				try{
					String response = HttpUtil.getHttpResponse(fechALLCampaignURL, username.toString(), apikey.toString(), "GET", null);
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

	public static JSONObject sendCampaign(ValueMap config, String campaignID){
		JSONObject jsonResponse = null;
		if(config != null){
			Object domain = config.get(Constants.METADATA_MAILCHIMP_DOMAIN);
			Object apikey = config.get(Constants.METADATA_MAILCHIMP_APIKEY);
			Object username = config.get(Constants.METADATA_MAILCHIMP_USERNAME);
			if(domain != null && apikey != null && username != null && campaignID != null){
				String sendCampaignURL = Constants.HTTPS_PROTOCOL + domain.toString() + Constants.API_ENDPOINT + Constants.CAMPAIGN_URL + campaignID + Constants.CAMPAIGN_ACTION_SEND;
				try{
					String response = HttpUtil.getHttpResponse(sendCampaignURL, username.toString(), apikey.toString(), "POST", null);
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

	public static JSONObject createList(ValueMap config, SubscriptionList list){
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
	}*/

	public static JSONObject createCampaign(ValueMap config, JSONObject params){
		JSONObject jsonResponse = null;
		if(config != null){
			Object domain = config.get(Constants.METADATA_MAILCHIMP_DOMAIN);
			Object apikey = config.get(Constants.METADATA_MAILCHIMP_APIKEY);
			Object username = config.get(Constants.METADATA_MAILCHIMP_USERNAME);
			if(domain != null && apikey != null && username != null){
				String campaignURL = Constants.HTTPS_PROTOCOL + domain.toString() + Constants.API_ENDPOINT + Constants.CAMPAIGN_URL;
				try{
					String response = HttpUtil.getHttpResponse(campaignURL, username.toString(), apikey.toString(), "POST", params);
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

	public static JSONObject updateCampaignContent(ValueMap config, String campaignID, JSONObject params){
		JSONObject jsonResponse = null;
		if(config != null){
			Object domain = config.get(Constants.METADATA_MAILCHIMP_DOMAIN);
			Object apikey = config.get(Constants.METADATA_MAILCHIMP_APIKEY);
			Object username = config.get(Constants.METADATA_MAILCHIMP_USERNAME);
			if(domain != null && apikey != null && username != null && campaignID != null){
				String updateCampaignContentURL = Constants.HTTPS_PROTOCOL + domain.toString() + Constants.API_ENDPOINT + Constants.CAMPAIGN_URL + campaignID + Constants.CAMPAIGN_CONTENT_URL;
				try{
					String response = HttpUtil.getHttpResponse(updateCampaignContentURL, username.toString(), apikey.toString(), "PUT", params);
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

}
