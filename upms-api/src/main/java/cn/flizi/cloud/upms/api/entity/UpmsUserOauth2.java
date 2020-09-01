package cn.flizi.cloud.upms.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("upms_user_oauth2")
@EqualsAndHashCode(callSuper = true)
public class UpmsUserOauth2 extends Model<UpmsUserOauth2> {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String clientRegistrationId;
    private String principalName;
    private String nickname;
    private String avatar;
    private Integer userId;
    private LocalDateTime createdAt;
}
