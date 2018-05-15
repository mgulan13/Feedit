package com.feedit.web;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

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
import com.feedit.model.User;
import com.feedit.repository.ArticleRepository;
import com.feedit.repository.UserRepository;

@RestController
@RequestMapping("/api/articles")
public class ArticleRestController {

	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping
	public Iterable<Article> getArticles(Pageable pageable) {
		return articleRepository.findAll(pageable);
	}

	@GetMapping("/search")
	public Iterable<Article> getFilteredArticles(
			String query, Pageable pageable) {
		return articleRepository.findByHeadlineIgnoreCaseContaining(query, pageable);
	}
	
	@GetMapping("/profile-search")
	public ResponseEntity<Iterable<Article>> getMyArticles(
			String query, Pageable pageable, Principal principal) {
		User user = userRepository.findByUsername(principal.getName());
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if (hasRole(user, "ROLE_ADMIN")) {
			return new ResponseEntity<>(getFilteredArticles(query, pageable), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(articleRepository.findByUserAndHeadlineIgnoreCaseContaining(user, query, pageable), HttpStatus.OK);
	}

	private boolean hasRole(User user, String role) {
		List<String> roles = user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList());
		return roles.contains(role);
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
