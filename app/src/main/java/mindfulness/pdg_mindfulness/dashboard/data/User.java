package mindfulness.pdg_mindfulness.dashboard.data;

public class User {

    private String name;

    private Long screenOnCount;

    private String screenTotalTime;

    private String callTotalTime;

    private Long callInCount;

    private Long callOutCount;

    private String appInformationTime;

    private String appHealthTime;

    private String appSystemTime;

    private String appEntertainmentTime;

    private String appSocialTime;

    private String appWorkTime;

    private String created;

    public User() {
    }
    public User(String name) {
        this.name = name;
    }

    public User(String name, Long screenOnCount, String screenTotalTime, String callTotalTime, Long callInCount, Long callOutCount, String appInformationTime, String appHealthTime, String appSystemTime, String appEntertainmentTime, String appSocialTime, String appWorkTime, String created) {
        this.name = name;
        this.screenOnCount = screenOnCount;
        this.screenTotalTime = screenTotalTime;
        this.callTotalTime = callTotalTime;
        this.callInCount = callInCount;
        this.callOutCount = callOutCount;
        this.appInformationTime = appInformationTime;
        this.appHealthTime = appHealthTime;
        this.appSystemTime = appSystemTime;
        this.appEntertainmentTime = appEntertainmentTime;
        this.appSocialTime = appSocialTime;
        this.appWorkTime = appWorkTime;
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getScreenOnCount() {
        return screenOnCount;
    }

    public void setScreenOnCount(Long screenOnCount) {
        this.screenOnCount = screenOnCount;
    }

    public String getScreenTotalTime() {
        return screenTotalTime;
    }

    public void setScreenTotalTime(String screenTotalTime) {
        this.screenTotalTime = screenTotalTime;
    }

    public String getCallTotalTime() {
        return callTotalTime;
    }

    public void setCallTotalTime(String callTotalTime) {
        this.callTotalTime = callTotalTime;
    }

    public Long getCallInCount() {
        return callInCount;
    }

    public void setCallInCount(Long callInCount) {
        this.callInCount = callInCount;
    }

    public Long getCallOutCount() {
        return callOutCount;
    }

    public void setCallOutCount(Long callOutCount) {
        this.callOutCount = callOutCount;
    }

    public String getAppInformationTime() {
        return appInformationTime;
    }

    public void setAppInformationTime(String appInformationTime) {
        this.appInformationTime = appInformationTime;
    }

    public String getAppHealthTime() {
        return appHealthTime;
    }

    public void setAppHealthTime(String appHealthTime) {
        this.appHealthTime = appHealthTime;
    }

    public String getAppSystemTime() {
        return appSystemTime;
    }

    public void setAppSystemTime(String appSystemTime) {
        this.appSystemTime = appSystemTime;
    }

    public String getAppEntertainmentTime() {
        return appEntertainmentTime;
    }

    public void setAppEntertainmentTime(String appEntertainmentTime) {
        this.appEntertainmentTime = appEntertainmentTime;
    }

    public String getAppSocialTime() {
        return appSocialTime;
    }

    public void setAppSocialTime(String appSocialTime) {
        this.appSocialTime = appSocialTime;
    }

    public String getAppWorkTime() {
        return appWorkTime;
    }

    public void setAppWorkTime(String appWorkTime) {
        this.appWorkTime = appWorkTime;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

}
