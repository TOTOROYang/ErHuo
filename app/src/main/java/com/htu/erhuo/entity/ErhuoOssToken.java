package com.htu.erhuo.entity;
/**
 * Description
 * Created by yzw on 2017/3/19.
 */

public class ErhuoOssToken{

    /**
     * securityToken : CAISoQN1q6Ft5B2yfSjIra7eB9//o7Z4z5esa0uDonpnPcd+hafKoDz2IHtNf3dpCeAXsf0wnmFV6f0SlqMqEMcVGRaYNZIvth2WAcVnINivgde8yJBZohPMewHKeZaSvqL7Z+H+U6k3E5XJQlvYlyh17KLnfDG5JTKMOoGIjpgVbLZyWRKjPxVLGPBcJAZptK1/MmDKZ86wLjnggGfbECgQvRFn209y7amjz+qW6BfVkD+fzfQO9aDwOYScZtNwJ/UPVMyujsV8cbDd9TNU9xlS/b1qsbRA/j7L3LaaGEIDxxSdL83e8NBkMBQDXMpcIaNfq+XmnvBVo/Hak5+NqyxAJuZISS/SNt7CssLPA7GuLc1rN+S5aX3VypWFP4L49gQ/ejVZVnNDcMFzLWRrW15+CGPTMrfi8lnRb0KkULOnqPhsgMcpkA65oYvReAnWH+2jvHxGasNmXSQBLAUL2GHtSKgCfjFXfklvb7TvFt4qN04H+fO54lSNDHw9kSoHpY73Y/LHp6YYcp7jWZFL14UQYplcunctVU7wT7++Jru62KTKj9wagAEroUoiKXXhdWqGTGG+cwVmHl7es+jvvdFyX9OvFfXcX5CwQor5sXfrelRtgiG1z2xJG5wJ/YDlkGkH9ipqxDTv4DS6TxBfYdy3H8+vFyfiz1PIKv1f8EnTUXiywAFfJvtF7zU/sajqiaNCSRkMuMsfTGbhLuP/igqARmiQcX4+6g==
     * accessKeySecret : 8F3bFzDAu3gxCfTwCXWRUbP9j4jZbC8c3SXs7qSzZwUq
     * accessKeyId : STS.LmkLeKNiYxUGim2Fz21HRjbaB
     * expiration : 2017-03-19T08:54:08Z
     */

    private String securityToken;
    private String accessKeySecret;
    private String accessKeyId;
    private String expiration;

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
}
