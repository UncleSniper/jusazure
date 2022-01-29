package org.unclesniper.azure;

import java.util.Locale;
import java.time.ZoneId;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import org.unclesniper.util.http.HTTPRequestHeaderSink;

import static org.unclesniper.util.ArgUtils.notNull;

public class HTTPUtils {

	private static final DateTimeFormatter HTTP_TIMESTAMP_FORMAT
			= DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH).withZone(ZoneId.of("GMT"));

	private HTTPUtils() {}

	public static void setAPIVersion(HTTPRequestHeaderSink request, APIVersion version) {
		notNull(request, "request").setRequestHeader("X-MS-Version",
				(version == null ? APIVersion.DEFAULT : version).getHeaderValue());
	}

	public static String getRequestTimestamp() {
		return HTTPUtils.HTTP_TIMESTAMP_FORMAT.format(Instant.now());
	}

	public static void setRequestTimestamp(HTTPRequestHeaderSink request) {
		notNull(request, "request").setRequestHeader("X-MS-Date", HTTPUtils.getRequestTimestamp());
	}

}
