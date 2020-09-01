package cn.flizi.cloud.upms.biz.web;

import cn.flizi.cloud.common.core.utils.R;
import cn.flizi.cloud.common.core.utils.TreeUtils;
import cn.flizi.cloud.upms.api.entity.UpmsDept;
import cn.flizi.cloud.upms.biz.service.DeptService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class DeptController {

    private final DeptService deptService;

    @PostMapping("/dept")
    public R saveItem(@RequestBody UpmsDept upmsDept) {
        return R.ok(deptService.save(upmsDept));
    }

    @DeleteMapping("/dept")
    public R delItem(@RequestParam List<Integer> ids) {
        return R.ok(deptService.removeByIds(ids));
    }

    @GetMapping("/dept/{id}")
    public R delItem(@PathVariable Integer id) {
        return R.ok(deptService.getById(id));
    }

    @PutMapping("/dept")
    public R updateItem(@RequestBody UpmsDept upmsDept) {
        if (upmsDept.getParentId().equals(upmsDept.getId())) {
            throw new IllegalArgumentException("参数有误, 不能设置自己为上一级");
        }
        return R.ok(deptService.updateById(upmsDept));
    }

    @GetMapping("/depts")
    public R getPage(@RequestParam(defaultValue = "" + TreeUtils.ROOT_PARENT_ID) Integer parentId,
                     @RequestParam(required = false) String name) {
        return deptService.tree(parentId, name);
    }
}

