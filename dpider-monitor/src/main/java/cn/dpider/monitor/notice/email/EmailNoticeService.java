package cn.dpider.monitor.notice.email;

import cn.dpider.monitor.notice.AbstractNotice;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author wanhongji
 * @date 2019/04/09
 */
public class EmailNoticeService extends AbstractNotice {

    @Autowired
    private EmailSender emailSender;

    private String[] recipients;

    @Override
    protected void doNotice(String content) {
        Email email = new Email();
        email.setSubject("【山海小管家-通知】" + content.substring(0,15) + "...");
        email.setContent(content);
        email.setRecipient(recipients);
        BlockingQueue<Email> queue = EmailSender.emailBlockingQueue;
        try {
            queue.offer(email,2, TimeUnit.SECONDS);
            if (!emailSender.isRunning()) {
                emailSender.startSend();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setRecipients(String[] recipients) {
        this.recipients = recipients;
    }
}
