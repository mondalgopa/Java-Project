package com.takehomeproj;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.google.gson.Gson;

public class WebCrawler {

	static Set<String> duplicateUrls = new HashSet<String>();
	static Set<String> errorUrls = new HashSet<String>();
	static int j = 100;// this is to put a limit to avoid forceful stop of crawling

	public static void main(String[] args) {
		// String inputURL =
		// "http://mrbool.com/how-to-create-a-web-crawler-and-storing-data-using-java/28925";//
		System.out.print("Enter a URL: ");
		Scanner myInput = new Scanner(System.in);
		String inputURL = myInput.nextLine();
		System.out.println("User Input: " + inputURL);

		List<String> dynamicList = new LinkedList<String>();
		List successList = new ArrayList<String>();
		List<String> totalUrls = new LinkedList<String>();
		if (inputURL != "" && inputURL.contains("http"))
			totalUrls = crawl(dynamicList, inputURL);
		dynamicList.addAll(totalUrls);

		// To call the child URLs and add them dynamically
		while (!dynamicList.isEmpty() && dynamicList.size() < j) {
			successList.addAll(dynamicList);
			dynamicList.addAll(crawl(dynamicList, dynamicList.remove(0)));
		}

		System.out.println("Success:" + convertStringtoJson(successList));
		System.out.println("Skipped:" + convertStringtoJson(duplicateUrls));
		System.out.println("Error:" + convertStringtoJson(errorUrls));
	}

	// Crawl method to move forward and find the http links
	public static List<String> crawl(List childList, String inputURL) {
		List<String> totalUrls = new LinkedList<String>();
		String tmp = "";
		try {
			URL url = new URL(inputURL);
			Scanner in = new Scanner(url.openStream());

			while (in.hasNext()) {
				String line = in.next();
				if (line.contains("href=\"http://") || line.contains("href=\"https://")) {
					int from = line.indexOf("\"");
					int to = line.lastIndexOf("\"");
					tmp = line.substring(from + 1, to);
					if (childList != null && !childList.contains(tmp)) {
						if (!totalUrls.contains(tmp) ? totalUrls.add(tmp) : duplicateUrls.add(tmp))
							;
					}
				}
			}

		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException is: " + e);
			if (!errorUrls.contains(tmp) ? errorUrls.add(tmp) : errorUrls.add(tmp))
				;

		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException is: " + e);
			if (!errorUrls.contains(tmp) ? errorUrls.add(tmp) : errorUrls.add(tmp))
				;
		} catch (IOException e) {
			System.out.println("IOException is: " + e);
			if (!errorUrls.contains(tmp) ? errorUrls.add(tmp) : errorUrls.add(tmp))
				;
		} catch (Exception e) {
			System.out.println("Exception is: " + e);
			if (!errorUrls.contains(tmp) ? errorUrls.add(tmp) : errorUrls.add(tmp))
				;
		}
		return totalUrls;
	}
	
	private static String convertStringtoJson(Collection<String> input) {
		Gson gson = new Gson();
		String json = gson.toJson(input);
		return json;
	}
}
