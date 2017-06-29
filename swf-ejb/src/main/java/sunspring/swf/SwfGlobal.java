package sunspring.swf;

public class SwfGlobal {
	
	//task status
	public final static int APPROVE_TYPE_WORKING=10;
	public final static int APPROVE_TYPE_AGREE=20;
	public final static int APPROVE_TYPE_PASS=21;
	public final static int APPROVE_TYPE_CLOSE=22;
	public final static int APPROVE_TYPE_SYSTEM_CLOSE=23;
	public final static int APPROVE_TYPE_CONDITION_AGREE=30;
	public final static int APPROVE_TYPE_NOT_AGREE=40;
	public final static int APPROVE_TYPE_WAITTING=50;
	public final static int APPROVE_TYPE_APPLY=60;
	public final static int APPROVE_TYPE_CANECL=90;
	
	//process status
	public final static int AVTIVITY_TYPE_REGISTER=10;
	public final static int AVTIVITY_TYPE_APPLY=11;
	public final static int AVTIVITY_TYPE_AUDIT=20;
	public final static int AVTIVITY_TYPE_AUDIT_RETURN=21;
	public final static int AVTIVITY_TYPE_APPROVE=40;
	public final static int AVTIVITY_TYPE_APPROVE_RETURN=41;
	public final static int AVTIVITY_TYPE_HANDLE=60;
	public final static int AVTIVITY_TYPE_HANDLE_CONFIRM=63;
	public final static int AVTIVITY_TYPE_ACCEPT=70;
	public final static int AVTIVITY_TYPE_CLOSE=80;
	public final static int AVTIVITY_TYPE_CANCEL=90;
	
	//process flag
	public final static int PROCESS_DONE=0;
	public final static int PROCESS_WAIT=1;
	public final static int PROCESS_UNDO=2;
	
	//process request level
	public final static String REQUEST_LEVEL_NORMAL="R";
	public final static String REQUEST_LEVEL_FAST="E";
	public final static String REQUEST_LEVEL_URGENT="U";
	
	//Rule type
	public final static int RULE_NONE=0;
	public final static int RULE_PASS=1;
	public final static int RULE_ASSIGN_DEPT_TOP=2;
	public final static int RULE_APPLICANT_TOP=3;
	public final static int RULE_SOME_ONE=4;
	public final static int RULE_SELF=5;
	public final static int RULE2_ASSIGN_DEPT_TOP=102;
	public final static int RULE2_APPLICANT_TOP=103;
	public final static int RULE2_SOME_ONE=104;
	
	//DEPT_MEMBER_TYPE
	public final static String DMEMBER_TYPE_MASTER="M";
	public final static String DMEMBER_TYPE_SLAVE="S";
	public final static String DMEMBER_TYPE_PLURALISM="P";
	public final static String DMEMBER_TYPE_NORMAL="N";
	
	//organization level 
	public final static int ORG_LEVEL_SECTION= 5;
	public final static int ORG_LEVEL_DEPARTMENT= 4;
	public final static int ORG_LEVEL_DIVISION= 3;
	public final static int ORG_LEVEL_SERVICE_CENTER= 2;
	public final static int ORG_LEVEL_GROUP= 1;
	
}
