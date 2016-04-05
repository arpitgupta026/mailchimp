package com.ttnd.mailchimp.service;

import com.ttnd.cms.helper.JcrHelper;
import com.ttnd.util.MailChimpUtil;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONObject;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Jatin on 3/3/2016.
 */

@SlingServlet(paths = "/services/mailchimp/campaigns")
public class CampaignService extends SlingAllMethodsServlet{

    private Logger log = Logger.getLogger(CampaignService.class.getName());

    @Reference
    private JcrHelper jcrHelper;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{

        if(("fetchAll").equalsIgnoreCase(request.getParameter("action"))){
            JSONObject campaigns = new JSONObject();//MailChimpUtil.getCampaigns(mailChimpConfig.getConfigDictionary());
            if (campaigns != null) {
                response.getWriter().write(campaigns.toString());
            }
        }
        else
            response.getWriter().write("No Action found for Campaign service");

    }

    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{
        if(("send").equalsIgnoreCase(request.getParameter("action"))){
            String campaignID = request.getParameter("campaignID");
            String configs = request.getParameter("configs");
            if(campaignID != null && campaignID.trim().length() > 0 &&
                    jcrHelper != null && configs != null){
                String configURL = "";
                if(configs.indexOf(",") > -1){
                   String[] configArray = configs.split(",");
                    configURL = jcrHelper.getMailChimpConfigFromConfigs(configArray);
                }else{
                    configURL = configs;
                }
                ValueMap map = jcrHelper.getConfigFromCloudService(configURL);
                if(map != null){
                    JSONObject responseObj = MailChimpUtil.sendCampaign(map, campaignID);
                    response.getWriter().write(responseObj.toString());
                }

            }
        }
    }
}
