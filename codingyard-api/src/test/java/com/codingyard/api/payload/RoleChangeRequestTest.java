package com.codingyard.api.payload;

import com.codingyard.api.BasicJsonTest;
import com.codingyard.api.entity.user.Role;

public class RoleChangeRequestTest extends BasicJsonTest<RoleChangeRequest> {

    private final RoleChangeRequest payload = new RoleChangeRequest(1L, Role.ADMIN);

    @Override
    public String getJsonFilePath() {
        return "fixtures/payload/role_change.json";
    }

    @Override
    public RoleChangeRequest getValidPojo() {
        return payload;
    }

    @Override
    public Class<? extends RoleChangeRequest> getEntityClass() {
        return RoleChangeRequest.class;
    }
}

