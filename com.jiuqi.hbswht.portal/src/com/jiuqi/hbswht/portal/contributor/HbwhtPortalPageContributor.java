package com.jiuqi.hbswht.portal.contributor;

import java.util.ArrayList;
import java.util.List;

import com.jiuqi.dna.core.type.Convert;
import com.jiuqi.dna.core.type.GUID;
import com.jiuqi.dna.ui.common.DataObject;
import com.jiuqi.dna.ui.custom.Option;
import com.jiuqi.dna.ui.custom.configure.editors.ComplexSelector;
import com.jiuqi.dna.ui.funcmgr.SubFunctionAndGroupProvider;
import com.jiuqi.dna.ui.portal.PortalPageContributor;
import com.jiuqi.dna.ui.portlet.PortletPage;
import com.jiuqi.dna.ui.portlet.PortletPageSource;
import com.jiuqi.dna.ui.viewpart.IViewPartGroup;
import com.jiuqi.hbswht.portal.viewpart.HbwhtContributorSettingViewPartDefine;

public class HbwhtPortalPageContributor extends PortalPageContributor{
	public HbwhtPortalPageContributor()
	{
	}
	@Override
	public String getTitle() {
		return "湖北文化厅首页多页签提供器";
	}

	public GUID[] getPerspectives()
	{
		List pages = PortletPageSource.getAllPortletPage(true);
		GUID dGuids[] = new GUID[pages.size()];
		int i = 0;
		for (int n = pages.size(); i < n; i++)
			dGuids[i] = ((PortletPage)pages.get(i)).getId();

		return dGuids;
	}
	
	public GUID[] getFunctionTreeRoot()
	{
		DataObject dataObject = getSettingData(HbwhtContributorSettingViewPartDefine.SETTING_DATA_ID);
		Option options[] = (Option[])null;
		if (dataObject != null)
			options = ComplexSelector.getOptions(dataObject.getString("LINE_", ""));
		if (options != null)
		{
			GUID functionIds[] = new GUID[options.length];
			List groupIds = new ArrayList();
			SubFunctionAndGroupProvider provider = new SubFunctionAndGroupProvider(getContext());
			for (int i = 0; i < options.length; i++)
			{
				functionIds[i] = Convert.toGUID(options[i].value);
				IViewPartGroup group = (IViewPartGroup)getContext().find(com.jiuqi.dna.ui.viewpart.IViewPartGroup.class, functionIds[i]);
				if (group != null)
				{
					Object children[] = provider.getChildren(group);
					if (children != null && children.length > 0)
						groupIds.add(functionIds[i]);
				}
			}

			return (GUID[])groupIds.toArray(new GUID[groupIds.size()]);
		} else
		{
			return super.getFunctionTreeRoot();
		}
	}
}
