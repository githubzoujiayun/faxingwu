package com.jm.connection;

import com.jm.session.SessionManager;

public class PictureObject {
	public static final int IMAGE_TYPE_DEFAULT = 0;
	public static final int IMAGE_TYPE_FANS = 1;
	public static final int IMAGE_TYPE_COMMENTS = 2;

	public static final int STATUS_NOT_CHECK = 0;
	public static final int STATUS_SUCCESSFUL = 1;
	public static final int STATUS_FAIL = 2;
	public static final int STATUS_ALREADY_EXISTS = 3;

	private String id;
	private int type = IMAGE_TYPE_DEFAULT;
	private int status = STATUS_NOT_CHECK;

	public static PictureObject small(String id, int type) {
		PictureObject po = new PictureObject(id, SessionManager.getInstance()
				.getSmallBitMapSize());
		po.setType(type);

		return po;
	}

	public static PictureObject small(String id) {
		PictureObject po = new PictureObject(id, SessionManager.getInstance()
				.getSmallBitMapSize());

		return po;
	}

	public static PictureObject mid(String id, int type) {
		PictureObject po = new PictureObject(id, SessionManager.getInstance()
				.getMidBitMapSize());
		po.setType(type);

		return po;
	}

	public static PictureObject mid(String id) {
		PictureObject po = new PictureObject(id, SessionManager.getInstance()
				.getMidBitMapSize());

		return po;
	}

	public static PictureObject large(String id, int type) {
		PictureObject po = new PictureObject(id, SessionManager.getInstance()
				.getLargeBitMapSize());
		po.setType(type);

		return po;
	}

	public static PictureObject large(String id) {
		PictureObject po = new PictureObject(id, SessionManager.getInstance()
				.getLargeBitMapSize());

		return po;
	}

	public static PictureObject setPicObj(String id, int size) {
		PictureObject po = new PictureObject(id, size);
		return po;
	}

	private PictureObject(String id, int size) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public boolean equals(Object anObject) {
		if (anObject == null) {
			return false;
		}

		if (!(anObject instanceof PictureObject)) {
			return false;
		}

		PictureObject po = (PictureObject) anObject;

		if (po.id == null || id == null) {
			return false;
		}

		if (po.id.equals(id)) {
			return true;
		}

		return false;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}