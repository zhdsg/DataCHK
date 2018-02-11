package com.hg.chk;

/**
 * Created by Administrator on 2018/2/6/006.
 */
public class Param {
    private Integer time =23;
    private Integer sleep=5;
    private Integer retry=3;
    private String projectId="20007";
    private String sourceName="h2010_19";
    private String sourceType="oracle";

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        if(time !=null) {
            this.time = time;
        }
    }

    public Integer getSleep() {
        return sleep;
    }

    public void setSleep(Integer sleep) {
        if(sleep !=null) {
            this.sleep = sleep;
        }
    }


    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        if(retry !=null) {
            this.retry = retry;
        }
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        if(projectId != null){
            this.projectId = projectId;
        }

    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        if(sourceName !=null) {
            this.sourceName = sourceName;
        }
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        if(sourceType !=null) {
            this.sourceType = sourceType;
        }
    }
}
