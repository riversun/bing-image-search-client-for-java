/**
 * Copyright 2016-2017 Tom Misawa, riversun.org@gmail.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"), to deal in the 
 * Software without restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the 
 * Software, and to permit persons to whom the Software is furnished to do so, 
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all 
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 *  INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR 
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package org.riversun.bing.client.v5.image_search.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * HTTP client
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 * 
 */
public class LiteHttpClient {

	private static final Logger LOGGER = Logger.getLogger(LiteHttpClient.class.getName());

	private Map<String, String> mHeaders = new LinkedHashMap<String, String>();

	private String mMethod = "POST";
	private String mEndpoint = "";
	private String mUserAgent = null;

	public LiteHttpClient() {

	}

	/**
	 * Add http headers
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public LiteHttpClient addHeader(String key, String value) {
		mHeaders.put(key, value);
		return LiteHttpClient.this;
	}

	public LiteHttpClient setUserAgent(String ua) {
		mUserAgent = ua;
		return LiteHttpClient.this;
	}

	public LiteHttpClient open(String method, String url) {
		mMethod = method;
		mEndpoint = url;
		return LiteHttpClient.this;
	}

	/**
	 * Send(Connect) to endpoint
	 * 
	 * @return
	 * @throws LiteHttpClientException
	 */
	public String send() throws LiteHttpClientException {
		return send(null);
	}

	/**
	 * Send string to endpoint
	 * 
	 * @param sendString
	 * @return
	 * @throws LiteHttpClientException
	 */
	public String send(String sendString) throws LiteHttpClientException {

		LOGGER.fine("sendString=" + sendString);

		URL url = null;

		PrintWriter pw = null;
		BufferedWriter bw = null;
		OutputStreamWriter osw = null;
		OutputStream os = null;

		BufferedReader br = null;
		InputStreamReader isr = null;
		InputStream is = null;
		HttpURLConnection con = null;

		try {

			url = new URL(mEndpoint);

			con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);

			if (mUserAgent != null) {
				con.setRequestProperty("User-Agent", mUserAgent);
			}
			for (String key : mHeaders.keySet()) {
				con.setRequestProperty(key, mHeaders.get(key));
			}
			con.setRequestMethod(mMethod);
			con.setInstanceFollowRedirects(false);
			con.connect();

			os = con.getOutputStream();
			osw = new OutputStreamWriter(os, "UTF-8");
			bw = new BufferedWriter(osw);
			pw = new PrintWriter(bw);

			if (sendString != null) {
				// send request
				pw.print(sendString);
			}
			pw.flush();

			is = con.getInputStream();
			isr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(isr);

			final StringBuilder sb = new StringBuilder();

			// receive response
			for (String line; (line = br.readLine()) != null;) {
				sb.append(line);
			}

			final String responseText = sb.toString();
			
			LOGGER.fine("responseText="+responseText);
			
			return responseText;

		} catch (MalformedURLException e) {

		} catch (IOException e) {
			// when network error occurred

			if (con != null) {

				try {
					final int responseCode = con.getResponseCode();

					final String errorMessage;

					if (responseCode >= 500) {
						// >=500
						errorMessage = getFromStream(con.getInputStream());
						throw new LiteHttpClientException(e);
					} else {
						// >=400
						errorMessage = getFromStream(con.getErrorStream());
						throw new LiteHttpClientException(errorMessage, e);
					}

				} catch (IOException e1) {
				}

			}

		} finally {

			if (pw != null) {
				pw.close();
			}

			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
				}
			}
			if (osw != null) {
				try {
					osw.close();
				} catch (IOException e) {
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}

		}
		return null;

	}

	private String getFromStream(InputStream is) {

		BufferedReader br = null;
		InputStreamReader isr = null;
		final StringBuilder sb = new StringBuilder();

		try {

			isr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(isr);

			for (String line; (line = br.readLine()) != null;) {
				sb.append(line);
			}
		} catch (Exception e) {

		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		return sb.toString();

	}

}
