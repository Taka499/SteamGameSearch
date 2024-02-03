/**
 * Author: George Saito (takatoms)
 * Last Modified: November 28th, 2022
 *
 * This is the class that is used for DashboardServlet
 * The servlet will convert Document class from MongoDB to this class
 */
package ds;

public class UserLog {
    private String id;
    private String searchWord;
    private String timeStamp;
    private String mobileModel;
    private int numResultFound;
    private long searchTimeSpent;
    private String firstApiRequest;
    private String firstCheapSharkResponse;
    private String secondApiRequest;
    private String secondCheapSharkResponse;
    private String reply2User;

    public UserLog() {}

    public UserLog(String id, String searchWord, String timeStamp, String mobileModel,
                   int numResultFound, long searchTimeSpent, String firstApiRequest,
                   String firstCheapSharkResponse, String secondApiRequest,
                   String secondCheapSharkResponse, String reply2User) {
        this.id = id;
        this.searchWord = searchWord;
        this.timeStamp = timeStamp;
        this.mobileModel = mobileModel;
        this.numResultFound = numResultFound;
        this.searchTimeSpent = searchTimeSpent;
        this.firstApiRequest = firstApiRequest;
        this.firstCheapSharkResponse = firstCheapSharkResponse;
        this.secondApiRequest = secondApiRequest;
        this.secondCheapSharkResponse = secondCheapSharkResponse;
        this.reply2User = reply2User;
    }

    public String getSecondApiRequest() {
        return secondApiRequest;
    }

    public int getNumResultFound() {
        return numResultFound;
    }

    public long getSearchTimeSpent() {
        return searchTimeSpent;
    }

    public String getFirstApiRequest() {
        return firstApiRequest;
    }

    public String getFirstCheapSharkResponse() {
        return firstCheapSharkResponse;
    }

    public String getId() {
        return id;
    }

    public String getMobileModel() {
        return mobileModel;
    }

    public String getReply2User() {
        return reply2User;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public String getSecondCheapSharkResponse() {
        return secondCheapSharkResponse;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setFirstApiRequest(String firstApiRequest) {
        this.firstApiRequest = firstApiRequest;
    }

    public void setFirstCheapSharkResponse(String firstCheapSharkResponse) {
        this.firstCheapSharkResponse = firstCheapSharkResponse;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMobileModel(String mobileModel) {
        this.mobileModel = mobileModel;
    }

    public void setNumResultFound(int numResultFound) {
        this.numResultFound = numResultFound;
    }

    public void setReply2User(String reply2User) {
        this.reply2User = reply2User;
    }

    public void setSearchTimeSpent(long searchTimeSpent) {
        this.searchTimeSpent = searchTimeSpent;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public void setSecondApiRequest(String secondApiRequest) {
        this.secondApiRequest = secondApiRequest;
    }

    public void setSecondCheapSharkResponse(String secondCheapSharkResponse) {
        this.secondCheapSharkResponse = secondCheapSharkResponse;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
