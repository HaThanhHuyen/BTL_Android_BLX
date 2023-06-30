package com.dhtl.btl_ptud.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Items implements Parcelable {
    private int id;
    private String question; //câu hỏi
    private String option1; //lựa chọn 1
    private String option2;
    private String option3;
    private String option4;
    private String answer; //đáp án
    private int illustrationId; //hình ảnh
    private boolean isAnswer;//cái này để check phần ôn thi
    private String myAnswer;//đáp an của tôi

    public Items(int id, String question, String option1, String option2, String option3, String option4, String answer, int illustrationId) {
        this.id = id;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.illustrationId = illustrationId;
    }


    public String getMyAnswer() {
        return myAnswer;
    }

    public void setMyAnswer(String myAnswer) {
        this.myAnswer = myAnswer;
    }
    public boolean isAnswer() {
        return isAnswer;
    }

    public void setAnswer(boolean answer) {
        isAnswer = answer;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getIllustrationId() {
        return illustrationId;
    }

    public void setIllustrationId(int illustrationId) {
        this.illustrationId = illustrationId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.question);
        dest.writeString(this.option1);
        dest.writeString(this.option2);
        dest.writeString(this.option3);
        dest.writeString(this.option4);
        dest.writeString(this.answer);
        dest.writeInt(this.illustrationId);
        dest.writeByte(this.isAnswer ? (byte) 1 : (byte) 0);
        dest.writeString(this.myAnswer);
    }

    protected Items(Parcel in) {
        this.id = in.readInt();
        this.question = in.readString();
        this.option1 = in.readString();
        this.option2 = in.readString();
        this.option3 = in.readString();
        this.option4 = in.readString();
        this.answer = in.readString();
        this.illustrationId = in.readInt();
        this.isAnswer = in.readByte() != 0;
        this.myAnswer = in.readString();
    }

    public static final Parcelable.Creator<Items> CREATOR = new Parcelable.Creator<Items>() {
        @Override
        public Items createFromParcel(Parcel source) {
            return new Items(source);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };
}
