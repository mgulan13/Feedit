package com.feedit.web;

import javax.validation.Valid;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.feedit.model.Article;
import com.feedit.repository.ArticleRepository;
import com.feedit.repository.UserRepository;
import com.feedit.web.dto.ArticleDTO;

@Controller
@RequestMapping("/articles")
public class ArticleController {

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private UserRepository userRepository;
	
	@ModelAttribute("article")
    public ArticleDTO articleDTO() {
		ArticleDTO article = new ArticleDTO();
		article.setVotes(0L);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		article.setUser(auth.getName());
		return article;
    }

	@GetMapping("/add")
	public String addArticle(Model model) {
		return "add-article";
	}

	@PostMapping("/add")
	public String addArticle(@ModelAttribute("article") @Valid ArticleDTO article, BindingResult result) {
		if (!validateURL(article.getLink())) {
			result.rejectValue("link", null, "Uneseni link nije ispravan.");
		};
			
		if (result.hasErrors()) {
			return "add-article";
		}
		
		articleRepository.save(converFromDTO(article));
		return "redirect:/articles/add?success";
	}

	private boolean validateURL(String link) {
		UrlValidator urlValidator = UrlValidator.getInstance();
		if (!urlValidator.isValid(link)) {
			return false;
		}
		
		RestTemplate restTemplate = new RestTemplate();
		try {
			restTemplate.exchange(link, HttpMethod.HEAD, null, String.class);
		} catch(Exception ex) {
			return false;
		}
		
		return true;
	}

	private Article converFromDTO(ArticleDTO article) {
		return new Article(
				userRepository.findByUsername(article.getUser()), 
				article.getHeadline(), 
				article.getLink(),
				article.getAuthor(), 
				article.getVotes()
		);
	}

}
