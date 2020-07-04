package kiz.learnwithvel.topratedmovies.request.respond;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Response;

public class ApiResponse<T> {

    public ApiResponse<T> error(Throwable err) {
        return new ApiErrorResponse<>(!Objects.equals(err.getMessage(), "") ? err.getMessage()
                : "Unknown Error\nCheck your network connection");
    }

    public ApiResponse<T> success(Response<T> response) {
        if (response.isSuccessful()) {
            T body = response.body();

            if (body instanceof MovieResponse) {
                if (!CheckApiKey.isMovieApiKeyValid((MovieResponse) body)) {
                    return new ApiErrorResponse<>("Api key is invalid or expired.");
                }
            }

            if (body instanceof VideoResponse) {
                if (!CheckApiKey.isMovieApiKeyValid((VideoResponse) body)) {
                    return new ApiErrorResponse<>("Api key is invalid or expired.");
                }
            }

            if (body == null || response.code() == 204) {
                return new ApiEmptyResponse<>();
            } else return new ApiSuccessResponse<>(body);
        } else {
            String message = "";
            try {
                if (response.errorBody() != null)
                    message = response.errorBody().string();
            } catch (IOException e) {
                e.printStackTrace();
                message = response.message();
            }
            return new ApiErrorResponse<>(message);
        }
    }

    public static class ApiSuccessResponse<T> extends ApiResponse<T> {
        private T body;

        public ApiSuccessResponse(T body) {
            this.body = body;
        }

        public T getBody() {
            return body;
        }
    }

    public static class ApiErrorResponse<T> extends ApiResponse<T> {
        private String message;

        public ApiErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class ApiEmptyResponse<T> extends ApiResponse<T> {

    }

}
