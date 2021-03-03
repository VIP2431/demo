package ru.vip.demo.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vip.demo.entity.ItemDirectory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReadJSON {

//    public ImmutableList<ItemDirectory> getJSON( String resourceName) throws IOException {
    public List<ItemDirectory> getJSON( String resourceName) throws IOException {
        // JSON Десериализация нескольких объектов
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = Resources.getResource(resourceName).openStream();
        List<ItemDirectory> lst = mapper.readValue(inputStream, new TypeReference<List<ItemDirectory>>() {});
        return lst;
//        return ImmutableList.copyOf(items);
    }

}
