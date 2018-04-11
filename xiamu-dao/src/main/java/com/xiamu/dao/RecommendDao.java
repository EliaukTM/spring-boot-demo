package com.xiamu.dao;

import com.xiamu.BaseDao;
import com.xiamu.entity.tables.records.RecommendRecord;
import org.jooq.Table;
import org.springframework.stereotype.Repository;

import static com.xiamu.entity.tables.Recommend.RECOMMEND;

@Repository
public class RecommendDao extends BaseDao<RecommendRecord> {

	@Override
	public Table<RecommendRecord> table() {
		return RECOMMEND;
	}
}
