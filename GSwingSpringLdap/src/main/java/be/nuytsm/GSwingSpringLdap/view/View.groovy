package be.nuytsm.GSwingSpringLdap.view

import groovy.util.logging.Slf4j;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import be.nuytsm.GSwingSpringLdap.ApplicationContextProvider;
import be.nuytsm.GSwingSpringLdap.LdapQueryService;

@Component
@Slf4j
class View {
	
	@Inject
	private LdapQueryService ldapQueryService;
	
	
	public static void main(String[] args){
//		LdapQueryService ldapQueryService2 = ApplicationContextProvider.getApplicationContext().getBean("LdapQueryService", LdapQueryService.class);
		log.info(LdapQueryService);
		
	}

}
