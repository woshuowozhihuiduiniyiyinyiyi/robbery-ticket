package com.hj.tj.gohome.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.hj.tj.gohome.annotation.SensitiveInfo;
import com.hj.tj.gohome.enums.SensitiveTypeEnum;
import com.hj.tj.gohome.utils.SensitiveInfoUtil;

import java.io.IOException;
import java.util.Objects;

public class SensitiveInfoSerialize extends JsonSerializer<String> implements ContextualSerializer {

    private SensitiveTypeEnum type;

    public SensitiveInfoSerialize() {
    }

    public SensitiveInfoSerialize(final SensitiveTypeEnum type) {
        this.type = type;
    }

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        switch (this.type) {
            case CHINESE_NAME: {
                jsonGenerator.writeString(SensitiveInfoUtil.chineseName(s));
                break;
            }
            case ID_CARD: {
                jsonGenerator.writeString(SensitiveInfoUtil.idCardNum(s));
                break;
            }
            case FIXED_PHONE: {
                jsonGenerator.writeString(SensitiveInfoUtil.fixedPhone(s));
                break;
            }
            case MOBILE_PHONE: {
                jsonGenerator.writeString(SensitiveInfoUtil.mobilePhone(s));
                break;
            }
            case ADDRESS: {
                jsonGenerator.writeString(SensitiveInfoUtil.address(s, 4));
                break;
            }
            case EMAIL: {
                jsonGenerator.writeString(SensitiveInfoUtil.email(s));
                break;
            }
            case BANK_CARD: {
                jsonGenerator.writeString(SensitiveInfoUtil.bankCard(s));
                break;
            }
            case CNAPS_CODE: {
                jsonGenerator.writeString(SensitiveInfoUtil.cnapsCode(s));
                break;
            }
            default: {
                jsonGenerator.writeString(s);
            }
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {

        if (beanProperty == null) {
            return serializerProvider.findNullValueSerializer(beanProperty);
        }

        if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
            SensitiveInfo sensitiveInfo = beanProperty.getAnnotation(SensitiveInfo.class);
            if (sensitiveInfo == null) {
                sensitiveInfo = beanProperty.getContextAnnotation(SensitiveInfo.class);
            }
            if (sensitiveInfo != null) {
                return new SensitiveInfoSerialize(sensitiveInfo.value());
            }
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);

    }
}
