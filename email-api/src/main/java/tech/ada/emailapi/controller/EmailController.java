package tech.ada.emailapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.ada.emailapi.model.Email;
import tech.ada.emailapi.model.dto.EmailRequest;
import tech.ada.emailapi.service.EmailService;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public Mono<Email> enviarEmail(@Valid @RequestBody EmailRequest emailRequest){
        return Mono.defer(() -> emailService.enviarEmail(emailRequest))
                .subscribeOn(Schedulers.parallel());
    }
}
