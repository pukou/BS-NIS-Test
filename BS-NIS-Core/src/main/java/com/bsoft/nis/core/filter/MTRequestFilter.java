package com.bsoft.nis.core.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 移动终端请求过滤器
 * Created by Administrator on 2016/10/13.
 */
public class MTRequestFilter implements Filter {
    private static String oauth_url = "/handle/login/invalid";
    private static Pattern pattern = Pattern.compile("(\\.[a-zA-Z0-9]+)");
    private static HashSet<String> mapping = new HashSet<String>();
    // 静态资源请求不拦截
    static {
        mapping.add(".png");
        mapping.add(".jpg");
        mapping.add(".gif");
        mapping.add(".jsc");
        mapping.add(".js");
        mapping.add(".css");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * 过滤
     * @param req
     * @param resp
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String uri = request.getRequestURI();

        if (filterMappings(request)) {
            chain.doFilter(req, resp);
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession();
        }

        // 判断session是否包含user对象
        Map user = (HashMap)session.getAttribute("user");
        if(user == null){
            // session失效或过期,交由统一处理器处理
            request.getRequestDispatcher(oauth_url).forward(request,response);
        }else{
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void destroy() {

    }

    protected boolean filterMappings(HttpServletRequest request) {
        String uri = request.getRequestURI();
        // web请求、rpc请求、静态资源请求、.mobile结尾但不带有auth开头的不处理
        if (uri.contains(".web") || uri.contains(".service")
                || uri.contains("/resources") || !uri.contains("/auth")) {
            return true;
        }

        Matcher m = pattern.matcher(uri);
        if (m.find()) {
            String p = m.group(1);
            return mapping.contains(p);
        }
        return false;
    }
}
