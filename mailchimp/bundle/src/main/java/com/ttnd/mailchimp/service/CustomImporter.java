package com.ttnd.mailchimp.service;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.polling.importer.ImportException;
import com.day.cq.polling.importer.Importer;

@Service(value = Importer.class)
@Component
@Property(name = Importer.SCHEME_PROPERTY, value = "mailchimpListData", propertyPrivate = true)
public class CustomImporter implements Importer {
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomImporter.class);

	public void importData(String scheme, String dataSource, Resource resource) throws ImportException {
		// TODO Auto-generated method stub
		LOGGER.info("MyImporter started...");
		LOGGER.info("Scheme {}", scheme);
		LOGGER.info("DataSource {}", dataSource);
		LOGGER.info("Resource {}", resource.getPath());
		
	}

	public void importData(String arg0, String arg1, Resource arg2, String arg3, String arg4) throws ImportException {
		// TODO Auto-generated method stub
		LOGGER.info("MyImporter started...");
	}
	
/*	   public void importData(String scheme, String dataSource, final Resource resource, String username, String password) throws ImportException{
	        LOGGER.info(":::::::::::::::::::::::::::   Dropbox first :::::::::::::::::::::::::::::::");
	        LOGGER.info(" :: scheme :: "  +  scheme + " :: dataSource :: " + dataSource  + " :: Resource :: " + resource);
	    }

	    public void importData(final String scheme, final String dataSource,
	                           final Resource resource) throws ImportException {
	        try {

	            LOGGER.info(":::::::::::::::::::::::::::   Dropbox :::::::::::::::::::::::::::::::");
	            LOGGER.info(" :: scheme :: "  +  scheme + " :: dataSource :: " + dataSource  + " :: Resource :: " + resource);
	            ResourceResolver resolver = resource.getResourceResolver();
	            if(resolver != null){
	                Resource dataSourceresource  = resolver.getResource(dataSource);
	                if(dataSourceresource != null){
	                    Node dataSourceNode = dataSourceresource.adaptTo(Node.class);
	                    if(dataSourceNode != null){
	                        String accessToken = dataSourceNode.getProperty("access-token").getString();
	                        String clientIdentifier = dataSourceNode.getProperty("dropbox-client-identifier").getString();
	                        JSONArray dataArray = null;

	                    }
	                }
	            }
	        }
	        catch (RepositoryException  e) {
	            LOGGER.error("Dropbox  RepositoryException", e);
	        }

	    }*/
 
}