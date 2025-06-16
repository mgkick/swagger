package web.mvc.advice;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import web.mvc.exception.ErrorCode;
import web.mvc.exception.ErrorCodeProvider;
import web.mvc.exception.MemberAuthenticationException;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@RestControllerAdvice
public class DefaultExceptionAdvice {
    @ExceptionHandler(RuntimeException.class) //런타임으로 들어오면, 프로바이더 타임으로 다운캐스팅할수있다.. 
    //자식쪽 접근안됨 -> 일일이 처리하기보다 에러프로바이더 위로빼기
    public ProblemDetail handleRuntimeException(RuntimeException e) {

        // 보드서치낫E, DML E, 멤버소텐티케이션 E //각각의 에러코드있음,
        ProblemDetail problemDetail;

        if (e instanceof ErrorCodeProvider provider) { //--------------------------------유연해짐 //핵심 //예외늘어나도 프로바이더만 추가하면됨
            ErrorCode errorCode = provider.getErrorCode();
            problemDetail = ProblemDetail.forStatus(errorCode.getHttpStatus());
            problemDetail.setTitle(errorCode.getTitle());
            problemDetail.setDetail(errorCode.getMessage());
        } else {
            // 알 수 없는 런타임 예외 처리
            problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            problemDetail.setTitle("Unexpected Error");
            problemDetail.setDetail(e.getMessage());
        }

        problemDetail.setProperty("exception", e.getClass().getSimpleName());
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }
}