package be.nuytsm.GSwingSpringLdap.view

import groovy.beans.Bindable;
import groovy.swing.SwingBuilder;

import java.awt.BorderLayout as BL
import java.awt.GridLayout;
import java.awt.datatransfer.StringSelection;

import groovy.util.logging.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import be.nuytsm.GSwingSpringLdap.ApplicationContextProvider;
import be.nuytsm.GSwingSpringLdap.LdapQueryService;
import be.nuytsm.GSwingSpringLdap.LdapQueryService.QueryResult;
import be.nuytsm.GSwingSpringLdap.springconfig.SpringConfig;

@Service
@Slf4j
class View {

	@Inject
	private LdapQueryService ldapQueryService;
	@Bindable
	List<QueryResult> results = new ArrayList<>(0);
	@Bindable
	String name = '';
	@Bindable
	String snumber = '';

	public initView(){
		SwingBuilder sb = new SwingBuilder();
		sb.edt {
			frame(title:'Frame', defaultCloseOperation:JFrame.EXIT_ON_CLOSE, size:[800, 600], locationRelativeTo: null, show: true) {
				borderLayout()
				panel(constraints:BL.NORTH){
						borderLayout()
						panel(constraints:BL.NORTH){
							borderLayout()
							textlabel = label(text:"Name: ", constraints: BL.WEST)
							nameField = textField(constraints: BL.CENTER, id:'fname');	
							bean(this, name: bind{fname.text})

						}
						
						panel(constraints:BL.CENTER){
							borderLayout()
							textlabel = label(text:"Servicenumber: ", constraints: BL.WEST)
							servicenumberField = textField(constraints: BL.CENTER, id:'snum');
							bean(this, snumber: bind{snum.text})	
						}
						button(text:'Search',
								actionPerformed: {
											findInfo()
								},constraints:BL.EAST)
					
				}

				splitPane(dividerSize: 0, constraints:BL.CENTER) {
					scrollPane(constraints: "left", preferredSize: [160, -1]) { list(id:'resultlist') }
					bean(this, results: bind{resultlist.model})
					scrollPane(constraints: "right") { textArea(id:'resultdetail') }
				}
			}
		}
	}

	private findInfo() {
		if (!name.empty){
			log.info name
			results = ldapQueryService.getByAccount(name)
			snumber = ''
		} else if (!snumber.empty){
			log.info snumber
			results = ldapQueryService.getByServiceNumber(snumber)
			name = ''
		} else {
			log.info 'nothing to search for'
		}
		
		log.info "Found ${results.size} results"
	}


	public static void main(String[] args){
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext();
		context.register(SpringConfig.class);
		context.refresh();
		View v = ApplicationContextProvider.getApplicationContext().getBean(View.class);
		log.info(v.ldapQueryService.toString());
		v.initView();
	}
}
