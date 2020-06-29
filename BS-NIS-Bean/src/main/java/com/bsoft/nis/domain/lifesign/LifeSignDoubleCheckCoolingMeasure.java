package com.bsoft.nis.domain.lifesign;


/**
 * @author Tank   E-mail:zkljxq@126.com
 * @version 创建时间：2013-12-4 上午3:49:34
 * @类说明
 */
public class LifeSignDoubleCheckCoolingMeasure{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 代码识别
	 */
	public int DMSB;
	/**
	 * 代码名称
	 */
	public String DMMC;
	
	@Override
	public boolean equals(Object o) {
		return this.DMSB==((LifeSignDoubleCheckCoolingMeasure)o).DMSB;
	}

}
