package com.ttnd.cms.servlet;

import com.ttnd.cms.helper.JcrHelper;
import com.ttnd.util.MailChimpUtil;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Jatin on 3/3/2016.
 */

@SlingServlet(paths = "/services/mailchimp/lists/subscribe")
public class SubscribeUser extends SlingAllMethodsServlet {

    private Logger log = Logger.getLogger(SubscribeUser.class.getName());

    @Reference
    private JcrHelper jcrHelper;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{
        if(jcrHelper != null){
            String path =  request.getParameter("path");
            String emailID = request.getParameter("emailID");
            String[] listIDs = request.getParameterValues("listID");
            if(path != null){
                Resource resource = jcrHelper.findResource(path);
                if(resource != null){
                    ValueMap map = jcrHelper.getConfigFromCloudService(path);
                    if(map != null){
                        String responseObj = MailChimpUtil.subscribeUser(emailID, listIDs, map);
                        response.getWriter().write(responseObj);
                    }
                }
            }

        }else{
            log.info("ImportListService : MailChimp Configuration Service Not Available");
        }
    }

}
