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
//    //查询结清列表
//    public static  String querySettleList="https://lbstec.cn/yzcl/operation/v1/settleReport/querySettleList";

//    //获取高危预警列表
//    public static  String queryHighAlarmList="https://lbstec.cn/yzcl/operation/v1/highAlarmReport/queryHighAlarmList";
    //警情明细即设备预警
    public static  String getAlarmInfo="https://lbstec.cn/yzcl/alarm/v1/alert/getAlarmInfo";
    public static String Jsonp_GetDevicePositionByUserID="http://m1api.chetxt.com:8083/customer.asmx/Jsonp_GetDevicePositionByUserID";
    //用于存放各种接口
    public static final String GET_PROVINCE_CITY ="http://m1api.chetxt.com:8083/DataLib.asmx/Json_GetProvinceCity";
//    http://101.37.119.32:20200//operation/v1/monitor/queVehicleListForSea?token
    public  static  String MonitorUrl="http://101.37.119.32:20200/";//测试服
//    public  static  String MonitorUrl="http://47.98.146.91:20200/";//正式服
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
//    //获取车辆报警类型
//    public static final String getCar=MonitorUrl+"/operation/v1/vehicleManage/getCar";
    //获取高危报警类型
    public static final String queryHighAlarmList=MonitorUrl+"/operation/v1/highAlarmReport/queryHighAlarmList";
    //获取设备报警状态统计
    public static final String queAlarmStati=MonitorUrl+"device/v1/queDeviceIsUsedStati";
    //获取设备在离线状态统计/device/v1/queDeviceInfoStatistics
    public static final String queDeviceIsUsedStati=MonitorUrl+"/device/v1/queDeviceInfoStatistics";
    //获取车辆逾期统计
    public static final String getRedBind=MonitorUrl+"operation/v1/vehicleManage/getRedBind";
    //获取客户组织
    public static final String getCustomer=MonitorUrl+"operation/v1/customer/getCustomer";
    //获取车辆列表getVehicleManageExp
    public static final String getCar=MonitorUrl+"/operation/v1/vehicleManage/getVehicleManageExp";
    //结清列表和统计
    public static final String querySettleList=MonitorUrl+"/operation/v1/settleReport/querySettleList";
    //选择账号搜索
    public static final String queCustMerBySearch=MonitorUrl+"/operation/v1/customer/queCustMerBySearch";
    //定时请求的接口
    public static final String queAlarmTopCount=MonitorUrl+"/alarm/v1/alert/queAlarmTopCount";
    //标记逾期光感/operation/v1/vehicleManage/
    public static final String setRemark=MonitorUrl+"/operation/v1/vehicleManage/setRemark";
    //取消结清
    public static  String cancelSettleById=MonitorUrl+"/operation/v1/settleReport/cancelSettleById";
    //设备列表
    public static  String queryDeviceList=MonitorUrl+"/device/v1/queryDeviceListApp";
    //统计车辆数量/operation/v1/monitor/queVehicleListCount
    public static  String queVehicleListCount=MonitorUrl+"/operation/v1/monitor/queVehicleListCount";
    //获取省市区接口operation/v1/vehicleManage/getAddress
    public static  String getAddress=MonitorUrl+"/operation/v1/vehicleManage/getAddress";
    //获取车辆品牌/operation/v1/vehicleManage/getCarBand
    public static  String getCarBand=MonitorUrl+"/operation/v1/vehicleManage/getCarBand";
    //新增车辆/operation/v1/vehicleManage/add
    public static  String add=MonitorUrl+"/operation/v1/vehicleManage/add";
    //新增车辆时添加设备/operation/v1/vehicleManage/getDeviceMess
    public static  String getDeviceMess=MonitorUrl+"/operation/v1/vehicleManage/getDeviceMess";
    //提交图片/file/v1/upload
    public static  String upload=MonitorUrl+"/file/v1/upload";
    //获取车辆下的设备信息/operation/v1/monitor/queCarGpsInfo
    public static  String queCarGpsInfo=MonitorUrl+"/operation/v1/monitor/queCarGpsInfo";
    //给车辆添加设备/operation/v1/vehicleManage/addDevice
    public static  String addDevice=MonitorUrl+"/operation/v1/vehicleManage/addDevice";
    //添加安装位置/operation/v1/vehicleManage/getInstallPart
    public static  String getInstallPart=MonitorUrl+"/operation/v1/vehicleManage/getInstallPart";

}
