package nl.quintor.staq.java10.httpclient.http;

import java.util.concurrent.CompletionException;

public class ExerciseRunner {
    public static void main(String... args) {
        basic();
        serverPush();
        countBytes();
        responseCodes();
    }

    static void basic() {
        var exercise = new BasicExercise();

        var simpleResponse = exercise.simple("https://www.google.com/").join();
        System.out.println(!simpleResponse.isEmpty());

        var manySimpleResponses = exercise.manySimpleResponses("https://www.milanboers.nl/").join();
        System.out.println(manySimpleResponses.size());
    }

    static void serverPush() {
        var exercise = new ServerPushExercise();
        var serverPushResponses = exercise.serverPushResponses("https://http2.akamai.com/demo/h2_demo_frame_sp2.html?pushnum=1").join();
        System.out.println(serverPushResponses.pushed.size());
    }

    static void countBytes() {
        var exercise = new ByteCountingExercise();
        exercise.countBytes("http://speedtest.xs4all.net/files/1mb.bin");
    }

    static void responseCodes() {
        var exercise = new ResponseCodeExercise();

        var emptyStringIfNot200 = exercise.emptyStringIfNot200("https://www.google.com/404.html").join();
        System.out.println("".equals(emptyStringIfNot200));

        var emptyOptionalIfNot200 = exercise.emptyOptionalIfNot200("https://www.google.com/404.html").join();
        System.out.println(emptyOptionalIfNot200);

        var failedFutureIfNot200 = exercise.failedFutureIfNot200("https://www.google.com/404.html");
        try {
            failedFutureIfNot200.join();
            System.out.println("Future dit not fail :(");
        } catch (CompletionException e) {
            System.out.println(e.getMessage());
        }
    }
}
