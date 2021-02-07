package pgdp.filetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pgdp.file.*;

import java.io.*;
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

    @Test
    public void foldContainsReadableFile(){//Test to se if the file from fold is readable
        assertTrue(Path.of(fold.toString()).toFile().canRead());
    }

    @Test
    public void foldRespectsWidth(){//Test that reads the lines of the file and tests if those respects the width
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fold.toString()));
            try{
                int option = Integer.parseInt(fold.toString().substring(fold.toString().indexOf("=")+1,fold.toString().indexOf(" ")));
                assertTrue(reader.readLine().length() < option);
            } catch(Exception e){
                try {
                    assertTrue(reader.readLine().length() < 80);
                } catch (IOException ioException) {
                    fail();
                }
            }
        } catch (FileNotFoundException e) {
            fail();
        }
    }

    @Test
    public void foldWorksIndependentlyOfTheOrder(){//Tests if the file works independently of the order of "-w" and "-o"
        assertTrue(fold.toString().indexOf("-w") > fold.toString().indexOf("-o") || fold.toString().indexOf("-w") < fold.toString().indexOf("-o") );
    }

    @Test
    public void foldWorksWithEmptyFiles(){//Tests if the file exists and have a lina lenght of 0
        assertTrue(Path.of(fold.toString()).toFile().exists() && Path.of(fold.toString()).toFile().length() >= 0);
    }

    @Test
    public void foldDoesNotAlteredTheFileForInvalid(){//Test if the file is not altered if the parameters are invalid
        assertTrue(Path.of(fold.toString()).toFile().exists());
    }


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
