package cn.flizi.cloud.upms.biz.service.impl;

import cn.flizi.cloud.common.core.datascope.DataScope;
import cn.flizi.cloud.common.core.utils.R;
import cn.flizi.cloud.common.core.utils.TreeUtils;
import cn.flizi.cloud.common.security.SecurityUtils;
import cn.flizi.cloud.upms.api.entity.UpmsAuthority;
import cn.flizi.cloud.upms.api.entity.UpmsUser;
import cn.flizi.cloud.upms.api.entity.UpmsUserRole;
import cn.flizi.cloud.upms.api.vo.AuthUserInfoVo;
import cn.flizi.cloud.upms.biz.mapper.DeptMapper;
import cn.flizi.cloud.upms.biz.mapper.UserMapper;
import cn.flizi.cloud.upms.biz.mapper.UserRoleMapper;
import cn.flizi.cloud.upms.biz.service.UserRoleService;
import cn.flizi.cloud.upms.biz.service.UserService;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UpmsUser> implements UserService {

    private final UserMapper userMapper;
    private final UserRoleService userRoleService;
    private final UserRoleMapper userRoleMapper;
    private final DeptMapper deptMapper;

    @Override
    public R userInfo() {
        Integer userId = SecurityUtils.userId();
        HashMap<String, Object> result = new HashMap<>();
        UpmsUser upmsUser = userMapper.selectById(SecurityUtils.userId());
        // 查询用户个人信息
        result.put("info", upmsUser);
        // 查询用户角色信息
        result.put("roles", userMapper.roles(userId));
        // 所属部门
        result.put("dept", deptMapper.selectById(upmsUser.getDeptId()).getName());
        // 功能: 1
        result.put("functions", userMapper.authorities(userId, 1));
        // 菜单: 0
        List<UpmsAuthority> menus = userMapper.authorities(userId, 0);
        result.put("menus", TreeUtil.build(menus, TreeUtils.ROOT_PARENT_ID, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getWeight());
            tree.setName(treeNode.getName());
            tree.putExtra("path", treeNode.getPath());
            tree.putExtra("hidden", treeNode.getHidden());
            tree.putExtra("alwaysShow", treeNode.getAlwaysShow());
            tree.putExtra("redirect", treeNode.getRedirect());
            tree.putExtra("type", treeNode.getType());
            tree.put("component", treeNode.getComponent());
            HashMap<String, Object> meta = new HashMap<>();
            meta.put("title", treeNode.getTitle());
            meta.put("icon", treeNode.getIcon());
            meta.put("breadcrumb", treeNode.getBreadcrumb());
            tree.putExtra("meta", meta);
        }));
        return R.ok(result);
    }

    @Override
    public R getPage(Page<UpmsUser> page, String username, String phone, Integer deptId, Boolean enabled) {
        DataScope dataScope = new DataScope();
        dataScope.setScopeOwnName("id");
        IPage<UpmsUser> result = userMapper.getPage(page, dataScope, username, phone, deptId, enabled);
        return R.ok(result);
    }

    @Override
    public R saveOrUpdateItem(UpmsUser upmsUser) {
        // 更新用户信息
        upmsUser.updateById();

        // 更新角色信息
        if (upmsUser.getRoles() != null) {
            List<UpmsUserRole> roleMenuList = Arrays.stream(upmsUser.getRoles()).map(userId -> {
                UpmsUserRole userRole = new UpmsUserRole();
                userRole.setRoleId(userId);
                userRole.setUserId(upmsUser.getId());
                return userRole;
            }).collect(Collectors.toList());
            userRoleMapper.delete(Wrappers.<UpmsUserRole>query().lambda()
                    .eq(UpmsUserRole::getUserId, upmsUser.getId()));
            userRoleService.saveBatch(roleMenuList);
        }

        return R.ok();
    }

    @Override
    public AuthUserInfoVo getAuthUserByUsername(String username) {
        AuthUserInfoVo userInfo = new AuthUserInfoVo();

        UpmsUser user = userMapper.selectOne(Wrappers.<UpmsUser>lambdaQuery()
                .eq(UpmsUser::getUsername, username));

        if (user == null) {
            return null;
        }

        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setPassword(user.getPassword());
        userInfo.setRoles(userMapper.roleNames(user.getId()).toArray(new String[0]));
        userInfo.setAuthorities(userMapper.authorityNames(user.getId(), UpmsAuthority.FUN).toArray(new String[0]));
        return userInfo;
    }
}
