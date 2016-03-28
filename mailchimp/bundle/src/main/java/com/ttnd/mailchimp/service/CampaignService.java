package com.ttnd.mailchimp.service;

import com.ttnd.cms.model.MailChimpConfiguration;
import com.ttnd.util.MailChimpUtil;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONObject;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Jatin on 3/3/2016.
 */

@SlingServlet(paths = "/services/mailchimp/campaigns")
public class CampaignService extends SlingSafeMethodsServlet {

    private Logger log = Logger.getLogger(CampaignService.class.getName());

    @Reference
    private MailChimpConfiguration mailChimpConfig;


    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{
        if(mailChimpConfig != null){
            if(("fetchAll").equalsIgnoreCase(request.getParameter("action"))){
                JSONObject campaigns = MailChimpUtil.getCampaigns(mailChimpConfig.getConfigDictionary());
                if (campaigns != null) {
                    response.getWriter().write(campaigns.toString());
                }
            }
            else if(("send").equalsIgnoreCase(request.getParameter("action"))){
                String campaignID = request.getParameter("campaignID");
                if(campaignID != null && campaignID.trim().length() > 0){
                    JSONObject jsonResponse = MailChimpUtil.sendCampaign(mailChimpConfig.getConfigDictionary(), campaignID);
                    if (jsonResponse != null) {
                        response.getWriter().write(jsonResponse.toString());
                    }
                }
            }
            else
                response.getWriter().write("No Action found for Campaign service");
        }else{
            log.info("ImportListService : MailChimp Configuration Service Not Available");
        }
    }
}
