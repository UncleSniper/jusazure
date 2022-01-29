package org.unclesniper.azure;

import java.io.IOException;
import org.unclesniper.oauth2.TokenSource;
import org.unclesniper.util.http.HTTPRequestOptions;
import org.unclesniper.oauth2.AuthorizationException;
import org.unclesniper.util.http.HTTPRequestCustomizer;

import static org.unclesniper.util.ArgUtils.notNull;

public class BearerTokenHTTPRequestCustomizer implements HTTPRequestCustomizer {

	private TokenSource tokenSource;

	public BearerTokenHTTPRequestCustomizer() {}

	public BearerTokenHTTPRequestCustomizer(TokenSource tokenSource) {
		this.tokenSource = tokenSource;
	}

	public TokenSource getTokenSource() {
		return tokenSource;
	}

	public void setTokenSource(TokenSource tokenSource) {
		this.tokenSource = tokenSource;
	}

	@Override
	public void customizeRequest(HTTPRequestOptions request) throws IOException {
		if(tokenSource == null)
			throw new IllegalStateException("No TokenSource has been configured");
		String token;
		try {
			token = tokenSource.getToken();
		}
		catch(AuthorizationException ae) {
			throw new CannotObtainAccessTokenIOException(ae);
		}
		notNull(request, "request").setRequestHeader("Authorization", "Bearer " + token);
	}

}
