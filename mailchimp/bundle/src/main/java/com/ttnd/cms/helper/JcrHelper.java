package com.ttnd.cms.helper;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.PageManagerFactory;
import com.ttnd.cms.Constants;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.*;
import org.apache.sling.jcr.api.SlingRepository;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

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

}
