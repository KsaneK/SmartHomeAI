package pl.edu.wat.smart.home.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.edu.wat.smart.home.entity.User;
import pl.edu.wat.smart.home.repository.UserRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = repo.findByUsername(s);

        if(user == null)
            throw new UsernameNotFoundException("No user found with username: " + s);

        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");

        return new org.springframework.security.core.userdetails
                .User(user.getUsername(), user.getPassword(), true, true, true, true, getAuthorities(roles));
    }

    private static List<GrantedAuthority> getAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(String role : roles)
            authorities.add(new SimpleGrantedAuthority(role));

        return authorities;
    }
}
