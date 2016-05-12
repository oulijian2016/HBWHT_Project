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
		return "�����Ļ�����չ";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ExtOpenTargetDetailAction";
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "��дĿ����ϸ";
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
		// ��ȡ����Ŀ���ӱ���ǰѡ�е��У��ж�Ŀ��˵���Ƿ�Ϊ�գ���Ϊ�գ�����ʾ����������Ŀ��˵��
		int curCursor = model.getCursor(WhtConstant.T_ZTMBZB);
		Object mbsm = model.getFieldVisitor().getGridFieldValue(WhtConstant.T_ZTMBZB, WhtConstant.F_ZTMBZB_MBSM,curCursor);
		if (mbsm == null){
			MessageDialog.alert("��ʾ��Ϣ", "��������Ŀ��˵����");
			return ;
		}
		if(mbsm !=null ){
			if(mbsm.toString().trim().equals("")){
				MessageDialog.alert("��ʾ��Ϣ", "Ŀ��˵������ȫ��Ϊ�ո�");
				return ;
			}
		}
		String recid = model.getFieldVisitor().getGridFieldValue(WhtConstant.T_ZTMBZB, WhtConstant.F_ZTMBZB_RECID, curCursor).toString();
		// �ж� recid��Ŀ����ϸ������MBSMRECID�Ƿ����
		FBillDefine define = BillCentre.findBillDefine(model.getContext(), WhtConstant.DEFINE_HBWHT_MBMX);
 		GUID mbmxZbRecid = findMbsmRecid(recid, model.getContext());
		if( mbmxZbRecid != null){// ����
			BillCentre.ShowBillUI(model.getContext(), null, define, UIStyle.UPDATE, mbmxZbRecid);
		}else{// ������
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