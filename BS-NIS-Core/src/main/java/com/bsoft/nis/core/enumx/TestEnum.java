package com.bsoft.nis.core.enumx;

/**
 * Describtion:
 * Created: dragon
 * Date： 2016/11/22.
 */
public enum  TestEnum implements EnumBehaviour {
    ;

    protected String name ;
    protected int id;

    TestEnum(String name,int id){
        this.name = name;
        this.id = id;
    }
    @Override
    public Integer get() {
        return this.id;
    }

}
