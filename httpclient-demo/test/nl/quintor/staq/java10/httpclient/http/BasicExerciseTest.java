package nl.quintor.staq.java10.httpclient.http;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

public class BasicExerciseTest {

    final BasicExercise exercise = new BasicExercise();

    @Test
    public void simple() {
        var result = exercise.simple("https://www.google.com/").join();
        assertThat(result, not(isEmptyOrNullString()));
    }

    @Test
    public void manySimpleResponses() {
        var result = exercise.manySimpleResponses("https://www.milanboers.nl/", 10).join();
        assertThat(result, hasSize(10));
    }
}