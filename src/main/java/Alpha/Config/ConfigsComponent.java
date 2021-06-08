package Alpha.Config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 信息接口的组合器
 * @param <T>
 */
public class ConfigsComponent<T> implements Config<T>{

    private HashMap<Class<? extends Config>, Config<?>> configs;
    private Class<?> typeForData;

    protected void set(T typeData) {
        typeForData = typeData.getClass();
    }
    @Override
    public T get(){
        try {
            Constructor<?> constructor = typeForData.getConstructor(null);
            T instance = (T) constructor.newInstance();
            fillInstance(instance);
            return instance;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void fillInstance(T instance) {
        Method[] methods = typeForData.getMethods();
        for (Method method:methods
             ) {
            // 调用setter方法
            if(method.getName().contains("set")){
                // 获取第一个参数类型
                Class<?> paraTypes = method.getParameterTypes()[0];
                Config<?> param = configs.get(paraTypes);
                try {
                    method.invoke(instance, param);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public <Type2>void add(Config<Type2> config){
        Class<? extends Config> c = config.getClass();
        configs.put(c, config);
    }
    public <Type2> boolean remove(Config<Type2> config){
        Class<? extends Config> c = config.getClass();
        return configs.remove(c, config);
    }

    public void add(Config<?> ...configs){
        for (Config<?> config:configs
             ) {
            add(config);
        }
    }

    public ConfigsComponent(T typeData){
        set(typeData);
        configs = new HashMap<>();
    }
}
