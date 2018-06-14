package nl.quintor.staq.java10.httpclient.http;

import org.junit.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

public class ServerPushExerciseTest {

    private final ServerPushExercise exercise = new ServerPushExercise();

    /*
    https://http2.akamai.com/demo/h2_demo_frame_sp2.html pushes a number of request/response pairs based on the
    `pushnum` query parameter.
     */

    @Test
    public void serverPushResponses_1() {
        var serverPushResponses = exercise.serverPushResponses("https://http2.akamai.com/demo/h2_demo_frame_sp2.html?pushnum=1").join();
        assertThat(serverPushResponses.pushed.entrySet(), hasSize(1));
    }

    @Test
    public void serverPushResponses_5() {
        var serverPushResponses = exercise.serverPushResponses("https://http2.akamai.com/demo/h2_demo_frame_sp2.html?pushnum=5").join();
        assertThat(serverPushResponses.pushed.entrySet(), hasSize(5));
    }

    @Test
    public void serverPushResponses_10() {
        var serverPushResponses = exercise.serverPushResponses("https://http2.akamai.com/demo/h2_demo_frame_sp2.html?pushnum=10").join();
        assertThat(serverPushResponses.pushed.entrySet(), hasSize(10));
    }
}