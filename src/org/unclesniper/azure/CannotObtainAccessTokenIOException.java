package org.unclesniper.azure;

import java.io.IOException;

public class CannotObtainAccessTokenIOException extends IOException {

	public CannotObtainAccessTokenIOException(Throwable cause) {
		super("Failed to obtain access token" + (cause == null || cause.getMessage() == null
				|| cause.getMessage().length() == 0 ? "" : ": " + cause.getMessage()), cause);
	}

}
