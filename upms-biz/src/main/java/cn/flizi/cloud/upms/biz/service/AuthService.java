package cn.flizi.cloud.upms.biz.service;

import cn.flizi.cloud.common.core.utils.R;
import cn.flizi.cloud.upms.api.vo.AuthUserInfoVo;

public interface AuthService {
    R userInfo();

    AuthUserInfoVo getAuthUserByUsername(String username);

    AuthUserInfoVo getAuthUserBySocial(String registrationId, String name);

}
