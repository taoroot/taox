package cn.flizi.cloud.upms.biz.mapper;


import cn.flizi.cloud.common.core.datascope.DataScope;
import cn.flizi.cloud.upms.api.entity.UpmsAuthority;
import cn.flizi.cloud.upms.api.entity.UpmsRole;
import cn.flizi.cloud.upms.api.entity.UpmsUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : zhiyi
 * Date: 2020/2/11
 */
public interface UserMapper extends BaseMapper<UpmsUser> {

    List<Integer> roleIds(@Param("userId") Integer userId);

    List<UpmsRole> roles(@Param("userId") Integer userId);

    List<UpmsAuthority> authorities(@Param("userId") Integer userId, @Param("type") Integer type);

    IPage<UpmsUser> getPage(@Param("page") Page<UpmsUser> page, @Param("dataScope") DataScope dataScope,
                            @Param("username") String username,
                            @Param("phone") String phone,
                            @Param("deptId") Integer deptId,
                            @Param("enabled") Boolean enabled);
}
