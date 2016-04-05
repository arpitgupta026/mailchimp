package com.ttnd.mailchimp.service;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.ttnd.cms.Constants;
import com.ttnd.cms.helper.JcrHelper;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import javax.jcr.Session;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Jatin on 3/3/2016.
 */

@SlingServlet(paths = "/services/mailchimp/import/lists")
public class ImportList extends SlingSafeMethodsServlet {

    private Logger log = Logger.getLogger(ImportList.class.getName());

    @Reference
    private JcrHelper jcrHelper;

    @Reference
    private QueryBuilder queryBuilder;


    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{
        if(jcrHelper != null){
            String source =  request.getParameter("source");
            String path =  request.getParameter("path");
            if(path != null && source != null){
                Resource resource = jcrHelper.findResource(path);
                if(resource != null){
                    if(("cloud").equalsIgnoreCase(source)){
                        ValueMap map = jcrHelper.getConfigFromCloudService(path);
                        if(map != null){
                            Object obj  = map.get(Constants.METADATA_MAILCHIMP_USERNAME);
                            if(obj != null){
                                String username = obj.toString();
                                if(username != null){
                                    JSONArray listArray = getMailChimpListBasedOnUsername(username);
                                    response.getWriter().write(listArray.toString());
                                }
                            }
                        }
                    }/*else if(("page").equalsIgnoreCase(source)){
                        ValueMap map = resource.adaptTo(ValueMap.class);
                        if(map != null){
                            String cloudServicePath = map.get(Constants.CQ_CLOUD_SERVICE_CONFIG_PROPERTY).toString();
                            JSONObject lists = null;//MailChimpUtil.getMailChimpList(jcrHelper.getConfigFromCloudService(cl0oudServicePath));
                            if(lists != null){
                                response.getWriter().write(lists.toString());
                            }
                        }
                    }*/
                    /**/
                }

            }

        }else{
            log.info("ImportListService : MailChimp Configuration Service Not Available");
        }
    }

    private JSONArray getMailChimpListBasedOnUsername(String username){
        JSONArray listArray = new JSONArray();
        Map<String,String> queryMap = new HashMap<String,String>();
        queryMap.put("path", "/home/groups");
        queryMap.put("1_property", "jcr:primaryType");
        queryMap.put("1_property.value", "rep:Group");
        queryMap.put("2_property", "profile/cq:authorizableCategory");
        queryMap.put("2_property.value", "mcm");
        queryMap.put("3_property", "profile/sling:resourceType");
        queryMap.put("3_property.value", "cq/security/components/profile");
        queryMap.put("4_property", "profile/account");
        queryMap.put("4_property.value", username);
        Session session = jcrHelper.getJCRSession();
        if(session != null && !session.isLive()){
            session.logout();
            session =jcrHelper.getJCRSession();
        }
        if(session != null && session.isLive()){
            Query query = queryBuilder.createQuery(PredicateGroup.create(queryMap), session);
            SearchResult result = query.getResult();
            if(result != null){
                Iterator<Resource> resources = result.getResources();

                while(resources.hasNext()){
                    Resource res = resources.next();
                    if(res != null){
                        Resource profileResource = res.getChild("profile");
                        if(profileResource != null){
                            ValueMap profileMap = profileResource.adaptTo(ValueMap.class);
                            try{
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("text", profileMap.getOrDefault("givenName", "No Name"));
                                jsonObject.put("value", profileMap.getOrDefault("list-id", ""));
                                listArray.put(jsonObject);
                            }catch (JSONException je){
                                je.printStackTrace();
                                session.logout();
                            }

                        }
                    }
                }
                session.logout();
            }
        }
        return listArray;
    }
}
