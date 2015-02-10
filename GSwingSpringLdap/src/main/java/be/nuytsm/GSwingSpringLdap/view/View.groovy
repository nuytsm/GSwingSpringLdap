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
	def results;
	String name = '';
	String snumber = '';
	SwingBuilder sb = new SwingBuilder();

	public initView(){
		sb.edt {
			def customMenuBar = {
				sb.menuBar{
				  menu(text: "File", mnemonic: 'F') {
					menuItem(text: "Exit", mnemonic: 'X', actionPerformed: { dispose() })
				  }
				}
			 }
			
			def searchpanel = {
				sb.panel(constraints:BL.NORTH){
							borderLayout()
							def textlabel = label(text:"Name: ", constraints: BL.WEST)
							def nameField = textField(constraints: BL.CENTER, id:'fname', actionPerformed: {
								findInfo()
							}	)
							bean(this, name: bind{fname.text})
							
						}
			}
			
			def serviceNumberPanel = {
				sb.panel(constraints:BL.CENTER){
							borderLayout()
							def textlabel = label(text:"Servicenumber: ", constraints: BL.WEST)
							def servicenumberField = textField(constraints: BL.CENTER, id:'snum', actionPerformed: {
								findInfo()
							}	)
							bean(this, snumber: bind{snum.text})	
						}
			}
			
			def searchbutton = {
				sb.button(text:'Search',
					actionPerformed: {
								findInfo()
					},constraints:BL.EAST)
			}
			
			def resultspanel = {
				sb.splitPane(dividerSize: 0, constraints:BL.CENTER) {
					scrollPane(constraints: "left", preferredSize: [160, -1]) { 
						results = list()
					}
					bean(this, results: bind{resultlist.model})
					scrollPane(constraints: "right") { textArea(id:'resultdetail') }
				}
			}
			
			frame(title:'Frame', defaultCloseOperation:JFrame.EXIT_ON_CLOSE, size:[800, 600], locationRelativeTo: null, show: true ) {
				customMenuBar()
				borderLayout()
				panel(constraints:BL.NORTH){
						borderLayout()
						searchpanel()
						serviceNumberPanel()
						
						searchbutton()
				}

				resultspanel()
			}
		}
	}

	def findInfo() {
		if (!name.empty){
			log.info name
			sb.doOutside {
				results.listData = ldapQueryService.getByAccount(name)
				snumber = ''
			}
		} else if (!snumber.empty){
			log.info snumber
			sb.doOutside {
				results.listData = ldapQueryService.getByServiceNumber(snumber)
			}
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
