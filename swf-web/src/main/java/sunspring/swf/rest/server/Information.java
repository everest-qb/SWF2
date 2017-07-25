package sunspring.swf.rest.server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sunspring.annotation.LogTrace;
import sunspring.swf.SwfGlobal;
import sunspring.swf.ejb.DepartmentService;
import sunspring.swf.ejb.EmployeeService;
import sunspring.swf.jpa.SwfDeptAll;
import sunspring.swf.jpa.SwfEmpsAll;
import sunspring.swf.jpa.SwfOfficeDutyAll;
import sunspring.swf.rest.SwfDepartment;
import sunspring.swf.rest.SwfDuty;
import sunspring.swf.rest.SwfEmployee;

/**
 * SWF employee and department SERVER REST
 * @author QB
 * 
 */
// TODO  need to do
@LogTrace
@Path("info")
public class Information {
	private Logger log=LoggerFactory.getLogger(Information.class);
	
	@Inject
	private DepartmentService deptService;
	@Inject
	private EmployeeService emplService;
	
	/**
	 * 根據輸入的主鍵ID,取得員工資料
	 * @param empId  key
	 * @return 員工資料
	 */
	@GET
	@Path("emplID")
	@Produces(MediaType.APPLICATION_JSON)
	public SwfEmployee findEmplById(@NotNull @QueryParam("id") BigDecimal empId){
		SwfEmpsAll e= emplService.find(empId);
		return convertEmplRest(e);
	}
	
	/**
	 * 根據輸入的工號,取得員工資料
	 * @param empNum 工號
	 * @return 員工資料
	 */
	@GET
	@Path("emplNumber")
	@Produces(MediaType.APPLICATION_JSON)
	public SwfEmployee findEmplByNu(@Size(min=2) @QueryParam("id") String empNum){
		SwfEmpsAll e=emplService.findByNu(empNum);
		return convertEmplRest(e);
	}
	
	private SwfEmployee convertEmplRest(SwfEmpsAll e){
		SwfEmployee tmp=new SwfEmployee();
		tmp.setDisableDate(e.getDisableDate());
		tmp.setEmailAddress(e.getEmailAddress());
		tmp.setEmpCname(e.getEmpCname());
		tmp.setEmpEname(e.getEmpEname());
		tmp.setEmpId(e.getEmpId());
		tmp.setEmpNum(e.getEmpNum());
		tmp.setEnableDate(e.getEnableDate());
		SwfOfficeDutyAll  duty=e.getDuty();
		SwfDuty restDuty=new SwfDuty();
		restDuty.setDisableDate(duty.getDisableDate());
		restDuty.setDutyId(duty.getDutyId());
		restDuty.setEnableDate(duty.getEnableDate());
		restDuty.setGrades(duty.getGrades());
		restDuty.setJobLevel(duty.getJobLevel());
		restDuty.setPositionCode(duty.getPositionCode());
		tmp.setDuty(restDuty);
		return tmp;
	}
	
	/**
	 * 根據部門代碼取得所屬員工資料
	 * @param depCode 部門代碼
	 * @return 員工資料 List
	 */
	@GET
	@Path("emplDeptCode")
	@Produces(MediaType.APPLICATION_JSON)
	public List<SwfEmployee> findEmplByDept(@Size(min=2) @QueryParam("code") String depCode){
		List<SwfEmployee> returnValue=new ArrayList<SwfEmployee>();
		List<SwfEmpsAll> eList=deptService.findSWFEmplOfDeptCode(depCode);
		for(SwfEmpsAll e :eList){
			SwfEmployee tmp=new SwfEmployee();
			tmp.setDisableDate(e.getDisableDate());
			tmp.setEmailAddress(e.getEmailAddress());
			tmp.setEmpCname(e.getEmpCname());
			tmp.setEmpEname(e.getEmpEname());
			tmp.setEmpId(e.getEmpId());
			tmp.setEmpNum(e.getEmpNum());
			tmp.setEnableDate(e.getEnableDate());
			SwfOfficeDutyAll  duty=e.getDuty();
			SwfDuty restDuty=new SwfDuty();
			restDuty.setDisableDate(duty.getDisableDate());
			restDuty.setDutyId(duty.getDutyId());
			restDuty.setEnableDate(duty.getEnableDate());
			restDuty.setGrades(duty.getGrades());
			restDuty.setJobLevel(duty.getJobLevel());
			restDuty.setPositionCode(duty.getPositionCode());
			tmp.setDuty(restDuty);
			returnValue.add(tmp);
		}
		return returnValue;
	}
	
	@GET
	@Path("deptID")
	@Produces(MediaType.APPLICATION_JSON)
	public SwfDepartment findDeptById(@NotNull @QueryParam("id") BigDecimal empId){
		
		return null;
	}
	
	@GET
	@Path("deptCode")
	@Produces(MediaType.APPLICATION_JSON)
	public SwfDepartment findDeptByCode(@Size(min=2) @QueryParam("code") String depCode){
		
		return null;
	}	
	
	private SwfEmployee convertDeptRest(){
		
		
		return null;
	}
	
	/**
	 * 根據輸入的工號做為前置,取得員工資料  以SQL表示 emplNu like 'Y4%'
	 * @param empNum 至少兩個輸入前置 例: 'Y4'
	 * @return 員工 list
	 */
	@GET
	@Path("emplAuto")
	@Produces(MediaType.APPLICATION_JSON)
	public List<SwfEmployee> findAuto(@Size(min=2) @QueryParam("pre") String pre){
		pre=pre.toUpperCase();
		List<SwfEmpsAll> list=emplService.findbyPreNu(pre);
		List<SwfEmployee> returnValue=new ArrayList<SwfEmployee>();
		for(SwfEmpsAll e:list){
			returnValue.add(convertEmplRest(e));
		}	
		return returnValue;
	}
	
	@GET
	@Path("audit")
	@Produces(MediaType.APPLICATION_JSON)
	public List<SwfEmployee> findAudit(@NotNull @QueryParam("id") BigDecimal empId,
			@NotNull @QueryParam("level") BigDecimal topLevel){
		List<SwfEmployee> returnValue=new ArrayList<SwfEmployee>();
		for(SwfEmpsAll e:findHierarchyManager(empId,topLevel,1)){
			returnValue.add(convertEmplRest(e));
		}
		 return returnValue;
	}
	
	@GET
	@Path("approver")
	@Produces(MediaType.APPLICATION_JSON)
	public List<SwfEmployee> findApprover(@NotNull @QueryParam("id") BigDecimal empId,
			@NotNull @QueryParam("level") BigDecimal topLevel){
		List<SwfEmployee> returnValue=new ArrayList<SwfEmployee>();
		for(SwfEmpsAll e:findHierarchyManager(empId,topLevel,2)){
			returnValue.add(convertEmplRest(e));
		}
		if(returnValue.size()==0)
			returnValue.add(genSystemSwfEmpl());
		return returnValue;
	}
	
	private List<SwfEmpsAll> findHierarchyManager(BigDecimal empId,BigDecimal topLevel ,int type){
		List<SwfEmpsAll> returnValue=new ArrayList<SwfEmpsAll>();
		SwfEmpsAll applicant=emplService.find(empId);
		SwfDeptAll userDept=applicant.getSwfDepartments().get(0);
		List<SwfDeptAll> deps=deptService.findHierarchy(userDept.getDeptCode());
		
		for(SwfDeptAll d:deps){				
			int depLevel=Integer.parseInt(d.getDeptLevelCode());
			if(depLevel>=topLevel.intValue()){
				if(type==2){
					if(depLevel>topLevel.intValue())
						continue;
				}
				
				List<SwfEmpsAll> emps=deptService.findManagers(d.getDeptId());
				if(userDept.getDeptId().longValue()==d.getDeptId().longValue()){ 
					for(SwfEmpsAll e:emps){
						if(e.getDuty().getJobLevel().intValue() > applicant.getDuty().getJobLevel().intValue()){
							SwfEmpsAll tmp=emplService.find(e.getEmpId());
							returnValue.add(tmp);
						}
					}
				}else{
					for(SwfEmpsAll e:emps){
						SwfEmpsAll tmp=emplService.find(e.getEmpId());
						returnValue.add(tmp);
						
					}
				}
			}
		}
		
		return returnValue;
	}
	
	private SwfEmployee genSystemSwfEmpl(){
		SwfEmployee tmp=new SwfEmployee();
		tmp.setEmpNum("SystemAdmin");
		SwfDuty restDuty=new SwfDuty();
		tmp.setDuty(restDuty);
		return tmp;
	}
}
