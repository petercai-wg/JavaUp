package com.ldap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.List;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class LDAPRepository {
	 	@Autowired
	    private LdapTemplate ldapTemplate;

	    /**
	     * Retrieves all the persons in the ldap server
	     * @return list of person names
	     */
	    public List<String> getAllPersonNames() {
	        return ldapTemplate.search(
	                query().where("objectclass").is("person"),
	                new AttributesMapper<String>() {
	                    public String mapFromAttributes(Attributes attrs)
	                            throws NamingException {
	                        return (String) attrs.get("cn").get();
	                    }
	                });
	    }
}
