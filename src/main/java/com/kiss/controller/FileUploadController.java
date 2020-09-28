package com.kiss.controller;


import com.kiss.util.RunShell;
import com.sun.org.glassfish.gmbal.ParameterNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * @ClassName FileUploadController
 * @Description TODO
 * @Author kiss
 * @Date 2020/9/26 15:59
 * @Version 1.0
 */
@Controller
public class FileUploadController
{

    @RequestMapping("index")
    public String index()
    {
        return "index";
    }

    //文件打包
    @ResponseBody
    @RequestMapping("unpack")
    public Boolean unpack(Integer commandType)
    {
        System.out.println("===============multifileUpload====================");
        System.out.println(commandType);
        Boolean result = true;

        String command = "/home/kiss/test/build.sh";
        if(commandType==1){
            command = "tar -cvf ./CommProto/MiniGame.tar ./CommProto/MiniGame/";
        }else if(commandType==21){
            command = "tar -cvf ./CommProto/autogen.tar ./CommProto/autogen/";
        }
        //String command = "/home/kiss/test/a.sh";
        try{
            String res = RunShell.exec(command);
            System.out.println("====================调用shell结果=======================");
            System.out.println(res);
        }catch (InterruptedException i){
            result = false;
        }
        return result;
    }


    //多文件上传
    @ResponseBody
    @RequestMapping(value = "multifileUpload", method = RequestMethod.POST)
    public  String multifileUpload(HttpServletRequest request,@RequestParam String upload)
    {
        System.out.println("===============multifileUpload====================");

        System.out.println(upload);
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("fileName");

        if (files.isEmpty())
        {
            return "false";
        }
        //String path = "F:/test";
        String path = "/home/kiss/test/CommProto/"+upload;
        System.out.println(path);
        for (MultipartFile file : files)
        {
            String fileName = file.getOriginalFilename();
            int size = (int) file.getSize();
            System.out.println(fileName + "-->" + size);

            if (file.isEmpty())
            {
                return "false";
            } else
            {
                File dest = new File(path + "/" + fileName);
                if (!dest.getParentFile().exists())
                { //判断文件父目录是否存在
                    dest.getParentFile().mkdir();
                }
                try
                {
                    file.transferTo(dest);
                } catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return "false";
                }
            }
        }
        return "true";
    }


    //文件下载
    @RequestMapping("/download/{fileName}")
    public String downLoad(HttpServletResponse response,@PathVariable("fileName")String fileName) throws UnsupportedEncodingException
    {
        System.out.println("=====================================");
        System.out.println("fileName");
        //String fileName="MiniGame.tar";
        String filePath = "/home/kiss/test/CommProto";
        File file = new File(filePath + "/" + fileName);

        System.out.println("=====================================");
        System.out.println(filePath + "/" + fileName);
        if(file.exists()){ //判断文件父目录是否存在
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            // response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" +   java.net.URLEncoder.encode(fileName,"UTF-8"));
            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while(i != -1){
                    os.write(buffer);
                    i = bis.read(buffer);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("----------file download---" + filePath + "/" + fileName);
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }



    @RequestMapping("file")
    public String file()
    {
        System.out.println("==============================");
        return "/file";
    }

    @RequestMapping("multifile")
    public String multifile()
    {
        return "/multifile";
    }


    @ResponseBody
    @RequestMapping("fileUpload")
    public String fileUpload(@RequestParam("fileName") MultipartFile file)
    {
        if (file.isEmpty())
        {
            return "false";
        }
        String fileName = file.getOriginalFilename();
        int size = (int) file.getSize();
        System.out.println(fileName + "-->" + size);

        String path = "F:/test";
        File dest = new File(path + "/" + fileName);
        if (!dest.getParentFile().exists())
        { //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try
        {
            file.transferTo(dest); //保存文件
            return "true";
        } catch (IllegalStateException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "false";
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "false";
        }
    }
}
