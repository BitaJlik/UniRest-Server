package com.unirest.core;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.core.utils.Colors;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EntityScan("com.unirest.data.models")
public class UniRestServerApplication {

    public static void main(String[] args) throws ClassNotFoundException {
        SpringApplication.run(UniRestServerApplication.class, args);

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(true);

        scanner.addIncludeFilter(new AnnotationTypeFilter(GetMapping.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(PostMapping.class));
        System.out.println();
        for (BeanDefinition bd : scanner.findCandidateComponents(UniRestServerApplication.class.getPackage().getName())) {
            Class<?> aClass = Class.forName(bd.getBeanClassName());
            for (Annotation requestAnnotation : aClass.getAnnotations()) {
                if (requestAnnotation instanceof RequestMapping) {
                    List<String> classRequests = new ArrayList<>();
                    System.out.printf("%-25s | %s %n", aClass.getSimpleName(), Colors.withReset(Colors.BLUE, Arrays.toString(((RequestMapping) requestAnnotation).value())));
                    if (aClass.getSuperclass().equals(BaseController.class)) {
                        System.out.println(Colors.withReset(Colors.GREEN, "Extend base requests"));
                    }
                    for (Method declaredMethod : aClass.getDeclaredMethods()) {
                        Annotation annotation = null;
                        for (Annotation declaredAnnotation : declaredMethod.getDeclaredAnnotations()) {
                            if (declaredAnnotation instanceof PostMapping || declaredAnnotation instanceof GetMapping) {
                                annotation = declaredAnnotation;
                                break;
                            }
                        }
                        if (annotation != null) {
                            StringBuilder stringRequest = getStringRequest(annotation);
                            Parameter[] parameters = declaredMethod.getParameters();
                            for (int i = 0; i < parameters.length; i++) {
                                Parameter parameter = parameters[i];
                                stringRequest.append(String.format("(%s) %s", parameter.getType().getSimpleName(), parameter.getName()));
                                if (i != parameters.length - 1) {
                                    stringRequest.append(", ");
                                }
                            }
                            classRequests.add(stringRequest.toString());
                        }
                    }
                    Collections.sort(classRequests);
                    for (String classRequest : classRequests) {
                        System.out.println(classRequest);
                    }
                    System.out.println();
                }
            }

        }
    }

    private static StringBuilder getStringRequest(Annotation annotation) {
        StringBuilder stringRequest = null;
        if (annotation instanceof PostMapping) {
            stringRequest = new StringBuilder((String.format("POST -> %-17s | ", Arrays.toString(((PostMapping) annotation).value()))));
        }
        if (annotation instanceof GetMapping) {
            stringRequest = new StringBuilder((String.format("GET  -> %-17s | ", Arrays.toString(((GetMapping) annotation).value()))));
        }
        return stringRequest;
    }

}

