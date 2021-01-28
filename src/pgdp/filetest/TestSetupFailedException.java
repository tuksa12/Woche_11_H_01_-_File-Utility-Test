package pgdp.filetest;

//This exception is thrown when preparing/cleaning up the test environment fails
public class TestSetupFailedException extends RuntimeException {
    public TestSetupFailedException(String e) {
        super(e);
    }
}
