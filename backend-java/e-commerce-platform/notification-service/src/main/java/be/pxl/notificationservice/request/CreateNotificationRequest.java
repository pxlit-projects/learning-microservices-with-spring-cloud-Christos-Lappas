package be.pxl.notificationservice.request;

public record CreateNotificationRequest(String name,String to,String subject,String message) {
}
