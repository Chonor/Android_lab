package cn.chonor.lab9.model;

/**
 * Created by Chonor on 2017/12/22.
 */

public class Repositories {
    private String name;//repos 名字
    private String language;//语言
    private String html_url;//具体地址
    private String description;//描述
    public  String getName(){return name;}
    public  String getLanguage(){return  language;}
    public String getDescription(){return description;}
    public String getHtml_url(){return html_url;}
}
