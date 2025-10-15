package fracture.detector.backend.exceptions;

public class AnalisiFratturaException extends Exception {
    public AnalisiFratturaException() {
        super("Analisi frattura can't be created for this user");
    }
}
