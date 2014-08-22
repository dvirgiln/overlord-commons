/*
 * Copyright 2013 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.overlord.commons.auth.jboss7;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Principal;
import java.security.acl.Group;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.dom.DOMSource;

import org.apache.commons.codec.binary.Base64;
import org.jboss.security.SimpleGroup;
import org.jboss.security.auth.spi.AbstractServerLoginModule;
import org.overlord.commons.auth.tomcat7.HttpRequestThreadLocalValve;
import org.overlord.commons.auth.util.SAMLBearerTokenUtil;
import org.overlord.commons.i18n.Messages;
import org.picketlink.identity.federation.core.parsers.saml.SAMLAssertionParser;
import org.picketlink.identity.federation.core.saml.v2.util.DocumentUtil;
import org.picketlink.identity.federation.saml.v2.assertion.AssertionType;
import org.picketlink.identity.federation.saml.v2.assertion.AttributeStatementType;
import org.picketlink.identity.federation.saml.v2.assertion.AttributeStatementType.ASTChoiceType;
import org.picketlink.identity.federation.saml.v2.assertion.NameIDType;
import org.picketlink.identity.federation.saml.v2.assertion.StatementAbstractType;
import org.picketlink.identity.federation.saml.v2.assertion.SubjectType;
import org.w3c.dom.Document;

/**
 * <p>
 * A login module that consumes a SAML Assertion passed via the password piece
 * of a Basic authentication request.  In other words, the SAML Assertion should
 * be passed as the password (with a username of "SAML-BEARER-TOKEN") in a BASIC
 * auth style request.  The Authorization HTTP header would look like a normal
 * BASIC auth version (e.g. "Basic U0FNTC1CRUFSRVItVE9LRU46PHNhbWw6QXNz="), but
 * the Base64 Decoded Credentials will look like:
 * </p>
 * <pre>
 *   SAML-BEARER-TOKEN:<saml:Assertion ...>...</saml:Assertion>
 * </pre>
 * <p>
 * This class will validate the SAML Assertion and then consume it, making the
 * JAAS principal the same as the SAML subject.  JAAS role information is
 * pulled from a multi-value SAML Attribute called "Role".
 * </p>
 *
 * @author eric.wittmann@redhat.com
 */
public class SAMLBearerTokenLoginModule extends AbstractServerLoginModule {

    private final static Messages messages = Messages.getInstance();

    /** Configured in standalone.xml in the login module */
    private final Set<String> allowedIssuers = new HashSet<String>();
    private String signatureRequired;
    private String keystorePath;
    private String keystorePassword;
    private String keyAlias;
    private String keyPassword;

    private Principal identity;
    private final Set<String> roles = new HashSet<String>();

    /**
     * Constructor.
     */
    public SAMLBearerTokenLoginModule() {
    }

    /**
     * @see org.jboss.security.auth.spi.AbstractServerLoginModule#initialize(javax.security.auth.Subject, javax.security.auth.callback.CallbackHandler, java.util.Map, java.util.Map)
     */
    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState,
            Map<String, ?> options) {
        super.initialize(subject, callbackHandler, sharedState, options);
        String val = (String) options.get("allowedIssuers"); //$NON-NLS-1$
        if (val != null) {
            String [] split = val.split(","); //$NON-NLS-1$
            for (String issuer : split) {
                if (issuer != null && issuer.trim().length() > 0)
                    allowedIssuers.add(issuer);
            }
        }
        signatureRequired = (String) options.get("signatureRequired"); //$NON-NLS-1$
        keystorePath = (String) options.get("keystorePath"); //$NON-NLS-1$
        keystorePassword = (String) options.get("keystorePassword"); //$NON-NLS-1$
        keyAlias = (String) options.get("keyAlias"); //$NON-NLS-1$
        keyPassword = (String) options.get("keyPassword"); //$NON-NLS-1$
    }

    /**
     * @see org.jboss.security.auth.spi.AbstractServerLoginModule#login()
     */
    @Override
    public boolean login() throws LoginException {
        try {
            HttpServletRequest request = getCurrentRequest();
            String authorization = request.getHeader("Authorization"); //$NON-NLS-1$
            if (authorization != null && authorization.startsWith("Basic")) { //$NON-NLS-1$
                String b64Data = authorization.substring(6);
                byte[] dataBytes = Base64.decodeBase64(b64Data);
                String data = new String(dataBytes, "UTF-8"); //$NON-NLS-1$
                if (data.startsWith("SAML-BEARER-TOKEN:")) { //$NON-NLS-1$
                    String assertionData = data.substring(18);
                    Document samlAssertion = DocumentUtil.getDocument(assertionData);
                    SAMLAssertionParser parser = new SAMLAssertionParser();
                    DOMSource source = new DOMSource(samlAssertion);
                    XMLEventReader xmlEventReader = XMLInputFactory.newInstance().createXMLEventReader(source);
                    Object parsed = parser.parse(xmlEventReader);
                    AssertionType assertion = (AssertionType) parsed;
                    SAMLBearerTokenUtil.validateAssertion(assertion, request, allowedIssuers);
                    if ("true".equals(signatureRequired)) { //$NON-NLS-1$
                        KeyPair keyPair = getKeyPair(assertion);
                        if (!SAMLBearerTokenUtil.isSAMLAssertionSignatureValid(samlAssertion, keyPair)) {
                            throw new LoginException(messages.format("SAMLBearerTokenLoginModule.InvalidSignature")); //$NON-NLS-1$
                        }
                    }
                    consumeAssertion(assertion);
                    loginOk = true;
                    return true;
                }
            }
        } catch (LoginException le) {
            throw le;
        } catch (Exception e) {
            e.printStackTrace();
            loginOk = false;
            return false;
        }
        return super.login();
    }

    /**
     * Gets the current HTTP servlet request.
     * @throws PolicyContextException
     */
    private HttpServletRequest getCurrentRequest() throws LoginException {
        HttpServletRequest request = HttpRequestThreadLocalValve.TL_request.get();
        if (request == null) {
            try {
                request = (HttpServletRequest) PolicyContext.getContext("javax.servlet.http.HttpServletRequest"); //$NON-NLS-1$
            } catch (Exception e) {
                request = null;
            }
        }
        if (request == null) {
            throw new LoginException("Failed to get current HTTP request."); //$NON-NLS-1$
        }
        return request;
    }

    /**
     * Gets the key pair to use to validate the assertion's signature.  The key pair is retrieved
     * from the keystore.
     * @param assertion
     * @throws LoginException
     */
    private KeyPair getKeyPair(AssertionType assertion) throws LoginException {
        KeyStore keystore = loadKeystore();
        try {
            return SAMLBearerTokenUtil.getKeyPair(keystore, keyAlias, keyPassword);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LoginException(messages.format("SAMLBearerTokenLoginModule.FailedToGetKeyPair") + keyAlias); //$NON-NLS-1$
        }
    }

    /**
     * Loads the keystore.
     * @throws LoginException
     */
    private KeyStore loadKeystore() throws LoginException {
        try {
            return SAMLBearerTokenUtil.loadKeystore(keystorePath, keystorePassword);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LoginException("Error loading signature keystore: " + e.getMessage()); //$NON-NLS-1$
        }
    }

    /**
     * Consumes the assertion, resulting in the extraction of the Subject as the
     * JAAS principal and the Role Statements as the JAAS roles.
     * @param assertion
     * @throws Exception
     */
    private void consumeAssertion(AssertionType assertion) throws Exception {
        SubjectType samlSubjectType = assertion.getSubject();
        String samlSubject = ((NameIDType) samlSubjectType.getSubType().getBaseID()).getValue();
        identity = createIdentity(samlSubject);

        Set<StatementAbstractType> statements = assertion.getStatements();
        for (StatementAbstractType statement : statements) {
            if (statement instanceof AttributeStatementType) {
                AttributeStatementType attrStatement = (AttributeStatementType) statement;
                List<ASTChoiceType> attributes = attrStatement.getAttributes();
                for (ASTChoiceType astChoiceType : attributes) {
                    if (astChoiceType.getAttribute() != null && astChoiceType.getAttribute().getName().equals("Role")) { //$NON-NLS-1$
                        List<Object> values = astChoiceType.getAttribute().getAttributeValue();
                        for (Object roleValue : values) {
                            if (roleValue != null) {
                                roles.add(roleValue.toString());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @see org.jboss.security.auth.spi.AbstractServerLoginModule#getIdentity()
     */
    @Override
    protected Principal getIdentity() {
        return identity;
    }

    /**
     * @see org.jboss.security.auth.spi.AbstractServerLoginModule#getRoleSets()
     */
    @Override
    protected Group[] getRoleSets() throws LoginException {
        Group[] groups = new Group[1];
        groups[0] = new SimpleGroup("Roles"); //$NON-NLS-1$
        try {
            for (String role : roles) {
                groups[0].addMember(createIdentity(role));
            }
        } catch (Exception e) {
            throw new LoginException(messages.format("SAMLBearerTokenLoginModule.FailedToCreateGroupPrincipal") + e.getMessage()); //$NON-NLS-1$
        }
        return groups;
    }

}
