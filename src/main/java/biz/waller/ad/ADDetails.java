package biz.waller.ad;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/*
MIT License

Copyright (c) 2017 Raphael Waller

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
public class ADDetails {
    public ADDetails() {
        super();
    }
    protected static String getDN(String samAccountName) throws NamingException {
        LdapContext cnx = getADConnection();
        SearchControls searchCtls = new SearchControls();
        String returnedAtts[]={};
        searchCtls.setReturningAttributes(returnedAtts);
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "samaccountname=" + samAccountName;
        //Specify the Base for the search
        String searchBase = PropLoader.searchBase;
        NamingEnumeration<SearchResult> answer = cnx.search(searchBase, searchFilter, searchCtls);
        cnx.close();
        return answer.next().getNameInNamespace();
    }
    protected static LdapContext getADConnection() {

            try
            {
                Hashtable<String, String> ldapEnv = new Hashtable<String, String>(11);
                ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                ldapEnv.put(Context.PROVIDER_URL,  PropLoader.ldapString);
                ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
                ldapEnv.put(Context.SECURITY_PRINCIPAL, PropLoader.adminDN);
                ldapEnv.put(Context.SECURITY_CREDENTIALS, PropLoader.adminPassword);
                ldapEnv.put(Context.SECURITY_PROTOCOL, "ssl");
                ldapEnv.put("com.sun.jndi.ldap.connect.pool", "true");
                ldapEnv.put("com.sun.jndi.ldap.connect.pool.protocol", "plain ssl");

                System.out.println(System.currentTimeMillis() + " <- Pre LDAP Context creation");
                LdapContext ldapContext = new InitialLdapContext(ldapEnv, null);
                System.out.println(System.currentTimeMillis() + " <- Post LDAP Context creation");
                return ldapContext;}
            catch (Exception e)
            {
                System.out.println(" Search error: " + e);
                e.printStackTrace();
                System.exit(-1);
            }
    return null;

    }

    public static List<String> getPhone(String mailaddress) {
        List<String> phone = new ArrayList<String>();
        try {
        LdapContext cnx = getADConnection();
        SearchControls searchCtls = new SearchControls();
        String returnedAtts[]={"mail", "displayName", "description", "homePhone", "telephoneNumber", "mobile"};
        searchCtls.setReturningAttributes(returnedAtts);
        //Specify the search scope
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        //specify the LDAP search filter
        String searchFilter = "mail=" + mailaddress;
        //Specify the Base for the search
        String searchBase = "DC=internal,DC=waller,DC=biz";
        //initialize counter to total the results
        int totalResults = 0;

        // Search for objects using the filter

            NamingEnumeration<SearchResult> answer = cnx.search(searchBase, searchFilter, searchCtls);
            //Loop through the search results
            while (answer.hasMoreElements())
            {
                SearchResult sr = (SearchResult)answer.next();

                totalResults++;
                String dn = sr.getName() + ", " + searchBase;
                //System.out.println(">>>" + sr.getName());

                Attributes searchAtt = cnx.getAttributes(dn, returnedAtts);
                Attributes attrs = sr.getAttributes();
                if (attrs.get("description") != null) { System.out.println(attrs.get("description").get());
                    }
                if (attrs.get("mail") != null) { System.out.println(attrs.get("mail").get()); }
                if (attrs.get("telephoneNumber") != null) { System.out.println(attrs.get("telephoneNumber").get());
                    phone.add(attrs.get("telephoneNumber").get().toString()); }
                if (attrs.get("homePhone") != null) { System.out.println(attrs.get("homePhone").get());
                    phone.add(attrs.get("homePhone").get().toString()); }
                if (attrs.get("mobile") != null) { System.out.println(attrs.get("mobile").get());
                    phone.add(attrs.get("mobile").get().toString()); }

        }
        } catch (NamingException e) {
            e.printStackTrace();
            System.exit(-1);
        }


        return phone;
    }


    public static String getSAMAccount(String mailaddress) {
        String samAccountName = new String();
        try {
            LdapContext cnx = getADConnection();
            SearchControls searchCtls = new SearchControls();
            String returnedAtts[]={"samAccountName"};
            searchCtls.setReturningAttributes(returnedAtts);
            //Specify the search scope
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            //specify the LDAP search filter
            String searchFilter = "mail=" + mailaddress;
            //Specify the Base for the search
            String searchBase = "DC=internal,DC=waller,DC=biz";
            //initialize counter to total the results
            int totalResults = 0;

            // Search for objects using the filter

            NamingEnumeration<SearchResult> answer = cnx.search(searchBase, searchFilter, searchCtls);
            //Loop through the search results
            while (answer.hasMoreElements())
            {
                SearchResult sr = (SearchResult)answer.next();

                totalResults++;
                String dn = sr.getName() + ", " + searchBase;
                //System.out.println(">>>" + sr.getName());

                Attributes searchAtt = cnx.getAttributes(dn, returnedAtts);
                Attributes attrs = sr.getAttributes();
                samAccountName = attrs.get("samaccountname").get().toString();
                System.out.println(samAccountName);
            }
        } catch (NamingException e) {
            e.printStackTrace();
            System.exit(-1);
        }


        return samAccountName;
    }
    public static Boolean setPassword(String samAccountName, String password) {

        try
        {
            System.out.println("updating password...\n");
            String quotedPassword = "\"" + password + "\"";
            char unicodePwd[] = quotedPassword.toCharArray();
            byte pwdArray[] = new byte[unicodePwd.length * 2];
            for (int i = 0; i < unicodePwd.length; i++)
            {
                pwdArray[i * 2 + 1] = (byte) (unicodePwd[i] >>> 8);
                pwdArray[i * 2 + 0] = (byte) (unicodePwd[i] & 0xff);
            }
            System.out.print("encoded password: ");
            for (int i = 0; i < pwdArray.length; i++)
            {
                System.out.print(pwdArray[i] + " ");
            }
            System.out.println();
            ModificationItem[] mods = new ModificationItem[1];

            //mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("employeeNumber","18"));
            String fullDN = getDN(samAccountName);
            LdapContext cnx = getADConnection();

            System.out.println(System.currentTimeMillis() + " <- Pre Create Attribute");
            BasicAttribute attr = new BasicAttribute("UnicodePwd", pwdArray);
            System.out.println(System.currentTimeMillis() + " <- Pre Make modification item");
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
            System.out.println(System.currentTimeMillis() + " <- Pre Modify Context (Password)");
            cnx.modifyAttributes(fullDN, mods);
            System.out.println(System.currentTimeMillis() + " <- Post Modify Context (Password)");
            cnx.close();
            System.out.println(System.currentTimeMillis() + " <- Post Connection Close");
            return true;
        }
        catch (Exception e)
        {
            System.out.println("update password error: " + e);
            return false;
        }
    }
}