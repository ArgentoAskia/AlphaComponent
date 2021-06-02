package Alpha.Exceptions.FileException;

import java.io.IOException;

public class AlphaMakeFileException extends IOException {
    public AlphaMakeFileException(){
        super();
    }
    public AlphaMakeFileException(String s){
        super(s);
    }
    public AlphaMakeFileException(String s,Throwable cause){
        super(s,cause);
    }
    public AlphaMakeFileException(Throwable cause){
        super(cause);
    }
}
