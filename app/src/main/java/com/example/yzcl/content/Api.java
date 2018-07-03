package com.example.yzcl.content;

/**
 * Created by Lenovo on 2017/11/28.
 */

public class Api {
    public  static  String GPSURL="http://101.37.119.32:20204/";
    //登录接口
    public  static  String Jsonp_GetLogin=GPSURL+"v1/login";
    //用户管理所有下属客户
    public  static  String GetCustomer=GPSURL+"operation/v1/customer/getCustomer";
    //围栏批量开启
    public static  String batchOpen=GPSURL+"fence/v1/batchOpen";
    //围栏批量关闭
    public static  String batchClose=GPSURL+"fence/v1/batchClose";
    //删除围栏
    public static  String delFence=GPSURL+"fence/v1/delFence";
    //查询车辆列表
    public static  String getCar="http://yzapi.chetxt.com:82/yzcl/operation/v1/vehicleManage/getCar";
    //查询结清列表
    public static  String querySettleList="https://lbstec.cn/yzcl/operation/v1/settleReport/querySettleList";
    //取消结清
    public static  String cancelSettleById="https://lbstec.cn/yzcl/operation/v1/settleReport/cancelSettleById";
    //获取各预警数量
    public static  String queAlarmStati="https://lbstec.cn/yzcldevice/v1/queAlarmStati";
    //获取高危预警列表
    public static  String queryHighAlarmList="https://lbstec.cn/yzcl/operation/v1/highAlarmReport/queryHighAlarmList";
    //警情明细即设备预警
    public static  String getAlarmInfo="https://lbstec.cn/yzcl/alarm/v1/alert/getAlarmInfo";
    public static String Jsonp_GetDevicePositionByUserID="http://m1api.chetxt.com:8083/customer.asmx/Jsonp_GetDevicePositionByUserID";
    //用于存放各种接口
    public static final String GET_PROVINCE_CITY ="http://m1api.chetxt.com:8083/DataLib.asmx/Json_GetProvinceCity";
//    http://101.37.119.32:20200//operation/v1/monitor/queVehicleListForSea?token
    public  static  String MonitorUrl="http://101.37.119.32:20200/";
    //模糊搜索设备名车架号
    public static final String queVehicleListForSea=MonitorUrl+"/operation/v1/monitor/queVehicleListForSea";
    //获取车辆信息
    public static final String queVehicleList=MonitorUrl+"/operation/v1/monitor/queVehicleList";
    //获取车辆设备的GPS
    public static final String queCarDeviceGps=MonitorUrl+"/devicegps/v1/queCarDeviceGps";
    //获取车辆信息
    public static final String getCarMessageById=MonitorUrl+"/operation/v1/vehicleManage/getCarMessageById";
    //获取设备信息
    public static final String getDeviceMessageById=MonitorUrl+"/operation/v1/vehicleManage/getDeviceMessageById";

}
