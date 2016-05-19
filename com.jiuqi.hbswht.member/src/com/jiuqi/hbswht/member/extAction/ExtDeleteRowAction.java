package com.jiuqi.hbswht.member.extAction;

import java.sql.ResultSet;
import java.util.List;

import com.jiuqi.dna.bap.model.common.define.base.BusinessObject;
import com.jiuqi.dna.bap.model.common.define.intf.ActionConst;
import com.jiuqi.dna.bap.model.common.define.intf.IAction;
import com.jiuqi.dna.bap.model.common.runtime.base.BusinessModel;
import com.jiuqi.dna.bap.model.common.type.ModelState;
import com.jiuqi.dna.core.Context;
import com.jiuqi.dna.core.da.DBCommand;
import com.jiuqi.dna.core.da.RecordSet;
import com.jiuqi.dna.core.type.GUID;
import com.jiuqi.dna.ui.common.constants.JWT;
import com.jiuqi.dna.ui.wt.events.SelectionEvent;
import com.jiuqi.dna.ui.wt.events.SelectionListener;
import com.jiuqi.dna.ui.wt.widgets.MessageDialog;
import com.jiuqi.hbswht.member.constant.WhtConstant;

public class ExtDeleteRowAction implements IAction{

	@Override
	public String getGroup() {
		// TODO Auto-generated method stub
		return "湖北文化厅扩展";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ExtDeleteRowAction";
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "删行（扩展）";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIcon() {
		// TODO Auto-generated method stub
		return ActionConst.ico_delete_row;
	}

	@Override
	public String getShortcut() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEnabled(BusinessModel model) {
		int zt = (Integer) model.getModelData().getMaster().getFieldValue("WORKFLOWSTATE");
		if( zt == 0){
			if (model.getModelState() == ModelState.EDIT
					|| model.getModelState() == ModelState.NEW) {
				return true;
			} else {
				return false;
			}
		}else{
			return false;
		}
	}

	@Override
	public void execute(final BusinessModel model, String param) {
		List<BusinessObject> lists = model.getModelData().getDetailsList()
				.get(0);
		if (lists.size() == 0) {
			return;
		}else{
			final int curCursor = model.getCursor(WhtConstant.T_ZTMBZB);
			final String recid = model.getFieldVisitor().getGridFieldValue(WhtConstant.T_ZTMBZB, WhtConstant.F_ZTMBZB_RECID, curCursor).toString();
			final MessageDialog resultMsg = MessageDialog.confirm("警告", "删除后，对应的目标明细将消失，是否确定删除？");
			
				resultMsg.addSelectionListener(new SelectionListener() {
				
				public void widgetSelected(SelectionEvent arg0) {
					if(resultMsg.getReturnCode() == JWT.OK){
						model.deleteItem(model.getITableByName(WhtConstant.T_ZTMBZB),curCursor);
						GUID mbmxRecid = checkExist(recid, model.getContext());
						if(mbmxRecid != null){// 填写了目标明细
							// 先删除7张子表的数据
							// 1、数量指标
							deleteDataZB(WhtConstant.T_SLZB, mbmxRecid.toString(), model.getContext());
							// 2、质量指标
							deleteDataZB(WhtConstant.T_ZLZB, mbmxRecid.toString(), model.getContext());
							// 3、时效指标
							deleteDataZB(WhtConstant.T_SXZB, mbmxRecid.toString(), model.getContext());
							// 4、成本指标
							deleteDataZB(WhtConstant.T_CBZB, mbmxRecid.toString(), model.getContext());
							// 5、经济效益指标
							deleteDataZB(WhtConstant.T_JJXYZB, mbmxRecid.toString(), model.getContext());
							// 6、社会效益指标
							deleteDataZB(WhtConstant.T_SHXYZB, mbmxRecid.toString(), model.getContext());
							// 7、生态效益指标
							deleteDataZB(WhtConstant.T_STXYZB, mbmxRecid.toString(), model.getContext());
							// 8、可持续影响指标
							deleteDataZB(WhtConstant.T_KCXYXZB, mbmxRecid.toString(), model.getContext());
							// 9、服务对象满意度指标
							deleteDataZB(WhtConstant.T_FWDXMYDZB, mbmxRecid.toString(), model.getContext());
							// 删除目标明细主表的数据
							deleteData(recid, model.getContext());
						}
					}
				}
			});
		}
	}
	
	private GUID checkExist(String recid,Context context){
		StringBuffer sb = new StringBuffer();
		sb.append(" define query data()")
		.append(" begin")
		.append(" select t.recid from HBWHT_MBMXZB as t where t.mbsmrecid = guid '"+recid+"'")
		.append(" end");
		DBCommand dbc = null;
		GUID mbmxRecid = null;
		try{
			dbc = context.prepareStatement(sb);
			RecordSet rs = dbc.executeQuery();
			while( rs.next()){
				mbmxRecid = rs.getFields().get(0).getGUID();
				break;
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			dbc.unuse();
		}
		return mbmxRecid;
	}
	
	
	private void deleteData(String recid,Context context){
		StringBuffer sb = new StringBuffer();
		sb.append(" define delete data()")
		.append(" begin ")
		.append(" delete from HBWHT_MBMXZB as t where t.mbsmrecid = guid '"+recid+"'")
		.append(" end");
		DBCommand dbc = null;
		try{
			dbc = context.prepareStatement(sb);
			dbc.executeUpdate();
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			dbc.unuse();
		}
	}
	
	private void deleteDataZB(String tableName,String recid,Context context){
		StringBuffer sb = new StringBuffer();
		sb.append(" define delete data()")
		.append(" begin ")
		.append(" delete from "+tableName+" as t where t.mrecid = guid '"+recid+"'")
		.append(" end");
		DBCommand dbc = null;
		try{
			dbc = context.prepareStatement(sb);
			dbc.executeUpdate();
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			dbc.unuse();
		}
	}
}
