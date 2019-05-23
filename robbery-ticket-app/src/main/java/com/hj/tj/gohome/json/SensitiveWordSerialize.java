package com.hj.tj.gohome.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.hj.tj.gohome.annotation.SensitiveWord;
import com.hj.tj.gohome.utils.SensitiveWordUtil;
import java.io.IOException;
import java.util.Objects;

public class SensitiveWordSerialize extends JsonSerializer<String> implements ContextualSerializer {

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(SensitiveWordUtil.replace(s));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty == null) {
            return serializerProvider.findNullValueSerializer(beanProperty);
        }

        if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
            SensitiveWord annotation = beanProperty.getAnnotation(SensitiveWord.class);
            if (annotation == null) {
                annotation = beanProperty.getContextAnnotation(SensitiveWord.class);
            }
            if (annotation != null) {
                return new SensitiveWordSerialize();
            }
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }
}
