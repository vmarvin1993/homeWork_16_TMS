

import java.util.ArrayList;
import java.util.List;

public class Document {
    private List<String> docNum = new ArrayList<>();
    private String mobNum;
    private String email;

    public Document(List<String> docNum, String mobNum, String email) {
        this.docNum = docNum;
        this.mobNum = mobNum;
        this.email = email;
    }

    public Document() {
    }

    public String getMobNum() {
        return mobNum;
    }

    public void setMobNum(String mobNum) {
        this.mobNum = mobNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getDocNum() {
        return docNum;
    }

    public void setDocNum(List<String> docNum) {
        this.docNum = docNum;
    }

    @Override
    public String toString() {
        return "Document Number: " + docNum + ", mobile: " + mobNum + ", email: " + email + "\n";
    }
}


