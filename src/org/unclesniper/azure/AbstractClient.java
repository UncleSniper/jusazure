package org.unclesniper.azure;

import java.io.IOException;
import org.unclesniper.util.IOUtils;
import org.unclesniper.util.http.HTTPVerb;
import org.unclesniper.util.http.HTTPClient;
import org.unclesniper.util.http.HTTPRequest;
import org.unclesniper.util.http.HTTPResponse;
import org.unclesniper.util.http.HTTPRequestCustomizer;
import org.unclesniper.util.http.URLConnectionHTTPClient;

public abstract class AbstractClient {

	protected enum AlternateStatus {

		CREATED(201);

		private final int statusCode;

		private AlternateStatus(int statusCode) {
			this.statusCode = statusCode;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public boolean isAcceptable(int actualCode) {
			return statusCode > 0 && actualCode == statusCode;
		}

	}

	private static final HTTPClient DEFAULT_HTTP_CLIENT = new URLConnectionHTTPClient();

	private HTTPClient httpClient;

	private HTTPRequestCustomizer authorization;

	public AbstractClient() {}

	public HTTPClient getHTTPClient() {
		return httpClient;
	}

	public void setHTTPClient(HTTPClient httpClient) {
		this.httpClient = httpClient;
	}

	public HTTPRequestCustomizer getAuthorization() {
		return authorization;
	}

	public void setAuthorization(HTTPRequestCustomizer authorization) {
		this.authorization = authorization;
	}

	protected HTTPRequest makeHTTPRequest(HTTPVerb verb, String url) throws IOException {
		HTTPRequest request = (httpClient == null ? AbstractClient.DEFAULT_HTTP_CLIENT : httpClient)
				.request(verb, url);
		if(authorization != null)
			authorization.customizeRequest(request);
		return request;
	}

	protected String getResponseBody(HTTPResponse response) throws IOException {
		return IOUtils.toString(response.getResponseBody(), IOUtils.safeCharset(response.getContentCharset()));
	}

	protected void requireResponseStatus(HTTPResponse response, AlternateStatus alternateStatus, String responseBody)
			throws UnexpectedResponseStatusException, IOException {
		int actualCode = response.getResponseCode();
		if(actualCode != 200 && (alternateStatus == null || !alternateStatus.isAcceptable(actualCode)))
			throw new UnexpectedResponseStatusException(actualCode,
					responseBody == null ? getResponseBody(response) : responseBody);
	}

}
