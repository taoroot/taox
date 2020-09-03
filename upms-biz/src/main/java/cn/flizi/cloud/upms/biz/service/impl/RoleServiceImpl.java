package cn.flizi.cloud.upms.biz.service.impl;

import cn.flizi.cloud.common.core.datascope.DataScopeTypeEnum;
import cn.flizi.cloud.common.core.utils.R;
import cn.flizi.cloud.common.security.SecurityUtils;
import cn.flizi.cloud.upms.api.entity.UpmsRole;
import cn.flizi.cloud.upms.api.entity.UpmsRoleAuthority;
import cn.flizi.cloud.upms.api.entity.UpmsUser;
import cn.flizi.cloud.upms.biz.mapper.RoleAuthorityMapper;
import cn.flizi.cloud.upms.biz.mapper.RoleMapper;
import cn.flizi.cloud.upms.biz.mapper.UserMapper;
import cn.flizi.cloud.upms.biz.service.DeptService;
import cn.flizi.cloud.upms.biz.service.RoleAuthorityService;
import cn.flizi.cloud.upms.biz.service.RoleService;
import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, UpmsRole> implements RoleService {

    private final RoleMapper roleMapper;
    private final RoleAuthorityService roleAuthorityService;
    private final RoleAuthorityMapper roleAuthorityMapper;
    private final UserMapper userMapper;
    private final DeptService deptService;

    @Override
    public R getPage(Page<UpmsRole> page) {
        return R.ok(roleMapper.getPage(page));
    }

    @Override
    public R saveOrUpdateItem(UpmsRole upmsRole) {
        List<Integer> deptIds = new ArrayList<>();
        UpmsUser upmsUser = userMapper.selectById(SecurityUtils.userId());

        // 全部
        if (upmsRole.getScopeType().equals(DataScopeTypeEnum.ALL)) {
            upmsRole.setScope(new Integer[]{});
            upmsRole.setScope(deptIds.toArray(new Integer[0]));
        }

        // 本级
        if (upmsRole.getScopeType().equals(DataScopeTypeEnum.THIS_LEVEL)) {
            deptIds.add(upmsUser.getDeptId());
            upmsRole.setScope(deptIds.toArray(new Integer[0]));
        }

        // 本级, 下级
        if (upmsRole.getScopeType().equals(DataScopeTypeEnum.THIS_LEVEL_CHILDREN)) {
            deptIds.add(upmsUser.getDeptId()); // 本级
            List<Tree<Integer>> tree = deptService.tree(upmsUser.getDeptId()); // 下级
            treeToList(deptIds, tree);
            upmsRole.setScope(deptIds.toArray(new Integer[0]));
        }

        // 自定义不能空
        if (upmsRole.getScopeType().equals(DataScopeTypeEnum.CUSTOMIZE)) {
            if (upmsRole.getScope() == null || upmsRole.getScope().length == 0) {
                throw new RuntimeException("自定义权限范围时,必须至少包含一个范围");
            }
        }

        // 用户级别,无部门id
       if (upmsRole.getScopeType().equals(DataScopeTypeEnum.CUSTOMIZE)) {
           deptIds.clear();
        }


        saveOrUpdate(upmsRole);

        // 更新角色权限
        if (upmsRole.getAuthorities() != null) {
            List<UpmsRoleAuthority> roleMenuList = Arrays.stream(upmsRole.getAuthorities()).map(menuId -> {
                UpmsRoleAuthority roleMenu = new UpmsRoleAuthority();
                roleMenu.setRoleId(upmsRole.getId());
                roleMenu.setAuthorityId(menuId);
                return roleMenu;
            }).collect(Collectors.toList());

            roleAuthorityMapper.delete(Wrappers.<UpmsRoleAuthority>query().lambda()
                    .eq(UpmsRoleAuthority::getRoleId, upmsRole.getId()));
            roleAuthorityService.saveBatch(roleMenuList);
        }

        return R.ok();
    }

    private void treeToList(List<Integer> list, List<Tree<Integer>> tree) {
        for (Tree<Integer> node : tree) {
            list.add(node.getId());
            if (node.getChildren() != null && node.getChildren().size() > 0) {
                treeToList(list, node.getChildren());
            }
        }
    }
}
