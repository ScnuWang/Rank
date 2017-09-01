package cn.geekview.controller;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.List;
import java.util.UUID;

@Controller
public class IndexController {

    protected Logger logger = Logger.getLogger(this.getClass());

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    /**
     * 重定向到指定地址
     * @param response
     */
    @RequestMapping("/redirect")
    public void redirect(String url, HttpServletResponse response){
        if (url!=null){
            try {
                response.sendRedirect(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/file")
    public  String  fileUpload(HttpServletRequest request) throws IOException, ServletException {

        List<Part> parts = (List<Part>) request.getParts();

        for (Part part : parts) {
            System.out.println(part.getName());
            System.out.println(part.getInputStream());
        }
        return "index";
    }



    @RequestMapping("/fileUpload")
    @ResponseBody
    public void fileUpload(HttpServletRequest request,HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");//设置响应编码
        request.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();//获取响应输出流

        ServletInputStream inputStream = request.getInputStream();//获取请求输入流

    /*
     * 1、创建DiskFileItemFactory对象，设置缓冲区大小和临时文件目录
     *  该类有两个构造方法一个是无参的构造方法，
     *  另一个是带两个参数的构造方法
     * @param int sizeThreshold,该参数设置内存缓冲区的大小，默认值为10K。当上传文件大于缓冲区大小时，fileupload组件将使用临时文件缓存上传文件
     * @param java.io.File repository,该参数指定临时文件目录，默认值为System.getProperty("java.io.tmpdir");
     *
     *  如果使用了无参的构造方法，则使用setSizeThreshold(int sizeThreshold),setRepository(java.io.File repository)
     *  方法手动进行设置
     */
        DiskFileItemFactory factory = new DiskFileItemFactory();

        int sizeThreshold=1024*1024;
        factory.setSizeThreshold(sizeThreshold);
        File repository = new File(request.getSession().getServletContext().getRealPath("temp"));
//        System.out.println(request.getSession().getServletContext().getRealPath("temp"));
//        System.out.println(request.getRealPath("temp"));
        factory.setRepository(repository);

    /*
     * 2、使用DiskFileItemFactory对象创建ServletFileUpload对象，并设置上传文件的大小
     *
     *  ServletFileUpload对象负责处理上传的文件数据，并将表单中每个输入项封装成一个FileItem
     *  该对象的常用方法有：
     *      boolean isMultipartContent(request);判断上传表单是否为multipart/form-data类型
     *      List parseRequest(request);解析request对象，并把表单中的每一个输入项包装成一个fileItem 对象，并返回一个保存了所有FileItem的list集合
     *      void setFileSizeMax(long filesizeMax);设置单个上传文件的最大值
     *      void setSizeMax(long sizeMax);设置上传温江总量的最大值
     *      void setHeaderEncoding();设置编码格式，解决上传文件名乱码问题
     */
        ServletFileUpload upload = new ServletFileUpload(factory);

        upload.setHeaderEncoding("utf-8");//设置编码格式，解决上传文件名乱码问题
    /*
     * 3、调用ServletFileUpload.parseRequest方法解析request对象,得到一个保存了所有上传内容的List对象
     */
        List<FileItem> parseRequest=null;
        try {
            parseRequest = upload.parseRequest(request);
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
    /*
     * 4、对list进行迭代，每迭代一个FileItem对象，调用其isFormField方法判断是否是文件上传
     *  true表示是普通表单字段，则调用getFieldName、getString方法得到字段名和字段值
     *  false为上传文件，则调用getInputStream方法得到数据输入流，从而读取上传数据
     *
     *  FileItem用来表示文件上传表单中的一个上传文件对象或者普通的表单对象
     *  该对象常用方法有：
     *     boolean isFormField();判断FileItem是一个文件上传对象还是普通表单对象
     *     true表示是普通表单字段，
     *         则调用getFieldName、getString方法得到字段名和字段值
     *     false为上传文件，
     *         则调用getName()获得上传文件的文件名，注意：有些浏览器会携带客户端路径，需要自己减除
     *         调用getInputStream()方法得到数据输入流，从而读取上传数据
     *         delete(); 表示在关闭FileItem输入流后，删除临时文件。
     */

        for (FileItem fileItem : parseRequest) {
            if (fileItem.isFormField()) {//表示普通字段
                if ("username".equals(fileItem.getFieldName())) {
                    String username = fileItem.getString();
                    writer.write("您的用户名："+username+"<br>");
                }
                if ("userpass".equals(fileItem.getFieldName())) {
                    String userpass = fileItem.getString();
                    writer.write("您的密码："+userpass+"<br>");
                }

            }else {//表示是上传的文件
                //不同浏览器上传的文件可能带有路径名，需要自己切割
                String clientName = fileItem.getName();
                String filename = "";
                if (clientName.contains("\\")) {//如果包含"\"表示是一个带路径的名字,则截取最后的文件名
                    filename = clientName.substring(clientName.lastIndexOf("\\")).substring(1);
                }else {
                    filename = clientName;
                }

                UUID randomUUID = UUID.randomUUID();//生成一个128位长的全球唯一标识

                filename = randomUUID.toString()+filename;

        /*
         * 设计一个目录生成算法，如果所用用户上传的文件总数是亿数量级的或更多，放在同一个目录下回导致文件索引非常慢，
         * 所以，设计一个目录结构来分散存放文件是非常有必要，且合理的
         * 将UUID取哈希算法，散列到更小的范围，
         * 将UUID的hashcode转换为一个8位的8进制字符串，
         * 从这个字符串的第一位开始，每一个字符代表一级目录，这样就构建了一个八级目录，每一级目录中最多有16个子目录
         * 这无论对于服务器还是操作系统都是非常高效的目录结构
         */
                int hashUUID =randomUUID.hashCode();
                String hexUUID = Integer.toHexString(hashUUID);
                //System.out.println(hexUUID);
                //获取将上传的文件存存储在哪个文件夹下的绝对路径
                String filepath=request.getSession().getServletContext().getRealPath("upload");
                for (char c : hexUUID.toCharArray()) {
                    filepath = filepath+"/"+c;
                }
                //如果目录不存在就生成八级目录
                File filepathFile = new File(filepath);
                if (!filepathFile.exists()) {
                    filepathFile.mkdirs();
                }
                //从Request输入流中读取文件，并写入到服务器
                InputStream inputStream2 = fileItem.getInputStream();
                //在服务器端创建文件
                File file = new File(filepath+"/"+filename);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

                byte[] buffer = new byte[10*1024];
                int len = 0;
                while ((len= inputStream2.read(buffer, 0, 10*1024))!=-1) {
                    bos.write(buffer, 0, len);
                }
                writer.write("您上传文件"+clientName+"成功<br>");
                //关闭资源
                bos.close();
                inputStream2.close();
            }
        }
    }
}
