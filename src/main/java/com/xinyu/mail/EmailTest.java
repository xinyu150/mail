package com.xinyu.mail;

import java.io.File;

import java.util.ArrayList;

import java.util.List;

public class EmailTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        EmailSender sender = new EmailSender() {

            @Override
            protected String getSubject() {
                // TODO Auto-generated method stub
                return "测试邮件，请不要回复";
            }

            @Override
            protected String getContent() {
                // TODO Auto-generated method stub
                return "<h1>尊敬的用户:</h1><br /><p>这是一封测试邮件，请不要恢复改邮件!</p>";
            }

            @Override
            protected List<File> getAttachments() {
                // TODO Auto-generated method stub
                
                List<File> files=new ArrayList<>();
                files.add(new File("C:\\Users\\Administrator\\Desktop\\log文件\\error.log"));
//                files.add(new File("d:/test2.jar"));
                
                return files;
            }
        };

        List<String> to = new ArrayList<>();
//        to.add("409253645@qq.com");
//        to.add("sam@steperp.com");
//        to.add("hui@126.com");
        to.add("644754131@qq.com");

//        sender.send(to);
        sender.send("1060607327@qq.com");

    }

}
