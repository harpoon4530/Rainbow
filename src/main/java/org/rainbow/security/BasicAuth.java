package org.rainbow.security;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.UserStore;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Password;

public class BasicAuth {

    public static final SecurityHandler basicAuth(String username, String password, String realm) {

        UserStore userStore = new UserStore();
        userStore.addUser(username, new Password(password), new String[] { "users"});

        HashLoginService hashLoginService = new HashLoginService(realm);
        hashLoginService.setUserStore(userStore);

        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setRoles(new String[]{"users"});
        constraint.setName(username);
        constraint.setAuthenticate(true);

        ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setConstraint(constraint);
        constraintMapping.setPathSpec("/*");

        ConstraintSecurityHandler constraintSecurityHandler = new ConstraintSecurityHandler();
        constraintSecurityHandler.setAuthenticator(new BasicAuthenticator());
        constraintSecurityHandler.setLoginService(hashLoginService);
        constraintSecurityHandler.addConstraintMapping(constraintMapping);
        constraintSecurityHandler.setRealmName(realm);

        return constraintSecurityHandler;
    }
}
