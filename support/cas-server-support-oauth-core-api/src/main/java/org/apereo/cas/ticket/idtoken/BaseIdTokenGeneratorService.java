package org.apereo.cas.ticket.idtoken;

import org.apereo.cas.support.oauth.web.endpoints.OAuth20ConfigurationContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jose4j.jwt.JwtClaims;
import org.springframework.beans.factory.ObjectProvider;

/**
 * This is {@link BaseIdTokenGeneratorService}.
 *
 * @author Misagh Moayyed
 * @since 6.0.0
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
@Getter
public abstract class BaseIdTokenGeneratorService<T extends OAuth20ConfigurationContext> implements IdTokenGeneratorService {
    private final ObjectProvider<T> configurationContextProvider;

    protected T getConfigurationContext() {
        return this.configurationContextProvider.getObject();
    }

    protected String encodeAndFinalizeToken(final JwtClaims claims, final IdTokenGenerationContext context) throws Throwable {

        LOGGER.debug("Received claims for the id token [{}] as [{}]", context.getAccessToken(), claims);
        val idTokenResult = getConfigurationContext().getIdTokenSigningAndEncryptionService().encode(context.getRegisteredService(), claims);
        context.getAccessToken().setIdToken(idTokenResult);
        LOGGER.debug("Updating access token [{}] in ticket registry with ID token [{}]", context.getAccessToken().getId(), idTokenResult);
        if (context.getAccessToken().getExpiresIn() > 0) {
            getConfigurationContext().getTicketRegistry().updateTicket(context.getAccessToken());
        }
        return idTokenResult;
    }
}
