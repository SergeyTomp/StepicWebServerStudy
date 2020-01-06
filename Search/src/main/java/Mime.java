public enum Mime {

    TEXT_PLAIN("text/plain"),
    TEXT_CSV("text/csv");

    private String fileType;

    Mime(String fileType) {}

    public String getFileType() {
        return fileType;
    }
}
