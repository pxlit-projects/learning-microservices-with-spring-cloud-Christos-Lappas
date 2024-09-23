package be.pxl.notificationservice.response;

import be.pxl.notificationservice.model.Notification;

public class NotificationDto {
    private Long id;
    private String name;
    private String to;
    private String subject;
    private String message;

    public NotificationDto() {
    }

    public NotificationDto(Notification notification) {
        this.id = notification.getId();
        this.name = notification.getName();
        this.to = notification.getTo();
        this.subject = notification.getSubject();
        this.message = notification.getMessage();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
