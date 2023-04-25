package tech.ada.emailapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.ada.emailapi.model.dto.EmailRequest;
import tech.ada.emailapi.model.dto.EmailResponse;
import tech.ada.emailapi.service.EmailService;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EmailResponse> enviarEmail(@Valid @RequestBody EmailRequest emailRequest){
        return Mono.defer(() -> emailService.enviarEmail(emailRequest))
                .map(EmailResponse::convertToResponse)
                .subscribeOn(Schedulers.parallel());
    }
}
