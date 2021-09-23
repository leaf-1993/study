package com.cx.io;


import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author chenxiang
 * @create 2021-09-24 0:04
 */
public class FileTest {

    @Test
    public void createFile() throws URISyntaxException, IOException {
        // 指向某个路径
        File file = new File("lib");

        // 指向某个不存在的文件、路径
        File file1 = new File("lib1");

        // 父路径 + 子路径
        File file2 = new File("lib", "b");
        file2.mkdirs();

        // 基于文件协议
        File file3 = new File(new URI("file:///H:/sourcecode/study/lib/e.txt"));
        file3.createNewFile();
        System.out.println(file3);
        file3.mkdirs();
    }

    @Test
    public void fileApi() throws IOException {
        // 创建一个目录  / 开头代表绝对路径
        File file = new File("fileApi");
        // 获取目录名称
        System.out.println(file.getName());
        Assert.assertEquals(file.getName(), "fileApi");
        // 判断目录是否存在
        if(file.exists()){
            Assert.assertTrue(file.canRead());
            Assert.assertTrue(file.canWrite());
        }else {
            file.mkdir();
        }
        // 判断是否是目录
        File file1 = new File("isDir");
        System.out.println(file1.isDirectory());
        // 判断是否文件
        System.out.println(file1.isFile());
        // 获取父路径
        System.out.println(file.getParent());
        // 获取路径
        System.out.println(file.getPath());
        // 获取绝对路径
        System.out.println(file.getAbsolutePath());
        // 获取处理后的路径比如去掉 .. 和 .
        System.out.println(file.getCanonicalPath());
        // 转换为path
        System.out.println(file.toPath().getFileName());
    }
}
