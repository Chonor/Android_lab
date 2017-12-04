import java.util.*;
import java.sql.*;
public class DeleteStu {
    static private Connection conn;
    static int cnt = 0;
    static int count_data=-1;
    static Scanner in = new Scanner(System.in);
    public static void main(String args[]) {
        if (!connect()) {
            System.out.println("Connect Error!");
            return;
        }
        String s;
        while ((s=getNextLineWords())!=null){
            if(s.substring(0,1).equals("-")) {
                s = s.substring(1, s.length());
                executeUpdate("DELETE  FROM stu WHERE  id=" + s + ";");
            }
            else
                executeUpdate("DELETE  FROM stu WHERE num LIKE '%"+s+"%'  OR name LIKE '%"+s+"%';");
        }
    }
    public static String getNextLineWords(){
        if (cnt++ == 0)
            System.out.println("输入学号,姓名或者id,学号，姓名，exit或空行退出.\r\n");
        System.out.print(cnt+"> ");
        if (!in.hasNextLine()) // 是否还有输入
            return null;
        String s = in.nextLine().replace(" ","");//读取下一行
        if (s.equals("exit") || s.length() == 0)
            return null;
        return s;
    }
    private static boolean connect() {// 建立连接
        String connectString = "jdbc:mysql://172.18.187.231:53306/teaching13"
                +"?autoReconnect=true&useUnicode=true"
                +"&characterEncoding=UTF-8&useSSL=false";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(connectString, "user", "123");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    static private void executeUpdate(String sqlSentence) {//执行SQL查询语句, 返回结果集
        Statement statement;
        ResultSet rs = null;
        try {
            statement = conn.createStatement();       //获取执行sql语句的对象
            count_data = statement.executeUpdate(sqlSentence); //执行sql插入，返回结果集
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if(count_data>=0)System.out.println(count_data + "个记录被删除\n");
        else System.out.println("0个记录被删除\n");
    }
}


