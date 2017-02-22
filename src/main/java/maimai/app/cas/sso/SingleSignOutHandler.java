package maimai.app.cas.sso;


import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jasig.cas.client.session.HashMapBackedSessionMappingStorage;
import org.jasig.cas.client.session.SessionMappingStorage;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.XmlUtils;

public final class SingleSignOutHandler {

    /** Logger instance */
    private final Log log = LogFactory.getLog(getClass());

    /** Mapping of token IDs and session IDs to HTTP sessions */
    private SessionMappingStorage sessionMappingStorage = new HashMapBackedSessionMappingStorage();
    
    /** The name of the artifact parameter.  This is used to capture the session identifier. */
    private String artifactParameterName = "ticket";

    /** Parameter name that stores logout request */
    private String logoutParameterName = "logoutRequest";
    
    private String[] clusterNodeUrls = new String[0];
    
    private String logoutClusterNodesParameterName = "logoutClusterNodesRequest";

    public void setSessionMappingStorage(final SessionMappingStorage storage) {
        this.sessionMappingStorage = storage;
    }

    public SessionMappingStorage getSessionMappingStorage() {
        return this.sessionMappingStorage;
    }
    
    public String[] getClusterNodeUrls() {
		return clusterNodeUrls;
	}

	public void setClusterNodeUrls(String[] clusterNodeUrls) {
		this.clusterNodeUrls = clusterNodeUrls;
	}

	/**
     * @param name Name of the authentication token parameter.
     */
    public void setArtifactParameterName(final String name) {
        this.artifactParameterName = name;
    }

    /**
     * @param name Name of parameter containing CAS logout request message.
     */
    public void setLogoutParameterName(final String name) {
        this.logoutParameterName = name;
    }

    /**
     * Initializes the component for use.
     */
    public void init() {
        CommonUtils.assertNotNull(this.artifactParameterName, "artifactParameterName cannot be null.");
        CommonUtils.assertNotNull(this.logoutParameterName, "logoutParameterName cannot be null.");
        CommonUtils.assertNotNull(this.sessionMappingStorage, "sessionMappingStorage cannote be null."); 
    }
    
    public void init(String[] clusterNodeUrls){
    	init();
    	this.clusterNodeUrls = clusterNodeUrls;
    }
    
    /**
     * Determines whether the given request contains an authentication token.
     *
     * @param request HTTP reqest.
     *
     * @return True if request contains authentication token, false otherwise.
     */
    public boolean isTokenRequest(final HttpServletRequest request) {
        return CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, this.artifactParameterName));
    }

    /**
     * Determines whether the given request is a CAS logout request.
     *
     * @param request HTTP request.
     *
     * @return True if request is logout request, false otherwise.
     */
    public boolean isLogoutRequest(final HttpServletRequest request) {
        return "POST".equals(request.getMethod()) && !isMultipartRequest(request) &&
            CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, this.logoutParameterName));
    }

    /**
     * Associates a token request with the current HTTP session by recording the mapping
     * in the the configured {@link SessionMappingStorage} container.
     * 
     * @param request HTTP request containing an authentication token.
     */
    public void recordSession(final HttpServletRequest request) {
        final HttpSession session = request.getSession(true);

        final String token = CommonUtils.safeGetParameter(request, this.artifactParameterName);
        if (log.isDebugEnabled()) {
            log.debug("Recording session for token " + token);
        }

        try {
            this.sessionMappingStorage.removeBySessionById(session.getId());
        } catch (final Exception e) {
            // ignore if the session is already marked as invalid.  Nothing we can do!
        }
        sessionMappingStorage.addSessionById(token, session);
    }
   
    /**
     * Destroys the current HTTP session for the given CAS logout request.
     *
     * @param request HTTP request containing a CAS logout message.
     */
    public void destroySession(final HttpServletRequest request) {
        final String logoutMessage = CommonUtils.safeGetParameter(request, this.logoutParameterName);
        if (log.isTraceEnabled()) {
            log.trace ("Logout request:\n" + logoutMessage);
        }
        
        final String token = XmlUtils.getTextForElement(logoutMessage, "SessionIndex");
        if (CommonUtils.isNotBlank(token)) {
            final HttpSession session = this.sessionMappingStorage.removeSessionByMappingId(token);

            if (session != null) {
                String sessionID = session.getId();

                if (log.isDebugEnabled()) {
                    log.debug ("Invalidating session [" + sessionID + "] for token [" + token + "]");
                }
                try {
                	session.invalidate();
                } catch (final IllegalStateException e) {
                    log.debug("Error invalidating session.", e);
                }
            }
            destroySessionOfClusterNodes(token);
            
        }
    }


	private boolean isMultipartRequest(final HttpServletRequest request) {
        return request.getContentType() != null && request.getContentType().toLowerCase().startsWith("multipart");
    }
	
    private void destroySessionOfClusterNodes(final String token) {
		if(clusterNodeUrls != null && clusterNodeUrls.length > 0){
			for (String url : clusterNodeUrls) {
				if(StringUtils.isBlank(url)){
					continue;
				}
				HttpClient httpClient = new DefaultHttpClient();
				
				HttpPost httpPostReq = new HttpPost(url);
				ArrayList<NameValuePair> paramList = new ArrayList<NameValuePair>();
				paramList.add(new BasicNameValuePair(this.logoutClusterNodesParameterName, "true"));
				paramList.add(new BasicNameValuePair(this.artifactParameterName, token));
				
				try {
					httpPostReq.setEntity(new UrlEncodedFormEntity(paramList));
					httpClient.execute(httpPostReq);
				} catch (Exception e) {
					log.debug("Error destroySessionOfClusterNodes.",e);
				} finally {
					HttpClientUtils.closeQuietly(httpClient);
				}
			}
		}
	}
    
    public void destroySessionFromClusterNode(final HttpServletRequest request){
    	String token = request.getParameter(this.artifactParameterName);
    	if(CommonUtils.isNotBlank(token)){
    		final HttpSession session = sessionMappingStorage.removeSessionByMappingId(token);
    		if(session != null){
    			String sessionID = session.getId();
    			if(log.isDebugEnabled()){
    				log.debug("Invalidating session [" + sessionID + "] for token [" + token + "]");
    			}
    			try{
    				session.invalidate();
    			}catch(final IllegalStateException e){
    				log.debug("Error invalidating session.",e);
    			}
    		}
    	}
    }
    public boolean isLogoutRequestFromClusterNode(final HttpServletRequest request) {
        return "POST".equals(request.getMethod()) && !isMultipartRequest(request) &&
            CommonUtils.isNotBlank(safeGetParameter(request,this.logoutClusterNodesParameterName));
    }

    public String safeGetParameter(final HttpServletRequest request, final String parameter) {
        if ("POST".equals(request.getMethod()) && this.logoutClusterNodesParameterName.equals(parameter)) {
        	log.debug("safeGetParameter called on a POST HttpServletRequest for "+this.logoutClusterNodesParameterName+".  Cannot complete check safely.  Reverting to standard behavior for this Parameter");
            return request.getParameter(parameter);
        }
        return request.getQueryString() == null || request.getQueryString().indexOf(parameter) == -1 ? null : request.getParameter(parameter);       
    }

}