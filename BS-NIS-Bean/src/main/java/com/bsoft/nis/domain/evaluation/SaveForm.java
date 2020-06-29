package com.bsoft.nis.domain.evaluation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SaveForm {

	public String ID;

	public String YSXH;

	public String Score;

	public String TXGH;

	public String JLGH;

	public String TXSJ;

	public String JLSJ;

	public String ZYH;

	public String YSLX;

	public String DLBZ;

	public String QMBZ;

	public String HSQM1;

	public String HSQM2;

	public String LYBS;

	public String FLLX = "0";

	public String IsScored;
	@JsonProperty(value = "Item")
	public VEntity[] entities;

	public String XSFLLX;

}
