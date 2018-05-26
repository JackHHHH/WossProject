package com.briup.util;

import com.briup.woss.client.Client;
import com.briup.woss.client.Gather;
import com.briup.woss.common.ConfigurationAWare;
import com.briup.woss.common.WossModule;
import com.briup.woss.server.DBStore;
import com.briup.woss.server.Server;
import com.briup.woss.util.BackUP;
import com.briup.woss.util.Configuration;
import com.briup.woss.util.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ConfigurationImpl implements Configuration {

    String filePath="GatherAndStore\\src\\main\\java\\com\\briup\\File\\conf.xml";
    //����wossģ�����
    Map<String,WossModule> wossMap=new HashMap<>();
    //���������Ϣ
    Properties pro=new Properties();
    @Override
    public BackUP getBackup() throws Exception {
        return (BackUP) wossMap.get("backup");
    }

    @Override
    public Logger getLogger() throws Exception {
        return (Logger) wossMap.get("logger");
    }

    @Override
    public Server getServer() throws Exception {
        return (Server) wossMap.get("server");
    }

    @Override
    public DBStore getDbStore() throws Exception {
        return (DBStore) wossMap.get("dbstore");
    }

    @Override
    public Client getClient() throws Exception {
        return (Client) wossMap.get("client");
    }

    @Override
    public Gather getGather() throws Exception {
        return (Gather) wossMap.get("gather");
    }


    public static void main(String[] args) throws Exception {
        new ConfigurationImpl().getDbStore();
    }

    public ConfigurationImpl() {
        try {
            //1.��ȡ����������ȡconf.xml
            //����SAXReader��ȡ����ר�����ڶ�ȡxml
            SAXReader saxReader=new SAXReader();
            //2.��ȡ����
            Document document=saxReader.read(filePath);
            Element rootElement=document.getRootElement();
            //3.��ȡ�ӽڵ�--����ֵ
            List elements=rootElement.elements();
            for(Object object:elements){
                Element e=(Element)object;
                String name=e.getName();
                String attValue=e.attributeValue("class");
                //ͨ�������ȡ����
                WossModule woss;
                try {
                    woss = (WossModule)Class.forName(attValue).newInstance();
                    //System.out.println(woss);
                    wossMap.put(name, woss);
                    //System.out.println(name);
                    for(String key:wossMap.keySet()){
                    //System.out.println(key+":"+wossMap.get(key));
                    //4.�̶�ֵ-->Properties
                    List ee=e.elements();
                    for(Object obj:ee){
                        Element el=(Element)obj;
                        String key1=el.getName();
                        String value=el.getText();
//						System.out.println(key1);
//						System.out.println(el.getName()+"*:*"+el.getText());
                        pro.put(key1, value);
                        String po=(String)pro.get("po");
                    }
                    //������Ϣ����ע��
                    for(Object obj:wossMap.values()){
                        //����init����������ע��������Ϣ
                        if(obj instanceof WossModule){
                            ((WossModule) obj).init(pro);
                        }
                        if(obj instanceof ConfigurationAWare){
                            ((ConfigurationAWare)obj).setConfiguration(this);
                        }
                    }
                    }
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
