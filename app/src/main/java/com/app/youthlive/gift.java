package com.app.youthlive;

public class gift {

    public static final String TABLE_NAME = "gifts";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_GID = "gid";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_FILE = "file_name";
    public static final String COLUMN_FL = "file";
    public static final String COLUMN_PRICE = "price";

    private int id;
    private String gid;
    private String name;
    private String file;
    private String price;
    private byte[] fl;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_GID + " TEXT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_FILE + " TEXT,"
                    + COLUMN_FL + " BLOB,"
                    + COLUMN_PRICE + " TEXT"
                    + ")";

    public gift() {
    }

    public gift(int id, String name, String file , String price) {
        this.id = id;
        this.name = name;
        this.file = file;
        this.price = price;
    }


    public int getId() {
        return id;
    }

    public String getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public byte[] getFl() {
        return fl;
    }

    public void setFl(byte[] fl) {
        this.fl = fl;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }
}
