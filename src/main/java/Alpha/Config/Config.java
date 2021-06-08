package Alpha.Config;

/**
 * 配置信息的接口.
 * <p>该接口涵盖各类主机的信息,包括但不仅限于IP地址、主机名称、端口等
 *
 * @author Askia
 * @version 1.0
 * @since dev-2.0
 * @param <T> 信息的原始类型，如端口为整型（int）、主机名称为字符串类型（String）。
 */
public interface Config<T> {

    /**
     * 获取该信息接口
     * @return 信息
     */
    T get();
}
