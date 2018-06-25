package com.example.yzcl.mvp.model.bean;

import java.util.List;

/**
 * Created by Lenovo on 2017/11/29.
 */

public class ChildAccountBean extends BaseApiResponse{

//    {
//        Result: 0,
//                ErrorMsg: "成功",
//            UserID: "3ab403f00d324cb9807dfbb7d8bd63ee",
//            CustomerName: "test总公司",
//            TempQty: 19,
//            AleaQty: 18,
//            CarQty: 18,
//            children: [
//        {
//            UserID: "7031935cd3964bc991c5dde2046a136d",
//                    CustomerName: "test1店",
//                TempQty: 3,
//                AleaQty: 3,
//                CarQty: 3,
//                children: [
//            {
//                UserID: "cd4ad944f8fc455eb93f64d85ca74db4",
//                        CustomerName: "test11分店",
//                    TempQty: 1,
//                    AleaQty: 0,
//                    CarQty: 0
//            },
//            {
//                UserID: "4ce373ec06db4cb3b54b7b71c71139ff",
//                        CustomerName: "test12分店",
//                    TempQty: 0,
//                    AleaQty: 0,
//                    CarQty: 0
//            },
//            {
//                UserID: "9055effa805a45058b946d540be82cbb",
//                        CustomerName: "test3分店",
//                    TempQty: 0,
//                    AleaQty: 0,
//                    CarQty: 0
//            },
//            {
//                UserID: "ed7f91b5f3d54fd3bbba5817cf948889",
//                        CustomerName: "test总店",
//                    TempQty: 7,
//                    AleaQty: 5,
//                    CarQty: 5,
//                    children: [
//                {
//                    UserID: "d7b2d040025646848e03dcd58b00f256",
//                            CustomerName: "test追车",
//                        TempQty: 8,
//                        AleaQty: 2,
//                        CarQty: 2
//                },
//                {
//                    UserID: "587ae715bac74a198201df9bec74c6c5",
//                            CustomerName: "催收一组",
//                        TempQty: 1,
//                        AleaQty: 1,
//                        CarQty: 1
//                }
//]
//            }
//]
//        },
//        {
//            UserID: "c2eec583c11a41ad99bad509194d77f7",
//                    CustomerName: "测试账户",
//                TempQty: 0,
//                AleaQty: 0,
//                CarQty: 0
//        },
//        {
//            UserID: "b99bc57beb92453fb3deb95c2d555bda",
//                    CustomerName: "子账户2",
//                TempQty: 0,
//                AleaQty: 0,
//                CarQty: 0
//        },
//        {
//            UserID: "519182f7c2b142b4a4edee8a351c7df6",
//                    CustomerName: "test1店",
//                TempQty: 0,
//                AleaQty: 0,
//                CarQty: 0
//        }
//]
//    }

    private String UserID;
    private String CustomerName;
    private int TempQty;
    private int AleaQty;
    private int CarQty;
    /**
     * UserID : 132c042a4fba4f9b98f8ef979d679592
     * CustomerName : cccccc
     * TempQty : 0
     * AleaQty : 0
     * CarQty : 0
     */

    private List<ChildAccountBean> children;
    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public int getTempQty() {
        return TempQty;
    }

    public void setTempQty(int tempQty) {
        TempQty = tempQty;
    }

    public int getAleaQty() {
        return AleaQty;
    }

    public void setAleaQty(int aleaQty) {
        AleaQty = aleaQty;
    }

    public int getCarQty() {
        return CarQty;
    }

    public void setCarQty(int carQty) {
        CarQty = carQty;
    }

    public List<ChildAccountBean> getChildren() {
        return children;
    }

    public void setChildren(List<ChildAccountBean> children) {
        this.children = children;
    }



}
