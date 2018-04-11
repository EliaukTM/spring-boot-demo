package com.xiamu;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.collections.CollectionUtils;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.UpdatableRecord;
import org.jooq.impl.UpdatableRecordImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.xiamu.constants.SystemConst;
import com.xiamu.util.Times;

/**
 * Description:
 *
 * @author haoyuan.yang
 * @version 1.0
 * @date: 2017/3/11
 * @since JDK 1.8
 */
public abstract class BaseDao<R extends UpdatableRecordImpl> implements SystemConst {


    @Autowired
    protected DSLContext dsl;

    private static final String FIELD_ID = "id";

    private static final String FIELD_CREATE_TIME = "create_time";

    private static final String FIELD_DEL_FLAG = "del_flag";

    private static final Map<String, Byte> DEL_MAPPER = ImmutableMap.of(FIELD_DEL_FLAG, DEL);

    /**
     * Description:table
     * @return table
     */
    public abstract Table<R> table();

    public R newRecord() {
        return dsl.newRecord(table());
    }

    public R newRecord(Object object) {
        return dsl.newRecord(table(), object);
    }

    public R get(long id) {
        return dsl.selectFrom(table()).where(" id=? ", id).fetchAny();
    }

    public List<R> get(Collection<Long> ids) {

        if (CollectionUtils.isEmpty(ids)) {
            return ImmutableList.of();
        }

        StringBuilder sql = new StringBuilder(" id in (");
        String pattern = IntStream.range(0, ids.size()).mapToObj(i -> "?").reduce((s1, s2) -> s1 + "," + s2).orElse("");
        sql.append(pattern).append(")");

        return dsl.selectFrom(table()).where(sql.toString(), ids.toArray()).fetch();
    }

    public List<R> findAllValid() {
        return dsl.selectFrom(table()).where(" del_flag=? ", NOT_DEL).fetch();
    }

    public R update(R record) {
        record.store();
        return record;
    }

    public void insert(R record) {
        record.from(ImmutableMap.of(FIELD_CREATE_TIME, Times.nowUnixTime()), FIELD_CREATE_TIME);
        dsl.batchInsert(record).execute();
    }

    public void delete(R record) {
        record.from(DEL_MAPPER, FIELD_DEL_FLAG);
        record.store();
    }

    /**
     * 自动根据id判断插入或者更新
     *
     * @param record
     * @return
     */
    public void store(R record) {
        Long id = record.getValue(FIELD_ID, Long.class);
        if (id == null || id == 0) {
            dsl.batchInsert(record).execute();
        }
        else {
            update(record);
        }
    }

    public void batchUpdate(List<? extends UpdatableRecord<?>> records) {

        dsl.batchUpdate(records).execute();

    }
}
