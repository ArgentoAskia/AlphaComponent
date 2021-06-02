package Alpha.Exceptions.ServerException;

import java.io.IOException;

public class AlphaServerException extends IOException {
    public AlphaServerException(){
        super();
    }
    public AlphaServerException(String s){
        super(s);
    }
    public AlphaServerException(String s, Throwable cause){
        super(s,cause);
    }
    public AlphaServerException(Throwable cause){
        super(cause);
    }
}
