package cn.flizi.cloud.upms.biz.service.impl;


import cn.flizi.cloud.upms.api.entity.UpmsRoleAuthority;
import cn.flizi.cloud.upms.biz.mapper.RoleAuthorityMapper;
import cn.flizi.cloud.upms.biz.service.RoleAuthorityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class RoleAuthorityServiceImpl extends ServiceImpl<RoleAuthorityMapper, UpmsRoleAuthority> implements RoleAuthorityService {


}
