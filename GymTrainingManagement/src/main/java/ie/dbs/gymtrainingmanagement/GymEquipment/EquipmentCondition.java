package ie.dbs.gymtrainingmanagement.GymEquipment;

public enum EquipmentCondition {
    NEW("New", "text-bg-success"),
    GOOD("Good", "text-bg-primary"),
    WORN("Worn", "text-bg-warning"),
    NEEDS_REPAIR("Needs repair", "text-bg-danger"),
    OUT_OF_SERVICE("Out of service", "text-bg-secondary");

    private final String label;
    private final String badgeClass;

    EquipmentCondition(String label, String badgeClass) {
        this.label = label;
        this.badgeClass = badgeClass;
    }

    public String getLabel() {
        return label;
    }

    // Bootstrap class used by the badge in the list page
    public String getBadgeClass() {
        return badgeClass;
    }
}
