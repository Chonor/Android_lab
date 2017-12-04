import java.io.*;
import java.util.*;

class ShowTags{
    public static void main(String[] args)throws IOException{
        HashMap<String,Integer>map=new HashMap<>();
        String content = readFile(".\\grassland.htm");
        content=content.toUpperCase();
        boolean flag = false;
        ArrayList<String>out=new ArrayList<>();
        String label="";
        for(int i=0;i<content.length();i++){ //分离标签
            char temp=content.charAt(i);
            if(flag&& (temp == '>' || temp == ' ')) {
                flag = false;
                out.add(label);
            }
            if(flag)
                label+=temp;
            if(temp == '<') {
                label = "";
                flag=true;
            }
        }
        for(String s:out){//map映射
            if(!s.equals("")){
                if(map.containsKey(s))
                    map.put(s,map.get(s)+1);
                else map.put(s,1);
            }
        }
        int count=0;  //遍历map输出
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            int val = (Integer) entry.getValue();
            if((count++)%3==2)System.out.println("<"+key+">:"+val);
            else System.out.print("<"+key+">:"+val+"\t");
        }
    }

    static String readFile(String fileName) throws IOException{
        StringBuilder sb = new StringBuilder("");
        int c1;
        FileInputStream f1= new FileInputStream(fileName);
        InputStreamReader in = new InputStreamReader(f1, "UTF-8");

        while ((c1 = in.read()) != -1) {
            sb.append((char) c1);
        }
        return sb.toString();
    }
}
