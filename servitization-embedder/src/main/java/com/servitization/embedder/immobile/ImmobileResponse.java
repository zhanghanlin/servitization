package com.servitization.embedder.immobile;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 请求过程中，不变的response对象 可写，改变内容
 */
public interface ImmobileResponse {

    /**
     * 获取返回的输出流
     *
     * @return
     * @throws IOException
     */
    OutputStream getOutputStream() throws IOException;

    /**
     * 获取已经写入的内容
     *
     * @return
     */
    byte[] getContent();

    /**
     * 重置已经写入的内容
     */
    void resetContent();

    /**
     * 输出已经写入的内容,内容将不能被修改
     *
     * @throws IOException
     */
    void flushContent() throws IOException;

    /**
     * 设置输出的编码格式
     *
     * @param charset
     */
    void setCharacterEncoding(String charset);

    /**
     * 设置返回头
     */
    void setHeader();
}
