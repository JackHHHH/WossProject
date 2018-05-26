package com.briup.server;

import com.briup.woss.bean.BIDR;
import com.briup.woss.server.DBStore;
import com.briup.woss.server.Server;
import com.briup.woss.util.Configuration;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Properties;

public class ServerImpl implements Server{
    private ServerSocket ss;
    private int port;
    private ObjectInputStream oi;
    Configuration conf;

    @Override
    public void init(Properties p) {
        port=Integer.parseInt((String) p.get("port"));
    }
    @Override
    public void reciver() throws Exception {
        //������������Socket��������ĳһ�˿���
        ss=new ServerSocket(port);
        //���տͻ����󣬻�ȡ�ͻ���Socket
        while(true) {
            Socket s = ss.accept();
            //ͨ���ͻ���Socket����ȡ�ͻ��˵�������
            oi = new ObjectInputStream(s.getInputStream());
            List<BIDR> c = (List<BIDR>) oi.readObject();
            for (int i=0;i<c.size();i++){
                System.out.println(c.get(i).getLogin_ip());
            }
            DBStore dbStore = conf.getDbStore();
            dbStore.saveToDB(c);
        }

    }

    @Override
    public void shutDown() throws Exception {
        Socket s = ss.accept();
        s.shutdownOutput();
    }


    @Override
    public void setConfiguration(Configuration configuration) {
        this.conf=configuration;
    }
}
