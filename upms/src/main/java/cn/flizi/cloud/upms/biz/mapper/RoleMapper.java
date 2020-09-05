package cn.flizi.cloud.upms.biz.mapper;

import cn.flizi.cloud.upms.api.entity.UpmsRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @author : zhiyi
 * Date: 2020/2/11
 */
public interface RoleMapper extends BaseMapper<UpmsRole> {

    IPage<UpmsRole> getPage(Page<UpmsRole> page);

    List<Integer> selectAuthoritiesByRole(Integer roleId);
}
