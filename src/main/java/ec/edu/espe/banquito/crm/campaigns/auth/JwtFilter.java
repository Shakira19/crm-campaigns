package ec.edu.espe.banquito.crm.campaigns.auth;

import ec.edu.espe.banquito.crm.campaigns.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String tokenHeader = request.getHeader("Authorization");
        log.debug(tokenHeader);
        String username = null;
        String token = null;
        Enumeration<String> iterable = request.getHeaderNames();
        MultiValueMap<String, String> headers = new HttpHeaders();
        for (Enumeration names = request.getHeaderNames(); names.hasMoreElements();) {
            String name = (String) names.nextElement();
            for (Enumeration values = request.getHeaders(name); values.hasMoreElements();) {
                String value = (String) values.nextElement();
                log.info("key: {}, value: {}", name, value);
                headers.add(name, value);
            }
        }
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            token = tokenHeader.substring(7);
            try {
                username = tokenManager.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                log.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                log.error("JWT Token has expired");
            }
        } else {

            log.error("Bearer String not found in token {}", request.getHeader("access-control-request-headers"));
        }
        if (null != username && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (tokenManager.validateJwtToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null,
                        userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
