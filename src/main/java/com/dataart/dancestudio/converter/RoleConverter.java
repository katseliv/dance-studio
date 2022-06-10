package com.dataart.dancestudio.converter;

import com.dataart.dancestudio.model.Role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final Role attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public Role convertToEntityAttribute(final Integer dbData) {
        return Role.of(dbData).orElse(null);
    }

}
