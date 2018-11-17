package szczkrzy.kanteam.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import szczkrzy.kanteam.model.security.SecurityUserModel;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;


@Service
public class JwtTokenService {

    public String getLogin(String authToken) {
        return getClaim(authToken, Claims::getSubject);
    }

    boolean validate(String authToken, UserDetails userDetails) {
        String username = getLogin(authToken);
        return (username.equals(userDetails.getUsername()) && !isExpired(authToken));
    }

    public String generateToken(SecurityUserModel user) {

        Claims claims = Jwts.claims().setSubject(user.getLogin());
        claims.put("scopes", user.getAuthorities());

        Date now = new Date();
        LocalDate localExpiration = LocalDate.now().plusDays(10);
        Date expirationDate = Date.from(localExpiration.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, "kan_team")
                .compact();
    }

    private boolean isExpired(String authToken) {
        LocalDate expirationDate = getExpirationDate(authToken);
        return expirationDate.isBefore(LocalDate.now());
    }

    private LocalDate getExpirationDate(String authToken) {
        Date expirationDate = getClaim(authToken, Claims::getExpiration);
        return expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey("kan_team")
                .parseClaimsJws(token)
                .getBody();
    }

}
