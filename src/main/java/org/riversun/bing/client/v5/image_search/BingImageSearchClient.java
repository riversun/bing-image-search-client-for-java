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
package org.riversun.bing.client.v5.image_search;

import java.io.IOException;
import java.util.logging.Logger;

import org.riversun.bing.client.v5.image_search.http.LiteHttpClient;
import org.riversun.bing.client.v5.image_search.http.LiteHttpClientException;
import org.riversun.bing.client.v5.image_search.model.BingImageSearchRequest;
import org.riversun.bing.client.v5.image_search.model.BingImageSearchResponse;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Bing Image Search API client for Java
 * 
 * {@link "https://www.microsoft.com/cognitive-services/en-us/documentation"}
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 * 
 */
public class BingImageSearchClient extends LiteHttpClient {

	private static final Logger LOGGER = Logger.getLogger(BingImageSearchClient.class.getName());

	private final String mApikey;
	private final ObjectMapper mJackson = new ObjectMapper();

	/**
	 * Client for Bing Image Search
	 * <p>
	 * API document
	 * {@link "https://www.microsoft.com/cognitive-services/en-us/bing-image-search-api"}
	 * <p>
	 * Pricing
	 * {@link "https://www.microsoft.com/cognitive-services/en-us/pricing"}
	 * <p>
	 * How to create API Keys. <br>
	 * <br>
	 * How to get key(s) for Bing Image Search API <br>
	 * Step1. Access https://portal.azure.com<br>
	 * Step2. Click "+" mark on the left top.<br>
	 * Step3. Enter "Cognitive Services APIs" in the search box.<br>
	 * Step4. Select "Cognitive Services APIs" and "Create" it.<br>
	 * Step5. Create window will be shown and you can select "Bing Search APIs"
	 * in the "API type" selection box.<br>
	 * 
	 * Step6. After entering required items, the resource will be created.<br>
	 * Step7. Select resource you created, and click "Keys" in the RESOURCE
	 * MANAGEMENT category.<br>
	 * 
	 * Step8. Check key for Bing Search API.<br>
	 * <br>
	 */
	public BingImageSearchClient(String apikey) {
		super();
		mApikey = apikey;
	}

	/**
	 * Search images
	 * 
	 * @param keyword
	 * @return
	 * @throws BingImageSearchException
	 * @throws Exception
	 */
	public BingImageSearchResponse searchImage(BingImageSearchRequest searchReq) throws BingImageSearchException {

		LOGGER.fine("searchRequest=" + searchReq);

		addHeader("Ocp-Apim-Subscription-Key", mApikey);

		open("GET", "https://api.cognitive.microsoft.com/bing/v5.0/images/search?" + searchReq.toQueryString());

		String responseText;

		try {
			responseText = send();
		} catch (LiteHttpClientException e1) {
			final BingImageSearchException e2 = new BingImageSearchException(e1.getMessage(), e1);
			throw e2;
		}

		BingImageSearchResponse bingResponse = null;

		try {
			bingResponse = mJackson.readValue(responseText, BingImageSearchResponse.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bingResponse;

	}

}
