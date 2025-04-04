package com.spring.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;


/**
 * 
 * don't use, confused about it's life cycle
 *
 */
@Service
public class JWTCookieCsrfTokenRepository implements CsrfTokenRepository {
	
	private static final Logger log = LoggerFactory.getLogger(JWTCookieCsrfTokenRepository.class);

	static final String DEFAULT_CSRF_COOKIE_NAME = "XSRF-TOKEN";

	public static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";

	public static final String DEFAULT_CSRF_HEADER_NAME = "X-XSRF-TOKEN";

	private String parameterName = DEFAULT_CSRF_PARAMETER_NAME;

	private String headerName = DEFAULT_CSRF_HEADER_NAME;

	private String cookieName = DEFAULT_CSRF_COOKIE_NAME;

	private boolean cookieHttpOnly = true;

	private String cookiePath;

	private String cookieDomain;

	private Boolean secure;

	private int cookieMaxAge = -1;

	public JWTCookieCsrfTokenRepository() {
	}

	@Override
	public CsrfToken generateToken(HttpServletRequest request) {
		log.info("generate token for cookie");
		return new DefaultCsrfToken(this.headerName, this.parameterName, createNewToken());
	}

	@Override
	public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
		String tokenValue = (token != null) ? token.getToken() : "";
		
		log.info(" Save Token to Cookie :" +  tokenValue);
		Cookie cookie = new Cookie(this.cookieName, tokenValue);
		cookie.setSecure((this.secure != null) ? this.secure : request.isSecure());
		cookie.setPath(StringUtils.hasLength(this.cookiePath) ? this.cookiePath : this.getRequestContext(request));
		cookie.setMaxAge((token != null) ? this.cookieMaxAge : 0);
		cookie.setHttpOnly(this.cookieHttpOnly);
		if (StringUtils.hasLength(this.cookieDomain)) {
			cookie.setDomain(this.cookieDomain);
		}
		response.addCookie(cookie);
	}

	@Override
	public CsrfToken loadToken(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, this.cookieName);
		if (cookie == null) {
			return null;
		}
		String token = cookie.getValue();
		
		log.info(" loadToken from  Cookie :" +  token);
		
		if (!StringUtils.hasLength(token)) {
			return null;
		}
		return new DefaultCsrfToken(this.headerName, this.parameterName, token);
	}

	/**
	 * Sets the name of the HTTP request parameter that should be used to provide a token.
	 * @param parameterName the name of the HTTP request parameter that should be used to
	 * provide a token
	 */
	public void setParameterName(String parameterName) {
		Assert.notNull(parameterName, "parameterName cannot be null");
		this.parameterName = parameterName;
	}

	/**
	 * Sets the name of the HTTP header that should be used to provide the token.
	 * @param headerName the name of the HTTP header that should be used to provide the
	 * token
	 */
	public void setHeaderName(String headerName) {
		Assert.notNull(headerName, "headerName cannot be null");
		this.headerName = headerName;
	}

	/**
	 * Sets the name of the cookie that the expected CSRF token is saved to and read from.
	 * @param cookieName the name of the cookie that the expected CSRF token is saved to
	 * and read from
	 */
	public void setCookieName(String cookieName) {
		Assert.notNull(cookieName, "cookieName cannot be null");
		this.cookieName = cookieName;
	}

	/**
	 * Sets the HttpOnly attribute on the cookie containing the CSRF token. Defaults to
	 * <code>true</code>.
	 * @param cookieHttpOnly <code>true</code> sets the HttpOnly attribute,
	 * <code>false</code> does not set it
	 */
	public void setCookieHttpOnly(boolean cookieHttpOnly) {
		this.cookieHttpOnly = cookieHttpOnly;
	}

	private String getRequestContext(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		return (contextPath.length() > 0) ? contextPath : "/";
	}

	/**
	 * Factory method to conveniently create an instance that has
	 * {@link #setCookieHttpOnly(boolean)} set to false.
	 * @return an instance of CookieCsrfTokenRepository with
	 * {@link #setCookieHttpOnly(boolean)} set to false
	 */
	public static JWTCookieCsrfTokenRepository withHttpOnlyFalse() {
		JWTCookieCsrfTokenRepository result = new JWTCookieCsrfTokenRepository();
		result.setCookieHttpOnly(false);
		return result;
	}

	private String createNewToken() {
		Map<String, Object> claims = new HashMap<String, Object>();
		
		String token = JWTUtil.generateToken(claims,"logindefault" );
		  
		return token;
	}

	/**
	 * Set the path that the Cookie will be created with. This will override the default
	 * functionality which uses the request context as the path.
	 * @param path the path to use
	 */
	public void setCookiePath(String path) {
		this.cookiePath = path;
	}

	/**
	 * Get the path that the CSRF cookie will be set to.
	 * @return the path to be used.
	 */
	public String getCookiePath() {
		return this.cookiePath;
	}

	/**
	 * Sets the domain of the cookie that the expected CSRF token is saved to and read
	 * from.
	 * @param cookieDomain the domain of the cookie that the expected CSRF token is saved
	 * to and read from
	 * @since 5.2
	 */
	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}

	/**
	 * Sets secure flag of the cookie that the expected CSRF token is saved to and read
	 * from. By default secure flag depends on {@link ServletRequest#isSecure()}
	 * @param secure the secure flag of the cookie that the expected CSRF token is saved
	 * to and read from
	 * @since 5.4
	 */
	public void setSecure(Boolean secure) {
		this.secure = secure;
	}

	/**
	 * Sets maximum age in seconds for the cookie that the expected CSRF token is saved to
	 * and read from. By default maximum age value is -1.
	 *
	 * <p>
	 * A positive value indicates that the cookie will expire after that many seconds have
	 * passed. Note that the value is the <i>maximum</i> age when the cookie will expire,
	 * not the cookie's current age.
	 *
	 * <p>
	 * A negative value means that the cookie is not stored persistently and will be
	 * deleted when the Web browser exits.
	 *
	 * <p>
	 * A zero value causes the cookie to be deleted immediately therefore it is not a
	 * valid value and in that case an {@link IllegalArgumentException} will be thrown.
	 * @param cookieMaxAge an integer specifying the maximum age of the cookie in seconds;
	 * if negative, means the cookie is not stored; if zero, the method throws an
	 * {@link IllegalArgumentException}
	 * @since 5.5
	 */
	public void setCookieMaxAge(int cookieMaxAge) {
		Assert.isTrue(cookieMaxAge != 0, "cookieMaxAge cannot be zero");
		this.cookieMaxAge = cookieMaxAge;
	}
}
