package cn.flizi.cloud.auth.service;

import cn.flizi.cloud.auth.AuthUtils;
import cn.flizi.cloud.auth.api.entity.AuthUser;
import cn.flizi.cloud.auth.service.mapper.UserMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser user = userMapper.selectOne(Wrappers.<AuthUser>lambdaQuery()
                .eq(AuthUser::getUsername, username));

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        return AuthUtils.translate(user);
    }
}
