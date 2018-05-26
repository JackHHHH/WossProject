package com.briup.cxx.woss.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

public class WorssMapReduce extends Configured implements Tool {
    @Override
    public int run(String[] strings) throws Exception {
        //��ҵ����
        //��ȡconfiguration����
        Configuration conf = getConf();
        //���������������·��
        Path input=new Path(conf.get("input"));
        Path output= new Path(conf.get("output"));
        //������ҵ����.��ȡjob����
        Job job=Job.getInstance(conf,"WossMR");//����ģʽ
        //������ҵ��Ҫ���е���
        job.setJarByClass(this.getClass());
        //job.setJarByClass(WorssMapReduce.class);.class �������
        //���ú�map�׶���ص���Ϣ
        job.setMapperClass(WorssMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        //����mapper����������ĸ�ʽ�������·��
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,input);
        //����reduce�������Ϣ
        //����reduceִ�е�����
        job.setReducerClass(WorssReduce.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);
        /*map���-reduce���� �ɿ�ܾ���*/
        job.setOutputFormatClass(TextOutputFormat.class);//�����ʽ
        TextOutputFormat.setOutputPath(job,output);
        //set add ���·��ֻ��һ��������·�����кܶ��

        //�ύ��ҵ
        //true ռ���ն�
        //false
        boolean comp = job.waitForCompletion(true);
        //if(comp) return 0;
        //else return 1;
        return comp?0:1;
    }

    static class WorssMapper extends Mapper<LongWritable,Text,Text,Text>{
    //LongWritable��ƫ�������ֽ���0 +���з���
    //loginip����key
    //mapper�ҵ���ͬloginip����reduce
        private WossDataParser parser = new WossDataParser();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            parser.parse(value);
            if(parser.isValid()){
                context.write(new Text(parser.getLoginIp()),value);
            }
        }
    }


    //ʹ��sqoop2->mysql
    static class WorssReduce extends Reducer<Text,Text,NullWritable,Text>{
        private WossDataParser parser = new WossDataParser();
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        //map���ϲ��ɵ���
        //��������Iterable ��ײ�
            String result="";
            StringBuilder sb = new StringBuilder();
            for (Text value : values) {
                //value����һ������
                parser.parse(value);
                if(parser.isValid()){
                    String aaaName=null;
                    String loginIp=null;
                    Long loginDate=null;
                    Long logoutDate=null;
                    String nasIp=null;
                    Long timeDuration=null;
                    if("7".equals(parser.getFlag())){
                        aaaName=parser.getAaaName();
                        loginIp=parser.getLoginIp();
                        loginDate=parser.getTime();
                        nasIp=parser.getNasIp();
                    }else if("8".equals(parser.getFlag())){
                        logoutDate=parser.getTime();
                        timeDuration=logoutDate-loginDate;
                    }
                    sb.append(aaaName).append("\t");
                    sb.append(loginDate).append("\t");
                    sb.append(logoutDate).append("\t");
                    sb.append(loginIp).append("\t");
                    sb.append(nasIp).append("\t");
                    sb.append(timeDuration).append("\t");
                    context.write(
                            NullWritable.get(),
                            new Text(sb.toString()));
                }
            }
        }
    }
    //��̬�����в�����this
    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new WorssMapReduce(),args));
    }
}
