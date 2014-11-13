package be.nuytsm.GSwingSpringLdap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import be.nuytsm.GSwingSpringLdap.springconfig.SpringConfig;

public class Starter {
	
	private static final Logger logger = LoggerFactory.getLogger(Starter.class);
	
	public static void main(String[] args){
		doWithAnnotationContext();
//		doWithContextFile();
	}
	
	private static void doWithAnnotationContext() {
		logger.debug("Loading context");
		 AnnotationConfigApplicationContext context =
			     new AnnotationConfigApplicationContext();
		 context.register(SpringConfig.class);
		 context.refresh();
		
		logger.info("querying..");
		
		LdapQueryService ldapQueryService = (LdapQueryService) context.getBean("LdapQueryService");
		
		
		logger.info("" +ldapQueryService.getByServiceNumber("0401368"));
		
		LdapQueryService ldapQueryService2 = ApplicationContextProvider.getApplicationContext().getBean("LdapQueryService", LdapQueryService.class);
		logger.info("" +ldapQueryService2.getByAccount("nuyts.m"));
	}

	private static void doWithContextFile() {
		logger.debug("Loading context");
		ApplicationContext context = new ClassPathXmlApplicationContext("context/context.xml");
		
		logger.info("querying..");
		
		LdapQueryService ldapQueryService = (LdapQueryService) context.getBean("LdapQueryService");
		
		
		logger.info("" +ldapQueryService.getByServiceNumber("0401368"));
		
		LdapQueryService ldapQueryService2 = ApplicationContextProvider.getApplicationContext().getBean("LdapQueryService", LdapQueryService.class);
		logger.info("" +ldapQueryService2.getByAccount("nuyts.m"));
	}

}