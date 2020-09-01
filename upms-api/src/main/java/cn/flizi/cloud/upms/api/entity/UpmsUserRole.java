package cn.flizi.cloud.upms.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@TableName("upms_user_role")
@EqualsAndHashCode(callSuper = true)
public class UpmsUserRole extends Model<UpmsUserRole> {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private Integer roleId;
}
