package ru.otus.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsConfigNotFoundException;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private static final Logger logger = LoggerFactory.getLogger(AppComponentsContainerImpl.class);

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();
    public AppComponentsContainerImpl(Class<?>... initialConfigClass) {
        processConfig(initialConfigClass);
    }
    public AppComponentsContainerImpl(String pkg) {
        scanClasses(pkg);
    }

    private void processConfig(Class<?>... configClass) {
        checkConfigClass(configClass);
        final Map<AppComponentsContainerConfig, Class<?>> annTreeMap = new TreeMap<>(
                                                Comparator.comparing(AppComponentsContainerConfig::order));

        Arrays.stream(configClass)
                .forEach(c -> annTreeMap.put(c.getAnnotation(AppComponentsContainerConfig.class), c));

        annTreeMap.forEach((a,c) -> processComponent(c));
    }

    private void processComponent(Class<?> configClass) {
        final Map<AppComponent, Method> annTreeMap = new TreeMap<>(Comparator.comparing(a -> a.order() + a.name()));

        Arrays.stream(configClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .forEach(m -> annTreeMap.put(m.getAnnotation(AppComponent.class), m));

        logger.debug("annTreeMap: {}", annTreeMap);

        try {
            Object configClassInstance = configClass.getDeclaredConstructor().newInstance();

            for(Method method : annTreeMap.values()){
                method.setAccessible(true);
                Object appComponent = method.invoke(configClassInstance, filterMethodArgs(method, appComponents));
                appComponents.add(appComponent);
                appComponentsByName.put(method.getName(), appComponent);
            }

            logger.debug("appComponents: {}", appComponents);
            logger.debug("appComponentsByName: {}", appComponentsByName);

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void scanClasses(String pkg){
        //Фильтр исключения AppConfig.class, чтобы не было конфиликтов с другими конфигурациями
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                                            .setUrls(ClasspathHelper.forPackage(pkg))
                                            .setScanners(Scanners.TypesAnnotated)
                                            .filterInputsBy(new FilterBuilder().excludePattern(".*AppConfig.class"))
        );

        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);
        logger.debug("Classes scanned: {}", classSet);
        if(classSet.size() == 0){
            throw new AppComponentsConfigNotFoundException();
        }
        processConfig(classSet.toArray(Class<?>[]::new));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        for(Object obj : appComponents){
            if(componentClass.isInstance(obj)){
                return (C)obj;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        return (C)appComponentsByName.get(componentName);
    }

    private void checkConfigClass(Class<?>... configClass) {
        Arrays.stream(configClass).forEach(c -> {
            if (!c.isAnnotationPresent(AppComponentsContainerConfig.class)) {
                throw new IllegalArgumentException(String.format("Given class is not config %s", c.getName()));
            }
        });
    }

    private static Object[] filterMethodArgs(Method method, List<Object> appComponents){
        logger.debug("Method to filter args: {}", method.toString());
        List<Object> argsFiltered = new ArrayList<>();

        for(Class<?> pt : method.getParameterTypes()){
            for(Object obj : appComponents){
                if(pt.isInstance(obj)){
                    argsFiltered.add(obj);
                }
            }
        }
        logger.debug("Args filtered: {}", argsFiltered);
        return argsFiltered.toArray();
    }
}
