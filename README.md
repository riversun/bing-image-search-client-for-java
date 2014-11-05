# Overview
Bing Image Search API client for java

It is licensed under [MIT](https://opensource.org/licenses/MIT).

## Quick Start

```java
package com.example;

import org.riversun.bing.client.v5.image_search.*;
import org.riversun.bing.client.v5.image_search.model.*;

public class Example {

	public static void main(String[] args) {

		String apiKey = "[YOUR_API_KEY_OF_BING_SEARCH]";

		BingImageSearchClient client = new BingImageSearchClient(apiKey);

		BingImageSearchResponse response = null;

		try {
			response = client.searchImage(
					new BingImageSearchRequest()
					// keyword
					.keyword.set("the statue of Liberty")
					// from
					.offset.set(0)
					// num of request
					.count.set(5)
					// ex."en-US" "ja-JP"
					.mkt.set("en-US")
					// ex."Strict" "Moderate" "Off"
					.safeSearch.set("Moderate")
					// ex."Small" "Medium" "Large" "Wallpaper" "All"
					.size.set("All")
					);

			for (Image img : response.value) {
				System.out.println("image url=" + img.contentUrl);
			}

		} catch (BingImageSearchException e) {
			e.printStackTrace();
		}

	}
}

```

## How to get your API_KEY

- Step1. Access https://portal.azure.com
- Step2. Click "+" mark on the left top.
- Step3. Enter "Cognitive Services APIs" in the search box.
- Step4. Select "Cognitive Services APIs" and "Create" it.
- Step5. Create window will be shown and you can select "Bing Search APIs" in the "API type" selection box.
- Step6. After entering required items, the resource will be created.
- Step7. Select resource you created, and click "Keys" in the RESOURCE MANAGEMENT category.


#Downloads
## maven
- Add dependencies to maven pom.xml file.
```xml
<dependency>
 <groupId>org.riversun</groupId>
 <artifactId>bing-image-search-client</artifactId>
 <version>0.1.0</version>
</dependency>
```



