package org.unclesniper.azure;

public enum APIVersion {

	V20171109("2017-11-09");

	public static final APIVersion DEFAULT = APIVersion.V20171109;

	private final String headerValue;

	private APIVersion(String headerValue) {
		this.headerValue = headerValue;
	}

	public String getHeaderValue() {
		return headerValue;
	}

}
