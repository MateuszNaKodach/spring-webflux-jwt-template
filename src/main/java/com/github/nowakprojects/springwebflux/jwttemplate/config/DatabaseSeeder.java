package com.github.nowakprojects.springwebflux.jwttemplate.config;

import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Activity;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.UserId;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Username;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.security.EncodedPasswordFactory;
import com.github.nowakprojects.springwebflux.jwttemplate.rest.UserService;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.User;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Email;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Name;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.UUID;

@Configuration
@RequiredArgsConstructor
class DatabaseSeeder {

    private final UserService userService;
    private final EncodedPasswordFactory encodedPasswordFactory;

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedUserCollection();
    }

    private void seedUserCollection() {
        final String userIdRaw = UUID.randomUUID().toString();
        User userForAuthorization = User.from(
                UserId.from(userIdRaw),
                Username.from("admin"),
                encodedPasswordFactory.encodedPasswordFrom("admin"),
                Activity.ACTIVE);

        com.github.nowakprojects.springwebflux.jwttemplate.user.domain.User userForManagement = com.github.nowakprojects.springwebflux.jwttemplate.user.domain.User.from(
                com.github.nowakprojects.springwebflux.jwttemplate.user.domain.UserId.from(userIdRaw),
                com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Username.from("admin"),
                Name.from("Admin", "GitHub"),
                Email.from("kontakt.mateusznowak@gmail.com"),
                com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Activity.ACTIVE
        );
        userService.createUser(userForAuthorization, userForManagement).subscribe();

        final String userIdRaw2 = UUID.randomUUID().toString();
        User userForAuthorization2 = User.from(
                UserId.from(userIdRaw2),
                Username.from("julia"),
                encodedPasswordFactory.encodedPasswordFrom("pass"),
                Activity.ACTIVE);

        com.github.nowakprojects.springwebflux.jwttemplate.user.domain.User userForManagement2 = com.github.nowakprojects.springwebflux.jwttemplate.user.domain.User.from(
                com.github.nowakprojects.springwebflux.jwttemplate.user.domain.UserId.from(userIdRaw2),
                com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Username.from("julia"),
                Name.from("Julia", "Json"),
                Email.from("julia@json.com"),
                com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Activity.ACTIVE

        );
        userService.createUser(userForAuthorization2, userForManagement2).subscribe();
    }
}
