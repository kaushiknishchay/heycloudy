package com.kaushik.heycloudy.model;

/**
 * Created by SHolmes on 09-Jul-17.
 */

public class PathModel {
    private long id;
    private String dirPath;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    @Override
    public String toString() {
        return "PathModel{" +
                "id=" + id +
                ", dirPath='" + dirPath + '\'' +
                '}';
    }
}
