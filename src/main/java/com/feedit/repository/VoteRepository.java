package com.feedit.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.feedit.model.Article;
import com.feedit.model.User;
import com.feedit.model.Vote;

@Repository
public interface VoteRepository extends CrudRepository<Vote, Long>{
	Vote findByUserAndArticle(User user, Article article); 
}
