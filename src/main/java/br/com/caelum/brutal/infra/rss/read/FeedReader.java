package br.com.caelum.brutal.infra.rss.read;

import java.io.IOException;
import java.io.InputStream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;


@ApplicationScoped
public class FeedReader {

	@Inject private FeedConverter converter;
	private static final Logger LOG = Logger.getLogger(FeedReader.class);
	
	public RSSFeed read(String uri, Integer numberOfItems) {
		String rssXml = getXmlFrom(uri);
		return limitItems(converter.convert(rssXml), numberOfItems);
	}

	private RSSFeed limitItems(RSSFeed feed, Integer numberOfItems) {
		feed.limitItems(numberOfItems);
		return feed;
	}

	private String getXmlFrom(String uri){
		HttpGet httpGet = new HttpGet(uri);
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			LOG.debug("Receiving RSS from " + uri);
			String stringContent = IOUtils.toString(content);
			LOG.debug(stringContent);
			return stringContent;
		} catch (IOException e) {
			throw new RuntimeException("Cant get rss from uri:"+uri, e);
		}
	}
	
}
