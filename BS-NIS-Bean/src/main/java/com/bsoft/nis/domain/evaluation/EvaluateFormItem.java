package com.bsoft.nis.domain.evaluation;


import com.bsoft.nis.domain.evaluation.dynamic.Form;
import com.bsoft.nis.domain.synchron.SelectResult;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author hy
 *
 */
public class EvaluateFormItem {

	public Form form  = new Form();
	
	public EvaluateCheckForm BTX ;
	
	public int IsSync;

	public List<SelectResult> list = new ArrayList<>();
}
