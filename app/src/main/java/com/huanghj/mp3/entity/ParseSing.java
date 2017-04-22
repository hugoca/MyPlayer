package com.huanghj.mp3.entity;

import java.util.List;

/**
 * Created by xiefei on 2016/3/5.
 */
public class ParseSing {

    private ResultEntity result;

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public ResultEntity getResult() {
        return result;
    }

    public static class ResultEntity {
        /**
         * id : 27808044
         * name : 丑八怪
         * artists : [{"id":5781,"name":"薛之谦","picUrl":null,"alias":[],"albumSize":0,"picId":0,"trans":null,"img1v1Url":"http://p3.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1":0}]
         * album : {"id":2681139,"name":"意外","artist":{"id":0,"name":"","picUrl":null,"alias":[],"albumSize":0,"picId":0,"trans":null,"img1v1Url":"http://p4.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1":0},"publishTime":1381161600007,"size":10,"copyrightId":5003,"status":0,"picId":5816416510959096}
         * duration : 248426
         * copyrightId : 29001
         * status : 1
         * alias : []
         * fee : 0
         * mvid : 193084
         * rtype : 0
         * rUrl : null
         * ftype : 0
         */

        private List<SongsEntity> songs;

        public void setSongs(List<SongsEntity> songs) {
            this.songs = songs;
        }

        public List<SongsEntity> getSongs() {
            return songs;
        }

        public static class SongsEntity {
            private int id;
            private String name;
            /**
             * id : 2681139
             * name : 意外
             * artist : {"id":0,"name":"","picUrl":null,"alias":[],"albumSize":0,"picId":0,"trans":null,"img1v1Url":"http://p4.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1":0}
             * publishTime : 1381161600007
             * size : 10
             * copyrightId : 5003
             * status : 0
             * picId : 5816416510959096
             */

            private AlbumEntity album;
            private int duration;
            private int mvid;
            /**
             * id : 5781
             * name : 薛之谦
             * picUrl : null
             * alias : []
             * albumSize : 0
             * picId : 0
             * trans : null
             * img1v1Url : http://p3.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
             * img1v1 : 0
             */

            private List<ArtistsEntity> artists;

            public void setId(int id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setAlbum(AlbumEntity album) {
                this.album = album;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public void setMvid(int mvid) {
                this.mvid = mvid;
            }

            public void setArtists(List<ArtistsEntity> artists) {
                this.artists = artists;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public AlbumEntity getAlbum() {
                return album;
            }

            public int getDuration() {
                return duration;
            }

            public int getMvid() {
                return mvid;
            }

            public List<ArtistsEntity> getArtists() {
                return artists;
            }

            public static class AlbumEntity {
                private int id;

                public void setId(int id) {
                    this.id = id;
                }

                public int getId() {
                    return id;
                }
            }

            public static class ArtistsEntity {
                private int id;
                private String name;

                public void setId(int id) {
                    this.id = id;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getId() {
                    return id;
                }

                public String getName() {
                    return name;
                }
            }
        }
    }

}
