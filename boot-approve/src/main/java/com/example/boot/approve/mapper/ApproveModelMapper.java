package com.example.boot.approve.mapper;

import com.example.boot.approve.common.mybaitsplus.MyBaseMapper;
import com.example.boot.approve.entity.config.ApproveModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created  on 2022/3/8 15:15:52
 *
 * @author wmz
 */
@Mapper
public interface ApproveModelMapper extends MyBaseMapper<ApproveModel> {

    /**
     * 查询当前模板的最大版本号
     *
     * @param modelId version
     * @return 最大版本号
     */
    @Select(value = "SELECT MAX(t.version) FROM approve_model t WHERE t.id = #{modelId} GROUP BY t.id")
    int selectMaxVersionByModelId(long modelId);

}
