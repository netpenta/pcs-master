package th.co.pentasol.pcs.master.component;

import th.co.pentasol.pcs.master.configuration.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import th.co.pentasol.pcs.master.service.UserService;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("ALL")
@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String jwtToken = request.getHeader("Authorization");
        String username = null;
        try {
            if(jwtToken != null && jwtUtils.validateToken(jwtToken)){
                username = jwtUtils.getUsernameFromToken(jwtToken);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }catch (ExpiredJwtException e) {
            log.error(e.getMessage());
            request.setAttribute("exception", e.getMessage());
        }catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            request.setAttribute("exception", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error(e.getMessage());
            request.setAttribute("exception", "Invalid JWT token");
        }catch (Exception e) {
            log.error(e.getMessage());
            request.setAttribute("exception", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

}
