import java.util.*;
import java.sql.*;

public class QueryStu {
    static private Connection conn;
    static int cnt = 0;
    static Scanner in = new Scanner(System.in);
    public static void main(String args[]) {
        if (!connect()) {
            System.out.println("Connect Error!");
            return;
        }
        String s;
        while ((s=getNextLineWords())!=null) {
            ResultSet rs;
            if(s.substring(0,1).equals("*"))
                rs = executeQuery("select * from stu;");
            else if(s.substring(0,1).equals("-"))
                rs = executeQuery("SELECT * FROM stu WHERE id="+s.substring(1,s.length()) + ";");
            else
                rs = executeQuery("SELECT * FROM stu WHERE num LIKE \"%"+s+"%\""+" OR name LIKE \"%"+s+"%\" ORDER BY num;");
            showStuInfo(rs);
        }
    }

    public static String getNextLineWords(){
        if (cnt++ == 0)
            System.out.println("查询学号和姓名(部分匹配)，以-开头查询id，*查出所有的记录，exit或空行退出.\r\n");
        System.out.print(cnt+"> ");
        if (!in.hasNextLine()) // 是否还有输入
            return null;
        String line = in.nextLine().replace(" ", "");//读取下一行并去所有空格
        if (line.equals("exit") || line.length() == 0)
            return null;
        return line;
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
    static private ResultSet executeQuery(String sqlSentence) {//执行SQL查询语句, 返回结果集
        Statement statement;
        ResultSet rs = null;
        try {
            statement = conn.createStatement();       //获取执行sql语句的对象
            rs = statement.executeQuery(sqlSentence); //执行sql查询，返回结果集
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }
    private static void showStuInfo(ResultSet rs){//显示查询结果
        try {
            int count = 0;
            while(rs.next()){
                System.out.println(rs.getString("id")+" "+rs.getString("num")+" "+rs.getString("name"));
                count++;
            }
            System.out.println("["+count+"条记录]");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}