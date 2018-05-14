package com.feedit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
	name="votes", 
	uniqueConstraints=
		@UniqueConstraint(columnNames= {"USER_ID", "ARTICLE_ID"})
)
public class Vote {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(nullable=false)
	private User user;
	
	@ManyToOne
	@JoinColumn(nullable=false)
	private Article article;
	
	@Column(nullable=false)
	private Boolean upvote;
	
	Vote() {
	}
	
	public Vote(User user, Article article) {
		super();
		this.user = user;
		this.article = article;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Boolean getUpvote() {
		return upvote;
	}
	
	public void setUpvote(Boolean upvote) {
		this.upvote = upvote;
	}

}
