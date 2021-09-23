/**
 * The MIT License (MIT) * Copyright (c) 2020 铭软科技(mingsoft.net)

 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package net.mingsoft.mdiy.bean;

/**
 * 分页类
 * @author 铭飞开源团队
 * @date 2019年3月12日
 */
public class PageBean {
    /**
     * 下一篇文章编号;
     */
    private String nextId;
    /**
     * 总数
     */
    private int total;
    /**
     * 显示的条数;分页标签
     */
    private int size;
    /**
     * 上一篇文章编号;
     */
    private String preId;
    /**
     * 当前页
     */
    private int pageNo = 1;
    /**
     * 上一篇文章链接;
     */
    private String preUrl;
    /**
     * 下一篇文章链接;
     */
    private String nextUrl;
    /**
     * 第一篇文章链接;
     */
    private String indexUrl;
    /**
     * 最后一篇文章链接;
     */
    private String lastUrl;

    /**
     * 搜索总数;
     */
    private int rcount;



    public int getRcount() {
        return rcount;
    }

    public void setRcount(int rcount) {
        this.rcount = rcount;
    }

    public String getNextId() {
        return nextId;
    }
    public void setNextId(String nextId) {
        this.nextId = nextId;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public String getPreId() {
        return preId;
    }
    public void setPreId(String preId) {
        this.preId = preId;
    }
    public int getPageNo() {
        return pageNo;
    }
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
    public String getPreUrl() {
        return preUrl;
    }
    public void setPreUrl(String preUrl) {
        this.preUrl = preUrl;
    }
    public String getNextUrl() {
        return nextUrl;
    }
    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }
    public String getIndexUrl() {
        return indexUrl;
    }
    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }
    public String getLastUrl() {
        return lastUrl;
    }
    public void setLastUrl(String lastUrl) {
        this.lastUrl = lastUrl;
    }
}
