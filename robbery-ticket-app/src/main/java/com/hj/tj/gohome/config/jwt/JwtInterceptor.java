package com.hj.tj.gohome.config.jwt;

import com.hj.tj.gohome.consts.OwnerConstants;
import com.hj.tj.gohome.utils.OwnerContextHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * 身份验证工具
 */
@Slf4j
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private TokenHelper tokenHelper;

    public static final String BEARER = "Bearer ";

    /**
     * 身份过滤验证
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isEmpty(authHeader)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        if (!authHeader.startsWith(BEARER)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }

        final String token = authHeader.substring(7);

        try {
            Claims claims = tokenHelper.parser(token);

            HashMap<String, Object> map = new HashMap<>(3);
            map.put(OwnerConstants.OWNER_ID, claims.get(OwnerConstants.OWNER_ID));
            map.put(OwnerConstants.NICKNIME, claims.get(OwnerConstants.NICKNIME));
            map.put(OwnerConstants.FROM, claims.get(OwnerConstants.FROM));

            OwnerContextHelper.putOwner(map);
        } catch (ExpiredJwtException e) {
            log.error("Auth code expired.authHeader:{}", authHeader);
            response.setStatus(HttpStatus.PAYMENT_REQUIRED.value());
            return false;
        } catch (UnsupportedJwtException | IllegalArgumentException | MalformedJwtException e) {
            log.error("Auth token :{} is error", token, e);
            response.setStatus(HttpStatus.PAYMENT_REQUIRED.value());
            return false;
        }

        return true;
    }
}
