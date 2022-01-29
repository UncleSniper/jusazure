package org.unclesniper.azure;

public class UnexpectedResponseStatusException extends AzureException {

	private final int expectedStatus;

	private final int actualStatus;

	public UnexpectedResponseStatusException(int expectedStatus, int actualStatus, String serverResponse) {
		super("Expected HTTP " + expectedStatus + " response, but got HTTP " + actualStatus, serverResponse);
		this.expectedStatus = expectedStatus;
		this.actualStatus = actualStatus;
	}

	public UnexpectedResponseStatusException(int actualStatus, String serverResponse) {
		this(200, actualStatus, serverResponse);
	}

	public int getExpectedStatus() {
		return expectedStatus;
	}

	public int getActualStatus() {
		return actualStatus;
	}

}
