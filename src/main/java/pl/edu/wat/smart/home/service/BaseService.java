package pl.edu.wat.smart.home.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.edu.wat.smart.home.entity.User;
import pl.edu.wat.smart.home.repository.UserRepo;

public abstract class BaseService {

    @Autowired
    private UserRepo userRepo;

    protected User getCurrentlyLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
            return null;

        String name = authentication.getName();
        return userRepo.findByUsername(name);
    }
}
