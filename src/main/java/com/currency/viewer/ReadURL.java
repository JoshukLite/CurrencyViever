package com.currency.viewer;
import java.io.*;
import java.net.*;
import java.util.regex.*;
import java.util.*;

import org.jsoup.*;

class ReadURL {
	private String destUrl;
	// Holds loaded HTML code in ArrayList:
	private List<String> loadedHTML = null;
	// If "destURL" was changed, next time you call
	// "getHTMLpiece" new HTML code will be loaded
	// in ArrayList:
	private boolean changedURL = false;
	ReadURL(String destUrl) {
		this.destUrl = destUrl;
	}
	// This method loads HTML from URL, that passed
	// in argument. Increases reading data speed unlike
	// if HTML code was loaded every time you call
	// "getHTMLpiece" method.
	private void loadHTML(String destUrl) throws MalformedURLException, 
		UnknownHostException, NoRouteToHostException, IOException {
		URL url = new URL(destUrl);
		URLConnection con = url.openConnection();
		InputStream is =  con.getInputStream();
		try {
			// Create reader to read data from
			// site line by line
			BufferedReader br = new BufferedReader(
				new InputStreamReader(is));
			String line = null;
			if(loadedHTML == null) {
				loadedHTML = new ArrayList<String>();
			}	else {
				loadedHTML.clear();
			}
			while((line = br.readLine()) != null) {
				loadedHTML.add(line);
			}
		}	finally {
			is.close();
		}
	}
	// Returns a piece of HTML code 
	private String getHTMLpiece(String destUrl, 
	String startLine, String endLine) throws
	UnknownHostException, NoRouteToHostException {
		// Make an URL to the web page
		StringBuilder sb = new StringBuilder();
		try {
			// If you will call "setDestURL", than next
			// time you'll call this method, new HTML code
			// will be loaded in memory
			if(loadedHTML == null || changedURL) {
				loadHTML(destUrl);
				changedURL = false;
			}
			boolean searchFor = false;
			// Search for 
			// Capture a grooup of HTML code
			// that lies between startLine and endLine
			// (note sartLine is captired, endLine is not):
			for(String line : loadedHTML) {
				if(line.contains(startLine)) {
					searchFor = true;
				}	else if(line.contains(endLine)) {
					searchFor = false;
				}
				if(searchFor) {
					sb.append(line);
				}
			}
		}	catch(MalformedURLException e) {
			throw new RuntimeException("Wrong site name", e);
		}	catch(IOException e) {
			if(e instanceof UnknownHostException) {
				throw new UnknownHostException();
			}	else if(e instanceof NoRouteToHostException) {
				throw new NoRouteToHostException();
			}	else {
				throw new RuntimeException(e);
			}
		}
		return sb.toString();
	}
	/* 
	 * This method returns array of strings with buy and sell prices
	 * from one bank, you can specify, "statLine" - poit where you
	 * starts searching for data you need in HTML doc and
	 * "endLine" - point where ends searching for data ou need.
	 * "regex" - create regex with two groups: first group - 
	 * buying price; second group - selling price.
	 * UPD: You can also use regex with one group, if you
	 * want to get only one value
	 */
	String[] getCurrentXR(String startLine, 
	String endLine, String regex) throws 
	UnknownHostException, NoRouteToHostException {
		String price = getHTMLpiece(
			destUrl, startLine, endLine);
		// Create a pattern to find price of currency:
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(price);
		String[] usd = null;
		// Search for buy and sell prices
		// or only for one price:
		if(matcher.groupCount() == 2) {
			while(matcher.find()) {
				usd = new String[] { matcher.group(1), matcher.group(2) };
				return usd;
			}
		} else if(matcher.groupCount() == 1) {
			while(matcher.find()) {
				usd = new String[] { matcher.group(1) };
				return usd;
			}
		}
		return usd;
	}
	void setDestUrl(String destUrl) {
		if(!this.destUrl.equals(destUrl)) {
			this.destUrl = destUrl;
			changedURL = true;
		}
	}
	// Refresh stored HTML code:
	void reloadHTML() throws UnknownHostException, 
	NoRouteToHostException {
		try {
			loadHTML(destUrl);
		} catch(MalformedURLException e) {
			throw new RuntimeException("Wrong site name", e);
		}	catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	void releaseHTML() {
		loadedHTML = null;
	}
}
