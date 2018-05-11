package com.feedit.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feedit.model.Article;
import com.feedit.repository.ArticleRepository;

@RestController
@RequestMapping("/api/articles")
public class ArticleRestController {

	@Autowired
	private ArticleRepository articleRepository;

	@GetMapping
	public Iterable<Article> getArticles(Pageable pageable) {
		return articleRepository.findAll(pageable);
	}

	@GetMapping("/search")
	public Iterable<Article> getFilteredArticles(
			String query, Pageable pageable) {
		return articleRepository.findByHeadlineIgnoreCaseContaining(query, pageable);
	}

	@PutMapping
	public ResponseEntity<?> addArticle(@RequestBody Article article) {
		articleRepository.save(article);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<?> deleteArticle(@RequestBody Iterable<Article> articles) {
		articleRepository.deleteAll(articles);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
