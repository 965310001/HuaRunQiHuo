package com.genealogy.by.entity;

import com.flyco.tablayout.listener.CustomTabEntity;

import java.io.Serializable;

/**
 * Anthor:HeChuan
 * Time:2018/11/26
 * Desc: 國際期貨
 */
public class FutureInternatinal implements Serializable{

       public String symbol;
       public String last;
       public String pricechange;
       public String bid;
       public String ask;
       public String high;
       public String low;
       public String timeupdate;
       public String dateupdate;
       public String prev;
       public String open;
       public String totalvol;
       public String bidsize;
       public String asksize;
       public String currentvol;
       public String localdatetime;
       public String name;
       public String market;
       public boolean isAdd = false;

       public FutureInternatinal(boolean isAdd) {
              this.isAdd = isAdd;
       }

       public FutureInternatinal() {
       }

    public static class TabEntity implements CustomTabEntity {
        public String title;
        public int selectedIcon;
        public int unSelectedIcon;

        public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
            this.title = title;
            this.selectedIcon = selectedIcon;
            this.unSelectedIcon = unSelectedIcon;
        }

        @Override
        public String getTabTitle() {
            return title;
        }

        @Override
        public int getTabSelectedIcon() {
            return selectedIcon;
        }

        @Override
        public int getTabUnselectedIcon() {
            return unSelectedIcon;
        }
    }
}
