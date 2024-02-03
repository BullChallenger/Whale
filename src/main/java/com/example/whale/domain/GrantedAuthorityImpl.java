package com.example.whale.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GrantedAuthorityImpl implements GrantedAuthority {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    @JsonProperty("authority")
    private String role;

    public GrantedAuthorityImpl(String role) {
        Assert.hasText(role, "A granted authority textual representation is required");
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return this.role;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof GrantedAuthorityImpl) {
            return this.role.equals(((GrantedAuthorityImpl) obj).role);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.role.hashCode();
    }

    @Override
    public String toString() {
        return this.role;
    }

}
