package org.unclesniper.azure;

public class AzureException extends Exception {

	private final String serverResponse;

	public AzureException(String message, String serverResponse) {
		super(message);
		this.serverResponse = serverResponse;
	}

	public AzureException(String message, String serverResponse, Throwable cause) {
		super(message, cause);
		this.serverResponse = serverResponse;
	}

	public String getServerResponse() {
		return serverResponse;
	}

}
