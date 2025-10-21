// package com.uber.system.userService.config;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import java.io.IOException;
// import java.util.Collections;

// @Component
// public class JwtAuthenticationFilter extends OncePerRequestFilter {
//     private final String jwtSecret = "uberSecretKey";

//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//             throws ServletException, IOException {
//         String header = request.getHeader("Authorization");
//         if (header != null && header.startsWith("Bearer ")) {
//             String token = header.substring(7);
//             try {
//                 Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
//                 String userId = claims.getSubject();
//                 String role = claims.get("role", String.class);
//                 UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                         userId, null, Collections.singletonList(new SimpleGrantedAuthority(role)));
//                 authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                 SecurityContextHolder.getContext().setAuthentication(authentication);
//             } catch (Exception e) {
//                 SecurityContextHolder.clearContext();
//             }
//         }
//         filterChain.doFilter(request, response);
//     }
// }
