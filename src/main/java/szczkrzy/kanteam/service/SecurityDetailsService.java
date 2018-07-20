package szczkrzy.kanteam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import szczkrzy.kanteam.model.entity.KTUser;
import szczkrzy.kanteam.model.security.SecurityUserModel;
import szczkrzy.kanteam.repository.UserRepository;

@Service
public class SecurityDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        KTUser user = userRepository.findByEmail(s);

        return new SecurityUserModel(user);
    }
}
