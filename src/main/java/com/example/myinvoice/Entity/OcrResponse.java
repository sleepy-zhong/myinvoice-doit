package com.example.myinvoice.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // 允许忽略未知字段
public class OcrResponse {
    private String msg;
//    private List<OcrPage> results;
    private List<OcrPage> results = new ArrayList<>(); // 初始化空列表

    private String status; // 添加此字段

    public OcrResponse() {
    }

    public String getMsg() {
        return this.msg;
    }

    public List<OcrPage> getResults() {
        return this.results;
    }

    public String getStatus() {
        return this.status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setResults(List<OcrPage> results) {
        this.results = results;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof OcrResponse)) return false;
        final OcrResponse other = (OcrResponse) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$msg = this.getMsg();
        final Object other$msg = other.getMsg();
        if (this$msg == null ? other$msg != null : !this$msg.equals(other$msg)) return false;
        final Object this$results = this.getResults();
        final Object other$results = other.getResults();
        if (this$results == null ? other$results != null : !this$results.equals(other$results)) return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof OcrResponse;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $msg = this.getMsg();
        result = result * PRIME + ($msg == null ? 43 : $msg.hashCode());
        final Object $results = this.getResults();
        result = result * PRIME + ($results == null ? 43 : $results.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        return result;
    }

    public String toString() {
        return "OcrResponse(msg=" + this.getMsg() + ", results=" + this.getResults() + ", status=" + this.getStatus() + ")";
    }

    public static class OcrPage {
//        private List<OcrItem> data;
        private List<OcrItem> data = new ArrayList<>(); // 初始化空列表
        @JsonProperty("save_path")
        private String savePath;

        public OcrPage() {
        }

        public List<OcrItem> getData() {
            return this.data;
        }

        public String getSavePath() {
            return this.savePath;
        }

        public void setData(List<OcrItem> data) {
            this.data = data;
        }

        @JsonProperty("save_path")
        public void setSavePath(String savePath) {
            this.savePath = savePath;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof OcrPage)) return false;
            final OcrPage other = (OcrPage) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$data = this.getData();
            final Object other$data = other.getData();
            if (this$data == null ? other$data != null : !this$data.equals(other$data)) return false;
            final Object this$savePath = this.getSavePath();
            final Object other$savePath = other.getSavePath();
            if (this$savePath == null ? other$savePath != null : !this$savePath.equals(other$savePath)) return false;
            return true;
        }

        protected boolean canEqual(final Object other) {
            return other instanceof OcrPage;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $data = this.getData();
            result = result * PRIME + ($data == null ? 43 : $data.hashCode());
            final Object $savePath = this.getSavePath();
            result = result * PRIME + ($savePath == null ? 43 : $savePath.hashCode());
            return result;
        }

        public String toString() {
            return "OcrResponse.OcrPage(data=" + this.getData() + ", savePath=" + this.getSavePath() + ")";
        }
    }

    public static class OcrItem {
        private Double confidence;
        private String text;
        @JsonProperty("text_box_position")
        private List<List<Integer>> textBoxPosition;

        public OcrItem() {
        }

        public Double getConfidence() {
            return this.confidence;
        }

        public String getText() {
            return this.text;
        }

        public List<List<Integer>> getTextBoxPosition() {
            return this.textBoxPosition;
        }

        public void setConfidence(Double confidence) {
            this.confidence = confidence;
        }

        public void setText(String text) {
            this.text = text;
        }

        @JsonProperty("text_box_position")
        public void setTextBoxPosition(List<List<Integer>> textBoxPosition) {
            this.textBoxPosition = textBoxPosition;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof OcrItem)) return false;
            final OcrItem other = (OcrItem) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$confidence = this.getConfidence();
            final Object other$confidence = other.getConfidence();
            if (this$confidence == null ? other$confidence != null : !this$confidence.equals(other$confidence))
                return false;
            final Object this$text = this.getText();
            final Object other$text = other.getText();
            if (this$text == null ? other$text != null : !this$text.equals(other$text)) return false;
            final Object this$textBoxPosition = this.getTextBoxPosition();
            final Object other$textBoxPosition = other.getTextBoxPosition();
            if (this$textBoxPosition == null ? other$textBoxPosition != null : !this$textBoxPosition.equals(other$textBoxPosition))
                return false;
            return true;
        }

        protected boolean canEqual(final Object other) {
            return other instanceof OcrItem;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $confidence = this.getConfidence();
            result = result * PRIME + ($confidence == null ? 43 : $confidence.hashCode());
            final Object $text = this.getText();
            result = result * PRIME + ($text == null ? 43 : $text.hashCode());
            final Object $textBoxPosition = this.getTextBoxPosition();
            result = result * PRIME + ($textBoxPosition == null ? 43 : $textBoxPosition.hashCode());
            return result;
        }

        public String toString() {
            return "OcrResponse.OcrItem(confidence=" + this.getConfidence() + ", text=" + this.getText() + ", textBoxPosition=" + this.getTextBoxPosition() + ")";
        }
    }
}