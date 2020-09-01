package cn.flizi.cloud.upms.biz.service;

import cn.flizi.cloud.common.core.utils.R;
import cn.flizi.cloud.upms.api.entity.UpmsUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<UpmsUser> {

    R userInfo();

    R getPage(Page<UpmsUser> page, String username, String phone, Integer deptId, Boolean enabled);

    R saveOrUpdateItem(UpmsUser upmsUser);
}
