package com.bsoft.nis.domain.evaluation.dynamic;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class RadioBox {

	@JsonProperty(value = "CheckBox")
	public List<CheckBox> cbs ;

	@JsonProperty(value = "RadioBox")
	public List<RadioBox> rbs;

	@JsonProperty(value = "Numeric")
	public List<Numeric> numbers;

	@JsonProperty(value = "Label")
	public List<Label> labels;

	@JsonProperty(value = "Input")
	public List<Input> inputs;

	@JsonProperty(value = "DateTime")
	public List<DateTime> datetimes;

	@JsonProperty(value = "ChildViewModel")
	public List<ChildViewModel> childViewModelLists=new ArrayList<>();

	public int ID = 0;

	public String Text;

	public String ParentID;

	public String Value;

	public String ValueType;

	public String NewLine;

	public String CtrlType = "RadioBox";

	public String Font;

	public String IsScored;

	public String Score;

	public String GroupId;

	public int IsSelected = 0;

	public String FrontId;

	public String PostpositionId;

	public String Jfgz;

	public String Xxdj;

	public String Dzlx;

	public String Dzbd;

	public String Dzxm;

	public String Dzbdlx;

	public String Btbz;



	public Void addControlList(List list) {
		List<CheckBox> list1  = new ArrayList();
		List<RadioBox> list2  = new ArrayList();
		List<Numeric> list3  = new ArrayList();
		List<Input> list4  = new ArrayList();
		List<DateTime> list5  = new ArrayList();
		List<Label> list6 = new ArrayList();
		String name;
		for (int i = 0; i< list.size();i++){
			name = list.get(i).getClass().getSimpleName();
			switch (name){
				case "CheckBox":
					list1.add((CheckBox) list.get(i));
					childViewModelLists.add(new ChildViewModel(ChildViewModel.CheckBox,list.get(i)));
					break;
				case "RadioBox":
					list2.add((RadioBox) list.get(i));
					childViewModelLists.add(new ChildViewModel(ChildViewModel.RadioBox,list.get(i)));
					break;
				case "Numeric":
					list3.add((Numeric) list.get(i));
					childViewModelLists.add(new ChildViewModel(ChildViewModel.Numeric,list.get(i)));
					break;
				case "Input":
					list4.add((Input) list.get(i));
					childViewModelLists.add(new ChildViewModel(ChildViewModel.Input,list.get(i)));
					break;
				case "DateTime":
					list5.add((DateTime) list.get(i));
					childViewModelLists.add(new ChildViewModel(ChildViewModel.DateTime,list.get(i)));
					break;
				case "Label":
					list6.add((Label) list.get(i));
					childViewModelLists.add(new ChildViewModel(ChildViewModel.Label,list.get(i)));
					break;
				default:
					break;
			}
		}
		if(list1.size()>0){
			cbs = list1;
		}
		if(list2.size()>0){
			rbs = list2;
		}
		if(list3.size()>0){
			numbers = list3;
		}
		if(list4.size()>0){
			inputs = list4;
		}
		if(list5.size()>0){
			datetimes = list5;
		}
		if (list6.size() > 0) {
			labels = list6;
		}
		return null;
	}
}
