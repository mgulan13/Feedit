package com.feedit.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.feedit.model.Article;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {
}
