package maimai.app.cas.sso.validation;


import java.util.List;

import org.jasig.cas.client.util.XmlUtils;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.InvalidProxyChainTicketValidationException;
import org.jasig.cas.client.validation.ProxyList;
import org.jasig.cas.client.validation.TicketValidationException;

/**
 * Extension to the traditional Service Ticket validation that will validate service tickets and proxy tickets.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public class Cas20ProxyTicketValidator extends Cas20ServiceTicketValidator {

    private boolean acceptAnyProxy;

    /** This should be a list of an array of Strings */
    private ProxyList allowedProxyChains = new ProxyList();

    public Cas20ProxyTicketValidator(final String casServerUrlPrefix) {
        super(casServerUrlPrefix);
    }

    public ProxyList getAllowedProxyChains() {
        return this.allowedProxyChains;
    }

    public String getUrlSuffix() {
        return "proxyValidate";
    }

    public void customParseResponse(final String response, final Assertion assertion) throws TicketValidationException {
        final List<String> proxies = XmlUtils.getTextForElements(response, "proxy");
        final String[] proxiedList = proxies.toArray(new String[proxies.size()]);

        // this means there was nothing in the proxy chain, which is okay
        if (proxies.isEmpty() || this.acceptAnyProxy) {
            return;
        }

        if (this.allowedProxyChains.contains(proxiedList)) {
            return;
        }

        throw new InvalidProxyChainTicketValidationException("Invalid proxy chain: " + proxies.toString());
    }

    public void setAcceptAnyProxy(final boolean acceptAnyProxy) {
        this.acceptAnyProxy = acceptAnyProxy;
    }

    public void setAllowedProxyChains(final ProxyList allowedProxyChains) {
        this.allowedProxyChains = allowedProxyChains;
    }
}
