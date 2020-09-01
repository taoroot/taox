package cn.flizi.cloud.upms.biz.service.impl;


import cn.flizi.cloud.upms.api.entity.UpmsUserRole;
import cn.flizi.cloud.upms.biz.mapper.UserRoleMapper;
import cn.flizi.cloud.upms.biz.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UpmsUserRole> implements UserRoleService {

}
