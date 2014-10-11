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
package org.riversun.bing.client.v5.image_search.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.riversun.string_grabber.StringGrabber;

/**
 * Request params for Bing Image Search API
 * <p>
 * {@see "https://msdn.microsoft.com/en-us/library/dn760791.aspx"}
 *
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class BingImageSearchRequest {

	public static class Param<T> {

		private String mQueryKey;
		private T mValue;
		private final BingImageSearchRequest mRequest;

		public Param(BingImageSearchRequest req, String key, T value) {
			req.mParamList.add(this);
			mRequest = req;
			mQueryKey = key;
			mValue = value;
		}

		public BingImageSearchRequest set(T value) {
			mValue = value;
			return mRequest;
		}

		private String toQueryString() {

			if (mValue == null) {
				return "";
			}

			String encoded = String.valueOf(mValue);
			try {
				encoded = URLEncoder.encode(String.valueOf(mValue), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return mQueryKey + "=" + encoded;
		}

	}

	private final List<Param<?>> mParamList = new ArrayList<Param<?>>();

	/**
	 * The user's search query string. The query string cannot be empty. The
	 * query string may contain Bing Advanced Operators. For example, to limit
	 * images to a specific domain, use the site: operator.
	 */
	public Param<String> keyword = new Param<String>(BingImageSearchRequest.this, "q", null);

	/**
	 * The number of images to return in the response. The actual number
	 * delivered may be less than requested. <br>
	 * The default is 35. The maximum value is 150.
	 */
	public Param<Integer> count = new Param<Integer>(BingImageSearchRequest.this, "count", null);

	/**
	 * The zero-based offset that indicates the number of images to skip before
	 * returning images. The default is 0. The offset should be less than
	 * (totalEstimatedMatches - count).
	 */
	public Param<Integer> offset = new Param<Integer>(BingImageSearchRequest.this, "offset", null);
	/**
	 * The market where the results come from. Typically, this is the country
	 * where the user is making the request from; however, it could be a
	 * different country if the user is not located in a country where Bing
	 * delivers results. The market must be in the form <language code>-<country
	 * code>. For example, en-US. The string is case insensitive. For a list of
	 * possible values that you may specify, {@see Market
	 * Codes."https://msdn.microsoft.com/en-us/library/dn783426.aspx"}
	 * <p>
	 * like<br>
	 * en-US<br>
	 * ja-JP<br>
	 */
	public Param<String> mkt = new Param<String>(BingImageSearchRequest.this, "mkt", null);
	/**
	 * Filter images for adult content. The following are the possible filter
	 * values.<br>
	 * 
	 * Off — Return images with adult content. The Image API response will
	 * include thumbnail images that are clear (non-fuzzy); however, the Search
	 * API response will include thumbnail images that are pixelated (fuzzy).<br>
	 * 
	 * Moderate — For Image API requests, do not return images with adult
	 * content. For Search API requests, return images with adult content (the
	 * thumbnail images will be pixelated (fuzzy)).<br>
	 * 
	 * Strict — Do not return images with adult content.<br>
	 * 
	 * If not specified, the default is Moderate.<br>
	 */
	public Param<String> safeSearch = new Param<String>(BingImageSearchRequest.this, "safeSearch", null);

	/**
	 * Filter images by size. The following are the possible filter values.<br>
	 * <p>
	 * Small — Return images that are less than 200x200 pixels<br>
	 * 
	 * Medium — Return images that are greater than or equal to 200x200 pixels
	 * but less than 500x500 pixels<br>
	 * 
	 * Large — Return images that are 500x500 pixels or larger<br>
	 * 
	 * Wallpaper — Return wallpaper images.<br>
	 * 
	 * All — Do not filter by size. Specifying this value is the same as not
	 * specifying the size parameter.<br>
	 * 
	 * You may use this parameter in conjunction with the height or width
	 * parameters. For example, you may use height and size to request small
	 * images that are at least 150 pixels tall.
	 */
	public Param<String> size = new Param<String>(BingImageSearchRequest.this, "size", null);

	public String toQueryString() {

		final StringGrabber sg = new StringGrabber();

		for (Param<?> param : mParamList) {
			sg.append(param.toQueryString());
			sg.append("&");
		}
		sg.removeTail();

		return sg.toString();
	}

}
