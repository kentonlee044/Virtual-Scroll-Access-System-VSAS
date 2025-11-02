package a3.t10.g09;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ManageScrollFilteringTest {

    // Simple stub that avoids user input and records which filter was called.
    public static class AnalyticsStub extends AdminSystemAnalytics {
        boolean byDate, byOwner, byFile, byCategory;

        @Override
        public void filterByDate() {
            byDate = true;
        }

        @Override
        public void filterByOwner() {
            byOwner = true;
        }

        @Override
        public void filterByFilename() {
            byFile = true;
        }

        @Override
        public void filterByCategory() {
            byCategory = true;
        }
    }

    private ManageScrollFiltering newCliWithInput(String input, AnalyticsStub stub) throws Exception {
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        ManageScrollFiltering cli = new ManageScrollFiltering(scanner);
        // Replace final analytics field with our stub to avoid blocking
        Field f = ManageScrollFiltering.class.getDeclaredField("analytics");
        f.setAccessible(true);
        f.set(cli, stub);
        return cli;
    }

    @Test
    void selectsFilterByDateThenExit() throws Exception {
        AnalyticsStub stub = new AnalyticsStub();
        ManageScrollFiltering cli = newCliWithInput("1\n0\n", stub);
        assertDoesNotThrow(cli::run);
        assertTrue(stub.byDate);
    }

    @Test
    void selectsFilterByOwnerThenExit() throws Exception {
        AnalyticsStub stub = new AnalyticsStub();
        ManageScrollFiltering cli = newCliWithInput("2\n0\n", stub);
        assertDoesNotThrow(cli::run);
        assertTrue(stub.byOwner);
    }

    @Test
    void selectsFilterByFilenameThenExit() throws Exception {
        AnalyticsStub stub = new AnalyticsStub();
        ManageScrollFiltering cli = newCliWithInput("3\n0\n", stub);
        assertDoesNotThrow(cli::run);
        assertTrue(stub.byFile);
    }

    @Test
    void selectsFilterByCategoryThenExit() throws Exception {
        AnalyticsStub stub = new AnalyticsStub();
        ManageScrollFiltering cli = newCliWithInput("4\n0\n", stub);
        assertDoesNotThrow(cli::run);
        assertTrue(stub.byCategory);
    }

    @Test
    void invalidOptionThenExit() throws Exception {
        AnalyticsStub stub = new AnalyticsStub();
        ManageScrollFiltering cli = newCliWithInput("x\n0\n", stub);
        assertDoesNotThrow(cli::run);
        // No filters called
        assertTrue(!stub.byDate && !stub.byOwner && !stub.byFile && !stub.byCategory);
    }

    @Test
    void noInputExitsGracefully() throws Exception {
        AnalyticsStub stub = new AnalyticsStub();
        ManageScrollFiltering cli = newCliWithInput("", stub);
        assertDoesNotThrow(cli::run);
    }
}