package com.jiuqi.hbswht.portal.viewpart;

import com.jiuqi.dna.core.Context;
import com.jiuqi.dna.core.type.GUID;
import com.jiuqi.dna.ui.custom.configure.PropertyDescriptor;
import com.jiuqi.dna.ui.custom.configure.editors.ComplexSelector;
import com.jiuqi.dna.ui.funcmgr.FunctionGroupTreeSource;
import com.jiuqi.dna.ui.viewpart.ConfigurableViewPartDefine;
import com.jiuqi.dna.ui.wt.graphics.Point;

public class HbwhtContributorSettingViewPartDefine extends
ConfigurableViewPartDefine{
	public static final GUID SETTING_DATA_ID = GUID.valueOf("C640648608E044D7BA0DA7E55A5BED0D");
	public static final String USERSTYPE = "LINE_";

	public HbwhtContributorSettingViewPartDefine()
	{
	}

	public String getViewPageId()
	{
		return null;
	}

	public GUID getSettingDataId(Context context, GUID instanceId)
	{
		return SETTING_DATA_ID;
	}

	public PropertyDescriptor[] getProperties(Context context, int tabIndex, int groupIndex)
	{
		return (new PropertyDescriptor[] {
			new PropertyDescriptor("LINE_", "将功能转为页签", new ComplexSelector(new FunctionGroupTreeSource(), null, new Point(600, 360), "选择功能", "已选功能"))
		});
	}

	public String[] getPropertyGroups(Context context, int tabIndex)
	{
		return null;
	}

	public String[] getPropertyTabs(Context context)
	{
		return null;
	}

	public String getDescription()
	{
		return null;
	}

	public String getGroupName()
	{
		return "系统";
	}

	public String getHelpPageId()
	{
		return null;
	}

	public String getName()
	{
		return "hbwht_FunctionTabItem";
	}

	public String getTitle()
	{
		return "湖北文化厅功能转页签";
	}
}
