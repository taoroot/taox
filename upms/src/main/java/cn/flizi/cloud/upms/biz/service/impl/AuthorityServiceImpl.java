package cn.flizi.cloud.upms.biz.service.impl;


import cn.flizi.cloud.common.core.utils.R;
import cn.flizi.cloud.common.core.utils.TreeUtils;
import cn.flizi.cloud.upms.api.entity.UpmsAuthority;
import cn.flizi.cloud.upms.api.entity.UpmsRoleAuthority;
import cn.flizi.cloud.upms.biz.mapper.AuthorityMapper;
import cn.flizi.cloud.upms.biz.mapper.RoleAuthorityMapper;
import cn.flizi.cloud.upms.biz.service.AuthorityService;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;


@Service
@AllArgsConstructor
public class AuthorityServiceImpl extends ServiceImpl<AuthorityMapper, UpmsAuthority> implements AuthorityService {

    private final AuthorityMapper authorityMapper;
    private final RoleAuthorityMapper roleAuthorityMapper;

    @Override
    public R getTree(String title, Boolean hidden) {
        LambdaQueryWrapper<UpmsAuthority> query = Wrappers.lambdaQuery();

        if (hidden != null) {
            query.eq(UpmsAuthority::getHidden, hidden);
        }

        if (!StringUtils.isEmpty(title)) {
            query.like(UpmsAuthority::getTitle, title);
        }

        List<UpmsAuthority> sysAuthorities = authorityMapper.selectList(query);

        List<Tree<Integer>> authorityData = TreeUtil.build(sysAuthorities, TreeUtils.ROOT_PARENT_ID, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getWeight());
            tree.setName(treeNode.getName());
            tree.putExtra("path", treeNode.getPath());
            tree.putExtra("type", treeNode.getType());
            tree.putExtra("component", treeNode.getComponent());
            tree.putExtra("hidden", treeNode.getHidden());
            tree.putExtra("alwaysShow", treeNode.getAlwaysShow());
            tree.putExtra("redirect", treeNode.getRedirect());
            tree.putExtra("title", treeNode.getTitle());
            tree.putExtra("icon", treeNode.getIcon());
            tree.putExtra("authority", treeNode.getAuthority());
            tree.putExtra("breadcrumb", treeNode.getBreadcrumb());
        });

        if (authorityData.size() == 0) {
            return R.ok(sysAuthorities);
        }

        return R.ok(authorityData);
    }

    @Override
    public boolean removeById(Serializable id) {
        Integer count = roleAuthorityMapper.selectCount(Wrappers.<UpmsRoleAuthority>lambdaQuery()
                .eq(UpmsRoleAuthority::getAuthorityId, id));
        if (count > 0) {
            throw new RuntimeException("资源被角色绑定,请先解绑");
        }
        return super.removeById(id);
    }

    @Override
    public boolean saveOrUpdate(UpmsAuthority entity) {
        return super.saveOrUpdate(entity);
    }
}
