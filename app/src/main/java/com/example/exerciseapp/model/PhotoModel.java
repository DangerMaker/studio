package com.example.exerciseapp.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/6.
 */
public class PhotoModel extends ErrorMsg {
    /**
     * date : 2016-09-03
     * pic_list : [{"pic_path":"http://101.200.214.68/Uploads/AlbumPic/57ca7b45c1328.jpg","create_time":1472887621,"id":16,"pic_thumb_path":"http://101.200.214.68/Uploads/AlbumPic/t57ca7b45c1328.jpg"},{"pic_path":"http://101.200.214.68/Uploads/AlbumPic/57ca7b45c0b57.jpg","create_time":1472887621,"id":15,"pic_thumb_path":"http://101.200.214.68/Uploads/AlbumPic/t57ca7b45c0b57.jpg"},{"pic_path":"http://101.200.214.68/Uploads/AlbumPic/57ca7b44a1349.jpg","create_time":1472887620,"id":14,"pic_thumb_path":"http://101.200.214.68/Uploads/AlbumPic/t57ca7b44a1349.jpg"},{"pic_path":"http://101.200.214.68/Uploads/AlbumPic/57ca7b44a0b78.jpg","create_time":1472887620,"id":13,"pic_thumb_path":"http://101.200.214.68/Uploads/AlbumPic/t57ca7b44a0b78.jpg"},{"pic_path":"http://101.200.214.68/Uploads/AlbumPic/57ca7b4411a43.jpg","create_time":1472887620,"id":12,"pic_thumb_path":"http://101.200.214.68/Uploads/AlbumPic/t57ca7b4411a43.jpg"},{"pic_path":"http://101.200.214.68/Uploads/AlbumPic/57ca7b4411272.jpg","create_time":1472887620,"id":11,"pic_thumb_path":"http://101.200.214.68/Uploads/AlbumPic/t57ca7b4411272.jpg"},{"pic_path":"http://101.200.214.68/Uploads/AlbumPic/57ca7b4364670.jpg","create_time":1472887619,"id":10,"pic_thumb_path":"http://101.200.214.68/Uploads/AlbumPic/t57ca7b4364670.jpg"},{"pic_path":"http://101.200.214.68/Uploads/AlbumPic/57ca7b4363ea3.jpg","create_time":1472887619,"id":9,"pic_thumb_path":"http://101.200.214.68/Uploads/AlbumPic/t57ca7b4363ea3.jpg"},{"pic_path":"http://101.200.214.68/Uploads/AlbumPic/57ca7b428f9c3.jpg","create_time":1472887618,"id":8,"pic_thumb_path":"http://101.200.214.68/Uploads/AlbumPic/t57ca7b428f9c3.jpg"},{"pic_path":"http://101.200.214.68/Uploads/AlbumPic/57ca7b428f1f2.jpg","create_time":1472887618,"id":7,"pic_thumb_path":"http://101.200.214.68/Uploads/AlbumPic/t57ca7b428f1f2.jpg"},{"pic_path":"http://101.200.214.68/Uploads/AlbumPic/57ca7b41bf750.jpg","create_time":1472887617,"id":6,"pic_thumb_path":"http://101.200.214.68/Uploads/AlbumPic/t57ca7b41bf750.jpg"},{"pic_path":"http://101.200.214.68/Uploads/AlbumPic/57ca7b41bef80.jpg","create_time":1472887617,"id":5,"pic_thumb_path":"http://101.200.214.68/Uploads/AlbumPic/t57ca7b41bef80.jpg"},{"pic_path":"http://101.200.214.68/Uploads/AlbumPic/57ca7b3dab686.jpg","create_time":1472887613,"id":4,"pic_thumb_path":"http://101.200.214.68/Uploads/AlbumPic/t57ca7b3dab686.jpg"},{"pic_path":"http://101.200.214.68/Uploads/AlbumPic/57ca7b3daaea9.jpg","create_time":1472887613,"id":3,"pic_thumb_path":"http://101.200.214.68/Uploads/AlbumPic/t57ca7b3daaea9.jpg"},{"pic_path":"http://101.200.214.68/Uploads/AlbumPic/57ca7ab85dee5.jpg","create_time":1472887480,"id":2,"pic_thumb_path":"http://101.200.214.68/Uploads/AlbumPic/t57ca7ab85dee5.jpg"},{"pic_path":"http://101.200.214.68/Uploads/AlbumPic/57ca7ab85d717.jpg","create_time":1472887480,"id":1,"pic_thumb_path":"http://101.200.214.68/Uploads/AlbumPic/t57ca7ab85d717.jpg"}]
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String date;
        /**
         * pic_path : http://101.200.214.68/Uploads/AlbumPic/57ca7b45c1328.jpg
         * create_time : 1472887621
         * id : 16
         * pic_thumb_path : http://101.200.214.68/Uploads/AlbumPic/t57ca7b45c1328.jpg
         */

        private List<PicListBean> pic_list;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<PicListBean> getPic_list() {
            return pic_list;
        }

        public void setPic_list(List<PicListBean> pic_list) {
            this.pic_list = pic_list;
        }

        public static class PicListBean {
            private String pic_path;
            private int create_time;
            private int id;
            private String pic_thumb_path;

            public String getPic_path() {
                return pic_path;
            }

            public void setPic_path(String pic_path) {
                this.pic_path = pic_path;
            }

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPic_thumb_path() {
                return pic_thumb_path;
            }

            public void setPic_thumb_path(String pic_thumb_path) {
                this.pic_thumb_path = pic_thumb_path;
            }
        }
    }
}
