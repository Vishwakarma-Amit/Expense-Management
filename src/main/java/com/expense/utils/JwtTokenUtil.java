package com.expense.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {

	private static final long JWT_TOKEN_VALIDITY = 15 * 60 * 1000;

	@Value("${jwt.secret}")
	private String secret;

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();

		return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public String getUsernameFromToken(String jwtToken) {
		return getClaimsFromToken(jwtToken, Claims::getSubject);
	}

	private <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return claimsResolver.apply(claims);
	}

	public boolean validateToken(String jwtToken, UserDetails userDetails) {
		final String userName = getUsernameFromToken(jwtToken);
		return userName.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);
	}

	private boolean isTokenExpired(String jwtToken) {
		final Date expiration = getExpirationDateFromToken(jwtToken);
		return expiration.before(new Date());
	}

	private Date getExpirationDateFromToken(String jwtToken) {
		return getClaimsFromToken(jwtToken, Claims::getExpiration);
	}

}
