package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.dto.MailDTO;
import com.example.springboot_cy_marketplace.model.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class MailService {
    public static final String MAIL_FROM = "contact@tranhoangmanh.com";
    @Value("${validate.email.key}")
    String validateEmailKey;
    @Value("${validate.email.api.url}")
    String validateEmailAPI;
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    /*
     * @author: Manh Tran
     * @since: 13/06/2022 10:07 SA
     * @description-VN: Thiết kế giao diện và cấu hình gửi email.
     * @description-EN: Design and configure email.
     * @param: mail - Đối tượng chứa thông tin email, type - loại email muốn gửi.
     * @return:
     *
     * */
    public void customTemplateEmail(MailDTO mail, String type) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        //helper.addAttachment("FileWordCuaManh.docx", new ClassPathResource("FileWordCuaManh.docx"));
        Context context = new Context();
        context.setVariables(mail.getProps());
        String html = "";
        if (type.equals("confirm")) {
            html = templateEngine.process("confirm-email-template", context);
        } else if(type.equals(Constant.ORDER_PAY_VNPAY)){
            html = templateEngine.process("vnpay-checkout-success", context);
        } else if(type.equals(Constant.ORDER_PAY_PAYPAL)){
            html = templateEngine.process("paypal-checkout-success", context);
        }else if(type.equals(Constant.ORDER_PAY_COD)){
            html = templateEngine.process("confirm-order", context);
        } else {
            html = templateEngine.process("mail-welcome-template", context);
        }
        helper.setTo(mail.getMailTo());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(MAIL_FROM, "YD-Market");
        javaMailSender.send(message);
    }
    //  Thiết kế nội dung trong mail gửi về cho người dùng / Design content in emails sent to users

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:22 CH
     * @description-VN:  Thiết kế nội dung trong mail gửi về cho người dùng
     * @description-EN:  Design content in emails sent to users
     * @param: from,toAddress,subject,content,tokenString,model
     * @return:
     *
     * */

    public void sendTextMail(MailDTO mail) throws MessagingException, UnsupportedEncodingException {
        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariables(mail.getProps());
        MimeMessage mesasge = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mesasge, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        String html = templateEngine.process("mail-reset-password", ctx);
        helper.setTo(mail.getMailTo());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(MAIL_FROM, "YD-Market");
        this.javaMailSender.send(mesasge);
    }

    /*
    * @author: HaiPhong
    * @since: 15/06/2022 1:41 CH
    * @description-VN:  Gửi email cho người dùng khi admin trả lời một câu hỏi
    * @description-EN:  Send email to user when admin answer they question
    * @param: mail
    * @return:
    *
    * */
    public void sendMailNoticesAnswered(MailDTO mail) throws MessagingException, UnsupportedEncodingException {
        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariables(mail.getProps());
        MimeMessage mesasge = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mesasge, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        String html = templateEngine.process("mail-notices-anwsered", ctx);
        helper.setTo(mail.getMailTo());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(MAIL_FROM, "YD-Market");
        this.javaMailSender.send(mesasge);
    }

    /*
     * @author: Manh Tran
     * @since: 13/06/2022 10:06 SA
     * @description-VN: Kiểm tra địa chỉ email có dùng được hay không.
     * @description-EN: Check if email address is valid or not.
     * @param: email - Địa chỉ email cần kiểm tra.
     * @return:
     *
     * */
    public boolean validationEmail(String email) {

        String ip = ""; //ip address can be blank
        String targetURL = validateEmailAPI + validateEmailKey + "&email=" + email + "&ip_address=" + ip;

        HttpURLConnection connection = null;
        final String USER_AGENT = "Mozilla/5.0";

        try {
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("User-Agent", USER_AGENT);
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            return response.toString().contains("\"status\":\"invalid\"");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

        }
    }

    public void sendMailchangeInfoMail(MailDTO mail) throws MessagingException, UnsupportedEncodingException {
        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariables(mail.getProps());
        MimeMessage mesasge = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mesasge, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        String html = templateEngine.process("mail-confirm-change-password", ctx);
        helper.setTo(mail.getMailTo());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(MAIL_FROM, "YD-Market");
        this.javaMailSender.send(mesasge);
    }
}
