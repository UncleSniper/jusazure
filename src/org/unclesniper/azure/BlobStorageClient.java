package org.unclesniper.azure;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.unclesniper.util.IOUtils;
import org.unclesniper.util.http.HTTPVerb;
import org.unclesniper.util.http.HTTPRequest;
import org.unclesniper.util.http.HTTPResponse;

import static org.unclesniper.util.ArgUtils.notNull;

public class BlobStorageClient extends AbstractClient {

	private String storageAccount;

	public BlobStorageClient() {}

	public BlobStorageClient(String storageAccount) {
		this.storageAccount = storageAccount;
	}

	public String getStorageAccount() {
		return storageAccount;
	}

	public void setStorageAccount(String storageAccount) {
		this.storageAccount = storageAccount;
	}

	@Override
	protected HTTPRequest makeHTTPRequest(HTTPVerb verb, String url) throws IOException {
		HTTPRequest request = super.makeHTTPRequest(verb, url);
		HTTPUtils.setAPIVersion(request, null);
		HTTPUtils.setRequestTimestamp(request);
		request.setRequestHeader("Accept", "*/*");
		return request;
	}

	protected HTTPRequest makeBlobRequest(HTTPVerb verb, String path) throws IOException {
		if(storageAccount == null || storageAccount.length() == 0)
			throw new IllegalStateException("Storage account name has not been configured");
		StringBuilder builder = new StringBuilder();
		builder.append("https://");
		builder.append(storageAccount);
		builder.append(".blob.core.windows.net");
		if(path.length() == 0 || path.charAt(0) != '/')
			builder.append('/');
		builder.append(path);
		return makeHTTPRequest(verb, builder.toString());
	}

	public void putBlob(String path, InputStream content) throws IOException, AzureException {
		notNull(path, "path");
		notNull(content, "content");
		HTTPRequest request = makeBlobRequest(HTTPVerb.PUT, path);
		request.setRequestHeader("X-MS-Blob-Type", "BlockBlob");
		request.setRequestHeader("Content-Length", "0");
		request.setRequestBody(content);
		try(HTTPResponse response = request.request()) {
			String responseBody = getResponseBody(response);
			requireResponseStatus(response, AlternateStatus.CREATED, responseBody);
		}
	}

	public void getBlob(String path, OutputStream sink, long startByte, long endByte)
			throws IOException, AzureException {
		notNull(path, "path");
		notNull(sink, "sink");
		if(startByte >= 0L && endByte >= 0L && endByte < startByte)
			throw new IllegalArgumentException("Range end is lower than range start: "
					+ endByte + " < " + startByte);
		if(endByte >= 0L && startByte < 0L)
			startByte = 0L;
		HTTPRequest request = makeBlobRequest(HTTPVerb.GET, path);
		if(startByte >= 0L) {
			StringBuilder range = new StringBuilder();
			range.append("bytes=");
			range.append(String.valueOf(startByte));
			range.append('-');
			if(endByte >= 0L)
				range.append(String.valueOf(endByte));
			request.setRequestHeader("X-MS-Range", range.toString());
		}
		try(HTTPResponse response = request.request()) {
			requireResponseStatus(response, null, null);
			IOUtils.copy(response.getResponseBody(), sink);
		}
	}

	public void getBlob(String path, OutputStream sink) throws IOException, AzureException {
		getBlob(path, sink, -1L, -1L);
	}

}
