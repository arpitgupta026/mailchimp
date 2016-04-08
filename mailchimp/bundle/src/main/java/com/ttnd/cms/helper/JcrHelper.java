package com.ttnd.cms.helper;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.PageManagerFactory;
import com.ttnd.cms.Constants;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.*;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Jatin on 3/30/2016.
 */

@Component
@Service(value = JcrHelper.class)
public class JcrHelper {

    @Reference
    private PageManagerFactory pageManagerFactory;

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private SlingRepository repository;

    @Reference
    private QueryBuilder queryBuilder;

    public ValueMap getConfigFromCloudService(String path){
        ValueMap map = null;
        if(path != null && resolverFactory != null && pageManagerFactory != null){
            try {
                ResourceResolver resolver = resolverFactory.getAdministrativeResourceResolver(null);
                if(resolver != null){
                    PageManager pageManager = pageManagerFactory.getPageManager(resolver);
                    if(pageManager != null){
                        Page page = pageManager.getPage(path);
                        if(page != null){
                            map = page.getContentResource().adaptTo(ValueMap.class);
                        }
                    }
                }
            }  catch(LoginException e){
                e.printStackTrace();
            }
        }
        return map;
    }

    public Resource findResource(String path){
        Resource resource = null;
        if(path != null && resolverFactory != null ){
            ResourceResolver resolver = null;
            try {
                resolver = resolverFactory.getAdministrativeResourceResolver(null);
                if(resolver != null){
                    resource = resolver.getResource(path);
                }
            } catch (LoginException e) {
                e.printStackTrace();
            }
        }
        return resource;
    }

    public Session getJCRSession(){
        Session session = null;
        try {
            if(repository != null){
                session = repository.loginAdministrative(null);
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return session;
    }

    public String getMailChimpConfigFromConfigs(String[] configs){
        String configURL = null;
        for(String config : configs){
            if(config.indexOf(Constants.MAILCHIMP_CONFIG_PATH) > -1){
                configURL = config;
                break;
            }
        }
        return configURL;
    }

    public JSONArray getMailChimpListBasedOnUsername(String username){
        JSONArray listArray = new JSONArray();
        Map<String,String> queryMap = new HashMap<String, String>();
        queryMap.put("path", "/home/groups");
        queryMap.put("1_property", "jcr:primaryType");
        queryMap.put("1_property.value", "rep:Group");
        queryMap.put("2_property", "profile/cq:authorizableCategory");
        queryMap.put("2_property.value", "mcm");
        queryMap.put("3_property", "profile/sling:resourceType");
        queryMap.put("3_property.value", "cq/security/components/profile");
        queryMap.put("4_property", "profile/account");
        queryMap.put("4_property.value", username);
        Session session = getJCRSession();
        if(session != null && !session.isLive()){
            session.logout();
            session = getJCRSession();
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
