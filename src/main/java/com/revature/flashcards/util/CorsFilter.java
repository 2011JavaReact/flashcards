package com.revature.flashcards.util;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest servletRequest,
      ServletResponse servletResponse, FilterChain filterChain) throws
      IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) servletRequest;
    HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
    httpResponse.addHeader("Access-Control-Allow-Origin", "*");
    httpResponse.addHeader("Access-Control-Allow-Methods", "GET,POST," +
        "PUT,DELETE,HEAD,OPTIONS");
    httpResponse.addHeader("Access-Control-Allow-Headers",
        "Content-Type,X-Requested-With,Accept,Authorization,Origin," +
            "Access-Control-Request-Method,Access-Control-Request-Headers");
    httpResponse.addHeader("Access-Control-Max-Age", "10");

    filterChain.doFilter(servletRequest, servletResponse);
  }

  @Override
  public void destroy() {

  }
}
