package cn.dpider.monitor.notice.email;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by 万洪基 on 2017/8/3.
 */
public class Email {

    private String recipient[];

    private String sender;

    private String subject;

    private String content;

    private String attachment;

    private Date sendDate;

    public String[] getRecipient() {
        return recipient;
    }

    public void setRecipient(String[] recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    @Override
    public String toString() {
        return "Email{" +
                "recipient=" + Arrays.toString(recipient) +
                ", sender='" + sender + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", attachment='" + attachment + '\'' +
                ", sendDate=" + sendDate +
                '}';
    }
}
