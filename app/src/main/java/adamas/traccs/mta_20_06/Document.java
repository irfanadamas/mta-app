package adamas.traccs.mta_20_06;

/**
 * Created by arshad on 18/08/2017.
 */

public class Document {
    private String Doc_ID;
    private String Title;
    private String Created;
    private String status;
    private String CareDomian;
    private String Disciplne;
    private String Program;
    private String Classification;
    private String Category;
    private String modified;
    private String FileName;
    private String OriginalLocation;
    private String DocumentType;
    private String DocumentGroup;
    private String NewLocation;

    public  Document(){

    }

    public String getDoc_ID() {
        return Doc_ID;
    }

    public void setDoc_ID(String doc_ID) {
        Doc_ID = doc_ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCreated() {
        return Created;
    }

    public void setCreated(String created) {
        Created = created;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCareDomian() {
        return CareDomian;
    }

    public void setCareDomian(String careDomian) {
        CareDomian = careDomian;
    }

    public String getDisciplne() {
        return Disciplne;
    }

    public void setDisciplne(String disciplne) {
        Disciplne = disciplne;
    }

    public String getProgram() {
        return Program;
    }

    public void setProgram(String program) {
        Program = program;
    }

    public String getClassification() {
        return Classification;
    }

    public void setClassification(String classification) {
        Classification = classification;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getOriginalLocation() {
        return OriginalLocation;
    }

    public void setOriginalLocation(String originalLocation) {
        OriginalLocation = originalLocation;
    }

    public String getDocumentType() {
        return DocumentType;
    }

    public void setDocumentType(String documentType) {
        DocumentType = documentType;
    }

    public String getDocumentGroup() {
        return DocumentGroup;
    }

    public void setDocumentGroup(String documentGroup) {
        DocumentGroup = documentGroup;
    }

    public String getNewLocation() {
        return NewLocation;
    }

    public void setNewLocation(String newLocation) {
        NewLocation = newLocation;
    }
}