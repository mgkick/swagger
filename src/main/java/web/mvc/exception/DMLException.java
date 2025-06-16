package web.mvc.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class DMLException extends  RuntimeException implements  ErrorCodeProvider{ //각각 ErrorCodeProvider임플리먼트함
    private final ErrorCode errorCode;

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
