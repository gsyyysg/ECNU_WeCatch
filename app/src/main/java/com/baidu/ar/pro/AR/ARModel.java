package com.baidu.ar.pro.AR;

import org.litepal.crud.LitePalSupport;

public class ARModel extends LitePalSupport {
        private String AR_image_name;
        private Integer AR_ID;
        private String AR_Key;

        public ARModel()
        {

        }

        public ARModel(String image_name, Integer ID, String key)
        {
            AR_image_name = image_name;
            AR_ID = ID;
            AR_Key = key;
        }

        public Integer getAR_ID() {
            return AR_ID;
        }

        public String getAR_image_name() {
            return AR_image_name;
        }

        public String getAR_Key() {
            return AR_Key;
        }

        public void setAR_ID(Integer ar_ID) {
            AR_ID = ar_ID;
        }

        public void setAR_image_name(String ar_image_name) {
            AR_image_name = ar_image_name;
        }

        public void setAR_Key(String ar_name) {
            AR_Key = ar_name;
        }

    }

