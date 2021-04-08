package ec.edu.espe.banquito.crm.campaigns.service;

import java.util.ArrayList; 
import org.springframework.security.core.userdetails.User; 
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.security.core.userdetails.UserDetailsService; 
import org.springframework.security.core.userdetails.UsernameNotFoundException; 
import org.springframework.stereotype.Service; 

@Service
public class JwtUserDetailsService implements UserDetailsService { 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("user-crm".equals(username)) {
            return new User("user-crm",
                    "$2a$10$aDDRu4KfnWgWmNt3QfHsoutJBlGpemM/OlcmrN9n4pK25zU5LL0NW",
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
