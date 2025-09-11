package com.yx.seckill.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yx.seckill.entity.User;
import com.yx.seckill.vo.RespBean;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 批量生成用户并获取userTicket写到文件
 */
public class UserUtil {

    private static final String salt = "1a2b3c4d";

    /**
     * main方法入口
     */
    public static void main(String[] args) throws Exception {
        createUser(5000); // 生成5000个用户
    }

    /**
     * 批量生成用户、插入数据库、登录获取ticket写文件
     */
    private static void createUser(int count) throws Exception {
        List<User> users = new ArrayList<>(count);

        // 1. 构造内存中的用户对象
        for (int i = 0; i < count; i++) {
            User user = new User();
            // 模拟手机号作为id（长整型，11位）
            user.setId(13000000000L + i);
            user.setNickname("user" + i);
            user.setSlat(salt);
            user.setRegisterDate(LocalDateTime.now());
            user.setPassword(MD5Util.inputPassToDBPass("123456", salt)); // 与系统逻辑保持一致
            users.add(user);
        }

        System.out.println("一共生成用户: " + users.size());

        // // 2. 插入数据库
        // Connection conn = getConn();
        // String sql = "insert into t_user(id, nickname, password, slat, register_date) values(?,?,?,?,?)";
        // PreparedStatement pstmt = conn.prepareStatement(sql);
        // for (User user : users) {
        //     pstmt.setLong(1, user.getId());
        //     pstmt.setString(2, user.getNickname());
        //     pstmt.setString(3, user.getPassword());
        //     pstmt.setString(4, user.getSlat());
        //     pstmt.setTimestamp(5, Timestamp.valueOf(user.getRegisterDate()));
        //     pstmt.addBatch();
        // }
        // pstmt.executeBatch();
        // pstmt.close();
        // conn.close();
        // System.out.println("插入数据库完成");

        // 3. 利用登录接口获取token
        String urlString = "http://localhost:8080/login/doLogin";
        File file = new File("src/test/resources/users/config.txt");
        // 确保目录存在
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(0);

        for (User user : users) {
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);

            OutputStream out = co.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPassToFormPass("123456");
            out.write(params.getBytes());
            out.flush();
            out.close();

            InputStream is = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len;
            while ((len = is.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            is.close();
            String response = bout.toString();

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // 新增代码，使其支持 LocalDateTime
            RespBean respBean = mapper.readValue(response, RespBean.class);
            String userTicket = (String) respBean.getObject();

            System.out.println("用户 " + user.getId() + " 登录成功 => ticket=" + userTicket);

            // 一行写入文件：userId, userTicket
            String row = user.getId() + "," + userTicket + "\r\n";
            raf.seek(raf.length());
            raf.write(row.getBytes());
        }
        raf.close();

        System.out.println("config.txt 文件生成完成！");
    }

    /**
     * 获取数据库连接
     */
    private static Connection getConn() throws Exception {
        String url = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "aA1472580@";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }
}