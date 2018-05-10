package com.feedit.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.feedit.model.Role;
import com.feedit.model.User;
import com.feedit.repository.UserRepository;

@Service
public class UserServiceImpl implements UserDetailsService {

	@Autowired
    private UserRepository userRepository;
    
	@Override
	public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(arg0);
        if (user == null){
            throw new UsernameNotFoundException("Korisničko ime ili lozinka su neispravni.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
	}
	
	 private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
	        return roles.stream()
	                .map(role -> new SimpleGrantedAuthority(role.getName()))
	                .collect(Collectors.toList());
	    }
 
}
