package internetcafe_management.entity;

public enum SaleStatus {
    Pending("Đang chờ thanh toán"),
    Paid("Đã thanh toán"),
    Cancelled("Đã hủy");

    private final String displayName;

    SaleStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
