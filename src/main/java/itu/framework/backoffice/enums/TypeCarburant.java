package itu.framework.backoffice.enums;

public enum TypeCarburant {
    D("Diesel"),
    ES("Essence"),
    EL("Électrique"),
    H("Hybride");

    private final String label;

    TypeCarburant(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getCode() {
        return this.name();
    }
}
