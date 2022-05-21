package edu.tongji.community.controller.advice;

import edu.tongji.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// 标识这个注解只扫描带Controller注解的组件
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    private static  final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("服务器发生异常：" + e.getMessage());
        for(StackTraceElement element : e.getStackTrace()){
            logger.error(element.toString());
        }

        String xRequestWith = request.getHeader("x-requested-with");
        // 如果成立，表示该请求为异步请求，只有异步请求要求返回的是xml格式
        if("XMLHttpRequest".equals(xRequestWith)){
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJSONString(1,"服务器异常！"));
        }else{
            // 普通请求则重定向到错误页面
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}
