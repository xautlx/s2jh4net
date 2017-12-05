package com.entdiy.security;

import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppBasicAuthenticationFilter extends PathMatchingFilter {

    private static final Logger logger = LoggerFactory.getLogger(AppBasicAuthenticationFilter.class);

    private Properties appKeySecrets = new Properties();

    /**
     * HTTP Authorization header, equal to <code>Authorization</code>
     */
    protected static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        String authzHeader = getAuthzHeader(request);
        if (StringUtils.isNotBlank(authzHeader)) {
            String[] keySecrets = getPrincipalsAndCredentials(HttpServletRequest.BASIC_AUTH, authzHeader);
            String key = keySecrets[0];
            String secret = (String) appKeySecrets.get(key);
            if (StringUtils.isNotBlank(secret) && secret.equals(keySecrets[1])) {
                return true;
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug(AUTHORIZATION_HEADER + " header value '{}' not valid, sending 401 response.", authzHeader);
        }
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

    /**
     * Returns the {@link #AUTHORIZATION_HEADER AUTHORIZATION_HEADER} from the specified ServletRequest.
     * <p/>
     * This implementation merely casts the request to an <code>HttpServletRequest</code> and returns the header:
     * <p/>
     * <code>HttpServletRequest httpRequest = {@link WebUtils#toHttp(javax.servlet.ServletRequest) toHttp(reaquest)};<br/>
     * return httpRequest.getHeader({@link #AUTHORIZATION_HEADER AUTHORIZATION_HEADER});</code>
     *
     * @param request the incoming <code>ServletRequest</code>
     * @return the <code>Authorization</code> header's value.
     */
    protected String getAuthzHeader(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        return httpRequest.getHeader(AUTHORIZATION_HEADER);
    }

    /**
     * Returns the username and password pair based on the specified <code>encoded</code> String obtained from
     * the request's authorization header.
     * <p/>
     * Per RFC 2617, the default implementation first Base64 decodes the string and then splits the resulting decoded
     * string into two based on the ":" character.  That is:
     * <p/>
     * <code>String decoded = Base64.decodeToString(encoded);<br/>
     * return decoded.split(":");</code>
     *
     * @param scheme  the {@link #getAuthcScheme() authcScheme} found in the request
     *                {@link #getAuthzHeader(javax.servlet.ServletRequest) authzHeader}.  It is ignored by this implementation,
     *                but available to overriding implementations should they find it useful.
     * @param encoded the Base64-encoded username:password value found after the scheme in the header
     * @return the username (index 0)/password (index 1) pair obtained from the encoded header data.
     */
    protected String[] getPrincipalsAndCredentials(String scheme, String encoded) {
        if (encoded.indexOf(" ") > -1) {
            encoded = encoded.split(" ")[1];
        }
        String decoded = Base64.decodeToString(encoded);
        return decoded.split(":", 2);
    }

    public void setAppKeySecrets(Properties appKeySecrets) {
        this.appKeySecrets = appKeySecrets;
    }
}