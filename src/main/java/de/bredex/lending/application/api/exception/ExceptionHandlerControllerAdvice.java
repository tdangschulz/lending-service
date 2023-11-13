package de.bredex.lending.application.api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.StatusType;
import org.zalando.problem.ThrowableProblem;
import org.zalando.problem.spring.common.HttpStatusAdapter;
import org.zalando.problem.spring.web.advice.ProblemHandling;

import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice implements ProblemHandling {

    private static final String KEY_ERROR_CODE = "error_code";
    private static final String KEY_PARAMS = "params";

    @ExceptionHandler
    public ResponseEntity<Problem> handleIllegalArgumentException(final NativeWebRequest request,
                                                                  IllegalArgumentException exception) {
        return create(request, new HttpStatusAdapter(BAD_REQUEST), BAD_REQUEST.getReasonPhrase(),
                exception.getLocalizedMessage(), null, Collections.emptyList(), exception);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleIllegalArgumentException(final NativeWebRequest request, Exception exception) {
        return create(request, new HttpStatusAdapter(INTERNAL_SERVER_ERROR), INTERNAL_SERVER_ERROR.getReasonPhrase(),
                exception.getLocalizedMessage(), null, Collections.emptyList(), exception);
    }

    private ResponseEntity<Problem> create(final NativeWebRequest request, final StatusType statusType,
                                           final String title, final String detail, final Integer errorCode,
                                           final List<String> params, final Throwable throwable) {
        ProblemBuilder builder = Problem.builder().withStatus(statusType);

        if (title != null && title.isEmpty()) {
            builder = builder.withTitle(title);
        }

        builder = builder.withDetail(detail);

        if (errorCode != null) {
            builder = builder.with(KEY_ERROR_CODE, errorCode);
        }

        if (params != null && !params.isEmpty()) {
            builder = builder.with(KEY_PARAMS, params);
        }

        final ThrowableProblem problem = builder.build();
        problem.setStackTrace(new StackTraceElement[]{});

        return create(throwable, problem, request);
    }
}
