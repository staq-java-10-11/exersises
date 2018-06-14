package nl.quintor.staq.java10.httpclient.http;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class ByteCountingExerciseTest {

    private final ByteCountingExercise exercise = new ByteCountingExercise();

    @Test
    public void megabyte() {
        var megabyte = exercise.countBytes("http://speedtest.xs4all.net/files/1MB.bin");
        assertThat(megabyte, equalTo(1_000_000L));
    }

    @Test
    public void mebibyte() {
        var mebibyte = exercise.countBytes("http://speedtest.xs4all.net/files/1MiB.bin");
        assertThat(mebibyte, equalTo(1024L * 1024L));
    }
}