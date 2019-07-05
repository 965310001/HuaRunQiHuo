package com.genealogy.by.utils.my;

/**
 * Time:2018/11/8
 * Desc: 数据加载的基类. 包含数据请求的最外层  字段: code、msg、data
 */
public class BaseTResp2<T> {
        public  int status ;
        public String msg;
        public int countSum;
        public T data;

        public BaseTResp2(int status, String msg, T data,int countSum) {
                this.status = status;
                this.msg = msg;
                this.countSum =countSum;
                this.data = data;
        }
}
