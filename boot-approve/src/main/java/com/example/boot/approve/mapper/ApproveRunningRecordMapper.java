package com.example.boot.approve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.boot.approve.entity.runtime.ApproveRunningRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created  on 2022/3/15 10:10:08
 *
 * @author zl
 */
@Mapper
public interface ApproveRunningRecordMapper extends BaseMapper<ApproveRunningRecord> {

    @Update("delete from approve_running_record where node_record_id in ( #{nodeIds} ) ")
    void deleteByNodeIds(List<Long> nodeIds);
}
