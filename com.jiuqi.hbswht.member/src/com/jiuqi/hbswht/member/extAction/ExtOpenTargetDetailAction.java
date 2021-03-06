package com.jiuqi.hbswht.member.extAction;


import com.jiuqi.dna.bap.model.common.define.intf.ActionConst;
import com.jiuqi.dna.bap.model.common.define.intf.IAction;
import com.jiuqi.dna.bap.model.common.runtime.base.BusinessModel;
import com.jiuqi.dna.bap.model.common.type.ModelState;
import com.jiuqi.dna.bap.model.common.type.UIStyle;
import com.jiuqi.dna.core.Context;
import com.jiuqi.dna.core.da.DBCommand;
import com.jiuqi.dna.core.da.RecordSet;
import com.jiuqi.dna.core.type.GUID;
import com.jiuqi.hbswht.member.constant.WhtConstant;
import com.jiuqi.dna.ui.wt.widgets.MessageDialog;
import com.jiuqi.dna.bap.bill.intf.facade.model.FBillDefine;
import com.jiuqi.dna.bap.bill.common.model.BillCentre;
import com.jiuqi.dna.bap.bill.common.model.BillModel;

public class ExtOpenTargetDetailAction implements IAction {

	@Override
	public String getGroup() {
		// TODO Auto-generated method stub
		return "湖北文化厅扩展";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ExtOpenTargetDetailAction";
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "填写目标明细";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIcon() {
		// TODO Auto-generated method stub
		return ActionConst.ico_selcard;
	}

	@Override
	public String getShortcut() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEnabled(BusinessModel model) {
		return (model.getModelState() == ModelState.EDIT)
				|| (model.getModelState() == ModelState.NEW);
	}

	@Override
	public void execute(BusinessModel model, String param) {
		// 获取总体目标子表当前选中的行，判断目标说明是否为空，若为空，则提示，请先输入目标说明
		int curCursor = model.getCursor(WhtConstant.T_ZTMBZB);
		Object mbsm = model.getFieldVisitor().getGridFieldValue(WhtConstant.T_ZTMBZB, WhtConstant.F_ZTMBZB_MBSM,curCursor);
		if (mbsm == null){
			MessageDialog.alert("提示信息", "请先输入目标说明！");
			return ;
		}
		if(mbsm !=null ){
			if(mbsm.toString().trim().equals("")){
				MessageDialog.alert("提示信息", "目标说明不能全部为空格！");
				return ;
			}
		}
		String recid = model.getFieldVisitor().getGridFieldValue(WhtConstant.T_ZTMBZB, WhtConstant.F_ZTMBZB_RECID, curCursor).toString();
		// 判断 recid在目标明细主表，MBSMRECID是否存在
		FBillDefine define = BillCentre.findBillDefine(model.getContext(), WhtConstant.DEFINE_HBWHT_MBMX);
 		GUID mbmxZbRecid = findMbsmRecid(recid, model.getContext());
		if( mbmxZbRecid != null){// 存在
			BillCentre.ShowBillUI(model.getContext(), null, define, UIStyle.UPDATE, mbmxZbRecid);
		}else{// 不存在
			BillModel billmodel = BillCentre.ShowBillUI(model.getContext(), null, define, UIStyle.CREATE, GUID.randomID());
			billmodel.beginUpdate();
			billmodel.getModelData().getMaster().setFieldValue(WhtConstant.F_MBMXZB_MBSM, recid);
			billmodel.endUpdate();
		}
	}
	
	private GUID findMbsmRecid(String mbsmrecid,Context context){
		StringBuffer sb = new StringBuffer();
		sb.append(" define query MbsmRecid()")
		.append(" begin ")
		.append(" select t.recid from HBWHT_MBMXZB as t where t.mbsmrecid = guid '"+mbsmrecid+"'")
		.append(" end");
		DBCommand dbc = null;
		GUID recid = null;
		try{
			dbc = context.prepareStatement(sb);
			RecordSet rs = dbc.executeQuery();
			while (rs.next()){
				recid = rs.getFields().get(0).getGUID();
				break;
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			dbc.unuse();
		}
		return recid;
	}
	

}
