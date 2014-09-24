package net.dpkm.psm.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class FetchHtmlUtil {

	private static FetchHtmlUtil instance = null;

	private TrustManager[] trustAllCerts = null;

	private FetchHtmlUtil() {
	}

	private static TrustManager[] getTrustManager() {
		return FetchHtmlUtil.getInstance().trustAllCerts;
	}

	private static synchronized FetchHtmlUtil getInstance() {
		if (instance == null) {
			instance = new FetchHtmlUtil();

			// Create a trust manager that does not validate certificate chains
			instance.trustAllCerts = new TrustManager[] { new X509TrustManager() {
				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] certs,
						String authType) {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] certs,
						String authType) {
				}
			} };
		}
		return instance;
	}

	public static String getHtmlFromUrlString(String urlString) {
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			System.out.println("illegal url: [" + urlString + "]");
		}
		return getHtmlFromUrl(url);
	}

	public static String getHtmlFromUrl(URL url) {
		try {
			if (url.getProtocol().equals("http")) {
				return getByHttpProtocol(url);
			} else if (url.getProtocol().equals("https")) {
				return getByHttpsProtocol(url);
			}
		} catch (Exception e) {
			System.out.println("fetch html resource failed from [" + url + "]");
		}
		return "";
	}

	/**
	 * reference
	 * http://www.nakov.com/blog/2009/07/16/disable-certificate-validation
	 * -in-java-ssl-connections/
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private static String getByHttpsProtocol(URL url) throws Exception {
		StringBuilder sb = new StringBuilder();

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, getTrustManager(), new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		URLConnection con = url.openConnection();
		Reader reader = new InputStreamReader(con.getInputStream());
		while (true) {
			int ch = reader.read();
			if (ch == -1)
				break;
			sb.append((char) ch);
		}
		return sb.toString();
	}

	/**
	 * reference http://www.javaworld.com.tw/jute/post/view?bid=29&id=252512
	 * 
	 * @param url
	 * @return
	 */
	private static String getByHttpProtocol(URL url) throws Exception {
		StringBuilder sb = new StringBuilder();
		String line = null;
		Object obj = url.getContent();
		InputStreamReader isr = new InputStreamReader((InputStream) obj,
				"UTF-8");
		BufferedReader br = new BufferedReader(isr);
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		return sb.toString();
	}
}