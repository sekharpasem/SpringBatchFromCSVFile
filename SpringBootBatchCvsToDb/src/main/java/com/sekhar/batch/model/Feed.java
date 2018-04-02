package com.sekhar.batch.model;

import java.io.Serializable;

public class Feed implements Serializable {

	private static final long serialVersionUID = -6402068923614583448L;
	private String feedName;
	private String description;

	public Feed() {
	}

	public Feed(String feedName, String description) {
		super();
		this.feedName = feedName;
		this.description = description;

	}

	public String getFeedName() {
		return feedName;
	}

	public void setFeedName(String feedName) {
		this.feedName = feedName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Feed [feedName=" + feedName + ", description=" + description + "]";
	}
}
