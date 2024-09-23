package be.pxl.notificationservice.service;


import be.pxl.notificationservice.model.Notification;
import be.pxl.notificationservice.repository.NotificationRepository;
import be.pxl.notificationservice.request.CreateNotificationRequest;
import be.pxl.notificationservice.response.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private NotificationRepository notificationRepository;

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public NotificationDto getNotificationById(Long id) {
        return notificationRepository.findById(id).map(NotificationDto::new).orElse(null);

    }

    public Notification createNotification(CreateNotificationRequest notificationRequest) {
        Notification notification = new Notification(notificationRequest.name(), notificationRequest.to(), notificationRequest.subject(), notificationRequest.message());
        return notificationRepository.save(notification);
    }

    public Notification updateNotification(Long id, Notification notificationDetails) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setName(notificationDetails.getName());
        notification.setTo(notificationDetails.getTo());
        notification.setSubject(notificationDetails.getSubject());
        notification.setMessage(notificationDetails.getMessage());

        return notificationRepository.save(notification);
    }

    public void deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notificationRepository.delete(notification);
    }
}
