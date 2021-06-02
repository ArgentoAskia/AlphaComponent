package Alpha.Exceptions.ServerException;

public class AlphaFileTransException extends AlphaServerException {
    public AlphaFileTransException(){
        super();
    }
    public AlphaFileTransException(Throwable cause){
        super(cause);
    }
    public AlphaFileTransException(String s, Throwable cause){
        super(s,cause);
    }
    public AlphaFileTransException(String s){
        super(s);
    }
    public AlphaFileTransException(AlphaServerException ase){
        super(ase.getMessage(), ase);
    }
}
/*
请不要滥用异常
除非知道怎么解决，否则请再次抛出异常
最好针对有异常的语句单独使用try。。。catch。。。不要贪图方便，将一大堆代码放进一个大try语句块
实在需要上面这样做的，请在try。。catch中分清那些异常能够被处理！！
 */