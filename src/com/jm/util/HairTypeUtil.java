package com.jm.util;

import java.util.ArrayList;
import java.util.List;

import com.jm.entity.Type;
import com.jm.fxw.R;

public class HairTypeUtil {

	private List<Type> recommendList = new ArrayList<Type>();
	private List<Type> womenList = new ArrayList<Type>();
	private List<Type> manList = new ArrayList<Type>();
	private List<Type> facetypeList = new ArrayList<Type>();
	private List<Type> permList = new ArrayList<Type>();
	private List<Type> colorList = new ArrayList<Type>();

	public HairTypeUtil() {
		initrecommendList();
		initwomenList();
		initmanList();
		initfacetypeList();
		initpermList();
		initcolorList();
	}

	private void initrecommendList() {
		recommendList.add(new Type(1, 9, R.drawable.hairtype_1_9, "波波头"));
		recommendList.add(new Type(1, 7, R.drawable.hairtype_1_7, "梨花头"));
		recommendList.add(new Type(4, 4, R.drawable.hairtype_4_4, "水波纹", true));
		recommendList.add(new Type(4, 1, R.drawable.hairtype_4_1, "纹理烫", true));
		recommendList.add(new Type(1, 4, R.drawable.hairtype_1_4, "编发"));
		recommendList.add(new Type(1, 5, R.drawable.hairtype_1_5, "盘发"));
		recommendList.add(new Type(2, 9, R.drawable.hairtype_2_9, "男明星"));
		recommendList.add(new Type(1, 10, R.drawable.hairtype_1_10, "女明星"));
		recommendList.add(new Type(5, 2, R.drawable.hairtype_5_2, "渐变色", true));
	}

	private void initwomenList() {
		womenList.add(new Type(1, 1, R.drawable.hairtype_1_1, "短发"));
		womenList.add(new Type(1, 2, R.drawable.hairtype_1_2, "中发"));
		womenList.add(new Type(1, 3, R.drawable.hairtype_1_3, "长发"));
		womenList.add(new Type(1, 4, R.drawable.hairtype_1_4, "编发"));
		womenList.add(new Type(1, 5, R.drawable.hairtype_1_5, "盘发"));
		womenList.add(new Type(1, 6, R.drawable.hairtype_1_6, "公主头"));
		womenList.add(new Type(1, 7, R.drawable.hairtype_1_7, "梨花头"));
		womenList.add(new Type(1, 8, R.drawable.hairtype_1_8, "蛋卷头"));
		womenList.add(new Type(1, 9, R.drawable.hairtype_1_9, "波波头"));
		// womenList.add(new Type(1, 10, R.drawable.hairtype_1_10, "女明星"));
	}

	private void initmanList() {
		manList.add(new Type(2, 1, R.drawable.hairtype_2_1, "飞机头"));
		manList.add(new Type(2, 2, R.drawable.hairtype_2_2, "寸头"));
		manList.add(new Type(2, 3, R.drawable.hairtype_2_3, "斜庞克"));
		manList.add(new Type(2, 4, R.drawable.hairtype_2_4, "莫西干"));
		manList.add(new Type(2, 5, R.drawable.hairtype_2_5, "短发"));
		manList.add(new Type(2, 6, R.drawable.hairtype_2_6, "中发"));
		manList.add(new Type(2, 7, R.drawable.hairtype_2_7, "长发"));
		manList.add(new Type(2, 8, R.drawable.hairtype_2_8, "蘑菇头"));
		manList.add(new Type(2, 10, R.drawable.hairtype_2_10, "其他"));
		// manList.add(new Type(2, 10, R.drawable.hairtype_2_10, "男明星"));

	}

	private void initfacetypeList() {
		facetypeList.add(new Type(3, 1, R.drawable.hairtype_3_1, "圆脸", true));
		facetypeList.add(new Type(3, 2, R.drawable.hairtype_3_2, "方脸", true));
		facetypeList.add(new Type(3, 3, R.drawable.hairtype_3_3, "长脸", true));
		facetypeList.add(new Type(3, 4, R.drawable.hairtype_3_4, "瓜子脸", true));
	}

	private void initpermList() {
		permList.add(new Type(4, 1, R.drawable.hairtype_4_1, "纹理烫", true));
		permList.add(new Type(4, 2, R.drawable.hairtype_4_2, "定位烫", true));
		permList.add(new Type(4, 3, R.drawable.hairtype_4_3, "皮卡路", true));
		permList.add(new Type(4, 4, R.drawable.hairtype_4_4, "水波纹", true));
		permList.add(new Type(4, 5, R.drawable.hairtype_4_5, "玉米烫", true));
		permList.add(new Type(4, 6, R.drawable.hairtype_4_6, "螺旋烫", true));
		permList.add(new Type(4, 7, R.drawable.hairtype_4_7, "离子烫", true));
		permList.add(new Type(4, 8, R.drawable.hairtype_4_8, "梨花烫", true));
		permList.add(new Type(4, 9, R.drawable.hairtype_4_9, "其他", true));

	}

	private void initcolorList() {
		colorList.add(new Type(5, 1, R.drawable.hairtype_5_1, "整体色", true));
		colorList.add(new Type(5, 2, R.drawable.hairtype_5_2, "渐变色", true));
		colorList.add(new Type(5, 3, R.drawable.hairtype_5_3, "挑染", true));
		colorList.add(new Type(5, 4, R.drawable.hairtype_5_4, "多色染色", true));

	}

	public List<Type> getrecommendList() {
		return recommendList;
	}

	public List<Type> getwomenList() {
		return womenList;
	}

	public List<Type> getmanList() {
		return manList;
	}

	public List<Type> getfacetypeList() {
		return facetypeList;
	}

	public List<Type> getpermList() {
		return permList;
	}

	public List<Type> getcolorList() {
		return colorList;
	}

}
