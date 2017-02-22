package maimai.app.cas.sso.validation;


import java.net.URL;

import org.jasig.cas.client.util.CommonUtils;

/**
 * Abstract class that knows the protocol for validating a CAS ticket.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public abstract class AbstractCasProtocolUrlBasedTicketValidator extends AbstractUrlBasedTicketValidator {

    public AbstractCasProtocolUrlBasedTicketValidator(final String casServerUrlPrefix) {
        super(casServerUrlPrefix);
    }

    public final void setDisableXmlSchemaValidation(final boolean disable) {
        // nothing to do
    }

    /**
     * Retrieves the response from the server by opening a connection and merely reading the response.
     */
    public final String retrieveResponseFromServer(final URL validationUrl, final String ticket) {
        if (this.hostnameVerifier != null) {
	        return CommonUtils.getResponseFromServer(validationUrl, this.hostnameVerifier, getEncoding());
        } else {
	        return CommonUtils.getResponseFromServer(validationUrl, getEncoding());
        }
    }
}
