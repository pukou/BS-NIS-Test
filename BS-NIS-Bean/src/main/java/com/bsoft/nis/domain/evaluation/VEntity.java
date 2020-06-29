/**   
* @Title: AllowSave.java 
* @Package com.bsoft.mob.ienr.model.evaluate 
* @Description: 必填项未填写是否允许保存
* @author 吕自聪  lvzc@bsoft.com.cn
* @date 2015-12-29 下午3:18:50 
* @version V1.0   
*/ 
package com.bsoft.nis.domain.evaluation;

/**
 * @ClassName: AllowSave
 * @Description: 必填项未填写是否允许保存
 * @author 吕自聪 lvzc@bsoft.com.cn
 * @date 2015-12-29 下午3:18:50
 *
 */
public class VEntity {

	public int XXID;
	/*升级编号【56010048】============================================= start
    PDA端输入部分值（修改时选项变成新增），PC端无法显示
                              ================= Classichu 2017/10/17 8:55
    */
	public int XXID_Raw;
	/* =============================================================== end */
	public String Value = "";

	public String Score;

	public String CtrlType;

	public int XMID;

	public int JFGZ;

	public int XXDJ;

	// add by louis
	public String dzlx;
	public String dzbdjl;
	//===
}
