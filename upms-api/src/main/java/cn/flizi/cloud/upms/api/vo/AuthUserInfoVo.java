package cn.flizi.cloud.upms.api.vo;

import lombok.Data;

/**
 * 提供内部Auth-server访问接口
 */
@Data
public class AuthUserInfoVo {
    private Integer id;
    private String username;
    private String password;
    private String[] roles;
    private String[] authorities;
}
