package com.baidu.ar.pro.AR;

import org.litepal.crud.LitePalSupport;

public class ARModel extends LitePalSupport {
        private String Ar_image_name;
        private Integer Ar_ID;
        private String Ar_name;

        public ARModel()
        {

        }

        public ARModel(String image_name, Integer ID, String name)
        {
            Ar_image_name = image_name;
            Ar_ID = ID;
            Ar_name = name;
        }

        public Integer getAr_ID() {
            return Ar_ID;
        }

        public String getAr_image_name() {
            return Ar_image_name;
        }

        public String getAr_name() {
            return Ar_name;
        }

        public void setAr_ID(Integer ar_ID) {
            Ar_ID = ar_ID;
        }

        public void setAr_image_name(String ar_image_name) {
            Ar_image_name = ar_image_name;
        }

        public void setAr_name(String ar_name) {
            Ar_name = ar_name;
        }

    }

