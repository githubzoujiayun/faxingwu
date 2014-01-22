package com.jm.data;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jm.entity.DongTai;
import com.jm.entity.Hair;
import com.jm.util.LogUtil;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "fwx.db";
	private static final int DATABASE_VERSION = 31; // 数据库版本为项目的去掉小数点
													// 如3.0版本数据库版本为30
	private Dao<Hair, String> hairDao = null;

	private Dao<DongTai, String> dongTaiDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Hair.class);
			TableUtils.createTable(connectionSource, DongTai.class);
		} catch (SQLException e) {
			LogUtil.e(DatabaseHelper.class.getName() + "创建数据库失败", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource, int oldVer, int newVer) {
		LogUtil.d("Upgrade database from version " + oldVer + " to " + newVer);
		try {
			TableUtils.dropTable(connectionSource, Hair.class, true);
			TableUtils.dropTable(connectionSource, DongTai.class, true);
			onCreate(sqliteDatabase, connectionSource);
		} catch (SQLException e) {
			LogUtil.e(DatabaseHelper.class.getName()
					+ "Unable to create datbases", e);
		}
	}

	public Dao<Hair, String> getHairDao() throws SQLException {
		if (hairDao == null) {
			hairDao = getDao(Hair.class);
		}
		return hairDao;
	}

	public Dao<DongTai, String> getDongTaiDao() throws SQLException {
		if (dongTaiDao == null) {
			dongTaiDao = getDao(DongTai.class);
		}
		return dongTaiDao;
	}

	public DongTai getDongTai(String ID) throws SQLException {
		QueryBuilder<DongTai, String> query = getDongTaiDao().queryBuilder();
		Where<DongTai, String> where = query.where();
		where.eq("work_id", ID);
		query.setWhere(where);
		List<DongTai> list = query.query();
		if (list.size() != 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Hair getHair(String ID, String type) throws SQLException {
		QueryBuilder<Hair, String> query = getHairDao().queryBuilder();
		Where<Hair, String> where = query.where();
		where.and(where.eq("id", ID), where.eq("type", type));
		query.setWhere(where);
		List<Hair> list = query.query();
		if (list.size() != 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<DongTai> getDongTaiList() throws SQLException {
		QueryBuilder<DongTai, String> query = getDongTaiDao().queryBuilder();
		query.orderBy("work_id", false);
		return query.query();
	}

	public List<Hair> getHairsList(String type) throws SQLException {
		QueryBuilder<Hair, String> query = getHairDao().queryBuilder();
		Where<Hair, String> where = query.where();
		where.eq("type", type);
		query.setWhere(where);
		query.orderBy("id", false);
		return query.query();
	}

}
