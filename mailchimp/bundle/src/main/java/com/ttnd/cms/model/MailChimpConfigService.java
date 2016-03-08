package com.ttnd.cms.model;

import com.ttnd.cms.Constants;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import java.io.IOException;
import java.util.Dictionary;

/**
 * Created by Jatin on 3/6/2016.
 */
@Component
@Service(value = MailChimpConfigService.class)
public class MailChimpConfigService {

    @Reference
    private ConfigurationAdmin configurationAdmin;

    public Dictionary getMailChimpConfigDictionary(){
        try{
            if(configurationAdmin != null){
                Configuration config = configurationAdmin.getConfiguration(Constants.MAILCHIMP_BUNDLE_PID);
                if(config != null){
                    return config.getProperties();
                }
            }
        }catch (IOException io){
            io.printStackTrace();
        }
        return null;
    }
}
