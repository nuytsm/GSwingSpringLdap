package be.nuytsm.GSwingSpringLdap.springconfig;

import javax.inject.Inject;
import javax.naming.directory.SearchControls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
@ComponentScan("be.nuytsm")
@PropertySource(value="classpath:/properties/connection.properties")
public class SpringConfig {
	
	@Inject
    Environment env;
	
	private Logger logger = LoggerFactory.getLogger(SpringConfig.class);
	
    @Bean
    public LdapContextSource contextSource () {
        LdapContextSource contextSource= new LdapContextSource();
        String url = "ldap://" + env.getRequiredProperty("host") + ":" + env.getRequiredProperty("port") +"/";
        logger.info("url: " + url);
        contextSource.setUrl(url);
        contextSource.setBase(env.getRequiredProperty("base"));
        logger.info("name: " + env.getRequiredProperty("name"));
        logger.info("pass: " + env.getRequiredProperty("pass"));
        contextSource.setUserDn(env.getRequiredProperty("name"));
        contextSource.setPassword(env.getRequiredProperty("pass"));
        contextSource.afterPropertiesSet();
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        LdapTemplate ld =  new LdapTemplate(contextSource()); 
        ld.setDefaultCountLimit(10);
        ld.setDefaultTimeLimit(60000);
        ld.setDefaultSearchScope(SearchControls.SUBTREE_SCOPE);
        return ld;
    }

}
