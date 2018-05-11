package com.feedit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.feedit.model.Article;

@Repository
public interface ArticleRepository extends PagingAndSortingRepository<Article, Long> {
	Page<Article> findByHeadlineIgnoreCaseContaining(String headline, Pageable pageable);
}
