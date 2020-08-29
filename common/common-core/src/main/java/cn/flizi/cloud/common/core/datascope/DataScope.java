package cn.flizi.cloud.common.core.datascope;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class DataScope implements Serializable {

    /**
     * 限制范围为部门时字段名称
     */
    private String scopeAlias = "dept_id";
    /**
     * 当权限范围本用户时字段名称
     */
    private String scopeOwnAlias = "user_id";

    /**
     * 具体的数据范围
     */
    private List<Integer> scopes = new ArrayList<>();

    /**
     * 默认查询所有
     */
    private DataScopeTypeEnum dataScopeTypeEnum = DataScopeTypeEnum.ALL;
}
