package com.DevUp.NotificationService.Util;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.stereotype.Component;

@Component
public class EmailTemplateRenderer {

    private final TemplateEngine templateEngine;

    public EmailTemplateRenderer(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String render(String templateName, String doctorName, String patientName, String appointmentDateTime) {
        Context context = new Context();
        context.setVariable("doctorName", doctorName);
        context.setVariable("patientName", patientName);
        context.setVariable("appointmentDateTime", appointmentDateTime);

        return templateEngine.process( templateName, context);
    }
}
