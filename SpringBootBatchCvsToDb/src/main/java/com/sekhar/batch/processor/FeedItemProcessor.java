package com.sekhar.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.sekhar.batch.model.Feed;

public class FeedItemProcessor implements ItemProcessor<Feed, Feed> {

	private static final Logger log = LoggerFactory.getLogger(FeedItemProcessor.class);

	@Override
	public Feed process(final Feed feed) throws Exception {
		final String feed_name = feed.getFeedName().toUpperCase();
		final String desc = feed.getDescription().toUpperCase();

		final Feed transformedFeed = new Feed(feed_name, desc);

		log.info("Converting (" + feed + ") into (" + transformedFeed + ")");

		return transformedFeed;
	}

}