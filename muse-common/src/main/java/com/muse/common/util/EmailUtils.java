package com.muse.common.util;

import com.muse.common.entity.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/24 19 19
 **/
@Service("emailUtils")
public class EmailUtils {
    private static Logger log = LoggerFactory.getLogger("EmailUtils");

    @Autowired
    private JavaMailSender mailSender;

    public ResultData sendEmail(String toEmail, String subject, String content) {
        try {
            final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setFrom("1036038780@qq.com");
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(content);
            this.mailSender.send(mimeMessage);

            return ResultData.getSuccessResult();

        } catch (Exception ex) {
            log.error("发送邮件异常=" + ex.getMessage());
            return ResultData.getErrResult("发送邮件失败");
        }
    }


}
