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
//    //查询车辆列表
//    public static  String getCar="http://yzapi.chetxt.com:82/yzcl/operation/v1/vehicleManage/getCar";
    //查询结清列表
    public static  String querySettleList="https://lbstec.cn/yzcl/operation/v1/settleReport/querySettleList";
    //取消结清
    public static  String cancelSettleById="https://lbstec.cn/yzcl/operation/v1/settleReport/cancelSettleById";
    //获取各预警数量
    public static  String queAlarmStati="https://lbstec.cn/yzcldevice/v1/queAlarmStati";
//    //获取高危预警列表
//    public static  String queryHighAlarmList="https://lbstec.cn/yzcl/operation/v1/highAlarmReport/queryHighAlarmList";
    //警情明细即设备预警
    public static  String getAlarmInfo="https://lbstec.cn/yzcl/alarm/v1/alert/getAlarmInfo";
    public static String Jsonp_GetDevicePositionByUserID="http://m1api.chetxt.com:8083/customer.asmx/Jsonp_GetDevicePositionByUserID";
    //用于存放各种接口
    public static final String GET_PROVINCE_CITY ="http://m1api.chetxt.com:8083/DataLib.asmx/Json_GetProvinceCity";
//    http://101.37.119.32:20200//operation/v1/monitor/queVehicleListForSea?token
//    public  static  String MonitorUrl="http://101.37.119.32:20200/";//测试服
    public  static  String MonitorUrl="http://47.98.146.91:20200/";//正式服
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
    //获取我的信息
    public static final String getUserGeneralInfo=MonitorUrl+"/operation/v1/init/getUserGeneralInfo";
    //获取用户详细个人信息
    public static final String getDetailUser=MonitorUrl+"/operation/v1/user/getDetailUser";
    //更新用户数据
    public static final String updateUser=MonitorUrl+"/operation/v1/user/updateUser";
    //修改密码
    public static final String updateUserPw=MonitorUrl+"/operation/v1/user/updateUserPw";
    //获取单个设备的信息
    public static final String queDeviceGpsInfo=MonitorUrl+"/devicegps/v1/queDeviceGpsInfo";
    //获取当前设备的指令状态
    public static final String queDeviceSetting=MonitorUrl+"/device/v1/queDeviceSetting";
    //轨迹查询
    public static final String getTrack=MonitorUrl+"/devicegps/v1/getTrack";
    //提交指令模式
    public static final String saveOrUpdateDeviceSetting=MonitorUrl+"/device/v1/saveOrUpdateDeviceSetting";
    //不用验证码的登录接口
    public  static  String loginRbn=MonitorUrl+"auth/v1/loginRbn";
    //获取车辆报警类型
    public static final String getCar=MonitorUrl+"/operation/v1/vehicleManage/getCar";
    //获取高危报警类型
    public static final String queryHighAlarmList=MonitorUrl+"/operation/v1/highAlarmReport/queryHighAlarmList";
    //权限设置
//    public  static  String getUserGeneralInfo=MonitorUrl+"operation/v1/init/getUserGeneralInfo";
}
