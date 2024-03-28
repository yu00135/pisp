package com.mmy.pisp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.mmy.pisp.entity.VerifyCode;
import com.mmy.pisp.result.ResultVO;
import com.mmy.pisp.utils.VerifyCodeGenUtils;
import com.mmy.pisp.utils.VerifyCodeRandomUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping("/verify")
public class VerifyController {

    private final VerifyCodeGenUtils verifyCodeGenUtils;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public VerifyController(JavaMailSender mailSender,VerifyCodeGenUtils verifyCodeGenUtils,TemplateEngine templateEngine){
        this.verifyCodeGenUtils=verifyCodeGenUtils;
        this.mailSender=mailSender;
        this.templateEngine=templateEngine;

    }

    //@Autowired
    //private RedisUtils redisUtils;

    @PostMapping("/getImage")
    public void verifyCode(HttpServletRequest request, HttpServletResponse response) {
        //设置长宽
        VerifyCode verifyCode = verifyCodeGenUtils.generate(70, 32);
        String code = verifyCode.getCode();
        // 方式一：（session）将VerifyCode绑定session
        request.getSession().setAttribute("VerifyCode", code);

        // 待提交登录时候获取服务器存储的验证码作为比较
        // String verifyCode1 = (String) request.getSession().getAttribute("VerifyCode");

        // 方式二：（redis）获取session id 作为key,时间存5分钟
        //HttpSession session=request.getSession();
        //session.setAttribute("VerifyCode_" + request.getSession().getId(),code);
        //redisUtils.set("VerifyCode_" + request.getSession().getId(), code, 300);

        //设置响应头
        response.setHeader("Pragma","no-cache");
        //设置响应头
        response.setHeader("Cache-Control", "no-cache");
        //在代理服务器端防止缓冲
        response.setDateHeader("Expires", 0);
        //设置响应内容类型
        response.setContentType("image/jpeg");
        try {
            response.getOutputStream().write(verifyCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/emailCode")
    public ResultVO emailCode(HttpServletRequest request,@RequestBody Map<String,Object> map) throws MessagingException {
        String emailCode= VerifyCodeRandomUtils.randomString(4);
        String email=(String) map.get("email");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("个人信息托管平台验证操作");
        helper.setFrom("799816968@qq.com");
        helper.setTo(email);
        helper.setSentDate(new Date());
        // 这里引入的是Template的Context
        Context context = new Context();
        // 设置模板中的变量
        context.setVariable("emailCode", emailCode);
        // 第一个参数为模板的名称
        String process = templateEngine.process("mail.html", context);
        // 第二个参数true表示这是一个html文本
        helper.setText(process,true);
        mailSender.send(mimeMessage);
        request.getSession().setAttribute("emailCode",emailCode);
        return ResultVO.success(200, "邮件发送成功", null);
    }

}
