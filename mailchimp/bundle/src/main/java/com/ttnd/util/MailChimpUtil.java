package com.ttnd.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import com.ttnd.cms.Constants;
import com.ttnd.mailchimp.model.SubscriptionList;

public final class MailChimpUtil {

	public static List<SubscriptionList> getMailChimpList(ValueMap config) {
		JSONObject jsonObject = null;
		List<SubscriptionList> lists = new ArrayList<SubscriptionList>();
		if (config != null) {
			Object accountDomain = config.get(Constants.METADATA_MAILCHIMP_DOMAIN);
			Object apiKey = config.get(Constants.METADATA_MAILCHIMP_APIKEY);
			Object username = config.get(Constants.METADATA_MAILCHIMP_USERNAME);
			if (accountDomain != null && apiKey != null && username != null) {
				String listURL = Constants.HTTPS_PROTOCOL + accountDomain.toString() + Constants.API_ENDPOINT
						+ Constants.LIST_URL;
				try {
					String response = HttpUtil.getHttpResponse(listURL, username.toString(), apiKey.toString(), null,
							null);
					if (response != null) {
						jsonObject = new JSONObject(response);
						if(jsonObject != null){
							JSONArray listArray = jsonObject.getJSONArray("lists");
							if(listArray != null){
								for (int i = 0; i < listArray.length(); i++) {
									JSONObject listData = listArray.getJSONObject(i);
									JSONObject campaignDefault = listData.getJSONObject("campaign_defaults");
									
									SubscriptionList subscriptionList = new SubscriptionList();
									subscriptionList.setId(listData.get("id")!= null ? listData.get("id").toString() : "");
									subscriptionList.setName(listData.get("name") != null ? listData.get("name").toString() : "");
									subscriptionList.setLanguage(campaignDefault.get("language") != null ? campaignDefault.get("language").toString() : "");
									subscriptionList.setFromEmail(campaignDefault.get("from_email") != null ? campaignDefault.get("from_email").toString() : "");
									subscriptionList.setSubject(campaignDefault.get("subject") != null ? campaignDefault.get("subject").toString() : "");
									subscriptionList.setFromName(campaignDefault.get("from_name") != null ? campaignDefault.get("from_name").toString() : "");
									lists.add(subscriptionList);
								}
							}	
						}
					}
				} catch (JSONException je) {
					je.printStackTrace();
				}
			}
		}
		return lists;
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
				String sendCampaignURL = Constants.HTTPS_PROTOCOL + domain.toString() + Constants.API_ENDPOINT + Constants.CAMPAIGN_URL + "/" + campaignID + Constants.CAMPAIGN_ACTION_SEND;
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

	// Don't remove this code as this would be needed when the MailChimp API will support batch operation for lead creation
	//Current API has the bug.
	
	public static JSONObject subscribeUser(String emailD, String[] lists, ValueMap config){
		JSONObject jsonResponse = null;
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
					info.put("status", Constants.SUSBSCRIBER_STATUS_PENDING);
					obj.put("body", info.toString());
					jsonArray.put(obj);
				}
				params.put("operations", jsonArray);
				Object domain = config.get(Constants.METADATA_MAILCHIMP_DOMAIN);
				Object apikey = config.get(Constants.METADATA_MAILCHIMP_APIKEY);
				Object username = config.get(Constants.METADATA_MAILCHIMP_USERNAME);
				if(domain != null && apikey != null && username != null) {
					String listSubscribeBatchURL = Constants.HTTPS_PROTOCOL + domain.toString() + Constants.API_ENDPOINT + Constants.BATCH_URL;
					String response = HttpUtil.getHttpResponse(listSubscribeBatchURL, username.toString(), apikey.toString(), "POST", params);
					try{
						if(response != null){
							jsonResponse = new JSONObject(response);
						}
					}catch(JSONException je){
						je.printStackTrace();
					}
				}
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonResponse;
	}
	
	/*public static JSONObject subscribeUser(String emailD, String[] lists, ValueMap config){
		JSONObject jsonResponse = null;
		try {
			String s = HttpUtil.getHashString(emailD, "MD5");
			if(s != null && lists != null && lists.length > 0 ){
				Object domain = config.get(Constants.METADATA_MAILCHIMP_DOMAIN);
				Object apikey = config.get(Constants.METADATA_MAILCHIMP_APIKEY);
				Object username = config.get(Constants.METADATA_MAILCHIMP_USERNAME);
				if(domain != null && apikey != null && username != null) {
					for (int i=0 ;i < lists.length; i++){
						JSONObject params = new JSONObject();
						params.put("email_address", emailD);
						params.put("status", Constants.SUSBSCRIBER_STATUS_PENDING);
						String listSubscribeURL = Constants.HTTPS_PROTOCOL + domain.toString() + Constants.API_ENDPOINT + Constants.LIST_URL +  "/"+ lists[i] + Constants.MEMBERS  + "/" + s;
						String response = HttpUtil.getHttpResponse(listSubscribeURL, username.toString(), apikey.toString(), "PUT", params);
						try{
							if(response != null){
								jsonResponse = new JSONObject(response);
							}
						}catch(JSONException je){
							je.printStackTrace();
						}
					}
					
				}
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonResponse;
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
				String updateCampaignContentURL = Constants.HTTPS_PROTOCOL + domain.toString() + Constants.API_ENDPOINT + Constants.CAMPAIGN_URL + "/" + campaignID + Constants.CAMPAIGN_CONTENT_URL;
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
