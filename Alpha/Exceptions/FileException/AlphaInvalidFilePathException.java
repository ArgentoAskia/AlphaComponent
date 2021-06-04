package Alpha.Exceptions.FileException;

import java.io.IOException;

public class AlphaInvalidFilePathException extends IOException {
    public AlphaInvalidFilePathException(String s){
        super(s);
    }
    public AlphaInvalidFilePathException(Throwable cause){
        super(cause);
    }
    public AlphaInvalidFilePathException(String s, Throwable cause){
        super(s,cause);
    }
    public AlphaInvalidFilePathException(){
        super();
    }
}
