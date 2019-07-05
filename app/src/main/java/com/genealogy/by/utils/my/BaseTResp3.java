package com.genealogy.by.utils.my;

/**
 * Time:2018/11/8
 * Desc: 数据加载的基类. 包含数据请求的最外层  字段: code、msg、data
 */
public class BaseTResp3<T> {
        public T data;
        private int total;
        private String per_page;
        private int current_page;
        private int last_page;

        public BaseTResp3(T data, int total, String per_page, int current_page, int last_page) {
                this.data = data;
                this.total = total;
                this.per_page = per_page;
                this.current_page = current_page;
                this.last_page = last_page;
        }
}
