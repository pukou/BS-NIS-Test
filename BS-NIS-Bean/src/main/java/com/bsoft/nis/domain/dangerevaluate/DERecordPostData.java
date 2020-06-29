package com.bsoft.nis.domain.dangerevaluate;

import java.io.Serializable;

/**
 * Description: 保存风险记录时，从Android端接受的数据，包含DERecord,ZYH,BQID,JGID
 * User: 苏泽雄
 * Date: 16/12/8
 * Time: 15:02:30
 */
public class DERecordPostData implements Serializable {

	private static final long serialVersionUID = -4882488176033668194L;

	// 保存的风险记录数据
	public DERecord DERecord;

	// 住院号
	public String ZYH;

	// 病区id
	public String BQID;

	// 机构id
	public String JGID;
}
