package szczkrzy.kanteam.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import szczkrzy.kanteam.model.entities.KTUser;
import szczkrzy.kanteam.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClientTokenInterceptor extends HandlerInterceptorAdapter {

    private KTUser requestUser;

    @Autowired
    private JwtTokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    public ClientTokenInterceptor(KTUser requestUser) {
        this.requestUser = requestUser;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {

        String requestToken;
        try {
            requestToken = request.getHeader("Authorization").substring(7);
        } catch (NullPointerException e) {
            System.out.println("No authorization header! skipping...");
            return true;
        }
        this.requestUser = userRepository.findByEmail(tokenService.getLogin(requestToken));
        return true;
    }
}