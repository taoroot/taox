package cn.flizi.cloud.upms.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("upms_role_authority")
@EqualsAndHashCode(callSuper = true)
public class UpmsRoleAuthority extends Model<UpmsRoleAuthority> {

	private static final long serialVersionUID = 1L;

	private Integer roleId;

	private Integer authorityId;
}
