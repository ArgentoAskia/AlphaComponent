package Alpha.Exceptions.ResponsorException;

import Alpha.Exceptions.ServerException.AlphaServerException;

public class AlphaResponsorException extends AlphaServerException {
    public AlphaResponsorException(String s){
        super(s);
    }
    public AlphaResponsorException(){
        super();
    }
    public AlphaResponsorException(Throwable cause){
        super(cause);
    }
    public AlphaResponsorException(String s, Throwable cause){
        super(s, cause);
    }

    /**
     * 打包父类异常信息
     * @param ase   AlphaServerException类异常,对于抛出父类异常可以进行具体的打包工作!更多参考{@link AlphaServerException}
     */
    public AlphaResponsorException(AlphaServerException ase){
        super(ase.getMessage(),ase);
    }
}
