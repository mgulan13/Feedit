package com.feedit.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.feedit.model.Article;
import com.feedit.model.User;
import com.feedit.model.Vote;
import com.feedit.repository.ArticleRepository;
import com.feedit.repository.UserRepository;
import com.feedit.repository.VoteRepository;

@RestController
public class VotesController {

	@Autowired
	private VoteRepository voteRepository;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/api/vote")
	public ResponseEntity<Vote> vote(@NonNull Long articleId, Principal principal) {
		if (principal == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		Article article = articleRepository.findById(articleId).orElse(null);
		if (article == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		User user = userRepository.findByUsername(principal.getName());
		return new ResponseEntity<Vote>(
				voteRepository.findByUserAndArticle(user, article), 
				HttpStatus.OK);
	}
	
	@PostMapping("/api/upvote")
	public ResponseEntity<Long> upvote(@RequestBody Long articleId, Principal principal) {
		Article article = articleRepository.findById(articleId).get();
		User user = userRepository.findByUsername(principal.getName());
		Vote vote = voteRepository.findByUserAndArticle(user, article);
		
		if (vote != null) {
			if (vote.getUpvote()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
			article.setVotes(article.getVotes() + 2);
		} else {
			vote = new Vote(user, article);
			article.setVotes(article.getVotes() + 1);
		}
		
		vote.setUpvote(true);
		voteRepository.save(vote);
		articleRepository.save(article);
		
		return new ResponseEntity<>(article.getVotes(), HttpStatus.OK);
	}
	
	@PostMapping("/api/downvote")
	public ResponseEntity<Long> downvote(@RequestBody Long articleId, Principal principal) {
		Article article = articleRepository.findById(articleId).get();
		User user = userRepository.findByUsername(principal.getName());
		Vote vote = voteRepository.findByUserAndArticle(user, article);
		
		if (vote != null) {
			if (!vote.getUpvote()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
			article.setVotes(article.getVotes() - 2);
		} else {
			vote = new Vote(user, article);
			article.setVotes(article.getVotes() - 1);
		}
		
		vote.setUpvote(false);
		voteRepository.save(vote);
		articleRepository.save(article);
		
		return new ResponseEntity<>(article.getVotes(), HttpStatus.OK);
	}

}