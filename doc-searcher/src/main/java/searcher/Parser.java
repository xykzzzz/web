package searcher;

import model.DocInfo;

import java.io.*;
import java.util.ArrayList;

//解析
public class Parser {
    public static final String API_PATH="D:\\docs\\api";
    public static final String RAW_DATA="D:/raw_data.txt";
    public static final String RAW_DATA1="/root/opt/resource/raw_data.txt";
    public   static final String BASE_API="https://docs.oracle.com/javase/8/docs/api";
    public static void main(String[] args) {
        try {
            File resultFile=new File(RAW_DATA);
            FileWriter fw=new FileWriter(resultFile);
            PrintWriter pw=new PrintWriter(fw,true);
            //1.枚举这个目录下所有文件
            ArrayList<File> fileList=new ArrayList<>();
            enumFile(API_PATH,fileList);
            for (File html:fileList) {
                DocInfo doc=parseHtml(html);
                System.out.println(doc.toString());
                pw.println(doc.getTitle()+"\3"+doc.getUrl()+"\3"+doc.getContent());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static DocInfo parseHtml(File html) {
        DocInfo doc=new DocInfo();
        doc.setTitle(html.getName().substring(0,html.getName().length()-".html".length()));
        //获取相对路径
        String uri = html.getAbsolutePath().substring(API_PATH.length());
        doc.setUrl(BASE_API+uri);
        doc.setContent(parseContent(html));
        return doc;

    }
    //解析html文件内容 <标签> 内容<标签>，内容为正文 ，只取内容，有多个标签就凭借
    private static String parseContent(File html) {
        StringBuilder sb=new StringBuilder();
        try {
            FileReader fileReader=new FileReader(html);
            int i;
            boolean isContent=false;
            //一个一个字符读取
            while ((i=fileReader.read())!=-1){
                char c=(char)i;
                //根据读取的字符是否是'<'或'>',isContent决定是否拼接。
                if(isContent){
                    if(c=='<'){//当前标签的内容结束
                        isContent=false;
                        continue;
                    }else if(c=='\n'||c=='\r'){
                        sb.append(" ");
                    }else {
                        sb.append(c);//拼接标签内容
                    }
                }else if(c=='>'){//当前不是正文,并且读取到>,之后就是正文
                    isContent=true;
                }
            }
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
        return sb.toString();
    }
    private static void enumFile(String rootPath, ArrayList<File> fileList) {
        File rootFile=new File(rootPath);
        File[] files = rootFile.listFiles();
        for (File f:files) {
            if(f.isDirectory()){
                enumFile(f.getAbsolutePath(),fileList);
            }else if(f.getAbsolutePath().endsWith(".html")){
                fileList.add(f);
            }
        }
    }
}
