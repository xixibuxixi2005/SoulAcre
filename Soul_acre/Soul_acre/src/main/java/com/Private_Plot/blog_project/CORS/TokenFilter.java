package com.Private_Plot.blog_project.CORS;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String token = request.getHeader("Authorization"); // 假设token在Authorization头中传递

        if (token == null ||!isValidToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidToken(String token) {
        // 这里实现token的验证逻辑，比如检查token是否在数据库中存在、是否过期等
        // 简单示例，假设token以"Bearer "开头且长度大于7
        return token.startsWith("Bearer ") && token.length() > 7;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化方法，可用于加载配置等
    }

    @Override
    public void destroy() {
        // 销毁方法，可用于释放资源等
    }
}