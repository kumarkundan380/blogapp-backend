package com.blogapp.serializer;

import com.blogapp.dto.RoleDTO;
import com.blogapp.dto.UserDTO;
import com.blogapp.util.AuthorityUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserDTOSerializer extends StdSerializer<UserDTO> {

    public UserDTOSerializer() {
        this(null);
    }

    public UserDTOSerializer(Class<UserDTO> userDTOClass) {
        super(userDTOClass);
    }

    @Override
    public void serialize(UserDTO user, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("userId", user.getUserId());
        gen.writeStringField("userName", user.getUserName());
        gen.writeStringField("name", user.getFirstName());
        gen.writeStringField("gender", user.getGender().getValue());
        gen.writeStringField("phoneNumber", user.getPhoneNumber());
        gen.writeStringField("userImage", user.getUserImage());
        gen.writeFieldName("roles");
        gen.writeStartArray();
        for(RoleDTO roleDTO: user.getRoles()){
            gen.writeStartObject();
            gen.writeNumberField("roleId", roleDTO.getId());
            gen.writeStringField("roleName",roleDTO.getRoleName().getValue());
            gen.writeStringField("description", roleDTO.getDescription());
            gen.writeEndObject();
        }
        gen.writeEndArray();
        if(AuthorityUtil.isAdminRole()) {
            gen.writeStringField("createdAt", user.getCreatedAt().toString());
            gen.writeStringField("updatedAt", user.getUpdatedAt().toString());
            gen.writeStringField("status", user.getUserStatus().getValue());
            gen.writeBooleanField("isUserVerified", user.getIsUserVerified());
            gen.writeBooleanField("isAccountExpired", user.getIsAccountExpired());
            gen.writeBooleanField("isCredentialsExpired", user.getIsCredentialsExpired());
            gen.writeBooleanField("isAccountLocked", user.getIsAccountLocked());
        }
        gen.writeEndObject();
    }
}
