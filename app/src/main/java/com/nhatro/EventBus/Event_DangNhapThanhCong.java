package com.nhatro.EventBus;

/**
 * Created by CongHoang on 6/8/2018.
 */

public class Event_DangNhapThanhCong {
    // Cần truyền những biến gì ko? ví dụ tên tuổi token ,...
    // chỉ cần biết đổi hay chưa như t naxh nói là đc r :))
    private boolean isChange;

    public Event_DangNhapThanhCong() {
    }

    public Event_DangNhapThanhCong(boolean isChange, int iduser) {
        this.isChange = isChange;
        this.idUser = iduser;
    }
    public Event_DangNhapThanhCong(boolean isChange) {
        this.isChange = isChange;
    }

    public boolean isChange() {
        return isChange;
    }

    int idUser;

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setChange(boolean change) {
        isChange = change;
    }
}
