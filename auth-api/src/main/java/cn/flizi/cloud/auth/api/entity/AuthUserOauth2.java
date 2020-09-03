package cn.flizi.cloud.auth.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("upms_user_oauth2")
public class AuthUserOauth2 {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String clientRegistrationId;
    private String principalName;
    private String nickname;
    private String avatar;
    private String userId;
    private LocalDateTime createdAt;
}
