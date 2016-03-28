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

@SlingServlet(paths = "/services/mailchimp/import/lists")
public class ImportList extends SlingSafeMethodsServlet {

    private Logger log = Logger.getLogger(ImportList.class.getName());

    @Reference
    private MailChimpConfiguration mailChimpConfig;


    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{
        if(mailChimpConfig != null){
            JSONObject lists = MailChimpUtil.getMailChimpList(mailChimpConfig.getConfigDictionary());
            if(lists != null){
                response.getWriter().write(lists.toString());
            }
        }else{
            log.info("ImportListService : MailChimp Configuration Service Not Available");
        }
    }
}
