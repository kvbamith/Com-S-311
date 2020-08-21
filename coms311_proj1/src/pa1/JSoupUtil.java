package pa1;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Some simple methods for executing the necessary functionality of the JSoup Library.
 * 
 * @author Steven Rein and Amith Kopparapu Venkata Boja
 */
public class JSoupUtil {
	
	/** 
	 * Gets the document of the given url.
	 * @param url
	 * @return A Document containing the HTML from the webpage.
	 */
	public static Document getDocument(String url) {
		try {
			return Jsoup.connect(url).get();
		} catch (UnsupportedMimeTypeException umte) {
			return null;
		} catch (HttpStatusException hse) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Finds all of the links in a Document.
	 * @param document
	 * @return An object of Elements containing all links on a page.
	 */
	public static Elements getElements(Document document) {
		Elements elements = document.select("a[href]");
		if (elements == null) {
			return new Elements();
		}
		return elements;
	}
	
	/**
	 * Takes an Element element provided by the Elements object and give back a regular string.
	 * @param element
	 * @return String that can be used as a URL.
	 */
	public static String getAbsoluteUrl(Element element) {
		return element.attr("abs:href");
	}
	
	/**
	 * Used to follow the politeness policy specified by the project spec. Stops for 3 seconds.
	 */
	public static void bePolite() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException ignore) {}
	}
	
	/**
	 * Gets a very long string that contains all of the words that are within the "body" tag of them webpage.
	 * @param url
	 * @return Very long string with all the words on a page.
	 */
	public static String getBodyText(String url) {
		Document doc = getDocument(url);
		if (doc == null) {
			return null;
		}
		return getDocument(url).body().text();
	}

}
