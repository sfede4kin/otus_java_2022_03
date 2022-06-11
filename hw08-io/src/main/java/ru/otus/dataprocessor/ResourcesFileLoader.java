package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;
import ru.otus.model.MeasurementMixIn;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private final String fileName;
    private final ObjectMapper mapper = new ObjectMapper();

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        try(InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            mapper.addMixIn(Measurement.class, MeasurementMixIn.class);
            return mapper.readValue(is, new TypeReference<>(){});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
