package pgdp.filetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pgdp.file.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FoldTest {
    //DO NOT CHANGE THE SIGNATURE OF THIS ATTRIBUTE!
    //ALWAYS USE THIS ATTRIBUTE TO CALL THE MAIN METHOD OF THE FOLD IMPLEMENTATION
    //You can, however, change the initial value of this attribute here to other Fold implementations
    //But do not change this value anywhere else, only right here!
    //And before submitting to artemis, make sure to change this back to DummyFold::main or just don't set any value!
    public static FoldImplementation fold = DummyFold::main;

    //TODO: Write good tests...


    //Helper Methods
    //this methods recursively deletes everything in the directory
    private static void deleteAllInSandbox() {
        //traverse the sandbox directory in reverse order (to delete deepest first)
        try (Stream<Path> stream = Files.walk(Path.of("sandbox"))) {
            stream.skip(1) //don't delete the sandbox folder itself
                    .sorted(Comparator.reverseOrder())
                    .forEach(file -> {
                        try {
                            Files.delete(file);
                        } catch (IOException e) {
                            throw new TestSetupFailedException("Error trying to delete test file: " + e);
                        }
                    });
        } catch (IOException | UncheckedIOException e) {
            throw new TestSetupFailedException("Error trying to clean up the sandbox: " + e);
        }
    }

    //this method copies everything inside the file_vault into the sandbox recursively
    private static void copyAllFromVaultToSandbox() {
        Path source = Path.of("file_vault"), destination = Path.of("sandbox");
        //traverse the directory and copy every file and folder within it
        try (Stream<Path> stream = Files.walk(source)) {
            stream.skip(1) //don't copy the files folder itself
                    .forEach(file -> {
                        try {
                            Files.copy(file, destination.resolve(source.relativize(file)));
                        } catch (IOException e) {
                            throw new TestSetupFailedException("Error trying to copy test file: " + e);
                        }
                    });
        } catch (IOException | UncheckedIOException e) {
            throw new TestSetupFailedException("Error trying to walk over test files: " + e);
        }
    }
}
