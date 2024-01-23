package com.fges.todoapp.e2e;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fges.todoapp.App;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class GhostTests {

    /**
     * Version of the TP to use with the API, refer to the TP name on the slides!!
     */
    private static final String TP_NAME = "tp1";

    /**
     * Should not change
     */
    private static final String API_ENDPOINT = "https://testcase-api.jho.ovh";

    /**
     * Represents a sequence of command execution and their results.
     * Stdout is used to compare results, that's why the
     *
     * @param sequence List of arguments of the commands to execute. Example:<br>
     *                 {"insert", "-s", "source.json", "Hello World"},<br>
     *                 {"insert", "-s", "source.json", "Bye"},<br>
     *                 {"list", "-s", "source.json"},
     * @param stdoutLines List of lines in the stdout
     * @param exitCode Exit code of the program
     */
    public record ExecOutput(List<List<String>> sequence, List<String> stdoutLines, int exitCode) {
    }

    // Execution output used for the test
    private final ExecOutput execOutput;

    public GhostTests(ExecOutput execOutput) {
        this.execOutput = execOutput;
    }

    /**
     * Remove if you want to keep temporary test files
     */
    @After
    public void after() {
        deleteTmpFiles();
    }

    /**
     * The test
     * @throws Exception Any exception, it's ok to not handle them in tests because
     */
    @Test
    public void ghostTest() throws Exception{
        Assert.assertEquals(this.execOutput, runMain(this.execOutput.sequence));
    }

    private ExecOutput runMain(List<List<String>> sequence) throws IOException {
        var out = System.out;
        ByteArrayOutputStream sout = new ByteArrayOutputStream();
        System.setOut(new PrintStream(sout));

        int exitOutput = 0;

        for (var args: sequence) {
            exitOutput = App.exec(args.toArray(new String[0]));
        }

        System.setOut(out);

        return new ExecOutput(
                sequence,
                Arrays.stream(sout.toString().split("\n")).toList(),
                exitOutput
        );
    }

    private static List<ExecOutput> getApiExecOutput(String tpName) throws IOException {
        URL url = new URL(API_ENDPOINT + "/tp?tpName=" + tpName);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(response.toString(), new TypeReference<>() {
            });
        }
    }

    /**
     * The API gives sequences of code that uses the
     */
    private void deleteTmpFiles() {
        File directory = Paths.get(System.getProperty("user.dir")).toFile();
        var files = directory.listFiles();
        if (files == null) {
            System.err.println("Null directory");
            return;
        }
        for (File f : files) {
            if (f.getName().startsWith("tmp-")) {
                f.delete();
            }
        }
    }

    @Parameterized.Parameters
    public static List<Object[]> data() throws Exception {
        var output = getApiExecOutput(TP_NAME);

        return output.stream().map(
                o -> new Object[]{o}
        ).toList();
    }


}
