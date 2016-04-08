package com.ttnd.mailchimp.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.engine.SlingRequestProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.day.cq.commons.Externalizer;
import com.day.cq.contentsync.handler.util.RequestResponseFactory;
import com.day.cq.wcm.api.WCMMode;

@SlingServlet(paths = "/services/mailchimp/exportNewsletter")
public class ExportNewsletter extends SlingSafeMethodsServlet {

	@Reference
	Externalizer externalizer;

	@Reference
	private RequestResponseFactory requestResponseFactory;

	@Reference
	private SlingRequestProcessor requestProcessor;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		String myExternalizedUrl = null;

		/* The resource path to resolve. Use any selectors or extension. */
		String requestPath = "/content/campaigns/test.html";

		/* Setup request */
		HttpServletRequest req = requestResponseFactory.createRequest("GET", requestPath);
		WCMMode.DISABLED.toRequest(req);

		/* Setup response */
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		HttpServletResponse resp = requestResponseFactory.createResponse(out);

		/* Process request through Sling */
		requestProcessor.processRequest(req, resp, request.getResourceResolver());
		String html = out.toString();
		Document doc = Jsoup.parse(html);

		Elements links = doc.select("a[href]");
		Elements media = doc.select("[src]");
		Elements imports = doc.select("link[href]");

		for (Element src : media) {
			if (src.tagName().equals("img")) {
				myExternalizedUrl = externalizer.externalLink(request.getResourceResolver(), "mailchimp",
						src.attr("src"));
				src.attr("src", myExternalizedUrl);
			} else
			myExternalizedUrl = externalizer.externalLink(request.getResourceResolver(), "mailchimp", src.attr("src"));
			src.attr("src", myExternalizedUrl);
		}

		for (Element link : imports) {
			myExternalizedUrl = externalizer.externalLink(request.getResourceResolver(), "mailchimp", link.attr("src"));
			link.attr("src", myExternalizedUrl);
		}

		for (Element link : links) {
			myExternalizedUrl = externalizer.externalLink(request.getResourceResolver(), "mailchimp", link.attr("src"));
			link.attr("src", myExternalizedUrl);
		}
		
		html = doc.toString();
	}
}