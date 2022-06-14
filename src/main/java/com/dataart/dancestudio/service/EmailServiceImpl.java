package com.dataart.dancestudio.service;

import com.dataart.dancestudio.exception.EntityNotFoundException;
import com.dataart.dancestudio.model.dto.view.BookingViewDto;
import com.dataart.dancestudio.model.dto.view.LessonViewDto;
import com.dataart.dancestudio.model.entity.UserEntity;
import com.dataart.dancestudio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender emailSender;
    private final UserRepository userRepository;

    @Async
    @Override
    public void sendEmailAboutStartingLesson(final Map<String, List<BookingViewDto>> emailAndBookings) {
        try {
            log.info("Sending email about starting is started.");
            final String template = "lessonStartingTemplate";

            for (final Map.Entry<String, List<BookingViewDto>> entry : emailAndBookings.entrySet()) {
                final String email = entry.getKey();
                final List<BookingViewDto> listBookings = entry.getValue();
                final Map<String, Object> contextMap = new HashMap<>();
                final UserEntity userEntity = userRepository.findByEmail(email)
                        .orElseThrow(() -> new EntityNotFoundException("User not found!"));
                contextMap.put("userInfo", userEntity);
                contextMap.put("booking", listBookings.stream()
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Booking not found!")));
                sendEmail(contextMap, template, "Information about the coming lessons.");
                log.info("Email about starting has been sent to {}.", userEntity.getEmail());
            }
        } catch (final Exception e) {
            log.error("Failed to send email about starting.", e);
        }
    }

    @Async
    @Override
    public void sendEmailAboutChangingLesson(final LessonViewDto lessonViewDto) {
        try {
            log.info("Sending email about changing lesson is started.");
            final String template = "lessonChangingTemplate";

            final List<String> emails = userRepository.findAllEmailsByLessonId(lessonViewDto.getId());
            for (final String email : emails) {
                final Map<String, Object> contextMap = new HashMap<>();
                final UserEntity userEntity = userRepository.findByEmail(email)
                        .orElseThrow(() -> new EntityNotFoundException("User not found!"));
                contextMap.put("userInfo", userEntity);
                contextMap.put("lesson", lessonViewDto);
                sendEmail(contextMap, template, "Information about the changing of lesson.");
                log.info("Email about changing has been sent to {}.", userEntity.getEmail());
            }
        } catch (final Exception e) {
            log.error("Failed to send email about changing.", e);
        }
    }

    @Override
    public void sendEmailAboutCancelingLesson(final LessonViewDto lessonViewDto) {
        try {
            log.info("Sending email about canceling lesson is started.");
            final String template = "lessonCancelingTemplate";

            final List<String> emails = userRepository.findAllEmailsByLessonId(lessonViewDto.getId());
            for (final String email : emails) {
                final Map<String, Object> contextMap = new HashMap<>();
                final UserEntity userEntity = userRepository.findByEmail(email)
                        .orElseThrow(() -> new EntityNotFoundException("User not found!"));
                contextMap.put("userInfo", userEntity);
                contextMap.put("lesson", lessonViewDto);
                sendEmail(contextMap, template, "Information about the canceling of lesson.");
                log.info("Email about canceling has been sent to {}.", userEntity.getEmail());
            }
        } catch (final Exception e) {
            log.error("Failed to send email about canceling.", e);
        }
    }

    private void sendEmail(final Map<String, Object> contextMap, final String template, final String subject) throws MessagingException {
        final Context context = new Context();
        context.setVariables(contextMap);
        final UserEntity userInfo = (UserEntity) contextMap.get("userInfo");

        final String process = templateEngine.process("email/" + template, context);
        final MimeMessage mimeMessage = emailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject(subject);
        helper.setText(process, true);
        helper.setTo(userInfo.getEmail());
        emailSender.send(mimeMessage);
    }

}
