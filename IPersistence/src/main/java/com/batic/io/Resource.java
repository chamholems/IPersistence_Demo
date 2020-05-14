package com.batic.io;

import java.io.InputStream;

public class Resource {

    /**
     * 根据配置文件的路径，将配置文件加载成字节输入流，存储在内存中
     * @param path 文件路径
     * @return 流
     */
    public static InputStream getResourceAsSteam(String path){
        return Resource.class.getClassLoader().getResourceAsStream(path);
    }
}
