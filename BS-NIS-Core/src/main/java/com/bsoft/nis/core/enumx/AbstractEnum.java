package com.bsoft.nis.core.enumx;

/**
 * Describtion:枚举
 * Created: dragon
 * Date： 2016/11/22.
 */
public  enum  AbstractEnum implements EnumBehaviour{
    ;

    protected String name ;
    protected int id;

    AbstractEnum(String name,int id){
        this.name = name;
        this.id = id;
    }
    @Override
    public Integer get() {
        return this.id;
    }
}
