package com.xiamu;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.xiamu.constants.SystemConst;
import com.xiamu.entity.tables.pojos.User;
import com.xiamu.util.Times;
import org.apache.commons.collections.CollectionUtils;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.impl.UpdatableRecordImpl;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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

    private static final String FIELD_CREATE_TIME = "create_time";
    private static final String FIELD_DEL_FLAG = "del_flag";
    private static final String FIELD_ID = "id";

    private static final Map<String, Byte> DEL_MAPPER = ImmutableMap.of(FIELD_DEL_FLAG, DEL);

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

    public R getValid(long id) {
        return dsl.selectFrom(table()).where(" id=? and del_flag=? ", id, NOT_DEL).fetchAny();
    }

    public R getLastValid() {
        return dsl.selectFrom(table()).where(" del_flag= ? ", NOT_DEL).orderBy(table().field("id").desc()).fetchAny();
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

    public List<R> getValid(Collection<Long> ids) {

        if (CollectionUtils.isEmpty(ids)) {
            return ImmutableList.of();
        }

        return dsl.selectFrom(table())
                .where(" del_flag = 0 and id in (" + IntStream.range(0, ids.size())
                        .mapToObj(i -> "?")
                        .reduce((s1, s2) -> s1 + "," + s2)
                        .orElse("") + ")", ids.toArray()).fetch();
    }

    public List<R> findAllValid() {
        return dsl.selectFrom(table()).where(" del_flag=? ", NOT_DEL).fetch();
    }



    public R insert(R record) {
        record.from(ImmutableMap.of(FIELD_CREATE_TIME, Times.nowUnixTime()), FIELD_CREATE_TIME);
        record.insert();
        return record;
    }

    public R update(R record) {
        record.store();
        return record;
    }

    /**
     * 自动根据id判断插入或者更新
     *
     * @param record
     * @return
     */
    public R store(R record) {

        Long id = record.getValue(FIELD_ID, Long.class);
        if (id == null || id == 0) {
            return insert(record);
        } else {
            return update(record);
        }
    }

    public void delete(R record) {
        record.from(DEL_MAPPER, FIELD_DEL_FLAG);
        record.store();
    }

    public void delete(long id) {
        R record = get(id);
        delete(record);
    }

    public void batchInsert(List<? extends TableRecord<?>> records) {
        records.parallelStream().forEach(tableRecord -> tableRecord.from(
                ImmutableMap.of(FIELD_CREATE_TIME, Times.nowUnixTime()), FIELD_CREATE_TIME));
        dsl.batchInsert(records).execute();
    }

    public void batchUpdate(List<? extends UpdatableRecord<?>> records) {

        dsl.batchUpdate(records).execute();

    }

    public void batchDelete(List<? extends UpdatableRecord<?>> records) {
        records.parallelStream().forEach(tableRecord -> tableRecord.from(DEL_MAPPER, FIELD_DEL_FLAG));
        dsl.batchUpdate(records).execute();
    }
}
