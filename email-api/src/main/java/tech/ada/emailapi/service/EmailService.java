package tech.ada.emailapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import tech.ada.emailapi.model.Email;
import tech.ada.emailapi.model.dto.EmailRequest;
import tech.ada.emailapi.repository.EmailRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final EmailRepository emailRepository;
    private final JavaMailSender javaMailSender;

    public Mono<Email> enviarEmail(EmailRequest emailRequest) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        return Mono.defer(() -> {
            log.info("Inciando a construção da mensagem de e-mail - {}", emailRequest);
            mensagem.setFrom("email.webiii@gmail.com");
            mensagem.setTo(emailRequest.destinatario());
            mensagem.setText(emailRequest.mensagem());
            mensagem.setSubject(emailRequest.assunto());

            javaMailSender.send(mensagem);

            log.info("Enviando e-mail - {}",emailRequest);
            return emailRepository.save(new Email(UUID.randomUUID().toString(),
                    "email.webiii@gmail.com",
                    emailRequest.destinatario(),
                    emailRequest.assunto(),
                    emailRequest.mensagem()));
        });
    }
}
