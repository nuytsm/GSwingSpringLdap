package be.nuytsm.GSwingSpringLdap;

import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import javax.inject.Inject;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

@Service("LdapQueryService")
public class LdapQueryService {

	public class QueryResult {
		private String name;
		private String result;

		public QueryResult(String name, String result) {
			this.name = name;
			this.result = result;
		}

		public String getName() {
			return name;
		}

		public String getResult() {
			return result;
		}
	}

	private static final Logger logger = LoggerFactory
			.getLogger(LdapQueryService.class);

	@Inject
	private LdapTemplate ldapTemplate;

	private class PersonAttributesMapper implements
			AttributesMapper<QueryResult> {
		public QueryResult mapFromAttributes(Attributes attrs)
				throws NamingException {
			StringBuilder sb = new StringBuilder();
			String name = attrs.get("sAMAccountName").getAll().next()
					.toString();

			NamingEnumeration<? extends Attribute> ne = attrs.getAll();
			while (ne.hasMore()) {
				sb.append(ne.next().toString()).append("\n");
			}
			return new QueryResult(name, sb.toString());
		}
	}

	public List<QueryResult> getByAccount(String account) {
		return ldapTemplate.search(query().where("objectclass").is("person")
				.and("sAMAccountName").whitespaceWildcardsLike(account),
				new PersonAttributesMapper());

	}

	public List<QueryResult> getByServiceNumber(String serviceNumber) {
		return ldapTemplate.search(query().where("objectclass").is("person")
				.and("employeeID").whitespaceWildcardsLike(serviceNumber),
				new PersonAttributesMapper());
	}

}
