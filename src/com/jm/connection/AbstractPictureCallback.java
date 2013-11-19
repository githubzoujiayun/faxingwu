package com.jm.connection;

import java.util.List;
import java.util.Vector;

import android.content.Context;

import com.jm.util.LogUtil;

public abstract class AbstractPictureCallback {
	private boolean interrupted = true;
	private List<PictureObject> pList;

	public AbstractPictureCallback() {
		super();
		pList = new Vector<PictureObject>();
	}

	public void checkPictureTask(Context cntx) {
		// LogUtil.d("pList.isEmpty()"+pList.isEmpty()+"interrupted"+interrupted);
		if (pList.isEmpty() || !interrupted) {
			return;
		}

		resume();
		// LogUtil.d("callbacktask=--=-----");
		PictureAsynTask task = new PictureAsynTask(cntx, this);
		task.execute();
	}

	public boolean isEmpty() {
		return pList.isEmpty();
	}

	public List<PictureObject> getPictureList() {
		return pList;
	}

	public void addSmall(String id) {
		if (id != null && id != "") {
			pList.add(PictureObject.small(id));
		}
	}

	public void addPicObject(String id, int size) {
		if (id != null && id != "") {
			pList.add(PictureObject.setPicObj(id, size));
		}
	}

	public void addSmall(String id, int type) {
		if (id != null && id != "") {
			pList.add(PictureObject.small(id, type));
		}
	}

	public void addLarge(String id) {
		if (id != null && id != "") {
			pList.add(PictureObject.large(id));
		}
	}

	public void addMid(String id) {
		if (id != null && id != "") {
			pList.add(PictureObject.large(id));
		}
	}

	public void addLarge(String id, int type) {
		if (id != null && id != "") {
			pList.add(PictureObject.large(id, type));
		}
	}

	public final void interrupt() {
		interrupted = true;
	}

	public final void resume() {
		interrupted = false;
	}

	public final boolean isInterrupted() {
		return interrupted;
	}

	protected void remove(PictureObject pObj) {
		pList.remove(pObj);
	}

	protected void callback(PictureObject pObj) {
		if (pObj == null) {
			return;
		}

		LogUtil.i(pObj.getStatus() + "-" + pObj.getId() + "-" + pObj.getType());

		switch (pObj.getStatus()) {
		case PictureObject.STATUS_ALREADY_EXISTS:
		case PictureObject.STATUS_SUCCESSFUL:
			loaded(pObj);
			break;
		case PictureObject.STATUS_NOT_CHECK:
		case PictureObject.STATUS_FAIL:
			failed(pObj);
		}
	}

	public abstract void loaded(PictureObject pObj);

	public void failed(PictureObject pObj) {
		;
	}
}
