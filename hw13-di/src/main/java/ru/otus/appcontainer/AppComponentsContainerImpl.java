package ru.otus.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.exception.AppComponentNotFoundException;
import ru.otus.appcontainer.api.exception.AppComponentsConfigNotFoundException;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.api.exception.MultipleAppComponentInstanceException;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

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
        Arrays.stream(configClass)
              .sorted(Comparator.comparing(c -> c.getAnnotation(AppComponentsContainerConfig.class).order()))
              .forEach(this::processComponent);
    }

    private void processComponent(Class<?> configClass) {
        var methodList = Arrays.stream(configClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparing(m -> m.getAnnotation(AppComponent.class).order()))
                .toList();

        logger.debug("methodList: {}", methodList);

        try {
            Object configClassInstance = configClass.getDeclaredConstructor().newInstance();

            for(Method method : methodList){
                method.setAccessible(true);
                Object appComponent = method.invoke(configClassInstance, filterMethodArgs(method));
                appComponents.add(appComponent);
                appComponentsByName.put(method.toString(), appComponent);
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
        var objList = new ArrayList<>();
        for(Object obj : appComponents){
            if(objList.size() > 1) throw new MultipleAppComponentInstanceException();
            if(componentClass.isInstance(obj)){
                objList.add(obj);
            }
        }
        if(objList.size() == 0) throw new AppComponentNotFoundException();
        return (C)objList.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        Optional<C> comp = (Optional<C>) appComponentsByName.entrySet().stream()
                                                                    .filter(e -> e.getKey().contains(componentName))
                                                                    .map(Map.Entry::getValue)
                                                                    .findFirst();
        return comp.orElseThrow();
    }

    private void checkConfigClass(Class<?>... configClass) {
        Arrays.stream(configClass).forEach(c -> {
            if (!c.isAnnotationPresent(AppComponentsContainerConfig.class)) {
                throw new IllegalArgumentException(String.format("Given class is not config %s", c.getName()));
            }
        });
    }

    private Object[] filterMethodArgs(Method method){
        logger.debug("Method to filter args: {}", method.toString());

        return Arrays.stream(method.getParameterTypes())
                        .map(this::getAppComponent)
                        .toArray();
    }
}
