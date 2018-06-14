package nl.quintor.staq.java10.httpclient.http;

import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.CompletionException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ResponseCodeExerciseTest {

    private final ResponseCodeExercise exercise = new ResponseCodeExercise();

    @Test
    public void emptyStringIfNot200_ok() {
        var emptyStringIfNot200 = exercise.emptyStringIfNot200("https://www.google.com/").join();
        assertThat(emptyStringIfNot200, not(isEmptyOrNullString()));
    }

    @Test
    public void emptyStringIfNot200_notFound() {
        var emptyStringIfNot200 = exercise.emptyStringIfNot200("https://www.google.com/404.html").join();
        assertThat(emptyStringIfNot200, isEmptyString());
    }

    @Test
    public void emptyOptionalIfNot200_ok() {
        var emptyOptionalIfNot200 = exercise.emptyOptionalIfNot200("https://www.google.com/").join();
        assertThat(emptyOptionalIfNot200.get(), not(isEmptyOrNullString()));
    }

    @Test
    public void emptyOptionalIfNot200_notFound() {
        var emptyOptionalIfNot200 = exercise.emptyOptionalIfNot200("https://www.google.com/404.html").join();
        assertThat(emptyOptionalIfNot200, equalTo(Optional.empty()));
    }

    @Test
    public void failedFutureIfNot200_ok() {
        var failedFutureIfNot200 = exercise.failedFutureIfNot200("https://www.google.com/");
        assertThat(failedFutureIfNot200.join(), not(isEmptyOrNullString()));
    }

    @Test(expected = CompletionException.class)
    public void failedFutureIfNot200_notFound() {
        var failedFutureIfNot200 = exercise.failedFutureIfNot200("https://www.google.com/404.html");
        failedFutureIfNot200.join();
    }
}