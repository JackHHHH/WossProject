package com.briup.cxx.woss.mr;

import org.apache.hadoop.io.Text;

/**
 * ����ԭʼ������
 */
public class WossDataParser {
    private String aaaName;
    private String nasIp;
    private String flag;
    private Long time;
    private String loginIp;
    //���ֶ��жϽ����ĵ�ǰ�������Ƿ��Ǻ��������
    private boolean valid;

    public void parse(String line){
        String[] strs = line.split("[|]");
        //����ָ���ַ������鳤��С��5���������ݲ�����
        if (strs.length<5){
            valid=false;
            return;
        }
        aaaName=strs[0];
        nasIp=strs[1];
        flag=strs[2];
        time=Long.parseLong(strs[3])*1000;
        loginIp=strs[4];
        valid=true;
    }


    //����parse����
    //���л�java��ռ���ֽڸ���hadoop������ռ�ֽ���С�ܶ�
    public void parse(Text line){
        parse(line.toString());
    }

    //get set


    public String getAaaName() {
        return aaaName;
    }

    public void setAaaName(String aaaName) {
        this.aaaName = aaaName;
    }

    public String getNasIp() {
        return nasIp;
    }

    public void setNasIp(String nasIp) {
        this.nasIp = nasIp;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
