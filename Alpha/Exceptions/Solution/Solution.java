package Alpha.Exceptions.Solution;

import java.io.IOException;

/**
 * 在已知异常如何解决的情况下,提供一套可行的解决方案.
 */
public abstract class Solution<T> {
    protected Throwable exception;                    // 后期需要更多信息可以继承该类
    protected T keyData;
    public Solution(Throwable exception, T keyData){
        this.exception = exception;
        this.keyData = keyData;
    }
    protected abstract T solution(Throwable e, T keyData);       // 参数未完全确定
    protected T solve(){
        return solution(exception, keyData);
    }
}
