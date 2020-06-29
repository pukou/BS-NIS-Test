package com.bsoft.nis.domain.dangerevaluate;

import java.io.Serializable;

/**
 * Description: 保存风险记录时，从Android端接受的数据，包含MeasureRecord,PGDH,ZYH,BQID,JGID
 * User: 苏泽雄
 * Date: 16/12/12
 * Time: 1:31:30
 */
public class MeasureRecordPostData implements Serializable {

	private static final long serialVersionUID = 2151190606926806619L;

	public MeasureRecord MeasureRecord;

	public String PGDH;

	public String ZYH;

	public String BQID;

	public String JGID;
}
