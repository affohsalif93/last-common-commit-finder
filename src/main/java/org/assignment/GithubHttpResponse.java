package org.assignment;


import org.assignment.exceptions.*;

import java.net.HttpURLConnection;
import java.net.http.HttpResponse;

public class GithubHttpResponse {
    private final int statusCode; ;
    private final String body;

    private GithubHttpResponse(int statusCode, String body) {
        this.body = body;
        this.statusCode = statusCode;
    }

    public static GithubHttpResponse of(int statusCode, String bodyString) {
        return new GithubHttpResponse(statusCode, bodyString);
    }

    public static GithubHttpResponse of(HttpResponse<String> response) {
        return new GithubHttpResponse(response.statusCode(), response.body());
    }

    /**
     * Validate the HTTP response
     *
     * @param response The HTTP response to validate
     * @throws EmptyResponseException             if the response is empty
     * @throws InvalidTokenException              if the token is invalid
     * @throws RateLimitOrPermissionException     if the rate limit is exceeded or the user does not have permission
     * @throws RepositoryPrivateNotFoundException if the repository is private
     * @throws HttpRequestException               if the request is invalid
     */
    public static void validateHttpResponse(GithubHttpResponse response) throws EmptyResponseException, InvalidTokenException, RateLimitOrPermissionException, RepositoryPrivateNotFoundException, HttpRequestException {
        switch (response.statusCode()) {
            case HttpURLConnection.HTTP_OK -> {
                if (response.body().isEmpty()) {
                    throw new EmptyResponseException();
                }
            }
            case HttpURLConnection.HTTP_UNAUTHORIZED -> {
                throw new InvalidTokenException();
            }
            case HttpURLConnection.HTTP_FORBIDDEN -> {
                throw new RateLimitOrPermissionException();
            }
            case HttpURLConnection.HTTP_NOT_FOUND -> {
                throw new RepositoryPrivateNotFoundException();
            }
            default -> throw new HttpRequestException(response.statusCode());
        }
    }

    public int statusCode() {
        return statusCode;
    }

    public String body() {
        return body;
    }
}
