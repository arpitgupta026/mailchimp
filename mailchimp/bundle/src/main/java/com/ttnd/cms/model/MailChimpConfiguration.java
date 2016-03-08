package com.ttnd.cms.model;

import com.ttnd.cms.Constants;
import org.apache.felix.scr.annotations.*;
import org.osgi.service.component.ComponentContext;
import java.util.Dictionary;
import java.util.logging.Logger;

/**
 * Created by Jatin on 3/6/2016.
 */

@Component(label = "MailChimp Configuration Component",
        metatype = true,
        description = "MailChimp Configuration",
        immediate=true)
@Service(value = MailChimpConfiguration.class)
public class MailChimpConfiguration {

    private Logger log = Logger.getLogger(MailChimpConfiguration.class.getName());

    @Property(name = "mailchimp.apiConfig.usDomain",
            label = "MailChimp Account Domain",
            description = "Account Domain refers to us<any-number>. Account Domain can be identified from Account settings in MailChimp",
            propertyPrivate = false)
    private String usDomain ;

    @Property(name = "mailchimp.apiConfig.key",
            label = "MailChimp API Key",
            description = "API key can be identified from Account settings in MailChimp",
            propertyPrivate = false)
    private String apiKey ;

    @Property(name = "mailchimp.apiConfig.username",
            label = "MailChimp API username",
            description = "Username of MailChimp Account Holder",
            propertyPrivate = false)
    private String username ;

    private Dictionary configDictionary;

    @Activate
    protected void activate(ComponentContext componentContext) {
        Dictionary properties = componentContext.getProperties();
        log.info("Activate Method of MailChimp Configuration Component Called");
        log.info(properties.get(Constants.METADATA_MAILCHIMP_DOMAIN).toString());
        this.usDomain = properties.get(Constants.METADATA_MAILCHIMP_DOMAIN).toString();
        log.info(properties.get(Constants.METADATA_MAILCHIMP_APIKEY).toString());
        this.apiKey = properties.get(Constants.METADATA_MAILCHIMP_APIKEY).toString();
        log.info(properties.get(Constants.METADATA_MAILCHIMP_USERNAME).toString());
        this.username = properties.get(Constants.METADATA_MAILCHIMP_USERNAME).toString();
        configDictionary = properties;
    }

    @Modified
    protected void modified(ComponentContext componentContext) {
        activate(componentContext);
    }

    public Dictionary getConfigDictionary(){
        return this.configDictionary;
    }
}
