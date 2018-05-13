package com.feedit.web.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ArticleDTO {
	
	@NotNull
	private Long votes;

	@NotEmpty
	private String user;

	@NotEmpty(message="Molimo unesite naziv članka.")
	private String headline;

	private String link;

	@NotEmpty(message="Molimo unesite ime autora.")
	private String author;

	public Long getVotes() {
		return votes;
	}

	public void setVotes(Long votes) {
		this.votes = votes;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	
}
