package com.bsoft.nis.domain.evaluation.db;


/**
 * 表单记录分类表
 * @author hy
 *
 */
public class EvaluateBDJLFLVo {

	private Long JLFL;

	private String JLXH;

	private String YSFL;

	private String FLDJ;

	private String HSQM1;

	private String HSQM2;

	private String QMSJ1;

	private String QMSJ2;

	public  String type;

	public String FLLX = "0";

	public String dbtype;

	public Long getJLFL() {
		return JLFL;
	}

	public void setJLFL(Long JLFL) {
		this.JLFL = JLFL;
	}

	public String getJLXH() {
		return JLXH;
	}

	public void setJLXH(String JLXH) {
		this.JLXH = JLXH;
	}

	public String getYSFL() {
		return YSFL;
	}

	public void setYSFL(String YSFL) {
		this.YSFL = YSFL;
	}

	public String getFLDJ() {
		return FLDJ;
	}

	public void setFLDJ(String FLDJ) {
		this.FLDJ = FLDJ;
	}

	public String getHSQM1() {
		return HSQM1;
	}

	public void setHSQM1(String HSQM1) {
		this.HSQM1 = HSQM1;
	}

	public String getHSQM2() {
		return HSQM2;
	}

	public void setHSQM2(String HSQM2) {
		this.HSQM2 = HSQM2;
	}

	public String getQMSJ1() {
		return QMSJ1;
	}

	public void setQMSJ1(String QMSJ1) {
		this.QMSJ1 = QMSJ1;
	}

	public String getQMSJ2() {
		return QMSJ2;
	}

	public void setQMSJ2(String QMSJ2) {
		this.QMSJ2 = QMSJ2;
	}
}
