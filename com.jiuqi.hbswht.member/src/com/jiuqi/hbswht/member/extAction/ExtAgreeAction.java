package com.jiuqi.hbswht.member.extAction;

import com.jiuqi.dna.bap.bill.common.action.IWorkFlowAction;
import com.jiuqi.dna.bap.bill.common.model.BillModel;
import com.jiuqi.dna.bap.bill.common.workflow.actions.ActionEnable;
import com.jiuqi.dna.bap.bill.common.workflow.actions.BillWorkflowUtil;
import com.jiuqi.dna.bap.model.common.runtime.base.BusinessModel;
import com.jiuqi.dna.bap.model.common.type.ModelState;
import com.jiuqi.dna.ui.wt.widgets.MessageDialog;
import com.jiuqi.dna.workflow.define.DefaultAction;
import com.jiuqi.hbswht.member.constant.WhtConstant;

/**
 * ��չ����ͬ��
 * @author oulijian
 *
 */
public class ExtAgreeAction implements IWorkFlowAction{

	@Override
	public String getGroup() {
		// TODO Auto-generated method stub
		return "�����Ļ�����չ";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ExtAgreeAction";
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "ͬ��";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIcon() {
		// TODO Auto-generated method stub
		return DefaultAction.getImgSrc(getWorkFlowActionID());
	}

	@Override
	public String getShortcut() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEnabled(BusinessModel model) {
		// TODO Auto-generated method stub
		if( model.getModelState() == ModelState.EDIT){
			return false;
		}else {
			return ActionEnable.isEnable(model);
		}
		
	}

	@Override
	public void execute(BusinessModel model, String param) {
		BillWorkflowUtil workflowUtil = new BillWorkflowUtil(this, (BillModel)model);
		if( model.getModelData().getMaster().getFieldValue(WhtConstant.F_HBWHTSBB_ZTYJ) != null && 
				!model.getModelData().getMaster().getFieldValue(WhtConstant.F_HBWHTSBB_ZTYJ).toString().trim().equals("")){
			workflowUtil.onlySelectUserworkFlow();
		}else{
			MessageDialog.alert("��ʾ��Ϣ","����ͬ��ǰ��Ҫ¼���걨��˱�,�����棡");
			return ;
		}
		
	}

	@Override
	public int getWorkFlowActionID() {
		// TODO Auto-generated method stub
		return DefaultAction.ACCEPT.value();
	}
	
}