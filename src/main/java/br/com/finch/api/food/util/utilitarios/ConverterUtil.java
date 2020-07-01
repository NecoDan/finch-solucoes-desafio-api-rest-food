package br.com.finch.api.food.util.utilitarios;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class ConverterUtil {

    private ConverterUtil() {

    }

    public static <T> Object convertJSONFromStringToObjeto(String conteudoJSON, Class<T> objectClass) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        JavaTimeModule module = new JavaTimeModule();
        objectMapper.registerModule(module);
        return objectMapper.readValue(conteudoJSON, objectClass);
    }

    public static String asJSONString(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}
