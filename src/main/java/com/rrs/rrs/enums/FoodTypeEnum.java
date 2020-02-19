package com.rrs.rrs.enums;

import java.util.ArrayList;
import java.util.List;

public enum FoodTypeEnum {
    AA("AA","小炒"),
    AB("AB","蒸菜"),
    BC("BC","凉菜"),
    BD("BD","甜点"),
    BE("BE","汤类"),
    CF("CF","米饭"),
    AG("AG","海鲜"),
    DH("DH","美酒"),
    DI("DI","饮料"),
    CJ("CJ","粉面"),
    E("E","其它");


    private String type;
    private String message;

    FoodTypeEnum(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getClassify(){
        char classify=type.charAt(0);
        if (classify=='A')return "主菜";
        else if (classify=='B')return "配菜";
        else if (classify=='C')return "主食";
        else if (classify=='D')return "饮品";
        else return "其它";
    }

    public List listByClassify(){
        FoodTypeEnum[] foodTypeEnums=FoodTypeEnum.values();
        List A=new ArrayList();
        List B=new ArrayList();
        List C=new ArrayList();
        List D=new ArrayList();
        List E=new ArrayList();
        List list=new ArrayList();
        for (FoodTypeEnum foodType:foodTypeEnums) {
            if (foodType.getClassify().equals("主菜"))A.add(foodType);
            else if (foodType.getClassify().equals("配菜"))B.add(foodType);
            else if (foodType.getClassify().equals("主食"))C.add(foodType);
            else if (foodType.getClassify().equals("饮品"))D.add(foodType);
            else E.add(foodType);
        }
        list.add(A);
        list.add(B);
        list.add(C);
        list.add(D);
        list.add(E);
        return list;
    }
}
