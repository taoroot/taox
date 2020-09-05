package cn.flizi.cloud.upms.biz.service;

import cn.flizi.cloud.common.core.utils.R;
import cn.flizi.cloud.upms.api.entity.UpmsAuthority;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AuthorityService extends IService<UpmsAuthority> {
    R getTree(String title, Boolean hidden);
}
